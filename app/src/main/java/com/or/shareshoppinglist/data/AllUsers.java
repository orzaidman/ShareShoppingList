package com.or.shareshoppinglist.data;

import com.google.gson.Gson;

import java.util.ArrayList;

public class AllUsers {

    private ArrayList< User> allUsers;

    public AllUsers() {
        this.allUsers = new ArrayList<>();
    }

    public AllUsers(AllUsers other) {
        this.allUsers = other.allUsers;
    }

    public void add(User user)
    {
        allUsers.add(user);
    }
    public ArrayList<User> getAllUsers() {
        return allUsers;
    }

    public AllUsers(String data) {
        this(createPlayersFromString(data));
    }

    private static AllUsers createPlayersFromString(String data) {
        AllUsers tempP;
        if (data == "NA") {
            tempP = new AllUsers();
        }
        else {
            tempP = new Gson().fromJson(data, AllUsers.class);
        }
        return tempP;
    }


    public void removeUser(User userToRemove){
        for (int i = 0; i < allUsers.size(); i++) {
            if (allUsers.get(i).getUuid().equals(userToRemove.getUuid()))
                allUsers.remove(i);
        }
    }


 public void setAllUsers(ArrayList<User> allUsers) {
        this.allUsers = allUsers;
    }

    public User findUserByUUID(String uuid) {
        for (int i = 0; i < allUsers.size(); i++) {
            if(allUsers.get(i).getUuid().equals(uuid))
                return allUsers.get(i);
        }
        return null;
    }
}
