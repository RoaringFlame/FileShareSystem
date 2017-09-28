package com.fss.config;

import com.sun.mail.util.MailSSLSocketFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.security.GeneralSecurityException;
import java.util.Properties;

@Configuration
@ComponentScan("com.fss.service.impl")
@PropertySource("classpath:conf/config.mail/config.mail.properties")
public class MailConfig {

  @Bean
  public JavaMailSenderImpl mailSender(Environment env) throws GeneralSecurityException {
    final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
    // Get a Properties object
    Properties props = System.getProperties();
    MailSSLSocketFactory sf = new MailSSLSocketFactory();
    sf.setTrustAllHosts(true);
    props.put("config.mail.smtp.ssl.enable", "true");
    props.put("config.mail.smtp.ssl.socketFactory", sf);
    props.setProperty("config.mail.smtp.socketFactory.class", SSL_FACTORY);
    props.setProperty("config.mail.smtp.socketFactory.fallback", "false");
    props.put("config.mail.transport.protocol", "smtp");
    props.setProperty("config.mail.smtp.socketFactory.port", "465");
    props.put("config.mail.smtp.auth", "true");

    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    mailSender.setHost(env.getProperty("mailserver.host"));
    mailSender.setPort(Integer.parseInt(env.getProperty("mailserver.port")));
    mailSender.setUsername(env.getProperty("mailserver.username"));
    mailSender.setPassword(env.getProperty("mailserver.password"));
    mailSender.setJavaMailProperties(props);
    mailSender.setDefaultEncoding("UTF-8");
    return mailSender;
  }  
  
}
