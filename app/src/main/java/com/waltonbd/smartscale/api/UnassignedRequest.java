package com.waltonbd.smartscale.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UnassignedRequest {

    @Expose
    @SerializedName("userId")
    private int userId;
    @Expose
    @SerializedName("guestId")
    private int guestId;
    @Expose
    @SerializedName("unAssignId")
    private List<Integer> unAssignId;

    public UnassignedRequest(List<Integer> unAssignId) {
        this.unAssignId = unAssignId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getGuestId() {
        return guestId;
    }

    public void setGuestId(int guestId) {
        this.guestId = guestId;
    }

    public List<Integer> getUnAssignId() {
        return unAssignId;
    }

    public void setUnAssignId(List<Integer> unAssignId) {
        this.unAssignId = unAssignId;
    }
}
