package com.waltonbd.smartscale.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.jaeger.library.StatusBarUtil;
import com.waltonbd.smartscale.R;
import com.waltonbd.smartscale.constant.ConstantValues;
import com.waltonbd.smartscale.session.SharedPreferencesManager;
import com.yolanda.health.qnblesdk.constant.QNUnit;

import java.util.ArrayList;
import java.util.List;

public class LauncherActivity extends AppCompatActivity {

    private final Context mContext = LauncherActivity.this;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1001;

    // Duration of wait
    private static final long DELAY_TIME = 1500L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setTransparentForImageView(this, null);
        setContentView(R.layout.activity_launcher);

        // Handle run time permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getAllPermission()) proceedAfterPermission();
        } else {
            proceedAfterPermission();
        }
    }

    private boolean getAllPermission() {

        List<String> listPermissionsNeeded = new ArrayList<>();

        int accessFineLocation = ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION);
        int accessCoarseLocation = ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION);
        int waleLock = ContextCompat.checkSelfPermission(mContext, Manifest.permission.WAKE_LOCK);

        if (accessFineLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (accessCoarseLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if (waleLock != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WAKE_LOCK);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
                return false;
            }
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_ID_MULTIPLE_PERMISSIONS) {
            //check if all permissions are granted
            boolean allgranted = false;
            for (int grantResult : grantResults) {
                if (grantResult == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }

            if (allgranted) {
                proceedAfterPermission();
            } else {
                new MaterialAlertDialogBuilder(this, R.style.MaterialAlertDialogRounded)
                        .setTitle("Need Permissions")
                        .setMessage("This app needs Location permission to work correctly")
                        .setNegativeButton("Cancel", (dialog, which) -> {
                            dialog.dismiss();
                            finish();
                        })
                        .setPositiveButton("Grant", (dialog, which) -> {
                            dialog.dismiss();
                            getAllPermission();
                        }).show();
            }
        }
    }

    private void proceedAfterPermission() {
        //-------------- Retrieve Values From Shared Pref ------------------
        SharedPreferencesManager.init(getApplicationContext());
        boolean loginStatus = SharedPreferencesManager.readLoginStatus(SharedPreferencesManager.AUTO_LOGIN, false);
        long parentId = SharedPreferencesManager.read(SharedPreferencesManager.PARENT_ID, 0L);
        long userId = SharedPreferencesManager.read(SharedPreferencesManager.USER_ID, 0L);
        String userEmail = SharedPreferencesManager.read(SharedPreferencesManager.USER_EMAIL, null);
        String userPass = SharedPreferencesManager.read(SharedPreferencesManager.USER_PASS, null);
        String accessToken = SharedPreferencesManager.readToken(SharedPreferencesManager.ACCESS_TOKEN, "");
        float lastWeight = SharedPreferencesManager.read(SharedPreferencesManager.LAST_MEASURED_WEIGHT, 0f);
        int weightUnit = SharedPreferencesManager.read(SharedPreferencesManager.WEIGHT_UNIT, QNUnit.WEIGHT_UNIT_KG);

        // save for further use
        ConstantValues.PARENT_ID = parentId;
        ConstantValues.USER_ID = userId;
        ConstantValues.USER_EMAIL = userEmail;
        ConstantValues.USER_PASS = userPass;
        ConstantValues.ACCESS_TOKEN = accessToken;
        ConstantValues.IS_USER_LOGGED_IN = loginStatus;
        ConstantValues.LAST_MEASURED_WEIGHT = lastWeight;
        ConstantValues.WEIGHT_UNIT = weightUnit;

        //-------------------- Redirect User -----------------------
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (loginStatus) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finishAffinity();
            } else {
                startActivity(new Intent(getApplicationContext(), WelcomeActivity.class));
                finish();
            }
        }, DELAY_TIME);
    }
}