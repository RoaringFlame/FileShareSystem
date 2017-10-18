package com.fss.service.impl;

import com.fss.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.File;

@Component
public class MailServiceImpl implements MailService {

    private JavaMailSender mailSender;

    @Autowired
    public MailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendSimpleEmail(String to, String subject,String context) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("747522309@qq.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(context);
        mailSender.send(message);
    }

    @Override
    public void sendEmailWithAttachment(HttpServletRequest request, String to, String context,
                                        String fileName) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom("747522309@qq.com");
        helper.setTo(to);
        helper.setSubject("测试发送带附件的邮件");
        helper.setText(context);
        String filePath = "/upload/" + fileName;
        String realPath = request.getSession().getServletContext().getRealPath(filePath);
        File file = new File(realPath);
        helper.addAttachment("test.txt", file);
        mailSender.send(message);
    }

}
