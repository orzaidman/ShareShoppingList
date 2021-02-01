package com.or.shareshoppinglist.data;

import com.google.gson.Gson;

import java.util.ArrayList;

public class AllFamilies {

    private ArrayList< Family> allFamilies;

    public AllFamilies() {
        this.allFamilies = new ArrayList<>();
    }

    public AllFamilies(AllFamilies other) {
        this.allFamilies = other.allFamilies;
    }

    public void add(Family family)
    {
        family.setFamilyID((allFamilies.size())+"");
        allFamilies.add(family);
    }
    public ArrayList<Family> getAllFamilies() {
        return allFamilies;
    }

    public AllFamilies(String data) {
        this(createPlayersFromString(data));
    }

    private static AllFamilies createPlayersFromString(String data) {
        AllFamilies tempP;
        if (data == "NA") {
            tempP = new AllFamilies();
        }
        else {
            tempP = new Gson().fromJson(data, AllFamilies.class);
        }
        return tempP;
    }



    public void setAllFamilies(ArrayList<Family> allFamilies) {
        this.allFamilies = allFamilies;
    }

    public Family getFamilyByID(String id) {
        for (int i = 0; i < allFamilies.size(); i++) {
            if(allFamilies.get(i).getFamilyID().equals(id))
                return allFamilies.get(i);
        }
        return null;
    }



}
