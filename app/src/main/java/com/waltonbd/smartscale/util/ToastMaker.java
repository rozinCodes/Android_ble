package com.waltonbd.smartscale.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by hdr on 15/9/1.
 */
public class ToastMaker {

    private final static Object synObj = new Object();
    static Toast toast;

    public static void show(Context context, String text) {

        synchronized (synObj) {
            if (toast != null) {
                toast.cancel();
            }
            toast = new Toast(context);

            TextView textView = newTv(context);
            textView.setText(text);

            toast.setView(textView);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private static TextView newTv(Context context) {
        TextView textView = new TextView(context);
        textView.setPaddingRelative(UIUtils.dpToPx(context, 8), UIUtils.dpToPx(context, 8), UIUtils.dpToPx(context, 8), UIUtils.dpToPx(context, 8));
        textView.setGravity(Gravity.CENTER);

        GradientDrawable gd = new GradientDrawable();
        gd.setColor(0xFF5866E0);
        gd.setCornerRadius(UIUtils.dpToPx(context, 16));
        textView.setBackground(gd);

        textView.setTextSize(14);
        textView.setTextColor(Color.WHITE);
        return textView;
    }


    public static void show(Context context, int textResId) {
        show(context, context.getString(textResId));
    }
}
