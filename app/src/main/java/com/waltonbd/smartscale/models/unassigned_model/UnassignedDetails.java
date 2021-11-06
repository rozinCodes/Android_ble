package com.waltonbd.smartscale.models.unassigned_model;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class UnassignedDetails {

    private int id;
    private String weight;
    private String heartRate;
    private String cardiacIndex;
    private String bmi;
    private String bodyFat;
    private String fatFreeBodyWeight;
    private String subcutaneousFat;
    private String visceralFat;
    private String bodyWater;
    private String skeletalMuscle;
    private String createDateTime;
    private String updateDateTime;

    private boolean isChecked;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(String heartRate) {
        this.heartRate = heartRate;
    }

    public String getCardiacIndex() {
        return cardiacIndex;
    }

    public void setCardiacIndex(String cardiacIndex) {
        this.cardiacIndex = cardiacIndex;
    }

    public String getBmi() {
        return bmi;
    }

    public void setBmi(String bmi) {
        this.bmi = bmi;
    }

    public String getBodyFat() {
        return bodyFat;
    }

    public void setBodyFat(String bodyFat) {
        this.bodyFat = bodyFat;
    }

    public String getFatFreeBodyWeight() {
        return fatFreeBodyWeight;
    }

    public void setFatFreeBodyWeight(String fatFreeBodyWeight) {
        this.fatFreeBodyWeight = fatFreeBodyWeight;
    }

    public String getSubcutaneousFat() {
        return subcutaneousFat;
    }

    public void setSubcutaneousFat(String subcutaneousFat) {
        this.subcutaneousFat = subcutaneousFat;
    }

    public String getVisceralFat() {
        return visceralFat;
    }

    public void setVisceralFat(String visceralFat) {
        this.visceralFat = visceralFat;
    }

    public String getBodyWater() {
        return bodyWater;
    }

    public void setBodyWater(String bodyWater) {
        this.bodyWater = bodyWater;
    }

    public String getSkeletalMuscle() {
        return skeletalMuscle;
    }

    public void setSkeletalMuscle(String skeletalMuscle) {
        this.skeletalMuscle = skeletalMuscle;
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

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
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
