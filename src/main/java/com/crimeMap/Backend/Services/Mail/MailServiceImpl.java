package com.crimeMap.Backend.Services.Mail;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService{
    @Autowired
    private JavaMailSender mailSender;


    @Override
    public void sendSimpleMessage(String to, String subject, String crimeType, String location) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            String htmlContent = "<h1 style='color: navy;'>Crime Alert</h1>" +
                    "<p style='font-family: Arial, sans-serif; font-size: 16px;'>Crime <strong>" + crimeType + "</strong> has been reported at <em>" + location + "</em>.</p>";

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new IllegalStateException("Failed to send email", e);
        }
    }
}
