package com.technogenis.carmechanics.Model;

public class ComplainModel
{
    String message;
    String senderUID;
    String receivedUID;
    String sendingTime;
    String sendingDate;
    String chatKey;
    String garageName;
    String messageType;
    String garageContactNumber;

    public ComplainModel(String message, String senderUID, String receivedUID, String sendingTime, String sendingDate, String chatKey, String garageName, String messageType, String garageContactNumber) {
        this.message = message;
        this.senderUID = senderUID;
        this.receivedUID = receivedUID;
        this.sendingTime = sendingTime;
        this.sendingDate = sendingDate;
        this.chatKey = chatKey;
        this.garageName = garageName;
        this.messageType = messageType;
        this.garageContactNumber = garageContactNumber;
    }

    public ComplainModel() {
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

    public String getGarageName() {
        return garageName;
    }

    public void setGarageName(String garageName) {
        this.garageName = garageName;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getGarageContactNumber() {
        return garageContactNumber;
    }

    public void setGarageContactNumber(String garageContactNumber) {
        this.garageContactNumber = garageContactNumber;
    }
}
