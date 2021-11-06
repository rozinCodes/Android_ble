package com.waltonbd.smartscale.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tbl_guest_users")
public class GuestUsersTable {
    @PrimaryKey(autoGenerate = true)
    private int ID;
    private String Image;
    private String Name;
    private String Gender;
    private String BirthDate;
    private String Height;
    private char AthleteMode;
    private int User_ID;
    private String GuestType;
    private String IndependentMode;
    private String CreatedAt;
    private String UpdateAt;

    public GuestUsersTable() {
    }

    public GuestUsersTable(String image, String name, String gender, String birthDate, String height, char athleteMode, int user_ID, String guestType, String independentMode, String createdAt, String updateAt) {
        Image = image;
        Name = name;
        Gender = gender;
        BirthDate = birthDate;
        Height = height;
        AthleteMode = athleteMode;
        User_ID = user_ID;
        GuestType = guestType;
        IndependentMode = independentMode;
        CreatedAt = createdAt;
        UpdateAt = updateAt;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getBirthDate() {
        return BirthDate;
    }

    public void setBirthDate(String birthDate) {
        BirthDate = birthDate;
    }

    public String getHeight() {
        return Height;
    }

    public void setHeight(String height) {
        Height = height;
    }

    public char getAthleteMode() {
        return AthleteMode;
    }

    public void setAthleteMode(char athleteMode) {
        AthleteMode = athleteMode;
    }

    public int getUser_ID() {
        return User_ID;
    }

    public void setUser_ID(int user_ID) {
        User_ID = user_ID;
    }

    public String getGuestType() {
        return GuestType;
    }

    public void setGuestType(String guestType) {
        GuestType = guestType;
    }

    public String getIndependentMode() {
        return IndependentMode;
    }

    public void setIndependentMode(String independentMode) {
        IndependentMode = independentMode;
    }

    public String getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        CreatedAt = createdAt;
    }

    public String getUpdateAt() {
        return UpdateAt;
    }

    public void setUpdateAt(String updateAt) {
        UpdateAt = updateAt;
    }
}
