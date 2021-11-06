package com.waltonbd.smartscale.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ForgetPassword {

    @Expose
    @SerializedName("username")
    private String username;
    @Expose
    @SerializedName("newPassword")
    private String newPassword;
    @Expose
    @SerializedName("otp")
    private int otp;

    public ForgetPassword(String username) {
        this.username = username;
    }

    public ForgetPassword(String username, int otp) {
        this.username = username;
        this.otp = otp;
    }

    public ForgetPassword(String username, String newPassword) {
        this.username = username;
        this.newPassword = newPassword;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public int getOtp() {
        return otp;
    }

    public void setOtp(int otp) {
        this.otp = otp;
    }
}
