package com.example.photography.database;

public class User {
    String firstName;
    String lastNAme;
    String email;

    public User(){}

    public User(String firstName, String lastNAme, String email) {
        this.firstName = firstName;
        this.lastNAme = lastNAme;
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastNAme() {
        return lastNAme;
    }

    public void setLastNAme(String lastNAme) {
        this.lastNAme = lastNAme;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
