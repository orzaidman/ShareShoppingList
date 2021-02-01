package com.or.shareshoppinglist.data;

import com.google.gson.Gson;

import java.util.ArrayList;

public class Family {
    private ArrayList< User> allFamilyUsers;
    private String familyID;
    private ArrayList<Item> allItems;
    private String familyPassword;



    public String getFamilyID() {
        return familyID;
    }

    public void setFamilyID(String familyID) {
        this.familyID = familyID;
    }

    public Family() {
        this.allFamilyUsers = new ArrayList<>();
        this.allItems = new ArrayList<>();
        this.familyID = "1234";
        this.familyPassword = "";
    }

    public Family(Family other) {
        this.allFamilyUsers = other.getAllFamilyUsers();
        this.allItems = other.getAllItems();
    }

    public String getStrList(){
        String temp = "";
        for (int i = 0; i < allItems.size(); i++) {
            if(!allItems.get(i).isTaken())
            temp+= allItems.get(i).getName()+"\n";
        }
        return temp;
    }
    public void add(User user)
    {
        allFamilyUsers.add(user);
    }
    public ArrayList<User> getAllFamilyUsers() {
        return allFamilyUsers;
    }

    public ArrayList< Item> getAllItems() {
        return allItems;
    }

    public void setAllFamilyUsers(ArrayList<User> allFamilyUsers) {
        this.allFamilyUsers = allFamilyUsers;
    }

    public String getFamilyPassword() {
        return familyPassword;
    }

    public void setFamilyPassword(String familyPassword) {
        this.familyPassword = familyPassword;
    }

    public void setItems(ArrayList< Item> allItems) {
        this.allItems = allItems;
    }

    public Family(String data) {
        this(createFamilyFromString(data));
    }

    public void clearAllItems(){
        this.allItems.clear();
    }
    private static Family createFamilyFromString(String data) {
        Family tempP;
        if (data == "NA") {
            tempP = new Family();
        }
        else {
            tempP = new Gson().fromJson(data, Family.class);
        }
        return tempP;
    }


    public void setAllFamily(ArrayList<User> allFamily) {
        this.allFamilyUsers = allFamily;
    }



    public void removeUser(User userToRemove){
        for (int i = 0; i < allFamilyUsers.size(); i++) {
            if (allFamilyUsers.get(i).getUuid().equals(userToRemove.getUuid()))
                allFamilyUsers.remove(i);
        }
    }

    public void updateUser(User userToUpdate) {
        for (int i = 0; i < allFamilyUsers.size(); i++) {
            if (userToUpdate.getUuid().equals(allFamilyUsers.get(i).getUuid()))
                allFamilyUsers.set(i,userToUpdate);
        }
    }
}
