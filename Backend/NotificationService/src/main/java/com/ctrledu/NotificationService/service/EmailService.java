package com.ctrledu.NotificationService.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.retry.annotation.Backoff;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail; // This fetches email from the application.properties file


    @Async("emailTaskExecutor")
    @Retryable(
            value = {MessagingException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)
    )
    @CircuitBreaker(name="emailCircuitBreaker", fallbackMethod = "fallbackSendEmail")
    public void sendEmail(String to, String subject, String text) throws MessagingException {
        System.out.println("Going to send email");

        System.out.println(fromEmail);

        System.out.println(to);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,"utf-8");
        helper.setText(text,true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setFrom(fromEmail);
        mailSender.send(message);
    }

    public void fallbackSendEmail(String to, String subject, String text, Throwable throwable){
        System.out.println("Fallback : Failed to send mail to " + to + ". Reason : "  +throwable.getMessage());
    }
}
