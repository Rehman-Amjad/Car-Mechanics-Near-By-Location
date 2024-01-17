package com.technogenis.carmechanics.Model;

public class AdminChatListModel
{
    String garageAddKey;
    String ownerUserUID;
    String senderUID;
    String userEmail;
    String garageName;

    public AdminChatListModel(String garageAddKey, String ownerUserUID, String senderUID, String userEmail, String garageName) {
        this.garageAddKey = garageAddKey;
        this.ownerUserUID = ownerUserUID;
        this.senderUID = senderUID;
        this.userEmail = userEmail;
        this.garageName = garageName;
    }

    public AdminChatListModel() {
    }

    public String getGarageAddKey() {
        return garageAddKey;
    }

    public void setGarageAddKey(String garageAddKey) {
        this.garageAddKey = garageAddKey;
    }

    public String getOwnerUserUID() {
        return ownerUserUID;
    }

    public void setOwnerUserUID(String ownerUserUID) {
        this.ownerUserUID = ownerUserUID;
    }

    public String getSenderUID() {
        return senderUID;
    }

    public void setSenderUID(String senderUID) {
        this.senderUID = senderUID;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getGarageName() {
        return garageName;
    }

    public void setGarageName(String garageName) {
        this.garageName = garageName;
    }
}
