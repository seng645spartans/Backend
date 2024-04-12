package com.crimeMap.Backend.Controllers;

import com.crimeMap.Backend.Services.Mail.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/mail")
public class MailServiceController {

    @Autowired
    private MailService emailService;

    @GetMapping("/send-email")
    public String sendEmail() {
        emailService.sendSimpleMessage("bharadwajreddy1999@gmail.com", "Subject", "Email Content");
        return "Email sent successfully";
    }


}
