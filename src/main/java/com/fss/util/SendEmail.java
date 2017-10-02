package com.fss.util;


import com.fss.controller.vo.FileUploadParam;
import com.fss.dao.domain.User;
import com.fss.service.MailService;
import com.fss.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class SendEmail implements Runnable {

    private HttpServletRequest request;
    private String fileName;
    private String author;
    private FileUploadParam fileUploadParam;
    private MailService mailService;
    private UserService userService;

    public SendEmail(HttpServletRequest request, String fileName, String author, FileUploadParam fileUploadParam) {
        this.request = request;
        this.fileName = fileName;
        this.author = author;
        this.fileUploadParam = fileUploadParam;
    }

    public MailService getMailService() {
        return mailService;
    }

    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override public void run() {
        try {
            List<String> sendIds = fileUploadParam.getCanOnlyLoadUserIds();
            sendIds.addAll(fileUploadParam.getCanReviseUserIds());
            List<User> userLoad = userService.getByUserIdList(sendIds);
            for (User user : userLoad) {
                String content = user.getName() + "，您好！" + this.author + "分享给您了一个文件《" + fileName + "》，请登录文件分享系统查收！";
                mailService.sendSimpleEmail(request, user.getEmail(), content);
            }
        } catch (Exception e) {
            System.out.printf(e.getMessage());
        }
    }
}
