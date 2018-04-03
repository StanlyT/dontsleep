package com.stanlytango.android.dontsleepmanager.databasestructure;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Shift {
    public List<String> sentinelID;
    public String zoneID;
    public Long timeShiftStart;
    public Long timeShiftEnd;
    public List<Long> missedInspecTimeList;
    public Map<String, String> pausesList;

    public Shift (){}

    public Shift(
                 List<String> sentinelID,
                 String zoneID,
                 Long shiftStart,
                 Long shiftEnd,
                 List<Long> missedInspecTimeList,
                 Map<String, String> pausesList
    ) {
        this.sentinelID = sentinelID;
        this.zoneID = zoneID;
        this.timeShiftStart = shiftStart;
        this.timeShiftEnd = shiftEnd;
        this.missedInspecTimeList = missedInspecTimeList;
        this.pausesList = pausesList;
    }

    public Map<String, Object> toMap(){
        Map<String, Object> map = new HashMap<>();
        map.put("sentinelID", sentinelID);
        map.put("zoneID",zoneID);
        map.put("timeShiftStart", timeShiftStart);
        map.put("timeShiftEnd", timeShiftEnd);
        map.put("missedInspecTimeList", missedInspecTimeList);
        map.put("pausesList", pausesList);
        return map;
    }

    public void writeNewShift(DatabaseReference dbRef,
                              List<String> sentinelID,
                              String zoneID,
                              Long shiftStart,
                              Long shiftEnd,
                              List<Long> timeOfMissedCallList,
                              Map<String, String> pausesList){
        String key = dbRef.push().getKey();
        Shift shift = new Shift(sentinelID,zoneID,shiftStart,shiftEnd, timeOfMissedCallList,pausesList);

        Map<String, Object> shiftMap = shift.toMap();
        Map<String, Object> structure = new HashMap<>();
        structure.put(key, shiftMap);
        dbRef.updateChildren(structure);
    }


    public void letsSayThereIsShift (DatabaseReference dbRef){
        Shift shift = new Shift();
        Map<String, String> mapPauses = PauseFactory.getInst().init(2017,03,
                                                             "03:18:18:03:18:38",
                                                                "03:19:05:03:19:15",
                                                                "03:22:30:03:22:45",
                                                                "03:23:08:03:23:45");

        List<Long> timeOfMissedCallList = new ArrayList<>();
        timeOfMissedCallList.add(new GregorianCalendar(2018,03,3,21,15).getTime().getTime());
        timeOfMissedCallList.add(new GregorianCalendar(2018,03,3,23,43).getTime().getTime());
        timeOfMissedCallList.add(new GregorianCalendar(2018,03,4,5,10).getTime().getTime());

        shift.writeNewShift(dbRef,
                Arrays.asList("jamesbond","brooks"),
                "zone_Alfa",
                new GregorianCalendar(2017,03,01,18,0).getTime().getTime(),
                new GregorianCalendar(2017,04,01,8,0).getTime().getTime(),
                timeOfMissedCallList,
                mapPauses
        );
    }
}