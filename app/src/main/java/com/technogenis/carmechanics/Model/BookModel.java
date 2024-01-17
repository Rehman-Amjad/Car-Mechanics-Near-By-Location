package com.technogenis.carmechanics.Model;

public class BookModel
{
    String name;
    String phoneNumber;
    String persons;
    String message;
    String bookDate;
    String bookTime;
    String bookKey;
    String userUID;
    String orderDate;
    String orderTime;
    String bookRequest;
    String bookGarageMarqueeName;
    String bookGarageAddress;
    String bookGarageContact;


    public BookModel(String name, String phoneNumber, String persons, String message, String bookDate, String bookTime, String bookKey, String userUID, String orderDate, String orderTime, String bookRequest, String bookGarageMarqueeName, String bookGarageAddress, String bookGarageContact) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.persons = persons;
        this.message = message;
        this.bookDate = bookDate;
        this.bookTime = bookTime;
        this.bookKey = bookKey;
        this.userUID = userUID;
        this.orderDate = orderDate;
        this.orderTime = orderTime;
        this.bookRequest = bookRequest;
        this.bookGarageMarqueeName = bookGarageMarqueeName;
        this.bookGarageAddress = bookGarageAddress;
        this.bookGarageContact = bookGarageContact;
    }

    public BookModel() {
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

    public String getPersons() {
        return persons;
    }

    public void setPersons(String persons) {
        this.persons = persons;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getBookDate() {
        return bookDate;
    }

    public void setBookDate(String bookDate) {
        this.bookDate = bookDate;
    }

    public String getBookTime() {
        return bookTime;
    }

    public void setBookTime(String bookTime) {
        this.bookTime = bookTime;
    }

    public String getBookKey() {
        return bookKey;
    }

    public void setBookKey(String bookKey) {
        this.bookKey = bookKey;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getBookRequest() {
        return bookRequest;
    }

    public void setBookRequest(String bookRequest) {
        this.bookRequest = bookRequest;
    }

    public String getBookGarageMarqueeName() {
        return bookGarageMarqueeName;
    }

    public void setBookGarageMarqueeName(String bookGarageMarqueeName) {
        this.bookGarageMarqueeName = bookGarageMarqueeName;
    }

    public String getBookGarageAddress() {
        return bookGarageAddress;
    }

    public void setBookGarageAddress(String bookGarageAddress) {
        this.bookGarageAddress = bookGarageAddress;
    }

    public String getBookGarageContact() {
        return bookGarageContact;
    }

    public void setBookGarageContact(String bookGarageContact) {
        this.bookGarageContact = bookGarageContact;
    }
}
