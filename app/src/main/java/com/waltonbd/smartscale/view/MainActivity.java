package com.waltonbd.smartscale.view;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.location.LocationManagerCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.qingniu.qnble.utils.QNLogUtils;
import com.qingniu.scale.config.DecoderAdapterManager;
import com.qingniu.scale.config.DoubleDecoderAdapter;
import com.qingniu.scale.constant.DecoderConst;
import com.waltonbd.smartscale.MyApplication;
import com.waltonbd.smartscale.R;
import com.waltonbd.smartscale.bean.Config;
import com.waltonbd.smartscale.bean.User;
import com.waltonbd.smartscale.constant.ConstantValues;
import com.waltonbd.smartscale.entities.MeasurementsTable;
import com.waltonbd.smartscale.entities.UsersTable;
import com.waltonbd.smartscale.session.SharedPreferencesManager;
import com.waltonbd.smartscale.util.DateUtils;
import com.waltonbd.smartscale.util.ToastMaker;
import com.waltonbd.smartscale.utils.TimeUtil;
import com.waltonbd.smartscale.viewmodels.SmartScaleViewModel;
import com.waltonbd.smartscale.viewmodels.UserViewModel;
import com.yolanda.health.qnblesdk.constant.QNBleConst;
import com.yolanda.health.qnblesdk.constant.QNIndicator;
import com.yolanda.health.qnblesdk.constant.QNInfoConst;
import com.yolanda.health.qnblesdk.constant.QNScaleStatus;
import com.yolanda.health.qnblesdk.constant.QNUnit;
import com.yolanda.health.qnblesdk.constant.UserGoal;
import com.yolanda.health.qnblesdk.constant.UserShape;
import com.yolanda.health.qnblesdk.listener.QNBleProtocolDelegate;
import com.yolanda.health.qnblesdk.listener.QNResultCallback;
import com.yolanda.health.qnblesdk.listener.QNScaleDataListener;
import com.yolanda.health.qnblesdk.out.QNBleApi;
import com.yolanda.health.qnblesdk.out.QNBleDevice;
import com.yolanda.health.qnblesdk.out.QNBleProtocolHandler;
import com.yolanda.health.qnblesdk.out.QNConfig;
import com.yolanda.health.qnblesdk.out.QNScaleData;
import com.yolanda.health.qnblesdk.out.QNScaleItemData;
import com.yolanda.health.qnblesdk.out.QNScaleStoreData;
import com.yolanda.health.qnblesdk.out.QNUser;
import com.yolanda.health.qnblesdk.out.QNWiFiConfig;

import java.text.DecimalFormat;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private final Handler mHandler = new Handler(Looper.myLooper());

    private BluetoothGattCharacteristic qnReadBgc, qnWriteBgc, qnBleReadBgc, qnBleWriteBgc;

    private QNBleDevice mBleDevice;
    private QNBleApi mQNBleApi;

    private Config mConfig;
    private User mUser;
    private QNWiFiConfig mQnWiFiConfig;

    private BluetoothGatt mBluetoothGatt;

    private QNBleProtocolHandler mProtocolHandler;

    private boolean mIsConnected;
    private boolean isFirstService;

    private boolean isActivityOnPause;
    private boolean isActivityOnDestroy;

    //----------------------------------------------------
    private SmartScaleViewModel smartScaleViewModel;
    private UserViewModel userViewModel;
    private MyApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Init shared pref
        SharedPreferencesManager.init(getApplicationContext());

        mQNBleApi = QNBleApi.getInstance(this);
        // This API is used to monitor logs. If you need to upload logs to the server, you can use it, otherwise you don’t need to set up
        //mQNBleApi.setLogListener(log -> Log.e(TAG, log));

        smartScaleViewModel = new ViewModelProvider(this).get(SmartScaleViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        application = ((MyApplication) getApplicationContext());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);

        // Disable Fragment re-created on bottom navigation view item selected
        navView.setOnItemReselectedListener(item -> { });

        // Re-config scale for new user
        userViewModel.getSelectedUser().observe(this, this::setupSelectedUserConfig);

        setupConfig();
        initIntent();
    }

    //--------------- Config Data -----------------------
    private void setupConfig() {

        mConfig = new Config();
        mConfig.setUnit(ConstantValues.WEIGHT_UNIT);

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

    //-------------- Initialized Data ---------------
    private void initIntent() {

        // retrieve QNBleDevice from shared pref
        mBleDevice = SharedPreferencesManager.readQNDevice(SharedPreferencesManager.CONNECTED_DEVICE, null);

        // Get exist selected user info from database and config
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            UsersTable user = userViewModel.getUser(ConstantValues.USER_ID);
            mUser = application.getUser();
            mUser.setUserId(MyApplication.APP_ID);
            mUser.setHeight((int) Double.parseDouble(user.getHeight()));   // Height unit cm, minimum 40, maximum 240
            mUser.setGender(user.getGender());                             // QNInfoConst.GENDER_MAN;
            mUser.setBirthDay(DateUtils.stringToDate(user.getBirthDate(), "dd MMM, yyyy")); // yyyy-MM-dd
            mUser.setAthleteType(user.getAthleteMode() ? QNInfoConst.CALC_ATHLETE : QNInfoConst.CALC_NORMAL);
            mUser.setChoseGoal(UserGoal.GOAL_NONE.getCode());
            mUser.setChoseShape(UserShape.SHAPE_NONE.getCode());
            mUser.setClothesWeight(0);

            // save to application & re-init
            application.setUser(mUser);
            handler.post(this::initData);
        });
    }

    private void initData() {
        Log.d(TAG, "initData");

        initBleConnectStatus();
        initUserData(); // Set the data listener, return data, you need to set it before connecting to the current device

        // Connect current device
        if (mBleDevice != null) {
            connectQnDevice(mBleDevice);
        } else {
            ToastMaker.show(this, "No device connected. Please scan & connect");
        }
    }

    /**
     * Get the connection status of the device and Bluetooth
     */
    private void initBleConnectStatus() {
        BluetoothAdapter bluetoothAdapter = getBluetoothAdapter();
        if (bluetoothAdapter == null) {
            ToastMaker.show(this, "Your device not supported");
        }

        if (bluetoothAdapter != null && !bluetoothAdapter.isEnabled()) {
            // Turn on bluetooth
            bluetoothAdapter.enable();
        }
    }

    /*
     * ScaleSDKDemo > SelfMultiDeviceConnectActivity > line no: 655
     */
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            Log.d(TAG, "onConnectionStateChange: " + newState);

            if (status != BluetoothGatt.GATT_SUCCESS) {
                String err = "Cannot connect device with error status: " + status;
                // Calling the disconnect method when the attempt to connect fails will not cause this method to call back, so just call back directly here.
                gatt.close();
                if (mBluetoothGatt != null) {
                    mBluetoothGatt.disconnect();
                    mBluetoothGatt.close();
                    mBluetoothGatt = null;
                }
                setBleStatus(QNScaleStatus.STATE_DISCONNECTED);
                mIsConnected = false;
                Log.e(TAG, err);
                return;
            }

            if (newState == BluetoothProfile.STATE_CONNECTED) {
                mIsConnected = true;

                // When the Bluetooth device is connected
                mHandler.post(() -> setBleStatus(QNScaleStatus.STATE_CONNECTED));

                // TODO: 2019/9/7  Some mobile phones may have the problem of not being able to find the service, here can be a delay operation
                if (mBluetoothGatt != null) {
                    mBluetoothGatt.discoverServices();
                }
                Log.i(TAG, "onConnectionStateChange: " + "connection succeeded");
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                mIsConnected = false;

                // When the device cannot connect
                if (mBluetoothGatt != null) {
                    mBluetoothGatt.disconnect();
                    mBluetoothGatt.close();
                    mBluetoothGatt = null;
                }

                qnReadBgc = null;
                qnWriteBgc = null;
                qnBleReadBgc = null;
                qnBleWriteBgc = null;

                gatt.close();
                // Reconnection can be initiated in actual use
                if (mBleDevice != null && !isActivityOnPause && !isActivityOnDestroy) {
                    connectQnDevice(mBleDevice);
                }
                mHandler.post(() -> setBleStatus(QNScaleStatus.STATE_LINK_LOSS));
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            Log.d(TAG, "onServicesDiscovered------: " + "Discovery Service----" + status);

            if (status == BluetoothGatt.GATT_SUCCESS) {
                // Discover the service and traverse the service to find the equipment service for the company
                List<BluetoothGattService> services = gatt.getServices();

                for (BluetoothGattService service : services) {
                    // The first set
                    if (service.getUuid().equals(UUID.fromString(QNBleConst.UUID_IBT_SERVICES))) {
                        if (mProtocolHandler != null) {
                            // Enable all eigenvalues
                            initCharacteristic(gatt, true);
                            Log.d(TAG, "onServicesDiscovered------: " + "Discovery service is the first set");
                            mProtocolHandler.prepare(QNBleConst.UUID_IBT_SERVICES);
                        }
                        break;
                    }
                    // Second set
                    if (service.getUuid().equals(UUID.fromString(QNBleConst.UUID_IBT_SERVICES_1))) {
                        if (mProtocolHandler != null) {
                            // Enable all eigenvalues
                            Log.d(TAG, "onServicesDiscovered------: " + "Discovery service is the second set");
                            initCharacteristic(gatt, false);
                            mProtocolHandler.prepare(QNBleConst.UUID_IBT_SERVICES_1);
                        }
                        break;
                    }
                }
            } else {
                Log.d(TAG, "onServicesDiscovered---error: " + status);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);

            Log.d(TAG, "onCharacteristicRead---Data received:  " + QNLogUtils.byte2hex(characteristic.getValue()));
            if (status == BluetoothGatt.GATT_SUCCESS) {
                // Get data
                if (mProtocolHandler != null) {
                    mProtocolHandler.onGetBleData(getService(), characteristic.getUuid().toString(), characteristic.getValue());
                }
            } else {
                Log.d(TAG, "onCharacteristicRead---error: " + status);
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);

            Log.d(TAG, "onCharacteristicChanged---收到数据:  " + QNLogUtils.byte2hex(characteristic.getValue()));
            // Get data
            if (mProtocolHandler != null) {
                mProtocolHandler.onGetBleData(getService(), characteristic.getUuid().toString(), characteristic.getValue());
            }
        }
    };

    private String initWeight(double weight) {
        int unit = mQNBleApi.getConfig().getUnit();
        return mQNBleApi.convertWeightWithTargetUnit(weight, unit);
    }

    private void initUserData() {
        mQNBleApi.setDataListener(new QNScaleDataListener() {
            //--------------------------Get Real Time Weight Data--------------------------
            @Override
            public void onGetUnsteadyWeight(QNBleDevice device, double weight) {
                Log.d(TAG, "Weight is:" + weight);
                // Update weight of measuring animation
                smartScaleViewModel.setLiveWeight(initWeight(weight));
            }

            @Override
            public void onGetScaleData(QNBleDevice device, QNScaleData data) {
                Log.d(TAG, "Received measurement data");
                onReceiveScaleData(data);
                QNScaleItemData fatValue = data.getItem(QNIndicator.TYPE_SUBFAT);
                if (fatValue != null) {
                    String value = fatValue.getValue() + "";
                    Log.d(TAG, "Receive subcutaneous fat data:" + value);
                }
            }

            @Override
            public void onGetStoredScale(QNBleDevice device, List<QNScaleStoreData> storedDataList) {
                // Log.d(TAG, "Receive stored data");
                if (storedDataList != null && storedDataList.size() > 0) {
                    QNScaleStoreData data = storedDataList.get(0);
                    // Log.d(TAG, "Receive stored data:" + data.getWeight());
                    QNUser qnUser = createQNUser();
                    data.setUser(qnUser);
                    QNScaleData qnScaleData = data.generateScaleData();
                    //The stored data will not be displayed on the interface temporarily, and the processing logic can refer to the measured data
                    onReceiveScaleData(qnScaleData);
                }
            }

            @Override
            public void onGetElectric(QNBleDevice device, int electric) {
                String text = "Percentage of battery received:" + electric;
                // Log.d(TAG, text);
                if (electric == DecoderConst.NONE_BATTERY_VALUE) { //Failed to obtain battery information
                    return;
                }
                ToastMaker.show(getApplicationContext(), text);
            }

            // Connection status during measurement
            @Override
            public void onScaleStateChange(QNBleDevice device, int status) {
                Log.d(TAG, "The connection status of the scale is:" + status);
                setBleStatus(status);
            }

            @Override
            public void onScaleEventChange(QNBleDevice device, int scaleEvent) {
                Log.d(TAG, "The event returned by the scale is:" + scaleEvent);
            }
        });
    }

    private void onReceiveScaleData(QNScaleData data) {
        List<QNScaleItemData> allItem = data.getAllItem();

        DecimalFormat df = new DecimalFormat("#.##");
        MeasurementsTable mData = new MeasurementsTable();

        for (QNScaleItemData itemData : allItem) {
            //Log.e(TAG, itemData.getName() + " : " + itemData.getValue());
            switch (itemData.getName()) {
                case "weight":
                    mData.setWeight(df.format(itemData.getValue()));
                    break;
                case "BMI":
                    mData.setBmi(df.format(itemData.getValue()));
                    break;
                case "body fat rate":
                    mData.setBodyFat(df.format(itemData.getValue()));
                    break;
                case "subcutaneous fat":
                    mData.setSubCutaneousFat(df.format(itemData.getValue()));
                    break;
                case "visceral fat":
                    mData.setVisceralFat(df.format(itemData.getValue()));
                    break;
                case "body water rate":
                    mData.setBodyWater(df.format(itemData.getValue()));
                    break;
                case "muscle rate":
                    mData.setSkeletalMuscle(df.format(itemData.getValue()));
                    break;
                case "bone mass":
                    mData.setBoneMass(df.format(itemData.getValue()));
                    break;
                case "BMR":
                    mData.setBmr(df.format(itemData.getValue()));
                    break;
                case "protein":
                    mData.setProtein(df.format(itemData.getValue()));
                    break;
                case "lean body weight":
                    mData.setFatFreeBodyWeight(df.format(itemData.getValue()));
                    break;
                case "muscle mass":
                    mData.setMuscleMass(df.format(itemData.getValue()));
                    break;
                case "metabolic age":
                    mData.setMetabolicAge(df.format(itemData.getValue()));
                    break;
                case "heart rate":
                    mData.setHeartRate(df.format(itemData.getValue()));
                    break;
            }
        }

        // set userId and guest id
        mData.setUserId(ConstantValues.USER_ID);
        // set current timestamp
        mData.setCreateDateTime(TimeUtil.getCurrentDate());
        mData.setUpdateDateTime(TimeUtil.getCurrentDateTime());

        smartScaleViewModel.setMeasurementData(mData);
    }

    private void setBleStatus(int bleStatus) {
        String stateString;
        switch (bleStatus) {
            case QNScaleStatus.STATE_CONNECTING: {
                stateString = "Connecting";
                mIsConnected = true;
                break;
            }
            case QNScaleStatus.STATE_CONNECTED: {
                stateString = "Connected";
                mIsConnected = true;
                break;
            }
            case QNScaleStatus.STATE_DISCONNECTING: {
                stateString = "Disconnect in progress";
                mIsConnected = false;
                break;
            }
            case QNScaleStatus.STATE_LINK_LOSS: {
                stateString = "Disconnected";
                mIsConnected = false;
                break;
            }
            case QNScaleStatus.STATE_START_MEASURE:
            case QNScaleStatus.STATE_REAL_TIME: {
                stateString = "Measuring Weight";
                break;
            }
            case QNScaleStatus.STATE_BODYFAT: {
                stateString = "Impedance Measurement";
                break;
            }
            case QNScaleStatus.STATE_HEART_RATE: {
                stateString = "Measuring Heart Rate";
                break;
            }
            case QNScaleStatus.STATE_MEASURE_COMPLETED: {
                stateString = "Measure Complete";
                break;
            }
            default: {
                stateString = "Connection Disconnected";
                mIsConnected = false;
                break;
            }
        }

        if (!TextUtils.isEmpty(stateString)) {
            final String finalStateString = stateString;
            mHandler.post(() -> {
                // Update the live status when measuring
                smartScaleViewModel.setState(finalStateString);
            });
        }
    }

    //-------------------------------Create QNUser--------------------------------------------------
    private QNUser createQNUser() {
        // Remove some code (Ref: ScaleSDKDemo > SelfMultiDeviceConnectActivity > line no: 256)
        return mQNBleApi.buildUser(mUser.getUserId(),
                mUser.getHeight(), mUser.getGender(), mUser.getBirthDay(), mUser.getAthleteType(),
                UserShape.SHAPE_NONE, UserGoal.GOAL_NONE, mUser.getClothesWeight(), new QNResultCallback() {
                    @Override
                    public void onResult(int code, String msg) {
                        Log.d(TAG, "Create user information return:" + msg);
                    }
                });
    }

    private String getService() {
        if (isFirstService) {
            return QNBleConst.UUID_IBT_SERVICES;
        } else {
            return QNBleConst.UUID_IBT_SERVICES_1;
        }
    }

    private BluetoothGattCharacteristic getCharacteristic(final BluetoothGatt gatt, String serviceUuid, String characteristicUuid) {
        BluetoothGattService service = gatt.getService(UUID.fromString(serviceUuid));
        if (service == null) {
            return null;
        }
        return service.getCharacteristic(UUID.fromString(characteristicUuid));
    }

    private void initCharacteristic(BluetoothGatt gatt, boolean isFirstService) {

        this.isFirstService = isFirstService;

        //The first set of services
        if (isFirstService) {
            qnReadBgc = getCharacteristic(gatt, QNBleConst.UUID_IBT_SERVICES, QNBleConst.UUID_IBT_READ);
            qnWriteBgc = getCharacteristic(gatt, QNBleConst.UUID_IBT_SERVICES, QNBleConst.UUID_IBT_WRITE);
            qnBleReadBgc = getCharacteristic(gatt, QNBleConst.UUID_IBT_SERVICES, QNBleConst.UUID_IBT_BLE_READER);
            qnBleWriteBgc = getCharacteristic(gatt, QNBleConst.UUID_IBT_SERVICES, QNBleConst.UUID_IBT_BLE_WRITER);
        } else {
            qnReadBgc = getCharacteristic(gatt, QNBleConst.UUID_IBT_SERVICES_1, QNBleConst.UUID_IBT_READ_1);
            qnWriteBgc = getCharacteristic(gatt, QNBleConst.UUID_IBT_SERVICES_1, QNBleConst.UUID_IBT_WRITE_1);
        }

        enableNotifications(qnReadBgc);
        enableIndications(qnBleReadBgc);
    }

    //------------------------------Notification----------------------------------------------------
    private boolean enableNotifications(BluetoothGattCharacteristic characteristic) {

        final BluetoothGatt gatt = mBluetoothGatt;

        if (gatt == null || characteristic == null)
            return false;

        int properties = characteristic.getProperties();
        if ((properties & BluetoothGattCharacteristic.PROPERTY_NOTIFY) == 0)
            return false;

        boolean isSuccess = gatt.setCharacteristicNotification(characteristic, true);

        final BluetoothGattDescriptor descriptor = characteristic.getDescriptor(QNBleConst.CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR_UUID);
        if (descriptor != null) {
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            return gatt.writeDescriptor(descriptor);
        }
        return false;
    }


    private boolean enableIndications(BluetoothGattCharacteristic characteristic) {

        final BluetoothGatt gatt = mBluetoothGatt;

        if (gatt == null || characteristic == null)
            return false;

        int properties = characteristic.getProperties();
        if ((properties & BluetoothGattCharacteristic.PROPERTY_INDICATE) == 0)
            return false;

        boolean isSuccess = gatt.setCharacteristicNotification(characteristic, true);

        final BluetoothGattDescriptor descriptor = characteristic.getDescriptor(QNBleConst.CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR_UUID);
        if (descriptor != null) {
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
            Log.d(TAG, "enableIndications----------" + characteristic.getUuid());
            return gatt.writeDescriptor(descriptor);
        }
        return false;
    }

    //------------------------------Read Write Characteristics--------------------------------------
    private void readCharacteristicData(String service_uuid, String characteristic_uuid, String mac) {

        switch (characteristic_uuid) {
            case QNBleConst.UUID_IBT_READ:
                if (mBluetoothGatt != null && qnReadBgc != null) {
                    mBluetoothGatt.readCharacteristic(qnReadBgc);
                }
                break;

            case QNBleConst.UUID_IBT_BLE_READER:
                if (mBluetoothGatt != null && qnBleReadBgc != null) {
                    mBluetoothGatt.readCharacteristic(qnBleReadBgc);
                }
                break;

            case QNBleConst.UUID_IBT_READ_1:
                if (mBluetoothGatt != null && qnReadBgc != null) {
                    mBluetoothGatt.readCharacteristic(qnReadBgc);
                }
                break;
        }
    }

    private void writeCharacteristicData(String service_uuid, String characteristic_uuid, byte[] data, String mac) {
        switch (characteristic_uuid) {
            case QNBleConst.UUID_IBT_WRITE:
                if (mBluetoothGatt != null && qnWriteBgc != null) {
                    qnWriteBgc.setValue(data);
                    mBluetoothGatt.writeCharacteristic(qnWriteBgc);
                }
                break;

            case QNBleConst.UUID_IBT_BLE_WRITER:
                if (mBluetoothGatt != null && qnBleWriteBgc != null) {
                    qnBleWriteBgc.setValue(data);
                    mBluetoothGatt.writeCharacteristic(qnBleWriteBgc);
                }
                break;

            case QNBleConst.UUID_IBT_WRITE_1:
                if (mBluetoothGatt != null && qnWriteBgc != null) {
                    qnWriteBgc.setValue(data);
                    mBluetoothGatt.writeCharacteristic(qnWriteBgc);
                }
                break;
        }
    }

    //----------------------------------Bluetooth Adapter-------------------------------------------
    private BluetoothAdapter getBluetoothAdapter() {
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        return bluetoothManager.getAdapter();
    }

    /*
     *  Cache protocol resolution object
     */
    private void connectQnDevice(QNBleDevice device) {
        Log.d(TAG, "connectQnDevice");

        setBleStatus(QNScaleStatus.STATE_CONNECTING);
        buildHandler();

        BluetoothAdapter adapter = getBluetoothAdapter();
        BluetoothDevice mDevice = adapter.getRemoteDevice(device.getMac());

        if (mDevice != null) {
            Log.d(TAG, "connectQnDevice------: " + mDevice.getName());
            mBluetoothGatt = mDevice.connectGatt(MainActivity.this, false, mGattCallback);
        }

        // It is a dual-mode scale that needs to be configured with wifi configuration items (here you need to read the data after connecting to use it)
        if (mQnWiFiConfig != null && mBleDevice.isSupportWifi()) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    DoubleDecoderAdapter doubleDecoderAdapter = DecoderAdapterManager.getInstance().getDoubleDecoderAdapter();
                    Log.d(TAG, "connectQnDevice------: " + doubleDecoderAdapter);
                    doubleDecoderAdapter.sendWIFIInfo(mQnWiFiConfig.getSsid(), mQnWiFiConfig.getPwd(), QNBleConst.WIFI_INFO_AUTHMODE, QNBleConst.WIFI_INFO_TYPE);
                    // It needs to be initialized after setting to avoid repeated network configuration when reconnecting the device
                    mQnWiFiConfig = null;
                }
            }, 2000);
        }
    }

    //-----------------------------------------Build handler----------------------------------------
    private void buildHandler() {
        mProtocolHandler = mQNBleApi.buildProtocolHandler(mBleDevice, createQNUser(), new QNBleProtocolDelegate() {
            @Override
            public void writeCharacteristicValue(String service_uuid, String characteristic_uuid, byte[] data, QNBleDevice qnBleDevice) {
                writeCharacteristicData(service_uuid, characteristic_uuid, data, qnBleDevice.getMac());
            }

            @Override
            public void readCharacteristic(String service_uuid, String characteristic_uuid, QNBleDevice qnBleDevice) {
                readCharacteristicData(service_uuid, characteristic_uuid, qnBleDevice.getMac());

            }
        }, new QNResultCallback() {
            @Override
            public void onResult(int code, String msg) {
                Log.d(TAG, "Create result----" + code + " ------------- " + msg);
            }
        });
    }

    //------------------------------Connect And Disconnect------------------------------------------
    private void doConnect() {
        Log.d(TAG, "doConnect()");

        // get selected device from shared pref
        mBleDevice = SharedPreferencesManager.readQNDevice(
                SharedPreferencesManager.CONNECTED_DEVICE, null);
        if (mBleDevice == null || mUser == null) {
            return;
        }
        connectQnDevice(mBleDevice);
    }

    private void doDisconnect() {
        Log.d(TAG, "doDisconnect()");

        if (mBluetoothGatt != null) {
            mBluetoothGatt.disconnect();
        }

        if (mProtocolHandler != null) {
            mProtocolHandler = null;
        }
    }

    //------------- Config Selected User ---------------
    private void setupSelectedUserConfig(UsersTable user) {
        Log.i(TAG, "setupSelectedUserConfig");

        if (user != null) {
            mUser = application.getUser();
            mUser.setUserId(MyApplication.APP_ID);
            mUser.setHeight((int) Double.parseDouble(user.getHeight()));   // weight in cm
            mUser.setGender(user.getGender());                             // QNInfoConst.GENDER_MAN;
            mUser.setBirthDay(DateUtils.stringToDate(user.getBirthDate(), "dd MMM, yyyy")); // yyyy-MM-dd
            mUser.setAthleteType(user.getAthleteMode() ? QNInfoConst.CALC_ATHLETE : QNInfoConst.CALC_NORMAL);
            mUser.setChoseGoal(UserGoal.GOAL_NONE.getCode());
            mUser.setChoseShape(UserShape.SHAPE_NONE.getCode());
            mUser.setClothesWeight(0);

            // save to application & re-init
            application.setUser(mUser);

            // re config scale with user data
            mQNBleApi.setDataListener(null);
            initUserData();
        }
    }

    private void checkForLocationService() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean isLocationEnabled = LocationManagerCompat.isLocationEnabled(locationManager);

        if (!isLocationEnabled) {
            mHandler.postDelayed(() -> {
                // notify user
                new MaterialAlertDialogBuilder(this, R.style.MaterialAlertDialogRounded)
                        .setCancelable(false)
                        .setMessage("Need to open Location Service to connect Bluetooth devices.")
                        .setPositiveButton("Turn On", (dialog, which) -> {
                            dialog.dismiss();
                            // goto setting
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }).show();
            }, 800L);
        }
    }

    //------------------ Life Cycle Method --------------------
    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        // update flag
        isActivityOnPause = true;
        // disconnect from device
        doDisconnect();
        mIsConnected = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        checkForLocationService();

        Log.d(TAG, "mIsConnected: " + mIsConnected);
        Log.d(TAG, "isActivityOnPause: " + isActivityOnPause);
        // if device not connected then connect to device
        if (!mIsConnected && isActivityOnPause) {
            doConnect();
            isActivityOnPause = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        // update flag
        isActivityOnDestroy = true;
        // disconnect from device
        doDisconnect();
        mQNBleApi.setDataListener(null);
    }
}