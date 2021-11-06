package com.waltonbd.smartscale.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.suke.widget.SwitchButton;
import com.waltonbd.smartscale.R;
import com.waltonbd.smartscale.custom.MyNumberPicker;

public class HeightPickerDialog {

    /**
     * Height select listener
     */
    private static OnHeightSelectListener onHeightSelectListener;

    public interface OnHeightSelectListener {
        void onHeightSelected(String height);
    }

    public static void setOnCheckedChangeListener(OnHeightSelectListener listener) {
        onHeightSelectListener = listener;
    }

    public static void showDialog(Context context, int heightFeet, int heightInch) {

        Resources resources = context.getResources();
        LinearLayout LL = new LinearLayout(context);
        LL.setOrientation(LinearLayout.HORIZONTAL);
        LL.setPadding((int) resources.getDimension(R.dimen.margin_26dp),
                (int) resources.getDimension(R.dimen.margin_20dp),
                (int) resources.getDimension(R.dimen.margin_20dp),
                (int) resources.getDimension(R.dimen.margin_0dp));

        final MyNumberPicker aNumberPickerFeet = new MyNumberPicker(context);
        aNumberPickerFeet.setMaxValue(8);
        aNumberPickerFeet.setMinValue(1);
        if (heightFeet > 0) {
            aNumberPickerFeet.setValue(heightFeet);
        } else {
            aNumberPickerFeet.setValue(5);
        }

        final MyNumberPicker aNumberPickerInch = new MyNumberPicker(context);
        aNumberPickerInch.setMaxValue(11);
        aNumberPickerInch.setMinValue(0);
        if (heightInch > 0) {
            aNumberPickerInch.setValue(heightInch);
        }

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(50, 50);
        params.gravity = Gravity.CENTER;
        params.setMargins(0, (int) resources.getDimension(R.dimen.app_standard_padding), 0, 0);

        LinearLayout.LayoutParams feetParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        feetParams.weight = 1;
        feetParams.gravity = Gravity.CENTER;

        LinearLayout.LayoutParams inchParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        inchParams.weight = 1;
        inchParams.gravity = Gravity.CENTER;

        LinearLayout llFeet = new LinearLayout(context);
        llFeet.setOrientation(LinearLayout.VERTICAL);
        llFeet.setGravity(Gravity.CENTER_HORIZONTAL);

        TextView tvFeet = new TextView(context);
        tvFeet.setText("Feet");
        tvFeet.setTextSize(18);
        tvFeet.setTextColor(Color.BLACK);
        tvFeet.setGravity(Gravity.CENTER);
        tvFeet.setLayoutParams(feetParams);

        llFeet.addView(tvFeet);
        llFeet.addView(aNumberPickerFeet);

        LinearLayout llInch = new LinearLayout(context);
        llInch.setOrientation(LinearLayout.VERTICAL);

        TextView tvInch = new TextView(context);
        tvInch.setText("Inch");
        tvInch.setTextSize(18);
        tvInch.setTextColor(Color.BLACK);
        tvInch.setGravity(Gravity.CENTER);
        tvInch.setLayoutParams(inchParams);

        llInch.addView(tvInch);
        llInch.addView(aNumberPickerInch);

        LL.setLayoutParams(params);
        LL.addView(llFeet, feetParams);
        LL.addView(llInch, inchParams);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        //alertDialogBuilder.setTitle("Please select your height");
        alertDialogBuilder.setView(LL);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Ok", (dialog, id) -> {

            String heightFeetStr = aNumberPickerFeet.getValue() > 0 ?
                    String.valueOf(aNumberPickerFeet.getValue()) : "-";

            String heightInchStr = aNumberPickerInch.getValue() >= 0 ?
                    String.valueOf(aNumberPickerInch.getValue()) : "-";

            String height = String.format("%s'%s\"", heightFeetStr, heightInchStr);
            if (onHeightSelectListener != null) {
                onHeightSelectListener.onHeightSelected(height);
            }
        }).setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
