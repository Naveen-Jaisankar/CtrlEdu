package com.ctrledu.NotificationService.service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.io.IOException;
import java.util.Map;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Configuration freemarkerConfig;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Async("emailTaskExecutor")
    @Retryable(value = {MessagingException.class}, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    @CircuitBreaker(name = "emailCircuitBreaker", fallbackMethod = "fallbackSendTemplateEmail")
    public void sendTemplateEmail(String to, String subject, Map<String, Object> templateModel)
            throws MessagingException, IOException, TemplateException {

        // Prepare the Freemarker template
        Template template = freemarkerConfig.getTemplate("email-template.ftlh");
        StringWriter writer = new StringWriter();
        template.process(templateModel, writer);
        String htmlContent = writer.toString();

        // Prepare the email with the generated HTML content
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        helper.setFrom(fromEmail);

        mailSender.send(message);
    }

    public void fallbackSendTemplateEmail(String to, String subject, Map<String, Object> templateModel, Throwable throwable) {
        System.err.println("Fallback triggered: Could not send template email to " + to + ". Reason: " + throwable.getMessage());
    }
}
