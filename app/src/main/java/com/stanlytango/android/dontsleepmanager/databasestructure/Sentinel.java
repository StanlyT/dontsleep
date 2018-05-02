package com.stanlytango.android.dontsleepmanager.databasestructure;
import android.util.Log;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sentinel {
    private static final String TAG = "# Sentinel";

    public String login;
    public String password;
    public String name;
    public String surname;
    public Long state;

    public Sentinel(){
    }

    public Sentinel(String login, String password, String name, String surname, Long state) {
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


    public Sentinel mapToSentinel(Map<String, Object> map){
        Sentinel sentinel = new Sentinel();
        sentinel.login    =    map.get("login") == null ? null : (String)map.get("login");
        sentinel.password = map.get("password") == null ? null : (String)map.get("password");
        sentinel.name   =    map.get("name")    == null ? null : (String)map.get("name");
        sentinel.surname  =  map.get("surname") == null ? null : (String)map.get("surname");
        sentinel.state   =   map.get("state")   == null ? null : (Long) map.get("state");
        return sentinel;
    }

    public void writeNewSentinel(DatabaseReference dbRef, String login, String password, String name, String surname, Long state){
        String key = dbRef.child(login).getKey();
        Sentinel sentinel = new Sentinel(login, password, name, surname, state);
        Map<String,Object> node = sentinel.toMap();
        Map<String,Object> structure = new HashMap<String, Object>();
        structure.put(key, node);
        dbRef.updateChildren(structure);
    }

    // !!! ====== testing method ====== !!!
    public void letsSayThereIsSentinelDB(DatabaseReference dbSentinelRef){
        Long state = Long.valueOf(1);
        writeNewSentinel(dbSentinelRef, "jamesbond", "fdhjfr", "Alexander", "Borodach", state);
//        writeNewSentinel(dbSentinelRef, "mrsmith", "ghtnfjk", "Balera", "Shestiorkin", state);
//        writeNewSentinel(dbSentinelRef, "fbiagent", "gjrisdl", "Jora", "Pupkov", state);
//        writeNewSentinel(dbSentinelRef, "brooks", "egjhry", "Brooks", "Bychkov", state);
//        writeNewSentinel(dbSentinelRef, "tamada", "sdf211s", "Tamada", "Tyorkin", state);
//        writeNewSentinel(dbSentinelRef, "kolya", "gfddg", "Kolya", "Patsanchuk", state);
//        writeNewSentinel(dbSentinelRef, "jora", "asdfe", "Jors", "Gopnikovskyi", state);
//        writeNewSentinel(dbSentinelRef, "alex", "esdfsd", "Alex", "Chmoshkin", state);
    }

    // !!! ====== testing method ====== !!!
    public List<Sentinel> makeList (){
        Long state = Long.valueOf(1);

        List<Sentinel> list = new ArrayList<>();
        Sentinel s1 = new Sentinel("brooks", "pswrdBRKS", "John", "Brooks", state);
        Sentinel s2 = new Sentinel("borodach", "pswrdBRDCH", "Alex", "Borodach",state);
        Sentinel s3 = new Sentinel("agent007", "pswrdGNT7", "Prosto","Valera",state);
        list.add(s1);
        list.add(s2);
        list.add(s3);
        return list;
    }

}