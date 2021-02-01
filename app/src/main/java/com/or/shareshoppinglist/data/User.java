package com.or.shareshoppinglist.data;

import com.google.gson.Gson;

public class User {

    public enum USER_TYPE
    {
        CLIENT,MANAGER;
    }

    private String uuid;
    private String firstName;
    private String lastName;
    private String imageUser;
    private String phoneNumber;
    private  USER_TYPE userType;
    private String familyID;
    private String password;


    public User(String uuid, String firstName, String lastName, String imageUser, String phoneNumber,
                USER_TYPE userType, String familyID,String password ) {
        this.uuid = uuid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.imageUser = imageUser;
        this.phoneNumber = phoneNumber;
        this.userType = userType;
        this.familyID = familyID;
        this.password = password;
    }

    public User() {

    }

    public  User(String data) {
        new Gson().fromJson(data, User.class);
    }



    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFamilyID() {
        return familyID;
    }

    public void setFamilyID(String familyID) {
        this.familyID = familyID;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getImageUser() {
        return imageUser;
    }

    public void setImageUser(String imageUser) {
        this.imageUser = imageUser;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public USER_TYPE getUserType() {
        return userType;
    }

    public void setUserType(USER_TYPE userType) {
        this.userType = userType;
    }
}
