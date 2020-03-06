package com.example.sch.Entities;

public class Patient {
    //private variables
    private int id;
    private int age;
    private String name;
    private String phone_number;
    private String blood_groupe;
    private String gender;

    // Empty constructor
    public Patient(){

    }

    // constructor 1 with id
    public Patient(int id, int age, String name, String phone_number, String blood_groupe, String gender){
        this.id = id;
        this.age = age;
        this.name = name;
        this.phone_number = phone_number;
        this.blood_groupe = blood_groupe;
        this.gender = gender;
    }

    // constructor 2 without id
    public Patient(int age, String name, String phone_number, String blood_groupe, String gender){
        this.age = age;
        this.name = name;
        this.phone_number = phone_number;
        this.blood_groupe = blood_groupe;
        this.gender = gender;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getBlood_groupe() {
        return blood_groupe;
    }

    public void setBlood_groupe(String blood_groupe) {
        this.blood_groupe = blood_groupe;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", age=" + age +
                ", name='" + name + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", blood_groupe='" + blood_groupe + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }
}
