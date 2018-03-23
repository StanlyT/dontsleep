package com.stanlytango.android.dontsleepmanager.databasestructure;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Shift {
    public String shiftID;
    public List<String> sentinelID;
    public String zoneID;
    public Date shiftStart;
    public Date shiftEnd;
    public List<Date> missedInspecTimeList;
    public Map<Date, Date> pausesList;
}