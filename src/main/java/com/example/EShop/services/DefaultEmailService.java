package com.example.EShop.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultEmailService {
    @Value("${spring.mail.username}")
    private  String from;
    private final JavaMailSender emailSender;


    public void sendSimpleEmail(String toAddress, String subject,  String order) {

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(toAddress);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(order);
        simpleMailMessage.setFrom(from);
        emailSender.send(simpleMailMessage);
    }


}
