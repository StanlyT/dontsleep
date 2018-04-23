package com.stanlytango.android.dontsleepmanager.databasestructure;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sentinel {
    public String login;
    public String password;
    public String name;
    public String surname;
    public Integer state;

    public Sentinel(){
    }

    public Sentinel(String login, String password, String name, String surname, Integer state) {
        this.login = login;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.state = state;
    }

    public Map<String, Object> toMap(){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("login",login);
        map.put("password",password);
        map.put("name", name);
        map.put("surname", surname);
        map.put("state", state);
        return map;
    }

    public void writeNewSentinel(DatabaseReference dbRef, String login, String password, String name, String surname, Integer state){
        String key = dbRef.child(login).getKey();
        Sentinel sentinel = new Sentinel(login, password, name, surname, state);
        Map<String,Object> node = sentinel.toMap();
        Map<String,Object> structure = new HashMap<String, Object>();
        structure.put(key, node);
        dbRef.updateChildren(structure);
    }

    //
    public List<String> getSentinelDBList(DataSnapshot dataSnapshot, String dbName, String sentinelID){
        GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>(){};
        List<String> shiftIDList = dataSnapshot.child(dbName).child(sentinelID).getValue(t);
        return shiftIDList;
    }

    // !!! ====== testing method ====== !!!
    public void letsSayThereIsSentinelDB(DatabaseReference dbSentinelRef){
        writeNewSentinel(dbSentinelRef, "jamesbond", "fdhjfr", "Alexander", "Borodach", 1);
        writeNewSentinel(dbSentinelRef, "mrsmith", "ghtnfjk", "Balera", "Geraschenko", 1);
        writeNewSentinel(dbSentinelRef, "fbiagent", "gjrisdl", "Jora", "Pupkov", 1);
        writeNewSentinel(dbSentinelRef, "brooks", "egjhry", "Vasya", "Tyorkin", 1);
        writeNewSentinel(dbSentinelRef, "tamada", "edhtoe", "Vasya", "Tyorkin", 1);
    }

    // !!! ====== testing method ====== !!!
    public List<Sentinel> makeList (){
        List<Sentinel> list = new ArrayList<>();
        Sentinel s1 = new Sentinel("brooks", "pswrdBRKS", "John", "Brooks", 1);
        Sentinel s2 = new Sentinel("borodach", "pswrdBRDCH", "Alex", "Borodach",1);
        Sentinel s3 = new Sentinel("agent007", "pswrdGNT7", "Prosto","Valera",1);
        list.add(s1);
        list.add(s2);
        list.add(s3);
        return list;
    }

}