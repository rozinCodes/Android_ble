package com.waltonbd.smartscale.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.waltonbd.smartscale.adapters.DeviceAdapter;
import com.waltonbd.smartscale.databinding.ActivityDeviceManagementBinding;
import com.waltonbd.smartscale.session.SharedPreferencesManager;
import com.waltonbd.smartscale.util.ToastMaker;
import com.yolanda.health.qnblesdk.constant.QNDeviceType;
import com.yolanda.health.qnblesdk.out.QNBleDevice;

import java.lang.ref.WeakReference;
import java.util.List;

public class DeviceManagement extends AppCompatActivity {

    public static WeakReference<Activity> activity;

    private ActivityDeviceManagementBinding binding;

    private List<QNBleDevice> qnBleDevices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDeviceManagementBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity = new WeakReference<>(this);

        // read from shared pref
        SharedPreferencesManager.init(getApplicationContext());
        qnBleDevices = SharedPreferencesManager.readQNDeviceList(SharedPreferencesManager.QN_DEVICES, null);

        binding.actionBack.setOnClickListener(view -> finish());

        binding.addScale.setOnClickListener(view -> {
            /*SharedPreferencesManager.init(getApplicationContext());
            SharedPreferencesManager.write(SharedPreferencesManager.CONNECTED_DEVICE, "");
            deviceManagementViewModel.deleteAllDevices();*/

            // clear previous list & scan for available scale
            startActivity(new Intent(DeviceManagement.this, PairActivity.class));
        });

        initRecyclerView();
    }

    //-----------------------------Recycler View And Adapter Functionality--------------------------
    public void initRecyclerView() {
        DeviceAdapter mAdapter = new DeviceAdapter(qnBleDevices);
        binding.deviceRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener((device, position) -> {
            if (device.getDeviceType() == QNDeviceType.SCALE_BLE_DEFAULT) {
                SharedPreferencesManager.writeQNDevice(SharedPreferencesManager.CONNECTED_DEVICE, device);
                finish();
            } else {
                ToastMaker.show(getApplicationContext(), "This device is not compatible with this app");
            }
        });
    }
}