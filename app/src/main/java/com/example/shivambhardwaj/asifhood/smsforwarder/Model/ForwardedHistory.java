package com.example.shivambhardwaj.asifhood.smsforwarder.Model;

public class ForwardedHistory {
    String sender,message,chatId,Token,packageName,FordwardType;
    long forwardedAt;

    public ForwardedHistory() {
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getFordwardType() {
        return FordwardType;
    }

    public void setFordwardType(String fordwardType) {
        FordwardType = fordwardType;
    }

    public long getForwardedAt() {
        return forwardedAt;
    }

    public void setForwardedAt(long forwardedAt) {
        this.forwardedAt = forwardedAt;
    }
}
