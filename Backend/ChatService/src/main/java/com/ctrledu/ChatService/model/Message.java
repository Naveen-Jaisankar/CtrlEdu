package com.ctrledu.ChatService.model;

public class Message {
    private String sender;
    private String content;
    private String topic;

    private long timestamp;
    private int seqNum;

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

    public Message() {
    }

    public Message(String sender, String content, String topic, int seqNum, long timestamp) {
        this.sender = sender;
        this.content = content;
        this.topic = topic;
        this.seqNum = seqNum;
        this.timestamp = timestamp;
    }


    @Override
    public String toString() {
        return "Message{" +
                "sender='" + sender + '\'' +
                ", content='" + content + '\'' +
                ", topic='" + topic + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", seqNum='" + Integer.toString(seqNum) + '\'' +
                '}';
    }
}
