package com.example.sch;

import java.util.ArrayList;

public class Substance {

    private String sub_name;
    private String add_text;
    private double value;
    private int image;

    public Substance(String sub_name, String add_text, double value, int image) {
        this.sub_name = sub_name;
        this.add_text = add_text;
        this.value = value;
        this.image = image;
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

    public double getValue() {
        return value;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public void setValue(int value) {
        this.value = value;
    }
    public static ArrayList<Substance> createContactsList(int numContacts) {
        ArrayList<Substance> contacts = new ArrayList<Substance>();
        String[] subs = {"creatinine", "bnu", "potassium", "calcium"};
        int[] bLogos = {R.drawable.creatinine, R.drawable.bnu, R.drawable.potassium, R.drawable.calcium};
        for (int i = 0; i < numContacts; i++) {
            double rand = Math.random()*100;
            contacts.add(new Substance(subs[i%4], subs[i%4], rand, bLogos[i%4]));
        }

        return contacts;
    }
}
