package com.ctrledu.ChatService.model;

import jakarta.validation.constraints.NotNull;

public class Message {
    private String sender;
    private String content;
    private String topic;
    private long timestamp;
    private int seqNum;
    @NotNull
    private String classId; // New field to link the message to a class
    @NotNull
    private String moduleId; // New field to link the message to a module

    // Getters and Setters
    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getSeqNum() {
        return seqNum;
    }

    public void setSeqNum(int seqNum) {
        this.seqNum = seqNum;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    // Constructors
    public Message() {}

    public Message(String sender, String content, String topic, int seqNum, long timestamp, String classId, String moduleId) {
        this.sender = sender;
        this.content = content;
        this.topic = topic;
        this.seqNum = seqNum;
        this.timestamp = timestamp;
        this.classId = classId;
        this.moduleId = moduleId;
    }

    @Override
    public String toString() {
        return "Message{" +
                "sender='" + sender + '\'' +
                ", content='" + content + '\'' +
                ", topic='" + topic + '\'' +
                ", timestamp=" + timestamp +
                ", seqNum=" + seqNum +
                ", classId='" + classId + '\'' +
                ", moduleId='" + moduleId + '\'' +
                '}';
    }
}
