package com.stanlytango.android.dontsleepmanager.databasestructure;

import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

public class ShiftSettings {
    Integer shiftSettingsID;
    // during this time the guard must reply on application's request
    Integer timeForResponse;
    Integer quantityOfInspections;
    Integer pause;
    Boolean signal;
    Boolean blockBackHome;

    public ShiftSettings() {}

    public ShiftSettings(Integer timeForResponse, Integer quantityOfInspections, Integer pause, Boolean signal, Boolean blockBackHome) {
        // this.shiftSettingsID = shiftSettingsID;
        this.timeForResponse = timeForResponse;
        this.quantityOfInspections = quantityOfInspections;
        this.pause = pause;
        this.signal = signal;
        this.blockBackHome = blockBackHome;
    }

    public Map<String, Object> toMap(){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("timeForResponse",timeForResponse);
        map.put("quantityOfInspections", quantityOfInspections);
        map.put("pause", pause);
        map.put("signal", signal);
        map.put("blockBackHome", blockBackHome);
        return map;
    }

    public void writeNewSetting(DatabaseReference dbRef, Integer timeForResponse, Integer quantityOfInspections, Integer pause, Boolean signal, Boolean blockBackHome){
        ShiftSettings shiftSettings = new ShiftSettings(timeForResponse, quantityOfInspections, pause, signal, blockBackHome);
        String key = dbRef.push().getKey();
        Map<String, Object> node = shiftSettings.toMap();
        Map<String, Object> structure = new HashMap<>();
        structure.put(key, node);
        dbRef.updateChildren(structure);
    }

    // this method for first creating of default setting
    public void addDefaultSetting(DatabaseReference dbRef){
        ShiftSettings shiftSettings = new ShiftSettings(2, 4, 0, true, false);
        Map<String, Object> node = shiftSettings.toMap();
        Map<String, Object> structure = new HashMap<>();
        structure.put("defaultKeySetting", node);
        dbRef.updateChildren(structure);
    }

    // this method for custom setting
    public void addDefaultSetting(DatabaseReference dbRef, Integer timeForResponse, Integer quantityOfInspections, Integer pause, Boolean signal, Boolean blockBackHome){
        ShiftSettings shiftSettings = new ShiftSettings(timeForResponse, quantityOfInspections, pause, signal, blockBackHome);
        Map<String, Object> node = shiftSettings.toMap();
        Map<String, Object> structure = new HashMap<>();
        structure.put("defaultKeySetting", node);
        dbRef.updateChildren(structure);
    }


// SETTERS ==================================================================

// GETTERS ==================================================================

}
