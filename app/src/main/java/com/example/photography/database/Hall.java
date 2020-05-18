package com.example.photography.database;

public class Hall {
    String name;
    String street;
    int number;
    String city;

    public Hall(){}

    public Hall(String name, String street, int number, String city) {
        this.name = name;
        this.street = street;
        this.number = number;
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
