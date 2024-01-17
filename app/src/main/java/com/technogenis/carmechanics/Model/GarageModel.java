package com.technogenis.carmechanics.Model;

public class GarageModel
{
    String garageName;
    String garageOwnerName;
    String garageContactNumber;
    String garageBio;
    String ownerUserUID;
    String garageAddKey;
    String currentTime;
    String currentDate;
    String garageCoverLink;
    String garageAddress;

    public GarageModel(String garageName, String garageOwnerName, String garageContactNumber, String garageBio, String ownerUserUID, String garageAddKey, String currentTime, String currentDate, String garageCoverLink, String garageAddress) {
        this.garageName = garageName;
        this.garageOwnerName = garageOwnerName;
        this.garageContactNumber = garageContactNumber;
        this.garageBio = garageBio;
        this.ownerUserUID = ownerUserUID;
        this.garageAddKey = garageAddKey;
        this.currentTime = currentTime;
        this.currentDate = currentDate;
        this.garageCoverLink = garageCoverLink;
        this.garageAddress = garageAddress;
    }

    public GarageModel() {
    }

    public String getGarageName() {
        return garageName;
    }

    public void setGarageName(String garageName) {
        this.garageName = garageName;
    }

    public String getGarageOwnerName() {
        return garageOwnerName;
    }

    public void setGarageOwnerName(String garageOwnerName) {
        this.garageOwnerName = garageOwnerName;
    }

    public String getGarageContactNumber() {
        return garageContactNumber;
    }

    public void setGarageContactNumber(String garageContactNumber) {
        this.garageContactNumber = garageContactNumber;
    }

    public String getGarageBio() {
        return garageBio;
    }

    public void setGarageBio(String garageBio) {
        this.garageBio = garageBio;
    }

    public String getOwnerUserUID() {
        return ownerUserUID;
    }

    public void setOwnerUserUID(String ownerUserUID) {
        this.ownerUserUID = ownerUserUID;
    }

    public String getGarageAddKey() {
        return garageAddKey;
    }

    public void setGarageAddKey(String garageAddKey) {
        this.garageAddKey = garageAddKey;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getGarageCoverLink() {
        return garageCoverLink;
    }

    public void setGarageCoverLink(String garageCoverLink) {
        this.garageCoverLink = garageCoverLink;
    }

    public String getGarageAddress() {
        return garageAddress;
    }

    public void setGarageAddress(String garageAddress) {
        this.garageAddress = garageAddress;
    }
}
