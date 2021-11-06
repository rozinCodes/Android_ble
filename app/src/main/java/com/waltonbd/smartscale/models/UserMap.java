package com.waltonbd.smartscale.models;

public class UserMap {

    private long userId;
    private long guestId;

    public UserMap(long userId, long guestId) {
        this.userId = userId;
        this.guestId = guestId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getGuestId() {
        return guestId;
    }

    public void setGuestId(long guestId) {
        this.guestId = guestId;
    }
}
