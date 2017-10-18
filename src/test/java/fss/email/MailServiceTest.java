package fss.email;

import com.fss.service.MailService;
import com.icegreen.greenmail.spring.GreenMailBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.mail.Address;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
public class MailServiceTest {
    @Autowired
    private MailService mailService;

    @Autowired
    private GreenMailBean mailServer;

    @Test
    public void sendSimpleSpittleEmail() throws Exception {
        String to = "13260592767@163.com";
        String subject = "mail test!";
        String content = "this is a test for File Share System to send email.";
        mailService.sendSimpleEmail(to, subject,content);

        MimeMessage[] receivedMessages = mailServer.getReceivedMessages();
        assertEquals(1,  receivedMessages.length);
        assertEquals("mail test!", receivedMessages[0].getSubject());
        assertEquals("this is a test for File Share System to send email.", ((String) receivedMessages[0].getContent()).trim());
        Address[] from = receivedMessages[0].getFrom();
        assertEquals(1, from.length);
        assertEquals("747522309@qq.com", ((InternetAddress) from[0]).getAddress());
        assertEquals("13260592767@163.com", ((InternetAddress) receivedMessages[0].getRecipients(MimeMessage.RecipientType.TO)[0]).getAddress());
    }
}
