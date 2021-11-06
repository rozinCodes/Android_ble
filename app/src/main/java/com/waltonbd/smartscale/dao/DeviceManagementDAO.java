package com.waltonbd.smartscale.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.waltonbd.smartscale.entities.DeviceManagementTable;

import java.util.List;

@Dao
public interface DeviceManagementDAO {
    @Insert
    void insertDevice(DeviceManagementTable deviceManagementTable);

    @Update
    void updateDevice(DeviceManagementTable deviceManagementTable);

    @Delete
    void deleteDevice(DeviceManagementTable deviceManagementTable);

    @Query("DELETE FROM tbl_device_management")
    void deleteAllDevices();

    @Query("DELETE FROM tbl_device_management WHERE ID = :galleryID")
    void deleteDeviceByID(int galleryID);

    @Query("SELECT * FROM tbl_device_management ORDER BY ID DESC")
    LiveData<List<DeviceManagementTable>> getAllDevices();

    @Query("SELECT * FROM tbl_device_management ORDER BY ID DESC")
    List<DeviceManagementTable> getAllDeviceList();
}
