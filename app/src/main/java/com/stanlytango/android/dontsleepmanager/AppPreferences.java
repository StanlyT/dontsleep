package com.stanlytango.android.dontsleepmanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class AppPreferences {
    public static final String TAG = "#_AppPreferences";
    private static final String SETTINGS_NAME = "settings";

    private static AppPreferences appPreferences;
    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor mEditor;
    public boolean isEditing;

    public enum Key{
        TIME_TO_RESPONSE_INT,    // Stores the time during that the guard
        // must reply on application's request
        QUANTITY_OF_VERIF_INT,
        PAUSE_SWITCH_BOOLEAN,
        PAUSE_DURATION_INT,
        BACK_HOME_BLOCKED_BOOLEAN,
        SOUND_ON_BOOLEAN
    }

    // Method to get instance --------------------------
    public static AppPreferences getInstance (Context context){
        if(appPreferences == null) {
            appPreferences = new AppPreferences(context); //context.getApplicationContext()
        }
        return appPreferences;
    }
    // Method to get instance --------------------------
    public static AppPreferences getInstance (){
        if(appPreferences != null) {
            return appPreferences;
        } throw new IllegalArgumentException("You should use getInstance(Context) !!!");
    }

    // CONSTRUCTOR --------------------------------
    private AppPreferences(Context context){
        sharedPreferences = context.getSharedPreferences(SETTINGS_NAME, Context.MODE_PRIVATE);
        Log.d(TAG, "Called CONSTRUCTOR");
    }

    public void edit (){
        isEditing = true;
        mEditor = sharedPreferences.edit();
    }

    public void commit(){
        isEditing = false;
        mEditor.commit();
        mEditor = null;
    }

    private void doEdit(){
        if (!isEditing && mEditor == null){
            mEditor = sharedPreferences.edit();
            Log.d(TAG, "mEditor = "+mEditor.toString());
        }
        //  else Log.d(TAG, "doEdit ERROR:"+"isEditing = "+isEditing+" mEditor = "+mEditor.toString());
    }

    private void doCommit (){
        if (!isEditing && mEditor != null){
            mEditor.commit();
            mEditor = null;
        }
    }

    public void clear (){
        doEdit();
        mEditor.clear();
        doCommit();
    }

    public void save(Key key, int value){
        doEdit();
        mEditor.putInt(key.name(), value);
        doCommit();
    }

    public void save(Key key, boolean value){
        doEdit();
        mEditor.putBoolean(key.name(), value);
        doCommit();
    }

    public boolean read(Key key, boolean defaultValue){
        return sharedPreferences.getBoolean(key.name(), defaultValue);
    };


    public boolean read(Key key){
        throw new IllegalArgumentException("You should use read(Key key, DEFAULT_VALUE)");
    };


    public int read(Key key, int defaultValue){
        return sharedPreferences.getInt(key.name(), defaultValue);
    }
}

