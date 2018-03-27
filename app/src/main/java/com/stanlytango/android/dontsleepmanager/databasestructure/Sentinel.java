package com.stanlytango.android.dontsleepmanager.databasestructure;

import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

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

    public Map<String, Object> toMap(){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("login",login);
        map.put("password",password);
        map.put("name", name);
        map.put("surname", surname);
        return map;
    }

    public void writeNewSentinel(DatabaseReference dbRef, String login, String password, String name, String surname){
        String key = dbRef.child(login).getKey();
        Sentinel sentinel = new Sentinel(login, password, name, surname);
        Map<String,Object> node = sentinel.toMap();
        Map<String,Object> structure = new HashMap<String, Object>();
        structure.put(key, node);
        dbRef.updateChildren(structure);
    }

    public void letsSayThereIsSentinelDB(DatabaseReference dbSentinelRef){
        writeNewSentinel(dbSentinelRef, "jamesbond", "qwerty", "Alexander", "Borodach");
        writeNewSentinel(dbSentinelRef, "mrsmith", "qwerty", "Balera", "Geraschenko");
        writeNewSentinel(dbSentinelRef, "fbiagent", "qwerty", "Jora", "Pupkov");
        writeNewSentinel(dbSentinelRef, "brooks", "qwerty", "Vasya", "Tyorkin");
    }

}