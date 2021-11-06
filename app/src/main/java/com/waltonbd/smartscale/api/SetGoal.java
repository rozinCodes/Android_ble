package com.waltonbd.smartscale.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SetGoal {

    @Expose
    @SerializedName("userId")
    private long userId;
    @Expose
    @SerializedName("guestId")
    private int guestId;
    @Expose
    @SerializedName("weight")
    private String weight;
    @Expose
    @SerializedName("bodyFat")
    private String bodyFat;

    public SetGoal(long userId, int guestId, String weight, String bodyFat) {
        this.userId = userId;
        this.guestId = guestId;
        this.weight = weight;
        this.bodyFat = bodyFat;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getGuestId() {
        return guestId;
    }

    public void setGuestId(int guestId) {
        this.guestId = guestId;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getBodyFat() {
        return bodyFat;
    }

    public void setBodyFat(String bodyFat) {
        this.bodyFat = bodyFat;
    }
}
