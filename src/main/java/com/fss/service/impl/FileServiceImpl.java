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

    private static final int USER_ALERT = 1; //查询提示文件
    private static final int USER_RECEIVE = 2; //查询未接收文件
    private static final int USER_RECEIVED = 3;//查询已接收文件
    private static final int USER_UPLOADED = 4; //查询已上传文件

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
                fileReceive.setReceiver(user);
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
                fileReceive.setReceiver(user);
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

    @Override public JsonResultVO download(String userId, String versionId, HttpServletRequest request,
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
}
