package com.waltonbd.smartscale.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tbl_device_management")

public class DeviceManagementTable {

    @PrimaryKey(autoGenerate = true)
    private int ID;
    private String Name;
    private String MAC;
    private String DeviceToken;

    public DeviceManagementTable() {
    }

    public DeviceManagementTable(String name, String MAC, String deviceToken) {
        Name = name;
        this.MAC = MAC;
        DeviceToken = deviceToken;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMAC() {
        return MAC;
    }

    public void setMAC(String MAC) {
        this.MAC = MAC;
    }

    public String getDeviceToken() {
        return DeviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        DeviceToken = deviceToken;
    }
}
