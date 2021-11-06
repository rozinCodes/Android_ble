package com.waltonbd.smartscale.utils;

import java.text.NumberFormat;
import java.util.Locale;

public class NumberUtil {

    public static String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }

    public static String getFormattedNumber(String number) {
        if (number != null && !number.equals("null") && !number.isEmpty()) {
            double val = Double.parseDouble(number);
            return NumberFormat.getNumberInstance(Locale.getDefault()).format(val);
        } else {
            return number;
        }
    }

    public static String getFormattedNumber(double number) {
        return NumberFormat.getNumberInstance(Locale.getDefault()).format(number);
    }

    public static String toStringAsFixed(String value) {
        if (value != null && !value.equals("null") && !value.isEmpty()) {
            double val = Double.parseDouble(value);
            return String.format(Locale.getDefault(), "%.2f", val);
        } else {
            return value;
        }
    }
}
