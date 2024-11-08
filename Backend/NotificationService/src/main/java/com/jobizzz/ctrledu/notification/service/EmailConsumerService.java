package com.jobizzz.ctrledu.notification.service;

import com.jobizzz.ctrledu.notification.request.EmailRequest;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailConsumerService {

    @Autowired
    private EmailService emailService;

    @RabbitListener(queues = "emailQueue")
    public void processEmailQueue(EmailRequest emailRequest){
        try{
            emailService.sendEmail(emailRequest.getTo(),emailRequest.getSubject(), emailRequest.getBody());
        }catch(Exception e){
            System.err.println("Error sending email to: " + emailRequest.getTo());
        }

    }
}
