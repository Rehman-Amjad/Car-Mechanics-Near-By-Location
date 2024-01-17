package com.technogenis.carmechanics.Model;

public class ServicesModel
{
    String serviceCategory;
    String serviceProviderName;
    String serviceCharges;
    String servicesDetails;
    String serviceAddKey;
    String userUID;
    String serviceAddDate;
    String serviceAddTime;
    String garageAddKey;
    String garageOwnerName;

    public ServicesModel(String serviceCategory, String serviceProviderName, String serviceCharges, String servicesDetails, String serviceAddKey, String userUID, String serviceAddDate, String serviceAddTime, String garageAddKey, String garageOwnerName) {
        this.serviceCategory = serviceCategory;
        this.serviceProviderName = serviceProviderName;
        this.serviceCharges = serviceCharges;
        this.servicesDetails = servicesDetails;
        this.serviceAddKey = serviceAddKey;
        this.userUID = userUID;
        this.serviceAddDate = serviceAddDate;
        this.serviceAddTime = serviceAddTime;
        this.garageAddKey = garageAddKey;
        this.garageOwnerName = garageOwnerName;
    }

    public ServicesModel() {
    }

    public String getServiceCategory() {
        return serviceCategory;
    }

    public void setServiceCategory(String serviceCategory) {
        this.serviceCategory = serviceCategory;
    }

    public String getServiceProviderName() {
        return serviceProviderName;
    }

    public void setServiceProviderName(String serviceProviderName) {
        this.serviceProviderName = serviceProviderName;
    }

    public String getServiceCharges() {
        return serviceCharges;
    }

    public void setServiceCharges(String serviceCharges) {
        this.serviceCharges = serviceCharges;
    }

    public String getServicesDetails() {
        return servicesDetails;
    }

    public void setServicesDetails(String servicesDetails) {
        this.servicesDetails = servicesDetails;
    }

    public String getServiceAddKey() {
        return serviceAddKey;
    }

    public void setServiceAddKey(String serviceAddKey) {
        this.serviceAddKey = serviceAddKey;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public String getServiceAddDate() {
        return serviceAddDate;
    }

    public void setServiceAddDate(String serviceAddDate) {
        this.serviceAddDate = serviceAddDate;
    }

    public String getServiceAddTime() {
        return serviceAddTime;
    }

    public void setServiceAddTime(String serviceAddTime) {
        this.serviceAddTime = serviceAddTime;
    }

    public String getGarageAddKey() {
        return garageAddKey;
    }

    public void setGarageAddKey(String garageAddKey) {
        this.garageAddKey = garageAddKey;
    }

    public String getGarageOwnerName() {
        return garageOwnerName;
    }

    public void setGarageOwnerName(String garageOwnerName) {
        this.garageOwnerName = garageOwnerName;
    }
}
