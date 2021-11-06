package com.waltonbd.smartscale.view;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.qingniu.qnble.utils.QNLogUtils;
import com.waltonbd.smartscale.R;
import com.waltonbd.smartscale.adapters.DeviceAdapter;
import com.waltonbd.smartscale.bean.Config;
import com.waltonbd.smartscale.constant.ConstantValues;
import com.waltonbd.smartscale.databinding.ActivityPairBinding;
import com.waltonbd.smartscale.session.SharedPreferencesManager;
import com.waltonbd.smartscale.util.ToastMaker;
import com.yolanda.health.qnblesdk.constant.CheckStatus;
import com.yolanda.health.qnblesdk.constant.QNDeviceType;
import com.yolanda.health.qnblesdk.constant.QNUnit;
import com.yolanda.health.qnblesdk.listener.QNResultCallback;
import com.yolanda.health.qnblesdk.out.QNBleApi;
import com.yolanda.health.qnblesdk.out.QNBleDevice;
import com.yolanda.health.qnblesdk.out.QNConfig;

import java.util.ArrayList;
import java.util.List;

public class PairActivity extends AppCompatActivity {

    private static final String TAG = "PairActivity";

    protected ActivityPairBinding binding;
    private DeviceAdapter mAdapter;

    //-------------------------API Useful Classes---------------------------------------------------
    private QNBleApi mQNBleApi;
    private Config mConfig;
    private boolean isScanning;

    private final List<String> macList = new ArrayList<>();
    private final List<QNBleDevice> devices = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPairBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // start device pair animation
        Animation loadAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_pair_tip);
        loadAnimation.setFillAfter(true);
        binding.tipFoot.setAnimation(loadAnimation);

        mQNBleApi = QNBleApi.getInstance(this);

        mAdapter = new DeviceAdapter(devices);
        binding.deviceRecyclerView.setAdapter(mAdapter);

        //--------------- Test Config Data ------------------
        mConfig = new Config();
        mConfig.setUnit(ConstantValues.WEIGHT_UNIT);
        //mConfig.setUnit(QNUnit.WEIGHT_UNIT_KG);
        mConfig.setAllowDuplicates(false);
        mConfig.setConnectOutTime(6000);
        mConfig.setDuration(0);
        mConfig.setEnhanceBleBroadcast(false);
        mConfig.setOnlyScreenOn(false);
        mConfig.setScanOutTime(6000);
        mConfig.setUnit(0);

        //-----------------------------------------------
        initData();
        startScan();

        mAdapter.setOnItemClickListener((device, position) -> {
            // Stop running scan
            stopScan();

            // Store device list to local db
            SharedPreferencesManager.init(getApplicationContext());
            SharedPreferencesManager.writeQNDeviceList(SharedPreferencesManager.QN_DEVICES, devices);

            if (device.getDeviceType() == QNDeviceType.SCALE_BLE_DEFAULT) {
                SharedPreferencesManager.writeQNDevice(SharedPreferencesManager.CONNECTED_DEVICE, device);
                // Close all activity
                DeviceManagement.activity.get().finish();
                finish();
            } else {
                ToastMaker.show(getApplicationContext(), "This device is not compatible with this app");
            }
        });
    }

    //---------------------------Initialized Data---------------------------------------------------
    private void initData() {
        QNConfig mQnConfig = mQNBleApi.getConfig(); // Get the object set last time, and get the default object if it is not set
        mQnConfig.setAllowDuplicates(mConfig.isAllowDuplicates());
        mQnConfig.setDuration(mConfig.getDuration());
        //此API已废弃
        //mQnConfig.setScanOutTime(mConfig.getScanOutTime());
        mQnConfig.setConnectOutTime(mConfig.getConnectOutTime());
        mQnConfig.setUnit(mConfig.getUnit());
        mQnConfig.setOnlyScreenOn(mConfig.isOnlyScreenOn());
        //设置扫描对象
        mQnConfig.save(new QNResultCallback() {
            @Override
            public void onResult(int i, String s) {
                Log.d(TAG, "initData:" + s);
            }
        });
    }

    //-------------------------------Start And Stop Scan--------------------------------------------
    public void startScan() {
        BluetoothAdapter bluetoothAdapter = getBluetoothAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(PairActivity.this, "Device Not Supported", Toast.LENGTH_SHORT).show();
            return;
        }
        if (bluetoothAdapter.getState() != BluetoothAdapter.STATE_ON) {
            Toast.makeText(PairActivity.this, "Turn On Bluetooth", Toast.LENGTH_SHORT).show();
            return;
        }
        isScanning = bluetoothAdapter.startLeScan(scanCallback);
    }

    public void stopScan() {
        BluetoothAdapter bluetoothAdapter = getBluetoothAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(PairActivity.this, "Device Not Supported", Toast.LENGTH_SHORT).show();
            return;
        }
        bluetoothAdapter.stopLeScan(scanCallback);
        isScanning = false;
    }
    //----------------------------------------------------------------------------------------------


    //----------------------------------Bluetooth Adapter-------------------------------------------
    private BluetoothAdapter getBluetoothAdapter() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            return bluetoothManager == null ? null : bluetoothManager.getAdapter();
        } else {
            return BluetoothAdapter.getDefaultAdapter();
        }
    }
    //----------------------------------------------------------------------------------------------

    @Override
    protected void onDestroy() {
        stopScan();
        super.onDestroy();
    }

    //------------------------------API Useful Listener ------------------------------------
    private final BluetoothAdapter.LeScanCallback scanCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {

            if (device == null) {
                return;
            }

            QNBleDevice qnBleDevice = mQNBleApi.buildDevice(device, rssi, scanRecord, new QNResultCallback() {
                @Override
                public void onResult(int code, String msg) {
                    if (code != CheckStatus.OK.getCode()) {
                        QNLogUtils.log("Scan LeScanCallback", msg);
                    }
                }
            });

            if (qnBleDevice != null && !macList.contains(qnBleDevice.getMac())) {
                Log.e(TAG, "Device: " + qnBleDevice.getName() + qnBleDevice.getModeId());
                macList.add(qnBleDevice.getMac());
                devices.add(qnBleDevice);
                mAdapter.notifyDataSetChanged();

                if (devices.size() > 0) {
                    binding.animationLayout.setVisibility(View.GONE);
                    binding.deviceLayout.setVisibility(View.VISIBLE);
                }
            }
        }
    };
}