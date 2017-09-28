package com.fss.service;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

public interface IMailService {

    public abstract void sendSimpleEmail(HttpServletRequest request, String to, String context);

    public abstract void sendEmailWithAttachment(HttpServletRequest request, String to, String context, String filePath)
            throws MessagingException;

}