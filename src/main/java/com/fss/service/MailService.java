package com.fss.service;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

public interface MailService {

    public abstract void sendSimpleEmail(String to, String subject,String context);

    public abstract void sendEmailWithAttachment(HttpServletRequest request, String to, String context, String filePath)
            throws MessagingException;

}