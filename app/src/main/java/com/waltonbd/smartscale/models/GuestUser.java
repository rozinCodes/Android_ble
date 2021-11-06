package com.waltonbd.smartscale.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GuestUser {

    @Expose
    @SerializedName("id")
    private long id;
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
    @SerializedName("height")
    private String height;
    @Expose
    @SerializedName("guestType")
    private String guestType;
    @Expose
    @SerializedName("independentMode")
    private boolean independentMode;
    @Expose
    @SerializedName("athlateMode")
    private boolean athlateMode;
    @Expose
    @SerializedName("createDateTime")
    private String createDateTime;
    @Expose
    @SerializedName("updateDateTime")
    private String updateDateTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getGuestType() {
        return guestType;
    }

    public void setGuestType(String guestType) {
        this.guestType = guestType;
    }

    public boolean getIndependentMode() {
        return independentMode;
    }

    public void setIndependentMode(boolean independentMode) {
        this.independentMode = independentMode;
    }

    public boolean getAthlateMode() {
        return athlateMode;
    }

    public void setAthlateMode(boolean athlateMode) {
        this.athlateMode = athlateMode;
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
}
