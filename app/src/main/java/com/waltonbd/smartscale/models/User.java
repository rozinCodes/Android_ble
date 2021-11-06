package com.waltonbd.smartscale.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class User {

    @Expose
    @SerializedName("id")
    private long id;
    @Expose
    @SerializedName("username")
    private String username;
    @Expose
    @SerializedName("fullName")
    private String fullName;
    @Expose
    @SerializedName("image")
    private String image;
    @Expose
    @SerializedName("gender")
    private String gender;
    @Expose
    @SerializedName("birthdate")
    private String birthdate;
    @Expose
    @SerializedName("weightUnit")
    private String weightUnit;
    @Expose
    @SerializedName("heightUnit")
    private String heightUnit;
    @Expose
    @SerializedName("height")
    private String height;
    @Expose
    @SerializedName("weight")
    private String weight;
    @Expose
    @SerializedName("phone")
    private String phone;
    @Expose
    @SerializedName("athlateMode")
    private boolean athlateMode;
    @Expose
    @SerializedName("guestUser")
    private List<GuestUser> guestUsers;
    @Expose
    @SerializedName("measurements")
    private List<Measurement> measurements;
    @Expose
    @SerializedName("deviceManagements")
    private List<DeviceManagements> deviceManagements;
    @Expose
    @SerializedName("timeout")
    private String timeout;
    @Expose
    @SerializedName("tokenType")
    private String tokenType;
    @Expose
    @SerializedName("accessToken")
    private String accessToken;
    @Expose
    @SerializedName("createDateTime")
    private String createDateTime;
    @Expose
    @SerializedName("updateDateTime")
    private String updateDateTime;
    @Expose
    @SerializedName("status")
    private boolean status;
    @Expose
    @SerializedName("message")
    private String message;


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
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

    public boolean getAthlateMode() {
        return athlateMode;
    }

    public void setAthlateMode(boolean athlateMode) {
        this.athlateMode = athlateMode;
    }

    public List<GuestUser> getGuestUsers() {
        return guestUsers;
    }

    public void setGuestUsers(List<GuestUser> guestUsers) {
        this.guestUsers = guestUsers;
    }


    public List<Measurement> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(List<Measurement> measurements) {
        this.measurements = measurements;
    }

    public List<DeviceManagements> getDeviceManagements() {
        return deviceManagements;
    }

    public void setDeviceManagements(List<DeviceManagements> deviceManagements) {
        this.deviceManagements = deviceManagements;
    }

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
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

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @NotNull
    @Override
    public String toString() {
        Gson gson = new GsonBuilder()
                .serializeNulls()
                .disableHtmlEscaping()
                .create();
        return gson.toJson(this);
    }
}
