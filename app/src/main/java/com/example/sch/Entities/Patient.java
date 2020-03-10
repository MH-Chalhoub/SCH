package com.example.sch.Entities;

public class Patient {
    //private variables
    private int id;
    private String name;
    private int age;
    private String blood_groupe;
    private String gender;
    private int deleted;

    // Empty constructor
    public Patient(){

    }

    // constructor 1 with id
    public Patient(int id, String name, int age, String blood_groupe, String gender, int deleted){
        this.id = id;
        this.name = name;
        this.age = age;
        this.blood_groupe = blood_groupe;
        this.gender = gender;
        this.deleted = deleted;
    }

    // constructor 2 without id
    public Patient(String name, int age, String blood_groupe, String gender, int deleted){
        this.name = name;
        this.age = age;
        this.blood_groupe = blood_groupe;
        this.gender = gender;
        this.deleted = deleted;
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

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", blood_groupe='" + blood_groupe + '\'' +
                ", gender='" + gender + '\'' +
                ", deleted=" + deleted +
                '}';
    }
}
