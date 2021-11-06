package com.waltonbd.smartscale.constant;


import com.waltonbd.smartscale.BuildConfig;
import com.yolanda.health.qnblesdk.constant.QNUnit;

/**
 * ConstantValues contains some constant variables, used all over the App.
 **/
public class ConstantValues {

    //*********** API Base URL ********//
    public static final String BASE_URL = BuildConfig.DEBUG ? "http://192.168.150.242:8080/swsc_api/" : "http://103.243.142.54:8080/swsc_api/";
    public static final String IMAGE_URL = BuildConfig.DEBUG ? "http://192.168.150.242:8080/swsc_api" : "http://103.243.142.54:8080/swsc_api";
    public static final String POLICIES = BASE_URL + "privacy";

    public static long PARENT_ID;
    public static long USER_ID;
    public static String USER_EMAIL;
    public static String USER_PASS;
    public static String ACCESS_TOKEN;
    public static boolean IS_USER_LOGGED_IN;
    public static float LAST_MEASURED_WEIGHT;
    public static int WEIGHT_DIFFERENCE = 5;
    public static int WEIGHT_UNIT = QNUnit.WEIGHT_UNIT_KG;

    public static int MAX_LIMIT = 8;

    public static float weightMaxValue = 180;    // maximum value
    public static float weightMinValue = 2; // The smallest value
    public static float bodyFatMaxValue = 70;    // maximum value
    public static float bodyFatMinValue = 6; // The smallest value
    public static float mPerValue = 0.1f;   // indicates that every two scale differences are 1. 0.1:
}
