package com.or.shareshoppinglist.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import static android.content.Context.MODE_PRIVATE;

public class MySheredP {
    private SharedPreferences prefs;
    private Gson gson = new Gson();


    public MySheredP(Context context) {
         prefs = context.getSharedPreferences("MyPref1", MODE_PRIVATE);
    }


    public String getString(String key, String defValue)
    {
       return prefs.getString(key  , defValue);
    }

    public void putString(String key, String value)
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public int getInt(String key, int defValue)
    {
        return prefs.getInt(key  , defValue);
    }


    public void putInt(String key, int value)
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public void putOnMSP(AllFamilies allFamilies, AllUsers allUsers, User user, Family family) {
        String jsonAllFamilies = gson.toJson(allFamilies);
        putString(Constants.KEY_ALL_FAMILIES, jsonAllFamilies);

        String json2 = gson.toJson(allUsers);
        putString(Constants.KEY_ALL_USERS, json2);

        String json3 = gson.toJson(user);
        putString(Constants.KEY_USER, json3);

        if (family == null)
            family = allFamilies.getFamilyByID(user.getFamilyID());

        String json4 = gson.toJson(family);
        putString(Constants.KEY_FAMILY, json4);
    }

    public void putOnMSP(AllUsers allUsers, User user, Family family) {
        String json2 = gson.toJson(allUsers);
        putString(Constants.KEY_ALL_USERS, json2);

        String json3 = gson.toJson(user);
        putString(Constants.KEY_USER, json3);

        String json4 = gson.toJson(family);
        putString(Constants.KEY_FAMILY, json4);
    }

    public void putOnMSP(Family family) {
        String json4 = gson.toJson(family);
        putString(Constants.KEY_FAMILY, json4);
    }

    public void removeKey(String key)
    {
        prefs.edit().remove(key);
    }


    public boolean isValid()
    {
        if(!prefs.contains("initialized"))
            return true;
        return false;
    }
}
