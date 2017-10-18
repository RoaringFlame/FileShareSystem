package com.fss.service.impl;

import com.fss.controller.vo.FileUploadInit;
import com.fss.controller.vo.FileUploadParam;
import com.fss.controller.vo.JsonResultVO;
import com.fss.dao.domain.*;
import com.fss.dao.repositories.*;
import com.fss.service.FileService;
import com.fss.service.MailService;
import com.fss.service.UploadService;
import com.fss.service.UserService;
import com.fss.util.Selector;
import com.fss.util.SendEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
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
        if(fileUploadParam.isMailTo()) {
            SendEmail sendEmail = new SendEmail(file.getOriginalFilename(),author,fileUploadParam);
            sendEmail.setMailService(mailService);
            sendEmail.setUserService(userService);
            taskExecutor.execute(sendEmail); //线程执行发送邮件
        }
        return new JsonResultVO(JsonResultVO.SUCCESS, "文件上传成功！");
    }

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
        file.setNewVersionId(fileVersion.getId());
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
}
