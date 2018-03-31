package com.stanlytango.android.dontsleepmanager.databasestructure;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Smith on 30.03.2018.
 */

public class PauseFactory {
    static PauseFactory pauseFactory;

    static public PauseFactory getInst(){
        if(pauseFactory == null){
            return new PauseFactory();
        }
        return pauseFactory;
    }

    static public Map<String, String> init ( int year, int month,
                                         String ... tt){
        Map<String,String> map = new HashMap<>();

        for(int i=0; i<tt.length;i++){
            String[] time = tt[i].split(":");
            map.put(""+new GregorianCalendar(year,month,Integer.parseInt(time[0]), Integer.parseInt(time[1]), Integer.parseInt(time[2])).getTime().getTime(),
                    ""+new GregorianCalendar(year,month,Integer.parseInt(time[3]), Integer.parseInt(time[4]), Integer.parseInt(time[5])).getTime().getTime());
        }
        return map;
    }
}
