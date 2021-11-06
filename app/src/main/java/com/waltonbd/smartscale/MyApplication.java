package com.waltonbd.smartscale;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.multidex.MultiDex;

import com.qingniu.qnble.utils.QNLogUtils;
import com.waltonbd.smartscale.bean.User;
import com.yolanda.health.qnblesdk.listener.QNResultCallback;
import com.yolanda.health.qnblesdk.out.QNBleApi;


public class MyApplication extends Application {

    public static final String APP_ID = "Walton202003";
    private User mUser = new User();

    static {
        // disable night mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        // XmlPullParserException: invalid drawable tag vector
        // For Pre-Lollipop, add this in your Activity
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        String encryptPath = "file:///android_asset/Walton202003.qn";
        QNLogUtils.setLogEnable(BuildConfig.DEBUG);//Set the log printing switch, the default is off
        //QNLogUtils.setWriteEnable(true);//设置日志写入文件开关，默认关闭
        QNBleApi mQNBleApi = QNBleApi.getInstance(this);

        mQNBleApi.initSdk("Walton202003", encryptPath, new QNResultCallback() {
            @Override
            public void onResult(int code, String msg) {
                Log.d("BaseApplication", "Initialization file" + msg);
            }
        });
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        this.mUser = user;
    }
}
