package com.waltonbd.smartscale.repositories;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;

import com.waltonbd.smartscale.dao.DeviceManagementDAO;
import com.waltonbd.smartscale.database.SmartScaleDatabase;
import com.waltonbd.smartscale.entities.DeviceManagementTable;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DeviceManagementRepository {

    private DeviceManagementDAO deviceManagementDAO;
    private LiveData<List<DeviceManagementTable>> allDevices;

    public DeviceManagementRepository(Application application) {
        SmartScaleDatabase database = SmartScaleDatabase.getInstance(application);
        deviceManagementDAO = database.deviceManagementDAO();
        allDevices = deviceManagementDAO.getAllDevices();

    }

    public void insertDevice(DeviceManagementTable deviceManagementTable) {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            //Background work here
            deviceManagementDAO.insertDevice(deviceManagementTable);
//            handler.post(() -> {
//                //UI Thread work here
//            });
        });

    }

    public void updateDevice(DeviceManagementTable deviceManagementTable) {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            //Background work here
            deviceManagementDAO.updateDevice(deviceManagementTable);
//            handler.post(() -> {
//                //UI Thread work here
//            });
        });

    }

    public void deleteDelete(DeviceManagementTable deviceManagementTable) {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            //Background work here
            deviceManagementDAO.deleteDevice(deviceManagementTable);
//            handler.post(() -> {
//                //UI Thread work here
//            });
        });

    }

    public void deleteAllDevices() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            //Background work here
            deviceManagementDAO.deleteAllDevices();
//            handler.post(() -> {
//                //UI Thread work here
//            });
        });

    }

    public LiveData<List<DeviceManagementTable>> getAllDevices() {
        return allDevices;

    }

}
