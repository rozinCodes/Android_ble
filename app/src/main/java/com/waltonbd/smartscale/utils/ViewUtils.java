package com.waltonbd.smartscale.utils;

import android.app.Activity;

import androidx.annotation.NonNull;

public class ViewUtils {

    @NonNull
    public static final ViewUtils INSTANCE;

    static {
        INSTANCE = new ViewUtils();
    }

    public static void successToast(@NonNull Activity activity) {
    }
}
