package com.ctrledu.NotificationService.controller;

import com.ctrledu.NotificationService.service.EmailQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/notify/email")
public class EmailController {

    @Autowired
    private EmailQueueService emailQueueService;

    @PostMapping("/send")
    public String sendEmails(@RequestBody Map<String, Object> emailPayload){
        System.out.println("Send Emails triggered");

        String toEmail = (String) emailPayload.get("to");
        String subject = (String) emailPayload.get("subject");
        String name = (String) emailPayload.get("firstName") + (String) emailPayload.get("lastName");
        String uniqueCode = (String) emailPayload.get("uniqueCode");


        emailQueueService.enqueueEmail(toEmail,subject,name,uniqueCode);
        return "Emails are being processed!";

    }
}