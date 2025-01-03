package com.ctrledu.NotificationService.request;

import java.io.Serializable;

public class EmailRequest implements Serializable {

    private String to;
    private String subject;

    private String name;

    private String uniqueCode;

    public EmailRequest() {
    }

    public EmailRequest(String to, String subject, String name, String uniqueCode) {
        this.to = to;
        this.subject = subject;
        this.name = name;
        this.uniqueCode = uniqueCode;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }
}