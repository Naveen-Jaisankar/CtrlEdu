package com.ctrledu.NotificationService.service;

import com.ctrledu.NotificationService.request.EmailRequest;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EmailConsumerService {

    @Autowired
    private EmailService emailService;

    @RabbitListener(queues = "emailQueue")
    public void processEmailQueue(EmailRequest emailRequest) {
        try {
            Map<String, Object> model = Map.of(
                    "recipientName", emailRequest.getTo(),
                    "messageBody", emailRequest.getName(),
                    "subject", emailRequest.getSubject(),
                    "uniqueCode",emailRequest.getUniqueCode()
            );
            emailService.sendTemplateEmail(emailRequest.getTo(), emailRequest.getSubject(), model);
        } catch (Exception e) {
            System.err.println("Error sending template email to: " + emailRequest.getTo());
        }
    }

}