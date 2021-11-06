package com.waltonbd.smartscale.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tbl_set_goal")
public class SetGoalsTable {

    @PrimaryKey(autoGenerate = true)
    private int ID;
    private int UserID;
    private int GuestID;
    private String Wight;
    private String BodyFat;
    private String CreatedAt;
    private String UpdateAt;

    public SetGoalsTable() {
    }

    public SetGoalsTable(int userID, int guestID, String wight, String bodyFat, String createdAt, String updateAt) {
        UserID = userID;
        GuestID = guestID;
        Wight = wight;
        BodyFat = bodyFat;
        CreatedAt = createdAt;
        UpdateAt = updateAt;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public int getGuestID() {
        return GuestID;
    }

    public void setGuestID(int guestID) {
        GuestID = guestID;
    }

    public String getWight() {
        return Wight;
    }

    public void setWight(String wight) {
        Wight = wight;
    }

    public String getBodyFat() {
        return BodyFat;
    }

    public void setBodyFat(String bodyFat) {
        BodyFat = bodyFat;
    }

    public String getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        CreatedAt = createdAt;
    }

    public String getUpdateAt() {
        return UpdateAt;
    }

    public void setUpdateAt(String updateAt) {
        UpdateAt = updateAt;
    }
}
