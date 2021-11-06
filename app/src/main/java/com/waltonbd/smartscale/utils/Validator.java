package com.waltonbd.smartscale.utils;

import android.util.Patterns;

public class Validator {

    public static boolean validateText(String name) {
        if (name == null) return true;
        return name.isEmpty();
    }

    public static boolean validateEmail(String email) {
        if (email == null) return true;
        return !Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean validatePassword(String password) {
        if (password == null) return true;
        return password.length() < 6;
    }

    public static boolean validateLoginPassword(String password) {
        if (password == null) return true;
        return password.isEmpty();
    }

    public static boolean validateName(String name) {
        if (name == null) return true;
        return name.isEmpty();
    }

    public static boolean validateBirthday(String birthday) {
        if (birthday == null) return true;
        return birthday.isEmpty();
    }

    public static boolean validateHeight(String height) {
        if (height == null) return true;
        return height.isEmpty();
    }
}
