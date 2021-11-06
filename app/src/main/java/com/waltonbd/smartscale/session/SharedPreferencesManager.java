package com.waltonbd.smartscale.session;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.waltonbd.smartscale.bean.User;
import com.yolanda.health.qnblesdk.out.QNBleDevice;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SharedPreferencesManager {

    public static final String CONNECTED_DEVICE = "CONNECTED_DEVICE";
    public static final String QN_DEVICES = "QN_DEVICES";
    public static final String PARENT_ID = "PARENT_ID";
    public static final String USER_ID = "USER_ID";
    public static final String USER_EMAIL = "USER_EMAIL";
    public static final String USER_PASS = "USER_PASS";
    public static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    public static final String AUTO_LOGIN = "AUTO_LOGIN";
    public static final String LAST_MEASURED_WEIGHT = "LAST_MEASURED_WEIGHT";
    public static final String WEIGHT_UNIT = "WEIGHT_UNIT";

    private static SharedPreferences mSharedPref;

    private SharedPreferencesManager() {
    }

    public static void init(Context context) {
        if (mSharedPref == null)
            mSharedPref = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
    }

    public static String read(String key, String defValue) {
        return mSharedPref.getString(key, defValue);
    }

    public static void write(String key, String value) {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.putString(key, value);
        prefsEditor.apply();
    }

    public static int read(String key, int defValue) {
        return mSharedPref.getInt(key, defValue);
    }

    public static void write(String key, int value) {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.putInt(key, value).apply();
    }

    public static long read(String key, long defValue) {
        return mSharedPref.getLong(key, defValue);
    }

    public static void write(String key, long value) {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.putLong(key, value).apply();
    }

    public static float read(String key, float defValue) {
        return mSharedPref.getFloat(key, defValue);
    }

    public static void write(String key, float value) {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.putFloat(key, value).apply();
    }

    public static String readToken(String key, String defValue) {
        return mSharedPref.getString(key, defValue);
    }

    public static void writeToken(String key, String value) {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.putString(key, value);
        prefsEditor.apply();
    }

    public static boolean readLoginStatus(String key, boolean defValue) {
        return mSharedPref.getBoolean(key, defValue);
    }

    public static void writeLoginStatus(String key, boolean value) {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.putBoolean(key, value);
        prefsEditor.apply();
    }

    //--------------------------------------------------------------------------------
    public static List<QNBleDevice> readQNDeviceList(String key, String defValue) {
        List<QNBleDevice> qnBleDevices;
        String json = mSharedPref.getString(key, defValue);
        Type type = new TypeToken<List<QNBleDevice>>() {
        }.getType();
        qnBleDevices =  new Gson().fromJson(json, type);
        if (qnBleDevices == null) {
            qnBleDevices = new ArrayList<>();
        }
        return qnBleDevices;
    }

    public static void writeQNDeviceList(String key, List<QNBleDevice> QNDevices) {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        String json = new Gson().toJson(QNDevices);
        prefsEditor.putString(key, json);
        prefsEditor.apply();
    }

    public static QNBleDevice readQNDevice(String key, String defValue) {
        String json = mSharedPref.getString(key, defValue);
        return new Gson().fromJson(json, QNBleDevice.class);
    }

    public static void writeQNDevice(String key, QNBleDevice QNDevices) {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        String json = new Gson().toJson(QNDevices);
        prefsEditor.putString(key, json);
        prefsEditor.apply();
    }

}
