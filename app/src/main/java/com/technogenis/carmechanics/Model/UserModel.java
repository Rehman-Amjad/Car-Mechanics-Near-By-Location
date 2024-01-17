package com.technogenis.carmechanics.Model;

public class UserModel {

    String name;
    String phoneNumber;
    String email;
    String password;
    String userUID;
    String accountCreationKey;

    public UserModel(String name, String phoneNumber, String email, String password, String userUID, String accountCreationKey) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.userUID = userUID;
        this.accountCreationKey = accountCreationKey;
    }

    public UserModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public String getAccountCreationKey() {
        return accountCreationKey;
    }

    public void setAccountCreationKey(String accountCreationKey) {
        this.accountCreationKey = accountCreationKey;
    }
}
