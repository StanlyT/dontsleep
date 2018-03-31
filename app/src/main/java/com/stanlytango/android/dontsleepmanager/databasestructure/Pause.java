package com.stanlytango.android.dontsleepmanager.databasestructure;
import java.util.Date;
import java.util.Map;

public class Pause {
    Date pauseTimeStart;
    Date pauseTimeEnd;
    static Pause pausesInstance;

    private Pause(Date pauseTimeStart, Date pauseTimeEnd) {
        this.pauseTimeStart = pauseTimeStart;
        this.pauseTimeEnd = pauseTimeEnd;
    }
}
