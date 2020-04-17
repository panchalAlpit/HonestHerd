package com.mobilefirst.honestherd.HHGlobal;

import android.content.Context;
import android.content.SharedPreferences;

public class HHSharedPrefrence {
    private static final String PREF_NAME = "HonestHerd";
    private static final String LOGIN = "login";
    private static final String JOINDATE = "joindate";
    private static final String ADD_DATA = "add_data";
    private static final String HEALTHLOG_ID = "health_logid";
    private static final String HEALTH_STATUS = "health_status";



    public static void SetLogin(Context context,boolean login){
        SharedPreferences prefSignupData = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor;
        editor = prefSignupData.edit();
        editor.putBoolean(LOGIN, login);
        editor.apply();
        editor.commit();
    }

    public static Boolean getLogin(Context context){
        SharedPreferences pref = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return pref.getBoolean(LOGIN,false);
    }

    public static void SetJointDate(Context context, String sdate) {
        SharedPreferences prefSignupData = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor;
        editor = prefSignupData.edit();
        editor.putString(JOINDATE, sdate);
        editor.apply();
        editor.commit();
    }
    public static String getJoindate(Context context){
        SharedPreferences pref = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return pref.getString(JOINDATE,"");
    }

    public  static void addUserDetails(Context context,Boolean isadded){
        SharedPreferences prefSignupData = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor;
        editor = prefSignupData.edit();
        editor.putBoolean(ADD_DATA, isadded);
        editor.apply();
        editor.commit();
    }

    public static boolean getAddData(Context context){
        SharedPreferences pref = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return pref.getBoolean(ADD_DATA,true);
    }
    public static void ClearSession(Context context){
        SharedPreferences preferences =context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    public static void saveHealthLogID(Context context,String id){
        SharedPreferences prefSignupData = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor;
        editor = prefSignupData.edit();
        editor.putString(HEALTHLOG_ID,id);
        editor.apply();
        editor.commit();
    }

    public static String getsaveHealthLogID(Context context){
        SharedPreferences pref = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return pref.getString(HEALTHLOG_ID,"");
    }

    public static void setHealthStatus(Context context,String status){
        SharedPreferences prefSignupData = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor;
        editor = prefSignupData.edit();
        editor.putString(HEALTH_STATUS,status);
        editor.apply();
        editor.commit();
    }

    public static String getsetHealthStatus(Context context){
        SharedPreferences pref = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return pref.getString(HEALTH_STATUS,"");
    }

}
