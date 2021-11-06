package com.waltonbd.smartscale.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Measurement {

    @Expose
    @SerializedName("id")
    private long id;
    @Expose
    @SerializedName("userId")
    private long userId;
    @Expose
    @SerializedName("guestId")
    private long guestId;
    @Expose
    @SerializedName("weight")
    private String weight;
    @Expose
    @SerializedName("heartRate")
    private String heartRate;
    @Expose
    @SerializedName("cardiacIndex")
    private String cardiacIndex;
    @Expose
    @SerializedName("bmi")
    private String bmi;
    @Expose
    @SerializedName("bodyFat")
    private String bodyFat;
    @Expose
    @SerializedName("bodyType")
    private String bodyType;
    @Expose
    @SerializedName("fatFreeBodyWeight")
    private String fatFreeBodyWeight;
    @Expose
    @SerializedName("subcutaneousFat")
    private String subcutaneousFat;
    @Expose
    @SerializedName("visceralFat")
    private String visceralFat;
    @Expose
    @SerializedName("skeletalMuscle")
    private String skeletalMuscle;
    @Expose
    @SerializedName("muscleMass")
    private String muscleMass;
    @Expose
    @SerializedName("bodyWater")
    private String bodyWater;
    @Expose
    @SerializedName("boneMass")
    private String boneMass;
    @Expose
    @SerializedName("protein")
    private String protein;
    @Expose
    @SerializedName("bmr")
    private String bmr;
    @Expose
    @SerializedName("metabolicAge")
    private String metabolicAge;
    @Expose
    @SerializedName("createDateTime")
    private String createDateTime;
    @Expose
    @SerializedName("updateDateTime")
    private String updateDateTime;

    public Measurement() {
    }

    public Measurement(String weight, String heartRate, String cardiacIndex, String bmi, String bodyFat, String bodyType, String fatFreeBodyWeight, String subcutaneousFat, String visceralFat, String skeletalMuscle, String muscleMass, String bodyWater, String boneMass, String protein, String bmr, String metabolicAge, String createDateTime, String updateDateTime) {
        this.weight = weight;
        this.heartRate = heartRate;
        this.cardiacIndex = cardiacIndex;
        this.bmi = bmi;
        this.bodyFat = bodyFat;
        this.bodyType = bodyType;
        this.fatFreeBodyWeight = fatFreeBodyWeight;
        this.subcutaneousFat = subcutaneousFat;
        this.visceralFat = visceralFat;
        this.skeletalMuscle = skeletalMuscle;
        this.muscleMass = muscleMass;
        this.bodyWater = bodyWater;
        this.boneMass = boneMass;
        this.protein = protein;
        this.bmr = bmr;
        this.metabolicAge = metabolicAge;
        this.createDateTime = createDateTime;
        this.updateDateTime = updateDateTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getBodyType() {
        return bodyType;
    }

    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
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

    public String getSkeletalMuscle() {
        return skeletalMuscle;
    }

    public void setSkeletalMuscle(String skeletalMuscle) {
        this.skeletalMuscle = skeletalMuscle;
    }

    public String getMuscleMass() {
        return muscleMass;
    }

    public void setMuscleMass(String muscleMass) {
        this.muscleMass = muscleMass;
    }

    public String getBodyWater() {
        return bodyWater;
    }

    public void setBodyWater(String bodyWater) {
        this.bodyWater = bodyWater;
    }

    public String getBoneMass() {
        return boneMass;
    }

    public void setBoneMass(String boneMass) {
        this.boneMass = boneMass;
    }

    public String getProtein() {
        return protein;
    }

    public void setProtein(String protein) {
        this.protein = protein;
    }

    public String getBmr() {
        return bmr;
    }

    public void setBmr(String bmr) {
        this.bmr = bmr;
    }

    public String getMetabolicAge() {
        return metabolicAge;
    }

    public void setMetabolicAge(String metabolicAge) {
        this.metabolicAge = metabolicAge;
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
