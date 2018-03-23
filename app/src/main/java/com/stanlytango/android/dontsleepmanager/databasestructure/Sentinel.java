package com.stanlytango.android.dontsleepmanager.databasestructure;

public class Sentinel {
    String login;
    String password;
    String name;
    String surname;

    public Sentinel(){
    }

    public Sentinel(String login, String password, String name, String surname) {
        this.login = login;
        this.password = password;
        this.name = name;
        this.surname = surname;
    }

}