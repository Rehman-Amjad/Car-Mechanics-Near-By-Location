package com.technogenis.carmechanics.Model;

public class ChatModel
{
    String message;
    String senderUID;
    String receivedUID;
    String sendingTime;
    String sendingDate;
    String chatKey;
    String garageOwnerName;
    String messageType;

    public ChatModel(String message, String senderUID, String receivedUID, String sendingTime, String sendingDate, String chatKey, String garageOwnerName, String messageType) {
        this.message = message;
        this.senderUID = senderUID;
        this.receivedUID = receivedUID;
        this.sendingTime = sendingTime;
        this.sendingDate = sendingDate;
        this.chatKey = chatKey;
        this.garageOwnerName = garageOwnerName;
        this.messageType = messageType;
    }

    public ChatModel() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderUID() {
        return senderUID;
    }

    public void setSenderUID(String senderUID) {
        this.senderUID = senderUID;
    }

    public String getReceivedUID() {
        return receivedUID;
    }

    public void setReceivedUID(String receivedUID) {
        this.receivedUID = receivedUID;
    }

    public String getSendingTime() {
        return sendingTime;
    }

    public void setSendingTime(String sendingTime) {
        this.sendingTime = sendingTime;
    }

    public String getSendingDate() {
        return sendingDate;
    }

    public void setSendingDate(String sendingDate) {
        this.sendingDate = sendingDate;
    }

    public String getChatKey() {
        return chatKey;
    }

    public void setChatKey(String chatKey) {
        this.chatKey = chatKey;
    }

    public String getGarageOwnerName() {
        return garageOwnerName;
    }

    public void setGarageOwnerName(String garageOwnerName) {
        this.garageOwnerName = garageOwnerName;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }
}
