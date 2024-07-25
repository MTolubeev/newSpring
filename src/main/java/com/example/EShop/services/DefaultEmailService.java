package com.example.EShop.services;

import com.example.EShop.models.Order;
import com.example.EShop.models.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DefaultEmailService {
    @Value("${spring.mail.username}")
    private  String from;
    private final JavaMailSender emailSender;


    public void sendSimpleEmail(String toAddress, String subject,  List<Order> order) {

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(toAddress);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(String.valueOf(order));
        simpleMailMessage.setFrom(from);
        emailSender.send(simpleMailMessage);
    }


}
