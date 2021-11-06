package com.waltonbd.smartscale.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;


@Entity(tableName = "tbl_measurements")
public class MeasurementsTable {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private long userId;
    private String weight;
    private String heartRate;
    private String cardiacIndex;
    private String bmi;
    private String bodyFat;
    private String bodyType;
    private String fatFreeBodyWeight;
    private String subCutaneousFat;
    private String visceralFat;
    private String skeletalMuscle;
    private String muscleMass;
    private String bodyWater;
    private String boneMass;
    private String protein;
    private String bmr;
    private String metabolicAge;
    private String createDateTime;
    private String updateDateTime;

    public MeasurementsTable() {
        weight = "0.0";
        heartRate = "0.0";
        cardiacIndex = "0.0";
        bmi = "0.0";
        bodyFat = "0.0";
        bodyType = "0.0";
        fatFreeBodyWeight = "0.0";
        subCutaneousFat = "0.0";
        visceralFat = "0.0";
        skeletalMuscle = "0.0";
        muscleMass = "0.0";
        bodyWater = "0.0";
        boneMass = "0.0";
        protein = "0.0";
        bmr = "0.0";
        metabolicAge = "0.0";
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

    public String getSubCutaneousFat() {
        return subCutaneousFat;
    }

    public void setSubCutaneousFat(String subCutaneousFat) {
        this.subCutaneousFat = subCutaneousFat;
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

    @NotNull
    @Override
    public String toString() {
        return "MeasurementsTable{" +
                "id=" + id +
                ", userId=" + userId +
                ", weight='" + weight + '\'' +
                ", heartRate='" + heartRate + '\'' +
                ", cardiacIndex='" + cardiacIndex + '\'' +
                ", bmi='" + bmi + '\'' +
                ", bodyFat='" + bodyFat + '\'' +
                ", bodyType='" + bodyType + '\'' +
                ", fatFreeBodyWeight='" + fatFreeBodyWeight + '\'' +
                ", subCutaneousFat='" + subCutaneousFat + '\'' +
                ", visceralFat='" + visceralFat + '\'' +
                ", skeletalMuscle='" + skeletalMuscle + '\'' +
                ", muscleMass='" + muscleMass + '\'' +
                ", bodyWater='" + bodyWater + '\'' +
                ", boneMass='" + boneMass + '\'' +
                ", protein='" + protein + '\'' +
                ", bmr='" + bmr + '\'' +
                ", metabolicAge='" + metabolicAge + '\'' +
                ", createDateTime=" + createDateTime +
                ", updateDateTime=" + updateDateTime +
                '}';
    }
}
