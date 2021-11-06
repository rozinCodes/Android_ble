package com.waltonbd.smartscale.custom;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.NumberPicker;

import androidx.annotation.ColorInt;
import androidx.core.graphics.BlendModeColorFilterCompat;
import androidx.core.graphics.BlendModeCompat;

import java.lang.reflect.Field;

public class MyNumberPicker extends NumberPicker {

    public MyNumberPicker(Context context) {
        super(context);
        init();
    }

    public MyNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyNumberPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MyNumberPicker(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setDividerColor(Color.LTGRAY);
    }

    public void setDividerColor(@ColorInt int color) {
        try {
            Field fDividerDrawable = NumberPicker.class.getDeclaredField("mSelectionDivider");
            fDividerDrawable.setAccessible(true);
            Drawable drawable = (Drawable) fDividerDrawable.get(this);
            assert drawable != null;
            drawable.setColorFilter(BlendModeColorFilterCompat.createBlendModeColorFilterCompat(color, BlendModeCompat.SRC_ATOP));
            drawable.invalidateSelf();
            postInvalidate(); // Drawable is dirty
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
