package com.example.dogwalkerandroid;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {

private static SharedPreferences mSharedPref;
public static final String PREF_NAME = "DogWalker";
public static final String ACCESS_TOKEN = "ACCESS_TOKEN";
public static final String IS_READ_UNREAD_REQUIRED = "isReadUnreadRequired";
public static final String Access_State_Device = "accessStateDevice";
    public static final String USER_STATE = "USER_STATE";
    public static final String DOGOWNER = "DOGOWNER";
    public static final String DOGWALKER = "DOGWALKER";
    public static final String ADMIN = "ADMIN";



private SharedPref()
{

}

public static void init(Context context)
{
    if(mSharedPref == null)
        mSharedPref = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
}

 
public static String getString(String key, String defValue) {
    return mSharedPref.getString(key, defValue);
}

public static void putString(String key, String value) {
    SharedPreferences.Editor prefsEditor = mSharedPref.edit();
    prefsEditor.putString(key, value);
    prefsEditor.apply();
}

public static Integer getInteger(String key, int defValue) {
    return mSharedPref.getInt(key, defValue);
}

public static void putInteger(String key, Integer value) {
    SharedPreferences.Editor prefsEditor = mSharedPref.edit();
    prefsEditor.putInt(key, value);
    prefsEditor.apply();
}


public static boolean getBoolean(String key, boolean defValue) {
    return mSharedPref.getBoolean(key, defValue);
}

public static void putBoolean(String key, boolean value) {
    SharedPreferences.Editor prefsEditor = mSharedPref.edit();
    prefsEditor.putBoolean(key, value);
    prefsEditor.apply();
}

public static long getLong(String key, long defValue) {
    return mSharedPref.getLong(key, defValue);
}

public static void putLong(String key, long value) {
    SharedPreferences.Editor prefsEditor = mSharedPref.edit();
    prefsEditor.putLong(key, value);
    prefsEditor.apply();
}


public static float getFloat(String key, float defValue) {
    return mSharedPref.getFloat(key, defValue);
}

public static void putFloat(String key, float value) {
    SharedPreferences.Editor prefsEditor = mSharedPref.edit();
    prefsEditor.putFloat(key, value);
    prefsEditor.apply();
}


//// Clear Preference ////
public static void clearPreference(Context context) {
    mSharedPref.edit().clear().apply();
}

//// Remove ////
public static void removePreference(String Key){
    mSharedPref.edit().remove(Key).apply();
}

}