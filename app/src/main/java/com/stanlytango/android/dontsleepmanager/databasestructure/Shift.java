package com.stanlytango.android.dontsleepmanager.databasestructure;
import com.google.firebase.database.DatabaseReference;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Shift {
    //public String shiftID;
    public String zoneID;
    public List<String> sentinelID;
    public Date timeShiftStart;
    public Date timeShiftEnd;
    public List<Date> missedInspecTimeList;
    public Map<Date, Date> pausesList;

    public Shift(//String shiftID,
                 List<String> sentinelID,
                 String zoneID,
                 Date shiftStart,
                 Date shiftEnd,
                 List<Date> missedInspecTimeList,
                 Map<Date, Date> pausesList) {
//        this.shiftID = shiftID;
        this.sentinelID = sentinelID;
        this.zoneID = zoneID;
        this.timeShiftStart = shiftStart;
        this.timeShiftEnd = shiftEnd;
        this.missedInspecTimeList = missedInspecTimeList;
        this.pausesList = pausesList;
    }

    public Map<String, Object> toMap(){
        Map<String, Object> map = new HashMap<>();
     //   map.put("shiftID", shiftID);
        map.put("zoneID",zoneID);
        map.put("sentinelID", sentinelID);
        map.put("timeShiftStart", timeShiftStart);
        map.put("timeShiftEnd", timeShiftEnd);
        map.put("missedInspecTimeList", missedInspecTimeList);
        map.put("pausesList", pausesList);
        return map;
    }

    public void writeNewShift(DatabaseReference dbRef,
                                    List<String> sentinelID,
                                    String zoneID,
                                    Date shiftStart,
                                    Date shiftEnd,
                                    List<Date> missedInspecTimeList,
                                    Map<Date, Date> pausesList){
        String key = dbRef.push().getKey();
        Shift shift = new Shift(sentinelID,zoneID,shiftStart,shiftEnd,missedInspecTimeList,pausesList);
        Map<String, Object> shiftMap = shift.toMap();
        Map<String, Object> structure = new HashMap<>();
        structure.put(key, shiftMap);
        dbRef.updateChildren(structure);
    }


    public void letsSayThereIsShift (DatabaseReference dbRef){
      //  Shift shift = new Shift();

    }
}