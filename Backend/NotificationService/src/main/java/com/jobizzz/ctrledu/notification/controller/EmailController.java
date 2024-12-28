package com.jobizzz.ctrledu.notification.controller;

import com.jobizzz.ctrledu.notification.service.EmailQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notification/email")
public class EmailController {

    @Autowired
    private EmailQueueService emailQueueService;

    @PostMapping("/send")
    public String sendEmails(@RequestBody String recipients){
        String subject = "Test Subject";
        String messageBody = "<h1>Test Email</h1><p>This is a test email.</p>";
//        for (String recipient : recipients) {
//            emailQueueService.enqueueEmail(recipient, subject, messageBody);
//        }
        emailQueueService.enqueueEmail(recipients, subject, messageBody);
        return "Emails are being processed!";

    }

//    @PostMapping("/sendEmail")
//    public String sendEmail(@RequestParam String to, @RequestParam String subject, @RequestParam String body) {
//        emailQueueService.enqueueEmail(to, subject, body);
//        return "Email request queued successfully!";
//    }

}
