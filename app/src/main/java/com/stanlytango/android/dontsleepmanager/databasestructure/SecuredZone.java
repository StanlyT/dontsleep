package com.stanlytango.android.dontsleepmanager.databasestructure;
import com.google.firebase.database.DatabaseReference;
import java.util.HashMap;
import java.util.Map;

public class SecuredZone {
    public String gmailogin, name, settingID;

    public SecuredZone(){}

    public SecuredZone(String gmailogin, String name, String settingID) {
        this.gmailogin = gmailogin;
        this.name = name;
        this.settingID = settingID;
    }

    public Map<String, Object> toMap (){
        Map<String,Object> result = new HashMap<>();
        result.put("gmailogin",gmailogin);
        result.put("name",name);
        result.put("settingID",settingID);
        return result;
    }
    public void writeNewSecuredZone(DatabaseReference dbRef, String gmailogin,
                                    String name, String settingID){
        String key = dbRef.child(gmailogin).getKey();
        SecuredZone securedZone = new SecuredZone(gmailogin, name, settingID);
        Map <String, Object> valueMap = securedZone.toMap();
        Map <String, Object> structure = new HashMap<>();
        structure.put(key, valueMap);
        dbRef.updateChildren(structure);
    }

    public void letsSayThereIsSecuredZoneDB(DatabaseReference dbSecuredZoneRef){
        writeNewSecuredZone(dbSecuredZoneRef, "zone_Alfa", "GroceryStore 79", "settingsDefault");
        writeNewSecuredZone(dbSecuredZoneRef, "zone_Betta", "WallMarket side A", "settingsDefault");
        writeNewSecuredZone(dbSecuredZoneRef, "zone_Gamma", "CarParking 185 Avenue", "settingsDefault");
        writeNewSecuredZone(dbSecuredZoneRef, "zone_Omega", "NASA Zone Z", "settingsDefault");
        writeNewSecuredZone(dbSecuredZoneRef, "zone_Sigma", "Medicine Drugs Storage", "settingsDefault");
    }

    public boolean containsLogin (DatabaseReference dbRef, String gmailogin){
        return true;
    }
}