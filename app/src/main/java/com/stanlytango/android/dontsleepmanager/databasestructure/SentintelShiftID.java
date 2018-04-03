package com.stanlytango.android.dontsleepmanager.databasestructure;

import android.support.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Smith on 31.03.2018.
 */

public class SentintelShiftID {

    List<String> shiftID;

    public SentintelShiftID(){
    }

    public SentintelShiftID(List<String> shiftID) {
        this.shiftID = shiftID;
    }


    public void addNewSentinelShiftID(DatabaseReference dbRef, String sentinelID, List<String> listShiftID){
        String key = sentinelID;
        SentintelShiftID sentintelShiftID = new SentintelShiftID(listShiftID);
        Map<String, Object> node = new HashMap<>();
        node.put(key,sentintelShiftID.shiftID);
        dbRef.updateChildren(node);
    }

    public List<String> getShiftIDList(DataSnapshot dataSnapshot, String dbName, String sentinelID){
        GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>(){};
        List<String> shiftIDList = dataSnapshot.child(dbName).child(sentinelID).getValue(t);
        return shiftIDList;
    }
}
