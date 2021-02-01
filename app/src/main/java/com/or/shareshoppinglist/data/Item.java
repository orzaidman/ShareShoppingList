package com.or.shareshoppinglist.data;

public class Item {
    private String name;
    private boolean isTaken = false;

    public Item(String name, boolean isTaken) {
        this.name = name;
        this.isTaken = isTaken;
    }

    public boolean isTaken() {
        return isTaken;
    }

    public void setTaken(boolean taken) {
        this.isTaken = taken;
    }

    public Item() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
