package com.jobizzz.ctrledu.notification.service;

import com.jobizzz.ctrledu.notification.request.EmailRequest;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailQueueService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    //This method add the email to the message queue
    public void enqueueEmail(String to, String subject, String messageBody){
        EmailRequest emailMessage = new EmailRequest(to,subject,messageBody);
        rabbitTemplate.convertAndSend("emailQueue", emailMessage);
    }
}
