package com.waltonbd.smartscale.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.waltonbd.smartscale.R;
import com.waltonbd.smartscale.constant.ConstantValues;
import com.waltonbd.smartscale.databinding.ActivitySettingsBinding;
import com.waltonbd.smartscale.session.SharedPreferencesManager;
import com.waltonbd.smartscale.viewmodels.SmartScaleViewModel;
import com.waltonbd.smartscale.viewmodels.UserViewModel;
import com.waltonbd.smartscale.webLoader.WebLoaderActivity;

public class SettingsActivity extends AppCompatActivity {

    protected ActivitySettingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        SmartScaleViewModel smartScaleViewModel = new ViewModelProvider(this).get(SmartScaleViewModel.class);

        binding.actionBack.setOnClickListener(view -> onBackPressed());

        /*binding.setGoal.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), SetGoalActivity.class));
        });*/

        binding.changeUnit.setOnClickListener(view -> {
            BaseActivity.startActivity(getApplicationContext(), getString(R.string.title_change_unit));
        });

        binding.deviceManagement.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), DeviceManagement.class));
        });

        /*binding.measurementReminder.setOnClickListener(view -> {
            BaseActivity.startActivity(getApplicationContext(), getString(R.string.title_reminders));
        });*/

        binding.understandingMeasurement.setOnClickListener(view -> {
            Intent intent = WebLoaderActivity.getStartIntent(getApplicationContext(),
                    "Understanding Measurement", "file:///android_asset/measurement/index.html");
            startActivity(intent);
        });

        binding.contactUs.setOnClickListener(view -> {
            BaseActivity.startActivity(getApplicationContext(), getString(R.string.title_contact));
        });

        binding.aboutUs.setOnClickListener(view -> {
            BaseActivity.startActivity(getApplicationContext(), getString(R.string.title_about));
        });

        binding.policies.setOnClickListener(view -> {
            Intent intent = WebLoaderActivity.getStartIntent(getApplicationContext(), "Legal & Policies", ConstantValues.POLICIES);
            startActivity(intent);
        });

        binding.logoutBtn.setOnClickListener(view -> {
            //---------------- Clear all data -------------------------
            SharedPreferencesManager.init(getApplicationContext());
            SharedPreferencesManager.writeToken(SharedPreferencesManager.ACCESS_TOKEN, "");
            SharedPreferencesManager.writeLoginStatus(SharedPreferencesManager.AUTO_LOGIN, false);
            ConstantValues.IS_USER_LOGGED_IN = false;

            userViewModel.deleteAllUsers(); // delete all user
            //userViewModel.resetSequence();  // start (id) primary key from 1
            smartScaleViewModel.deleteAllMeasurement();
            smartScaleViewModel.resetSequence();

            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finishAffinity();
        });
    }
}