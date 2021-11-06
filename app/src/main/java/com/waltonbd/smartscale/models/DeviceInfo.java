package com.waltonbd.smartscale.models;

public class DeviceInfo {
    private long ID;
    private String Name;
    private String MAC;
    private String DeviceToken;

    public DeviceInfo() {
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
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

    public String isDeviceToken() {
        return DeviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        DeviceToken = deviceToken;
    }

    @Override
    public String toString() {
        return "DeviceManagement{" +
                "ID=" + ID +
                ", Name='" + Name + '\'' +
                ", MAC='" + MAC + '\'' +
                ", DeviceToken=" + DeviceToken +
                '}';
    }
}
