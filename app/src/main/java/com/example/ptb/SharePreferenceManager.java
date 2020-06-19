package com.example.ptb;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePreferenceManager {
    public static final String SP_APP = "spApp";
    public static final String SP_ID = "spId";
    public static final String SP_Pass = "spPass";
    public static final String SP_Username = "spUsername";
    public static final String SP_Lat = "spLat";
    public static final String SP_Lng = "spLng";

    public static final String SP_SUDAH_LOGIN = "spSudahLogin";

    SharedPreferences sp;
    SharedPreferences.Editor spEditor;

    public SharePreferenceManager(Context context){
        sp = context.getSharedPreferences(SP_APP, Context.MODE_PRIVATE);
        spEditor = sp.edit();
    }

    public void saveSPDouble(String keySP, double value){
        spEditor.putLong(keySP,  Double.doubleToRawLongBits(value));
        spEditor.commit();
    }

    public void saveSPString(String keySP, String value){
        spEditor.putString(keySP, value);
        spEditor.commit();
    }

    public void saveSPInt(String keySP, int value){
        spEditor.putInt(keySP, value);
        spEditor.commit();
    }

    public void saveSPBoolean(String keySP, boolean value){
        spEditor.putBoolean(keySP, value);
        spEditor.commit();
    }
    public String getSPString(String key, String t){
        return sp.getString(key, t);
    }
    public double getSPDouble(String key, double t){
        return Double.longBitsToDouble(sp.getLong(key, Double.doubleToLongBits(t)));
    }
    public String getSpId() {
        return sp.getString(SP_ID, "");
    }

    public String getSP_Pass() {
        return sp.getString(SP_Pass, "");
    }

    public boolean getSpSudahLogin() {
        return sp.getBoolean(SP_SUDAH_LOGIN, false);
    }
}
