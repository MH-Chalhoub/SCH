package com.example.sch;

import java.util.ArrayList;

public class Substance {

    private String sub_name;
    private String add_text;
    private int value;

    public Substance(String sub_name, String add_text, int value) {
        this.sub_name = sub_name;
        this.add_text = add_text;
        this.value = value;
    }

    public Substance() {
    }

    public String getSub_name() {
        return sub_name;
    }

    public void setSub_name(String sub_name) {
        this.sub_name = sub_name;
    }

    public String getAdd_text() {
        return add_text;
    }

    public void setAdd_text(String add_text) {
        this.add_text = add_text;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
    public static ArrayList<Substance> createContactsList(int numContacts) {
        ArrayList<Substance> contacts = new ArrayList<Substance>();
        String[] subs = {"Creatinine", "BNU", "Potassium", "Calcium"};
        for (int i = 0; i < numContacts; i++) {
            contacts.add(new Substance(subs[i%4], subs[i%4], 50));
        }

        return contacts;
    }
}
