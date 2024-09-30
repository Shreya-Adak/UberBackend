package com.codingshuttle.project.uber.UberAppBackend.services;


import org.springframework.stereotype.Service;


public interface EmailSenderService {

    void sendEmail(String toEmail, String subject, String body);

    void sendEmail(String toEmail[],String subject,String body);
}
