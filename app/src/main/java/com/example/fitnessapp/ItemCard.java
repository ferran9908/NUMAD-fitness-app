package com.example.fitnessapp;

import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.io.Serializable;

public class ItemCard implements Serializable {

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

    @Override
    public String toString() {
        return "ItemCard{" +
                "itemName='" + itemName + '\'' +
                ", itemDesc='" + itemDesc + '\'' +
                '}';
    }
}
