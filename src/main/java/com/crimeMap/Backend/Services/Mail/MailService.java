package com.crimeMap.Backend.Services.Mail;


import org.springframework.stereotype.Service;


public interface MailService {
    void sendSimpleMessage(String to, String subject, String text);
}
