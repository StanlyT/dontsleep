package com.stanlytango.android.dontsleepmanager.databasestructure;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SentinelStorage {
    private static final String TAG = "# SentinelStorage";

    public static SentinelStorage sentinelStorage;
    public List<Sentinel> sentinelsList; // public must be changed to private in production

    public static SentinelStorage get(){
        if(sentinelStorage == null){
            return new SentinelStorage();
        } return sentinelStorage;
    }

    private SentinelStorage (){
        sentinelsList = new ArrayList<>();
    }

    public List<Sentinel> getList(){
        return sentinelsList;
    }

    public void readSentinelsListFromDB(DatabaseReference dbRef, final SentinelFirebaseCallback firebaseCallback){
        final Sentinel sentinel = new Sentinel();

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // if database Sentinels exists then
                if (dataSnapshot.exists()){
                    // fetch database of Sentinels in Map view
                    GenericTypeIndicator<Map<String, Object>> t = new GenericTypeIndicator<Map<String, Object>>(){};
                    Map<String, Object> sentinelsMap = dataSnapshot.getValue(t);
                    for (Map.Entry<String,Object> entry : sentinelsMap.entrySet()){
                        // convert that fetched Map and add that one to sentinelsList
                        sentinelsList.add(sentinel.mapToSentinel((Map)entry.getValue()));
                    }
                    firebaseCallback.onCallback(sentinelsList);

                } else {
                    Log.d(TAG, "DB Sentinels doesn't exist");
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG,"onCancelled :: "+databaseError.getMessage());
            }
        };
        dbRef.addListenerForSingleValueEvent(valueEventListener);
    }
}











//
//    public void loadSentinelsListFromDB(DatabaseReference dbRef){
//        final Sentinel sentinel = new Sentinel();
//        Log.d(TAG, "Before onDataChange "+sentinelsList.toString());
//
//
//        ValueEventListener valueEventListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                GenericTypeIndicator<Map<String, Object>> t = new GenericTypeIndicator<Map<String, Object>>(){};
//                Map<String, Object> sentinelsMap = dataSnapshot.getValue(t);
//                for (Map.Entry<String,Object> entry : sentinelsMap.entrySet()){
//                    Log.d(TAG, entry.toString());
//                    sentinelsList.add(sentinel.mapToSentinel((Map)entry.getValue()));
//                }
//                Log.d(TAG, "inside onDataChange "+sentinelsList.toString());
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.d(TAG,databaseError.getMessage());
//            }
//        };
//
//        dbRef.addListenerForSingleValueEvent(valueEventListener);
//        Log.d(TAG, "after onDataChange");
//    }
