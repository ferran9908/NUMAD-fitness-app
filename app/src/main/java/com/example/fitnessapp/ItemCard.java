package com.example.fitnessapp;

public class ItemCard {

    private final String itemName;
    private final String itemDesc;

    public ItemCard(String linkName, String linkDesc) {
        this.itemName = linkName;
        this.itemDesc = linkDesc;
    }


    public String getItemName() {
        return itemName;
    }

    public String getItemDesc() {
        return itemDesc;
    }
}
