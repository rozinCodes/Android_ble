package com.waltonbd.smartscale.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tbl_unassigned_data")
public class UnassignedDataTable {

    @PrimaryKey(autoGenerate = true)
    private int ID;
    private String Weight;
    private String HeartRate;
    private String CardiacIndex;
    private String BIM;
    private String BodyFat;
    private String FatFreeBodyWeight;
    private String SubCutaneousFat;
    private String VisceralFat;
    private String BodyWater;
    private String SkeletalMuscle;
    private long UserID;
    private long GuestID;
    private String CreatedAt;
    private String UpdateAt;

    public UnassignedDataTable() {
    }

    public UnassignedDataTable(String weight, String heartRate, String cardiacIndex, String BIM, String bodyFat, String fatFreeBodyWeight, String subCutaneousFat, String visceralFat, String bodyWater, String skeletalMuscle, long userID, long guestID, String createdAt, String updateAt) {
        Weight = weight;
        HeartRate = heartRate;
        CardiacIndex = cardiacIndex;
        this.BIM = BIM;
        BodyFat = bodyFat;
        FatFreeBodyWeight = fatFreeBodyWeight;
        SubCutaneousFat = subCutaneousFat;
        VisceralFat = visceralFat;
        BodyWater = bodyWater;
        SkeletalMuscle = skeletalMuscle;
        UserID = userID;
        GuestID = guestID;
        CreatedAt = createdAt;
        UpdateAt = updateAt;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getWeight() {
        return Weight;
    }

    public void setWeight(String weight) {
        Weight = weight;
    }

    public String getHeartRate() {
        return HeartRate;
    }

    public void setHeartRate(String heartRate) {
        HeartRate = heartRate;
    }

    public String getCardiacIndex() {
        return CardiacIndex;
    }

    public void setCardiacIndex(String cardiacIndex) {
        CardiacIndex = cardiacIndex;
    }

    public String getBIM() {
        return BIM;
    }

    public void setBIM(String BIM) {
        this.BIM = BIM;
    }

    public String getBodyFat() {
        return BodyFat;
    }

    public void setBodyFat(String bodyFat) {
        BodyFat = bodyFat;
    }

    public String getFatFreeBodyWeight() {
        return FatFreeBodyWeight;
    }

    public void setFatFreeBodyWeight(String fatFreeBodyWeight) {
        FatFreeBodyWeight = fatFreeBodyWeight;
    }

    public String getSubCutaneousFat() {
        return SubCutaneousFat;
    }

    public void setSubCutaneousFat(String subCutaneousFat) {
        SubCutaneousFat = subCutaneousFat;
    }

    public String getVisceralFat() {
        return VisceralFat;
    }

    public void setVisceralFat(String visceralFat) {
        VisceralFat = visceralFat;
    }

    public String getBodyWater() {
        return BodyWater;
    }

    public void setBodyWater(String bodyWater) {
        BodyWater = bodyWater;
    }

    public String getSkeletalMuscle() {
        return SkeletalMuscle;
    }

    public void setSkeletalMuscle(String skeletalMuscle) {
        SkeletalMuscle = skeletalMuscle;
    }

    public long getUserID() {
        return UserID;
    }

    public void setUserID(long userID) {
        UserID = userID;
    }

    public long getGuestID() {
        return GuestID;
    }

    public void setGuestID(long guestID) {
        GuestID = guestID;
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
