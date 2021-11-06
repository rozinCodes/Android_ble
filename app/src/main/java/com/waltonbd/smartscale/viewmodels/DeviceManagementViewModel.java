package com.waltonbd.smartscale.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.waltonbd.smartscale.entities.DeviceManagementTable;
import com.waltonbd.smartscale.repositories.DeviceManagementRepository;

import java.util.List;

public class DeviceManagementViewModel extends AndroidViewModel {
    private DeviceManagementRepository repository;
    private LiveData<List<DeviceManagementTable>> allDevices;

    public DeviceManagementViewModel(@NonNull Application application) {
        super(application);
        repository = new DeviceManagementRepository(application);
        allDevices = repository.getAllDevices();
    }

    public void insert(DeviceManagementTable deviceManagementTable) {
        repository.insertDevice(deviceManagementTable);
    }

    public void deleteAllDevices() {
        repository.deleteAllDevices();
    }

    public LiveData<List<DeviceManagementTable>> getAllDevices() {
        return allDevices;
    }


}
