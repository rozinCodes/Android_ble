package com.waltonbd.smartscale.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Entity(tableName = "tbl_users")
public class UsersTable {

    @PrimaryKey()
    @ColumnInfo(name = "id")
    private long id;
    @ColumnInfo(name = "guestId")
    private long guestId;
    @ColumnInfo(name = "image")
    private String image;
    @ColumnInfo(name = "userName")
    private String userName;
    @ColumnInfo(name = "fullName")
    private String fullName;
    @ColumnInfo(name = "gender")
    private String gender;
    @ColumnInfo(name = "birthDate")
    private String birthDate;
    @ColumnInfo(name = "height")
    private String height;
    @ColumnInfo(name = "weight")
    private String weight;
    @ColumnInfo(name = "phone")
    private String phone;
    @ColumnInfo(name = "athleteMode")
    private boolean athleteMode;
    @ColumnInfo(name = "userType")
    private String userType;
    @ColumnInfo(name = "weightUnit")
    private String weightUnit;
    @ColumnInfo(name = "heightUnit")
    private String heightUnit;
    @ColumnInfo(name = "createDateTime")
    private String createDateTime;
    @ColumnInfo(name = "updateDateTime")
    private String updateDateTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getGuestId() {
        return guestId;
    }

    public void setGuestId(long guestId) {
        this.guestId = guestId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean getAthleteMode() {
        return athleteMode;
    }

    public void setAthleteMode(boolean athleteMode) {
        this.athleteMode = athleteMode;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(String weightUnit) {
        this.weightUnit = weightUnit;
    }

    public String getHeightUnit() {
        return heightUnit;
    }

    public void setHeightUnit(String heightUnit) {
        this.heightUnit = heightUnit;
    }

    public String getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(String createDateTime) {
        this.createDateTime = createDateTime;
    }

    public String getUpdateDateTime() {
        return updateDateTime;
    }

    public void setUpdateDateTime(String updateDateTime) {
        this.updateDateTime = updateDateTime;
    }

    @NonNull
    @Override
    public String toString() {
        Gson gson = new GsonBuilder()
                .serializeNulls()
                .disableHtmlEscaping()
                .create();
        return gson.toJson(this);
    }
}
