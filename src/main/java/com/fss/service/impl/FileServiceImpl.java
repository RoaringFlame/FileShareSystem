package com.fss.service.impl;

import com.fss.controller.vo.FileUploadInit;
import com.fss.controller.vo.FileUploadParam;
import com.fss.controller.vo.JsonResultVO;
import com.fss.dao.domain.*;
import com.fss.dao.repositories.*;
import com.fss.service.*;
import com.fss.util.FileDownloadUtil;
import com.fss.util.Selector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Service
public class FileServiceImpl implements IFileService {

    private static final int USER_ALERT = 1; //查询提示文件
    private static final int USER_RECEIVE = 2; //查询未接收文件
    private static final int USER_RECEIVED = 3;//查询已接收文件
    private static final int USER_UPLOADED = 4; //查询已上传文件

    private static final String FILE_PATH_HEAD = "/upload/file/";

    @Autowired
    private IUploadFileService uploadFileService;

    @Autowired
    private FileDao fileDao;

    @Autowired
    private OperateDao operateDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private CatalogDao catalogDao;

    @Autowired
    private FileVersionDao fileVersionDao;

    @Autowired
    private FileReceiveDao fileReceiveDao;

    @Autowired
    private IOperateService operateService;

    @Autowired
    private IMailService mailService;

    @Autowired
    private IUserService userService;

    @Resource(name = "taskExecutor")
    private TaskExecutor taskExecutor;

    @Override
    public FileUploadInit getCatalogAndUserSelector(String userId) {
        List<User> userList = userDao.getAll();
        List<Catalog> catalogList = catalogDao.getAll();
        FileUploadInit fileUploadInit = new FileUploadInit();
        List<Selector> userSelector = new ArrayList<>();
        for (User u : userList) {
            if (!userId.equals(u.getId())) {//避免放入自己
                userSelector.add(new Selector(u.getId(), u.getName()));
            }
        }
        List<Selector> catalogSelector = new ArrayList<>();
        for (Catalog c : catalogList) {
            if (!c.getId().equals(c.getParentId())) {//避免放入根目录
                catalogSelector.add(new Selector(c.getId(), c.getName()));
            }
        }
        fileUploadInit.setUserSelector(userSelector);
        fileUploadInit.setCatalogSelector(catalogSelector);
        return fileUploadInit;
    }

    /**
     * 文件上传
     *
     * @param file            上传文件
     * @param fileUploadParam 文件上传参数
     * @param request         请求
     * @return json信息
     */
    @Override
    @CacheEvict(value = "userAlert", allEntries = true)
    public JsonResultVO upload(String author,MultipartFile file,
                               FileUploadParam fileUploadParam, HttpServletRequest request) {
        try {
            //写入文件
            String fileURL = FILE_PATH_HEAD;
            Map<String, String> fileName = new HashMap<>();
            fileName.put("fileName", file.getOriginalFilename());
            String fileRealName = uploadFileService.upload(file, fileURL, request);
            fileName.put("realName", fileRealName);

            //写入数据库
            fileDao.uploadFile(fileName, fileUploadParam);
        } catch (Exception e) {
            return new JsonResultVO(JsonResultVO.FAILURE, e.getMessage());
        }
//        if(fileUploadParam.isMailTo()) {
//            SendEmail sendEmail = new SendEmail(request,file.getOriginalFilename(),author,fileUploadParam);
//            sendEmail.setMailService(mailService);
//            sendEmail.setUserService(userService);
//            taskExecutor.execute(sendEmail); //线程执行发送邮件
//        }
        return new JsonResultVO(JsonResultVO.SUCCESS, "文件上传成功！");
    }

    @Override
    public JsonResultVO download(String userId, String versionId, HttpServletRequest request,
                                 HttpServletResponse response) {
        FileReceive fileReceive = fileReceiveDao.getReceiveBy(userId, versionId);
        FileVersion fileVersion = fileVersionDao.get(versionId);
        File file = fileDao.get(fileVersion.getFileId());
        if (!file.getUserId().equals(userId) && fileReceive == null) {
            return new JsonResultVO(JsonResultVO.FAILURE, "无权下载此文件！");
        }
        if (!file.getUsable()) { //判断文件是否被删除
            return new JsonResultVO(JsonResultVO.FAILURE, "文件已被删除！");
        }
        String fileName = file.getFileName();
        String filePath = FILE_PATH_HEAD + fileVersion.getRealName();
        try {
            FileDownloadUtil.FileDownload(request, response, filePath, fileName);
            //写入数据库
            if(file.getUserId().equals(userId)){//自己下载
                operateService.selfDownloadFile(userId,fileVersion);
            }else if(fileReceive != null){//他人下载
                operateService.downloadFile(fileReceive);
            }
        } catch (Exception e) {
            return new JsonResultVO(JsonResultVO.FAILURE, e.getMessage());
        }
        return new JsonResultVO(JsonResultVO.SUCCESS, "下载成功！");
    }

    @Override
    @Transactional
    public JsonResultVO reviseFile(String userId, MultipartFile file, String fileVersionId,
                                   boolean canCover, HttpServletRequest request) {
        FileVersion fileVersion = fileVersionDao.get(fileVersionId);
        File oldFile = fileDao.get(fileVersion.getFileId());
        if (oldFile.getUsable()) {
            try {
                //写入文件
                String fileURL = FILE_PATH_HEAD;
                Map<String, String> fileName = new HashMap<>();
                String fileRealName = uploadFileService.upload(file, fileURL, request);

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
        String newVersionId = UUID.randomUUID().toString();
        String oldVersionId = fileVersion.getId();
        fileVersion.setId(newVersionId);
        fileVersion.setCreateTime(data);
        Double number = fileVersion.getNumber();
        if (fileVersion.getCanCover()) {
            //以前版本可覆盖
            fileVersion.setNumber(number + 0.1);
        } else {
            int i = number.intValue();//取整
            fileVersion.setNumber(i + 1.0);
        }
        fileVersion.setCanCover(canCover);
        fileVersion.setCount(0);
        fileVersionDao.save(fileVersion);

        //更新最新文件版本编号
        File file = fileDao.get(fileVersion.getFileId());
        file.setNewVersionId(newVersionId);
        file.setCreateTime(data);
        fileDao.update(file);

        //写操作表
        Operate operate = new Operate();
        String operateId = UUID.randomUUID().toString();
        operate.setId(operateId);
        operate.setUserId(userId);
        operate.setVersionId(newVersionId);
        operate.setOperateTime(data);
        operate.setOperateFlag(2);
        operateDao.save(operate);

        //处理已接收和未接收相关消息
        List<FileReceive> fileReceiveList = fileReceiveDao.getReceivesByFileId(file.getId());
        for (FileReceive fileReceive : fileReceiveList) {
                fileReceive.setIsReceived(userId.equals(fileReceive.getUserId()));
                fileReceive.setVersionId(newVersionId);
                fileReceive.setDownloadTime(data);
                fileReceiveDao.update(fileReceive);
        }
    }

    @Override
    public FileUploadInit getReviseSelector(String userId, String fileId) {
        List<User> userList = userDao.getAll();
        List<FileReceive> fileReceiveList = fileReceiveDao.getReceivesByFileId(fileId);
        List<Catalog> catalogList = catalogDao.getAll();
        List<Selector> userSelector = new ArrayList<>();
        Set<String> canLoadIds = new HashSet<>();
        Set<String> canReviseIds = new HashSet<>();
        List<Selector> userCanLoadSelector = new ArrayList<>();
        List<Selector> userCanReviseSelector = new ArrayList<>();

        //计算当前版本可修改人
        for (FileReceive fr : fileReceiveList) {
            if (fr.getCanRevise()) {//可修改的人
                canReviseIds.add(fr.getUserId());
            } else {
                canLoadIds.add(fr.getUserId());
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
            if (!c.getId().equals(c.getParentId())) {//避免放入根目录
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

    @Override
    @Transactional
    public JsonResultVO deleteFile(String fileId, String userId) {
        try {
            //写文件表
            File file = fileDao.get(fileId);
            file.setUsable(false);
            fileDao.update(file);

            //写操作表
            Operate operate = new Operate();
            String operateId = UUID.randomUUID().toString();
            Date date = new Date();
            operate.setId(operateId);
            operate.setUserId(userId);
            operate.setVersionId(file.getNewVersionId());
            operate.setOperateTime(date);
            operate.setOperateFlag(3);
            operateDao.save(operate);
            return new JsonResultVO(JsonResultVO.SUCCESS, "删除成功!");
        } catch (Exception e) {
            return new JsonResultVO(JsonResultVO.FAILURE, "删除失败!");
        }
    }

    /**
     * 修改权限
     *
     * @param userId 当前用户
     * @return
     */
    @Override
    @Transactional
    public JsonResultVO reviseRole(String userId, String fileId, Set<String> canLoadIds, Set<String> canReviseIds) {
        File file = fileDao.get(fileId);
        String versionId = file.getNewVersionId();
        Date date = new Date();
        if (userId.equals(file.getUserId())) {
            List<FileReceive> fileReceiveList = fileReceiveDao.getReceivesByFileId(fileId);//当前可下载
            for (FileReceive f : fileReceiveList) {
                String receiveId = f.getId();
                if (canLoadIds.contains(receiveId)) {//可下载
                    if (f.getCanRevise()) {//可改写
                        f.setCanRevise(false);
                        fileReceiveDao.update(f);
                    }
                    canLoadIds.remove(receiveId);
                } else if (canReviseIds.contains(receiveId)) {
                    if (!f.getCanRevise()) {//改为可改写
                        f.setCanRevise(true);
                        fileReceiveDao.update(f);
                    }
                    canReviseIds.remove(receiveId);
                } else {//不存在则删除
                    fileReceiveDao.delete(receiveId);
                }
            }
            for (String userLoadId : canLoadIds) {//新建可读
                String rId = UUID.randomUUID().toString();
                FileReceive fileReceive = new FileReceive();
                fileReceive.setId(rId);
                fileReceive.setFileId(fileId);
                fileReceive.setVersionId(versionId);
                fileReceive.setUserId(userLoadId);
                fileReceive.setCanRevise(false);
                fileReceive.setDownloadTime(date);
                fileReceive.setIsAlert(false);
                fileReceive.setIsReceived(false);
                fileReceiveDao.save(fileReceive);
            }

            for (String reviseIds : canReviseIds) {//新建可改写
                String id = UUID.randomUUID().toString();
                FileReceive fileReceive = new FileReceive();
                fileReceive.setId(id);
                fileReceive.setFileId(fileId);
                fileReceive.setVersionId(versionId);
                fileReceive.setUserId(reviseIds);
                fileReceive.setCanRevise(true);
                fileReceive.setDownloadTime(date);
                fileReceive.setIsAlert(false);
                fileReceive.setIsReceived(false);
                fileReceiveDao.save(fileReceive);
            }
            return new JsonResultVO(JsonResultVO.SUCCESS, "权限修改成功！");
        } else return new JsonResultVO(JsonResultVO.FAILURE, "您无权修改本文件权限!");
    }

    /**
     * 查询紧急文件的信息
     *
     * @param userId
     * @return
     */
    @Override
    @Cacheable(value = "userAlert", key = "#userId") //TODO: 下载了紧急文件记得删除
    public UserAlertVo getAlertById(String userId) {
        UserAlertVo userAlertVo = new UserAlertVo();

        List<Map<String, Object>> alertList = getAlertBy(userId);
        Integer count = fileDao.getAlertCount(userId);

        List<UserAlert> alerts = new ArrayList<>();
        for (Map<String, Object> alert : alertList) {
            UserAlert userAlert = new UserAlert();
            userAlert.setAuthor(ObjectUtils.toString(alert.get("name")));
            int genderId = ObjectUtils.toInt(alert.get("gender"));
            int roleId = ObjectUtils.toInt(alert.get("role"));
            userAlert.setPictureName(HeadPictureUtil.getPictureName(genderId, roleId));
            userAlert.setFileName(ObjectUtils.toString(alert.get("file_name")));
            userAlert.setVersionId(ObjectUtils.toString(alert.get("version_id")));
            Date createTime = ObjectUtils.toDate(alert.get("create_time"));
            userAlert.setDelayTime(DateCalculateUtil.calculateDelayTime(createTime, new Date()));
            alerts.add(userAlert);
        }
        userAlertVo.setCount(count);
        userAlertVo.setAlertList(alerts);
        return userAlertVo;
    }

    @Override
    public List<Map<String, Object>> getAlertBy(String userId) {
        return getReceive(userId, USER_ALERT, 6);
    }

    @Override
    //    @Cacheable(value = "userReceive", key = "#userId") //TODO: 下载未接收,有人分享了文件记得删除
    public HomeFileReceiveVo getHomeFileNeedReceive(String userId) {

        HomeFileReceiveVo homeFileReceiveVo = new HomeFileReceiveVo();
        List<Map<String, Object>> receiveInfo = getReceive(userId, USER_RECEIVE, 8);
        Integer count = fileDao.getNeedReceiveCount(userId);
        homeFileReceiveVo.setCount(count);
        List<FileInfoVo> fileInfoVoList = loadReceiveInfo(receiveInfo, false);
        homeFileReceiveVo.setFileInfoVoList(fileInfoVoList);
        return homeFileReceiveVo;
    }

    @Override
    //    @Cacheable(value = "userReceived", key = "#userId") //TODO: 下载了未接收文件记得删除
    public List<FileInfoVo> getHomeFileReceived(String userId) {
        List<Map<String, Object>> receiveInfo = getReceive(userId, USER_RECEIVED, 8);
        return loadReceiveInfo(receiveInfo, true);
    }

    @Override
    //    @Cacheable(value = "userUploaded", key = "#userId") //TODO: 上传了文件记得删除
    public List<FileInfoVo> getHomeFileUploaded(String userId) {
        FileSearchKeys fileSearchKeys = new FileSearchKeys();
        fileSearchKeys.setUserId(userId);
        PageConfig pageConfig = new PageConfig();
        pageConfig.setPageNum(1);
        pageConfig.setPageSize(8);
        List<Map<String, Object>> uploadedInfo = fileDao.searchUploaded(fileSearchKeys, pageConfig);
        return loadUploadedInfo(uploadedInfo);
    }

    public PageVo<FileInfoVo> getPageFileNeedReceive(FileSearchKeys fileSearchKeys, PageConfig pageConfig) {
        fileSearchKeys.setSearchFlag(USER_RECEIVE);
        List<Map<String, Object>> infoList = fileDao.searchFilesAndCount(fileSearchKeys, pageConfig);
        List<FileInfoVo> fileInfoVoList = loadReceiveInfo(infoList, false);
        return new PageVo<>(fileInfoVoList, pageConfig);
    }

    @Override
    public PageVo<FileInfoVo> getPageFileReceived(FileSearchKeys fileSearchKeys, PageConfig pageConfig) {
        fileSearchKeys.setSearchFlag(USER_RECEIVED);
        List<Map<String, Object>> infoList = fileDao.searchFilesAndCount(fileSearchKeys, pageConfig);
        List<FileInfoVo> fileInfoVoList = loadReceiveInfo(infoList, true);
        return new PageVo<>(fileInfoVoList, pageConfig);
    }

    @Override
    public PageVo<FileInfoVo> getPageUploaded(FileSearchKeys fileSearchKeys, PageConfig pageConfig) {
        fileSearchKeys.setSearchFlag(USER_UPLOADED);
        List<Map<String, Object>> infoList = fileDao.searchFilesAndCount(fileSearchKeys, pageConfig);
        List<FileInfoVo> fileInfoVoList = loadUploadedInfo(infoList);
        return new PageVo<>(fileInfoVoList, pageConfig);
    }

    private List<Map<String, Object>> getReceive(String userId, int flag, int pageSize) {
        FileSearchKeys fileSearchKeys = new FileSearchKeys();
        fileSearchKeys.setUserId(userId);
        fileSearchKeys.setSearchFlag(flag);
        PageConfig pageConfig = new PageConfig();
        pageConfig.setPageNum(1);
        pageConfig.setPageSize(pageSize);
        return fileDao.searchReceive(fileSearchKeys, pageConfig);
    }

    /**
     * 装载页面接收文件信息
     *
     * @param receiveInfoList 查询返回
     * @param isReceive       是否接收
     * @return 页面值
     */
    private List<FileInfoVo> loadReceiveInfo(List<Map<String, Object>> receiveInfoList, boolean isReceive) {
        List<FileInfoVo> fileInfoVoList = new ArrayList<>();
        for (Map<String, Object> info : receiveInfoList) {
            FileInfoVo fileInfoVo = new FileInfoVo();
            fileInfoVo.setVersionId(ObjectUtils.toString(info.get("version_id")));
            fileInfoVo.setFileId(ObjectUtils.toString(info.get("file_id")));
            fileInfoVo.setFileName(ObjectUtils.toString(info.get("file_name")));
            fileInfoVo.setName(ObjectUtils.toString(info.get("name")));
            fileInfoVo.setCatalog(ObjectUtils.toString(info.get("catalog_name")));
            int departmentId = ObjectUtils.toInt(info.get("department"), 1);
            fileInfoVo.setDepartment(Department.values()[departmentId].getText());
            fileInfoVo.setVersionNumber(ObjectUtils.toDouble(info.get("version_number"), 1.0));
            fileInfoVo.setAlert(ObjectUtils.toString(info.get("is_alert")).equals("true"));
            if (!isReceive) {//未接收
                fileInfoVo.setCreateTime(ObjectUtils.toDate(info.get("create_time")));
            } else {
                fileInfoVo.setDownloadTime(ObjectUtils.toDate(info.get("download_time")));
                fileInfoVo.setCanRevise(ObjectUtils.toString(info.get("can_revise")).equals("true"));
            }
            fileInfoVoList.add(fileInfoVo);
        }
        return fileInfoVoList;
    }

    /**
     * 装载已上传页面信息
     *
     * @param uploadedInfoList 数据返回
     * @return 页面值
     */
    private List<FileInfoVo> loadUploadedInfo(List<Map<String, Object>> uploadedInfoList) {
        List<FileInfoVo> fileInfoVoList = new ArrayList<>();
        for (Map<String, Object> info : uploadedInfoList) {
            FileInfoVo fileInfoVo = new FileInfoVo();
            fileInfoVo.setFileId(ObjectUtils.toString(info.get("file_id")));
            fileInfoVo.setFileName(ObjectUtils.toString(info.get("file_name")));
            fileInfoVo.setCreateTime(ObjectUtils.toDate(info.get("create_time")));
            fileInfoVo.setCatalog(ObjectUtils.toString(info.get("catalog_name")));
            fileInfoVo.setVersionId(ObjectUtils.toString(info.get("version_id")));
            fileInfoVo.setVersionNumber(ObjectUtils.toDouble(info.get("version_number")));
            fileInfoVoList.add(fileInfoVo);
        }
        return fileInfoVoList;
    }

}
