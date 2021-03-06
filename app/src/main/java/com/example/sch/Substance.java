package com.example.sch;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Substance {

    private String sub_name,sub_info;
    private String add_text;
    private double value;
    private int logo,image;
    private ArrayList<Double> values;
    private ArrayList<String> dates;

    public Substance(String sub_name, String add_text, double value, int logo, String sub_info, int image, ArrayList<Double> values, ArrayList<String> dates) {
        this.sub_name = sub_name;
        this.add_text = add_text;
        this.value = value;
        this.logo = logo;
        this.sub_info = sub_info;
        this.image = image;
        this.values = values;
        this.dates = dates;
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

    public String getSub_info() {
        return sub_info;
    }

    public void setSub_info(String sub_info) {
        this.sub_info = sub_info;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public int getLogo() {
        return logo;
    }

    public void setLogo(int logo) {
        this.logo = logo;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public ArrayList<Double> getValues() {
        return values;
    }

    public void setValues(ArrayList<Double> values) {
        this.values = values;
    }

    public ArrayList<String> getDates() {
        return dates;
    }

    public void setDates(ArrayList<String> dates) {
        this.dates = dates;
    }

    public void setLatestValues(double value) {
        this.values.add(value);
    }

    public static ArrayList<Substance> createContactsList(int numContacts) {
        ArrayList<Substance> contacts = new ArrayList<Substance>();
        String[] subs = {"creatinine", "bun", "potassium", "calcium"};
        String[] subsinfo = { "Creatinine is a breakdown product of creatine phosphate from muscle and protein metabolism. It is released at a constant rate by the body (depending on muscle mass).",
                "Blood urea nitrogen (BUN) is a medical test that measures the amount of urea nitrogen found in blood. The liver produces urea in the urea cycle as a waste product of the digestion of protein. Normal human adult blood should contain 6 to 20 mg/dL (2.1 to 7.1 mmol/L) of urea nitrogen.",
                "Potassium is a chemical element with the symbol K (from Neo-Latin kalium) and atomic number 19. Potassium is a silvery-white metal that is soft enough to be cut with a knife with little force.",
                "Calcium is a chemical element with the symbol Ca and atomic number 20. As an alkaline earth metal, calcium is a reactive metal that forms a dark oxide-nitride layer when exposed to air."};
        int[] subsImage = {R.drawable.creatinineimage, R.drawable.bunimage, R.drawable.potassiumimage, R.drawable.calciumimage};
        int[] bLogos = {R.drawable.creatinine, R.drawable.albumine, R.drawable.potassium, R.drawable.calcium};
        for (int i = 0; i < numContacts; i++) {
            double rand = Math.random()*100;
            ArrayList<Double> values = new ArrayList<>();
            ArrayList<String> dates = new ArrayList<>();
            DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy");
            for(int j =0; j<5; j++){
                double randvalues = (float) (2.5+(float)Math.round(Math.random() * (7.5 - 2.5)));
                String date = df.format(Calendar.getInstance().getTime());

                values.add(randvalues);
                dates.add(date);
            }
            contacts.add(new Substance(subs[i%4], subs[i%4], rand, bLogos[i%4], subsinfo[i%4], subsImage[i%4], values, dates));
        }

        return contacts;
    }
}
