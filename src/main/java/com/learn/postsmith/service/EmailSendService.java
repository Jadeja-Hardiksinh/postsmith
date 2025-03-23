package com.learn.postsmith.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSendService {
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendTextMail(String from, String to, String body) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(from);
        mailMessage.setTo(to);
        mailMessage.setText(body);
        javaMailSender.send(mailMessage);
    }

}
