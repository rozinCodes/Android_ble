package com.waltonbd.smartscale.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserProfile {

    @Expose
    @SerializedName("username")
    private String username;
    @Expose
    @SerializedName("gender")
    private String gender;
    @Expose
    @SerializedName("fullName")
    private String fullName;
    @Expose
    @SerializedName("birthdate")
    private String birthdate;
    @Expose
    @SerializedName("height")
    private String height;
    @Expose
    @SerializedName("athlateMode")
    private boolean athlateMode;


    public UserProfile(String gender, String fullName, String birthdate, String height, boolean athlateMode) {
        this.gender = gender;
        this.fullName = fullName;
        this.birthdate = birthdate;
        this.height = height;
        this.athlateMode = athlateMode;
    }

    public UserProfile(String username, String gender, String fullName, String birthdate, String height, boolean athlateMode) {
        this.username = username;
        this.gender = gender;
        this.fullName = fullName;
        this.birthdate = birthdate;
        this.height = height;
        this.athlateMode = athlateMode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public boolean isAthlateMode() {
        return athlateMode;
    }

    public void setAthlateMode(boolean athlateMode) {
        this.athlateMode = athlateMode;
    }
}
