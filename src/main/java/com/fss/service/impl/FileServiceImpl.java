package com.fss.service.impl;

import com.fss.controller.vo.*;
import com.fss.dao.domain.*;
import com.fss.dao.repositories.*;
import com.fss.service.FileService;
import com.fss.service.MailService;
import com.fss.service.UploadService;
import com.fss.service.UserService;
import com.fss.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Service
public class FileServiceImpl implements FileService{

    private static final String FILE_PATH_HEAD = "/upload/file/";

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private FileVersionRepository fileVersionRepository;

    @Autowired
    private OperateRepository operateRepository;

    @Autowired
    private FileReceiveRepository fileReceiveRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CatalogRepository catalogRepository;

    @Autowired
    private UploadService uploadService;

    @Autowired
    private MailService mailService;

    @Autowired
    private UserService userService;

    @Autowired
    private TaskExecutor taskExecutor;

    /**
     * 初始化上传信息
     */
    @Override
    public FileUploadInit getCatalogAndUserSelector(String userId) {
        List<User> userList = userRepository.findAll();
        List<Catalog> catalogList = catalogRepository.findAll();
        FileUploadInit fileUploadInit = new FileUploadInit();
        List<Selector> userSelector = new ArrayList<>();
        for (User u : userList) {
            if (!userId.equals(u.getId())) {//避免放入自己
                userSelector.add(new Selector(u.getId(), u.getName()));
            }
        }
        List<Selector> catalogSelector = new ArrayList<>();
        for (Catalog c : catalogList) {
            if (!c.getId().equals(c.getParentCatalog().getId())) {//避免放入根目录
                catalogSelector.add(new Selector(c.getId(), c.getName()));
            }
        }
        fileUploadInit.setUserSelector(userSelector);
        fileUploadInit.setCatalogSelector(catalogSelector);
        return fileUploadInit;
    }

    /**
     * 上传文件
     * @param author 文件作者
     * @param file  最新文件
     * @param fileUploadParam 文件上传参数
     */
    @Override
    public JsonResultVO upload(String author, MultipartFile file, FileUploadParam fileUploadParam,
            HttpServletRequest request) {
        try {
            //写入文件
            String fileURL = FILE_PATH_HEAD;
            Map<String, String> fileName = new HashMap<>();
            fileName.put("fileName", file.getOriginalFilename());
            String fileRealName = uploadService.upload(file, fileURL, request);
            fileName.put("realName", fileRealName);

            //写入数据库
            this.uploadFile(fileName, fileUploadParam);
        } catch (Exception e) {
            return new JsonResultVO(JsonResultVO.FAILURE, e.getMessage());
        }
//        if(fileUploadParam.isMailTo()) {
//            SendEmail sendEmail = new SendEmail(file.getOriginalFilename(),author,fileUploadParam);
//            sendEmail.setMailService(mailService);
//            sendEmail.setUserService(userService);
//            taskExecutor.execute(sendEmail); //线程执行发送邮件
//        }
        return new JsonResultVO(JsonResultVO.SUCCESS, "文件上传成功！");
    }

    /**
     * 写入上传文件信息到数据库
     * @param fileName 文件名
     * @param fileUploadParam 文件上传参数
     */
    private void uploadFile(Map<String, String> fileName, FileUploadParam fileUploadParam) {
        Date date = new Date();
        String fileName1 = fileName.get("fileName");
        boolean mailTo = fileUploadParam.isMailTo();
        String userId = fileUploadParam.getUserInfo().getUserId();
        User user = userRepository.findOne(userId);
        String catalogId = fileUploadParam.getCatalogId();
        Catalog catalog = catalogRepository.findOne(catalogId);

        //写入文件表
        File file = new File();
        file.setAuthor(user);
        file.setCatalog(catalog);
        file.setFileName(fileName1);
        file.setCreateTime(date);
        file.setUsable(true);
        file = fileRepository.save(file);//没有指定最新版本号，需生成后重新保存

        //写入版本表
        FileVersion fileVersion = new FileVersion();
        fileVersion.setAuthor(user);
        fileVersion.setFile(file);
        fileVersion.setNumber(1.0);
        fileVersion.setRealName(fileName.get("realName"));
        fileVersion.setCreateTime(date);
        fileVersion.setCount(0);
        fileVersion.setCanCover(fileUploadParam.isCanCover());
        fileVersion.setUsable(true);
        fileVersion = fileVersionRepository.save(fileVersion);
        file.setNewFileVersion(fileVersion);
        file = fileRepository.save(file);//保存最新版本号

        //写入操作表
        Operate operate = new Operate();
        String operateId = UUID.randomUUID().toString();
        operate.setId(operateId);
        operate.setOperator(user);
        operate.setFileVersion(fileVersion);
        operate.setOperateTime(date);
        operate.setCreateTime(date);
        operate.setOperateFlag(0);
        operate.setUsable(true);
        operateRepository.save(operate);


        //遍历可下载人员id，新建接收表
        List<String> canLoadIds = fileUploadParam.getCanOnlyLoadUserIds();
        if (canLoadIds != null && canLoadIds.size() > 0) {
            for (String id : canLoadIds) {
                FileReceive fileReceive = new FileReceive();
                fileReceive.setFile(file);
                fileReceive.setFileVersion(fileVersion);
                User receiver = userRepository.findOne(id);
                fileReceive.setReceiver(receiver);
                fileReceive.setIsAlert(mailTo);
                fileReceive.setIsReceived(false);
                fileReceive.setDownloadTime(date);
                fileReceive.setCanRevise(false);
                fileReceive.setUsable(true);
                fileReceiveRepository.save(fileReceive);
            }
        }

        //遍历可修改人员id列表
        List<String> canReviseIds = fileUploadParam.getCanReviseUserIds();
        if (canReviseIds != null && canReviseIds.size() > 0) {
            for (String id : canReviseIds) {
                FileReceive fileReceive = new FileReceive();
                fileReceive.setFile(file);
                fileReceive.setFileVersion(fileVersion);
                User receiver = userRepository.findOne(id);
                fileReceive.setReceiver(receiver);
                fileReceive.setIsAlert(mailTo);
                fileReceive.setIsReceived(false);
                fileReceive.setDownloadTime(date);
                fileReceive.setCanRevise(true);
                fileReceive.setUsable(true);
                fileReceiveRepository.save(fileReceive);
            }
        }
    }


    /**
     * 根据条件查找已上传文件分页信息
     * @param fileSearchKeys 查询条件
     * @param pageConfig 分页条件（之前项目冗余）
     * @return 分页信息
     */
    @Override
    public PageVO<FileInfoVO> getPageUploaded(FileSearchKeys fileSearchKeys, PageConfig pageConfig) {
        Specification<File> specification = getWhereClause(fileSearchKeys);
        Page<File> filePage = fileRepository.findAll(specification,
                new PageRequest(pageConfig.getPageNum()-1,pageConfig.getPageSize(),
                        new Sort(Sort.Direction.DESC,"createTime")));
        List<FileInfoVO> list = this.loadUploadedInfo(filePage.getContent());
        return (PageVO<FileInfoVO>) PageUtil.generateBy(filePage,list);
    }

    private Specification<File> getWhereClause(final FileSearchKeys fileSearchKeys){
        return new Specification<File>() {
            @Override public Predicate toPredicate(Root<File> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if(!StringUtils.isEmpty(fileSearchKeys.getFileNameKey())){
                    predicates.add(cb.like(root.get("fileName").as(String.class),"%" +fileSearchKeys.getFileNameKey()+ "%" ));
                }
                Join<File,User> userJoin = root.join(root.getModel().getSingularAttribute("author",User.class),JoinType.LEFT);
                predicates.add(cb.equal(userJoin.get("id").as(String.class),fileSearchKeys.getUserId()));
                if(!StringUtils.isEmpty(fileSearchKeys.getCatalogKey())){
                    Join<File,Catalog> catalogJoin = root.join(root.getModel().getSingularAttribute("catalog",Catalog.class),JoinType.LEFT);
                    predicates.add(cb.like(catalogJoin.get("name").as(String.class),"%" +fileSearchKeys.getCatalogKey()+ "%" ));
                }
                Predicate[] pre = new Predicate[predicates.size()];
                return query.where(predicates.toArray(pre)).getRestriction();
            }
        };
    }

    private List<FileInfoVO> loadUploadedInfo(List<File> fileList){
        List<FileInfoVO> fileInfoVoList = new ArrayList<>();
        for(File file : fileList){
            FileInfoVO fileInfoVO = new FileInfoVO();
            fileInfoVO.setFileId(file.getId());
            fileInfoVO.setFileName(file.getFileName());
            fileInfoVO.setCreateTime(file.getCreateTime());
            fileInfoVO.setCatalog(file.getCatalog().getName());
            fileInfoVO.setVersionId(file.getNewFileVersion().getId());
            fileInfoVO.setVersionNumber(file.getNewFileVersion().getNumber());
            fileInfoVoList.add(fileInfoVO);
        }
        return fileInfoVoList;
    }

    /**
     * 获得首页已上传数据
     */
    @Override public List<FileInfoVO> getHomeFileUploaded(String userId) {
        FileSearchKeys fileSearchKeys = new FileSearchKeys();
        fileSearchKeys.setUserId(userId);
        PageConfig pageConfig = new PageConfig();
        pageConfig.setPageNum(1);
        pageConfig.setPageSize(8);
        PageVO<FileInfoVO> fileInfoVOPage = getPageUploaded(fileSearchKeys,pageConfig);
        return fileInfoVOPage.getDataList();
    }

    /**
     * 文件下载
     */
    @Override
    public JsonResultVO download(String userId, String versionId, HttpServletRequest request,
            HttpServletResponse response) {
        FileReceive fileReceive = fileReceiveRepository.getReceiveBy(userId, versionId);
        FileVersion fileVersion = fileVersionRepository.findOne(versionId);
        File file = fileVersion.getFile();
        if (!file.getAuthor().getId().equals(userId) && fileReceive == null) {
            return new JsonResultVO(JsonResultVO.FAILURE, "无权下载此文件！");
        }
        if (!file.isUsable()) { //判断文件是否被删除
            return new JsonResultVO(JsonResultVO.FAILURE, "文件已被删除！");
        }
        String fileName = file.getFileName();
        String filePath = FILE_PATH_HEAD + fileVersion.getRealName();
        try {
            FileDownloadUtil.FileDownload(request, response, filePath, fileName);
            //写入数据库
            if(file.getAuthor().getId().equals(userId)){//自己下载
                this.selfDownloadFile(userId,fileVersion);
            }else if(fileReceive != null){//他人下载
                this.downloadFile(fileReceive);
            }
        } catch (Exception e) {
            return new JsonResultVO(JsonResultVO.FAILURE, e.getMessage());
        }
        return new JsonResultVO(JsonResultVO.SUCCESS, "下载成功！");
    }

    private void selfDownloadFile(String userId, FileVersion fileVersion) {
        //写操作表
        Date date = new Date();
        Operate operate = new Operate();
        User user = userRepository.findOne(userId);
        operate.setOperator(user);
        operate.setFileVersion(fileVersion);
        operate.setOperateTime(date);
        operate.setOperateFlag(1);
        operate.setUsable(true);
        operateRepository.save(operate);

        //写版本表
        fileVersion.setCount(fileVersion.getCount() + 1);
        fileVersionRepository.save(fileVersion);
    }

    private void downloadFile(FileReceive fileReceive) {
        //写操作表
        Date date = new Date();
        Operate operate = new Operate();
        operate.setOperator(fileReceive.getReceiver());
        operate.setFileVersion(fileReceive.getFileVersion());
        operate.setOperateTime(date);
        operate.setOperateFlag(1);
        operate.setUsable(true);
        operateRepository.save(operate);

        //写接收表
        if( !fileReceive.getIsReceived() ) { //如果未下载
            fileReceive.setIsReceived(true);
            fileReceive.setDownloadTime(date);
            fileReceive = fileReceiveRepository.save(fileReceive);
        }

        //写版本表，下载量+1
        FileVersion fileVersion = fileReceive.getFileVersion();
        fileVersion.setCount(fileVersion.getCount() + 1);
        fileVersionRepository.save(fileVersion);
    }

    /**
     * 删除文件
     * @param fileId 文件编号
     * @param userId 删除人
     */
    @Override
    public JsonResultVO deleteFile(String fileId, String userId) {
        try {
            //写文件表
            File file = fileRepository.findOne(fileId);
            file.setUsable(false);
            fileRepository.save(file);

            //写操作表
            Operate operate = new Operate();
            Date date = new Date();
            User user = userRepository.findOne(userId);
            operate.setOperator(user);
            operate.setFileVersion(file.getNewFileVersion());
            operate.setOperateTime(date);
            operate.setCreateTime(date);
            operate.setOperateFlag(3);
            operate.setUsable(true);
            operateRepository.save(operate);
            return new JsonResultVO(JsonResultVO.SUCCESS, "删除成功!");
        } catch (Exception e) {
            return new JsonResultVO(JsonResultVO.FAILURE, "删除失败!");
        }
    }

    /**
     * 初始化文件权限信息
     */
    @Override
    public FileUploadInit getReviseSelector(String userId, String fileId) {
        List<User> userList = userRepository.findAll();
        List<FileReceive> fileReceiveList = fileReceiveRepository.findByFileId(fileId);
        List<Catalog> catalogList = catalogRepository.findAll();
        List<Selector> userSelector = new ArrayList<>();
        Set<String> canLoadIds = new HashSet<>();
        Set<String> canReviseIds = new HashSet<>();
        List<Selector> userCanLoadSelector = new ArrayList<>();
        List<Selector> userCanReviseSelector = new ArrayList<>();

        //计算当前版本可修改人
        for (FileReceive fr : fileReceiveList) {
            if (fr.getCanRevise()) {//可修改的人
                canReviseIds.add(fr.getReceiver().getId());
            } else {
                canLoadIds.add(fr.getReceiver().getId());
            }
        }

        //放入对应的下拉框
        for (User u : userList) {
            String uid = u.getId();
            Selector selector = new Selector(u.getId(), u.getName());
            if (!userId.equals(uid)) {//避免放入自己
                if (canLoadIds.contains(uid)) {
                    userCanLoadSelector.add(selector);
                } else if (canReviseIds.contains(uid)) {
                    userCanReviseSelector.add(selector);
                } else {
                    userSelector.add(selector);
                }
            }
        }
        //目录下拉框
        List<Selector> catalogSelector = new ArrayList<>();
        for (Catalog c : catalogList) {
            if (!c.getId().equals(c.getParentCatalog().getId())) {//避免放入根目录
                catalogSelector.add(new Selector(c.getId(), c.getName()));
            }
        }
        FileUploadInit fileUploadInit = new FileUploadInit();
        fileUploadInit.setUserSelector(userSelector);
        fileUploadInit.setCatalogSelector(catalogSelector);
        fileUploadInit.setCanLoadSelector(userCanLoadSelector);
        fileUploadInit.setCanReviseSelector(userCanReviseSelector);
        return fileUploadInit;
    }

    /**
     * 修改文件权限
     */
    @Override
    public JsonResultVO reviseRole(String userId, String fileId, Set<String> canLoadIds,
            Set<String> canReviseIds) {
        File file = fileRepository.findOne(fileId);
        FileVersion fileVersion = file.getNewFileVersion();
        Date date = new Date();
        try {
            if (userId.equals(file.getAuthor().getId())) {
                List<FileReceive> fileReceiveList = fileReceiveRepository.findByFileId(fileId);//当前可下载
                for (FileReceive f : fileReceiveList) {
                    String receiverId = f.getReceiver().getId();
                    if (canLoadIds.contains(receiverId)) {//可下载
                        if (f.getCanRevise()) {//可改写
                            f.setCanRevise(false);
                            fileReceiveRepository.save(f);
                        }
                        canLoadIds.remove(receiverId);
                    } else if (canReviseIds.contains(receiverId)) {
                        if (!f.getCanRevise()) {//改为可改写
                            f.setCanRevise(true);
                            fileReceiveRepository.save(f);
                        }
                        canReviseIds.remove(receiverId);
                    } else {//不存在则删除
                        fileReceiveRepository.deleteByReceiverIdAndFileId(receiverId,fileId);
                    }
                }
                for (String userLoadId : canLoadIds) {//新建可读
                    FileReceive fileReceive = new FileReceive();
                    fileReceive.setFile(file);
                    fileReceive.setFileVersion(fileVersion);
                    User user = userRepository.findOne(userLoadId);
                    fileReceive.setReceiver(user);
                    fileReceive.setCanRevise(false);
                    fileReceive.setDownloadTime(date);
                    fileReceive.setIsAlert(false);
                    fileReceive.setIsReceived(false);
                    fileReceiveRepository.save(fileReceive);
                }

                for (String reviseIds : canReviseIds) {//新建可改写
                    FileReceive fileReceive = new FileReceive();
                    fileReceive.setFile(file);
                    fileReceive.setFileVersion(fileVersion);
                    User user = userRepository.findOne(reviseIds);
                    fileReceive.setReceiver(user);
                    fileReceive.setCanRevise(true);
                    fileReceive.setDownloadTime(date);
                    fileReceive.setIsAlert(false);
                    fileReceive.setIsReceived(false);
                    fileReceiveRepository.save(fileReceive);
                }
                return new JsonResultVO(JsonResultVO.SUCCESS, "权限修改成功！");
            } else
                return new JsonResultVO(JsonResultVO.FAILURE, "您无权修改本文件权限!");
        }catch (Exception e){
            return new JsonResultVO(JsonResultVO.FAILURE, "未知错误!");
        }
    }

    /**
     * 上传新版本文件
     */
    @Override
    public JsonResultVO reviseFile(String userId, MultipartFile file, String fileVersionId, boolean canCover,
            HttpServletRequest request) {
        FileVersion fileVersion = fileVersionRepository.findOne(fileVersionId);
        File oldFile = fileVersion.getFile();
        if (oldFile.isUsable()) {
            try {
                //写入文件
                String fileURL = FILE_PATH_HEAD;
                String fileRealName = uploadService.upload(file, fileURL, request);

                //写数据
                fileVersion.setRealName(fileRealName);
                this.writeReceive(userId, fileVersion, canCover);
            } catch (Exception e) {
                return new JsonResultVO(JsonResultVO.FAILURE, e.getMessage());
            }
            return new JsonResultVO(JsonResultVO.SUCCESS, "文件修改成功！");
        } else
            return new JsonResultVO(JsonResultVO.FAILURE, "文件已被删除！");
    }

    private void writeReceive(String userId, FileVersion fileVersion, boolean canCover) {
        //新建版本表
        Date data = new Date();
        FileVersion newFileVersion = new FileVersion();
        newFileVersion.setFile(fileVersion.getFile());
        newFileVersion.setAuthor(fileVersion.getAuthor());
        newFileVersion.setRealName(fileVersion.getRealName());
        newFileVersion.setCreateTime(data);
        newFileVersion.setUsable(true);
        Double number = fileVersion.getNumber();
        if (fileVersion.getCanCover()) {
            //以前版本可覆盖
            newFileVersion.setNumber(number + 0.1);
        } else {
            int i = number.intValue();//取整
            newFileVersion.setNumber(i + 1.0);
        }
        newFileVersion.setCanCover(canCover);
        newFileVersion.setCount(0);
        fileVersionRepository.save(newFileVersion);

        //更新最新文件版本编号
        File file = fileVersion.getFile();
        file.setNewFileVersion(newFileVersion);
        file.setCreateTime(data);
        fileRepository.save(file);

        //写操作表
        Operate operate = new Operate();
        User user = userRepository.findOne(userId);
        operate.setOperator(user);
        operate.setFileVersion(newFileVersion);
        operate.setOperateTime(data);
        operate.setCreateTime(data);
        operate.setOperateFlag(2);
        operate.setUsable(true);
        operateRepository.save(operate);

        //处理已接收和未接收相关消息
        List<FileReceive> fileReceiveList = fileReceiveRepository.findByFileId(file.getId());
        for (FileReceive fileReceive : fileReceiveList) {
            fileReceive.setIsReceived(userId.equals(fileReceive.getReceiver().getId()));
            fileReceive.setFileVersion(newFileVersion);
            fileReceive.setDownloadTime(data);
            fileReceiveRepository.save(fileReceive);
        }
    }

}
