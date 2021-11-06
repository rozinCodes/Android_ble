package com.waltonbd.smartscale.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.waltonbd.smartscale.GlideApp;
import com.waltonbd.smartscale.MyAppGlideModule;
import com.waltonbd.smartscale.R;
import com.waltonbd.smartscale.constant.ConstantValues;
import com.yolanda.health.qnblesdk.constant.QNUnit;
import com.yolanda.health.qnblesdk.out.QNBleApi;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.util.Locale;

public class Utilities {

    //*********** Checked first index of RadioButton ********//
    public static void selectFirstIndexRadioButton(RadioGroup radioGroup) {
        RadioButton rButton = (RadioButton) radioGroup.getChildAt(0);
        rButton.setChecked(true);
    }

    //*********** Checked second index of RadioButton ********//
    public static void selectSecondIndexRadioButton(RadioGroup radioGroup) {
        RadioButton rButton = (RadioButton) radioGroup.getChildAt(1);
        rButton.setChecked(true);
    }

    //*********** Get Selected option text from RadioButton ********//
    public static String getTagFromSelectedRadioButton(RadioGroup radioGroup) {
        // get selected radio button from radioGroup
        int selectedId = radioGroup.getCheckedRadioButtonId();
        // getCheckedRadioButtonId() returns -1 if no radio button is selected.
        if (selectedId == -1) return "male";

        // find the radiobutton by returned id
        RadioButton radioButton = radioGroup.findViewById(selectedId);
        return radioButton.getTag().toString();
    }

    //*********** Set RadioButton from based on tag ********//
    public static void setRadioButtonByTag(RadioGroup radioGroup, String tag) {
        int count = radioGroup.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = radioGroup.getChildAt(i);
            // https://stackoverflow.com/q/31541207/5280371
            if (view instanceof RadioButton) {
                RadioButton radioBtn = (RadioButton) view;
                String mTag = radioBtn.getTag().toString();
                radioBtn.setChecked(mTag.equalsIgnoreCase(tag));
            }
        }
    }

    //*********** Get Inch from Height ********//
    public static String getInchFromHeight(String height) {
        if (height != null) {
            String[] feetInch = height.split("'");
            double inch = (Double.parseDouble(feetInch[0]) * 12) + Double.parseDouble(feetInch[1].replace("\"", ""));
            return String.valueOf(inch);
        }
        return "";
    }

    //*********** Convert Inch to Height ********//
    public static String getHeightFromInch(String inch) {
        if (inch != null) {
            int inches = (int) Double.parseDouble(inch);
            int heightFeet = inches / 12;
            int heightInch = inches % 12;
            return String.format(Locale.getDefault(), "%d'%d\"", heightFeet, heightInch);
        }
        return "";
    }

    //*********** Get Inch from Height ********//
    public static String getCmFromHeight(String height) {
        if (!TextUtils.isEmpty(height)) {
            String[] feetInch = height.split("'");
            double heightInFeet = Double.parseDouble(feetInch[0]);
            double heightInInches = Double.parseDouble(feetInch[1].replace("\"", ""));
            double cm = (heightInFeet * 30.48) + (heightInInches * 2.54);
            return String.valueOf(cm);
        }
        return "";
    }

    //*********** Convert Inch to Height ********//
    public static String getHeightFromCm(String centimeter) {
        if (centimeter != null) {
            int cm = (int) Double.parseDouble(centimeter);
            int feetPart = (int) Math.floor((cm / 2.54) / 12);
            int inchesPart = (int) Math.ceil((cm / 2.54) - (feetPart * 12));
            return String.format(Locale.getDefault(), "%d'%d\"", feetPart, inchesPart);
        }
        return "";
    }

    public static int getWeightUnit(String unit) {
        switch (unit) {
            case QNUnit.WEIGHT_UNIT_LB_STR:
                return QNUnit.WEIGHT_UNIT_LB;
            case QNUnit.WEIGHT_UNIT_ST_STR:
                return QNUnit.WEIGHT_UNIT_ST;
            default:
                return QNUnit.WEIGHT_UNIT_KG;
        }
    }

    public static String getWeightUnitStr(int unit) {
        switch (unit) {
            case QNUnit.WEIGHT_UNIT_LB:
                return QNUnit.WEIGHT_UNIT_LB_STR;
            case QNUnit.WEIGHT_UNIT_ST:
                return QNUnit.WEIGHT_UNIT_ST_STR;
            default:
                return QNUnit.WEIGHT_UNIT_KG_STR;
        }
    }

    public static String getValueWithTargetUnit(QNBleApi mQNBleApi, double value) {
        DecimalFormat df = new DecimalFormat("#.##");
        return ConstantValues.WEIGHT_UNIT == QNUnit.WEIGHT_UNIT_ST ? mQNBleApi.convertWeightWithTargetUnit(value, ConstantValues.WEIGHT_UNIT)
                : df.format(ConstantValues.WEIGHT_UNIT == QNUnit.WEIGHT_UNIT_KG ? value : (value * 2.205) /* convert to lb */);
    }

    public static String getValueWithTargetUnit(QNBleApi mQNBleApi, String value) {
        double mValue = Double.parseDouble(value);
        DecimalFormat df = new DecimalFormat("#.##");
        return ConstantValues.WEIGHT_UNIT == QNUnit.WEIGHT_UNIT_ST ? mQNBleApi.convertWeightWithTargetUnit(mValue, ConstantValues.WEIGHT_UNIT)
                : df.format(ConstantValues.WEIGHT_UNIT == QNUnit.WEIGHT_UNIT_KG ? mValue : (mValue * 2.205) /* convert to lb */);
    }

    public static String getSubValueWithTargetUnit() {
        return ConstantValues.WEIGHT_UNIT != QNUnit.WEIGHT_UNIT_ST ? getWeightUnitStr(ConstantValues.WEIGHT_UNIT) : "";
    }

    //*********** Used to Tint the Background ********//
    public static Drawable changeBackgroundTint(Context context, int color) {
        Drawable unwrappedDrawable = AppCompatResources.getDrawable(context, R.drawable.background_sheet_grey);
        assert unwrappedDrawable != null;
        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable).mutate();
        DrawableCompat.setTint(wrappedDrawable, ContextCompat.getColor(context, color));
        /*
        // mutate to not share its state with any other drawable
        Drawable drawableWrap = DrawableCompat.wrap(context.getResources().getDrawable(R.drawable.background_sheet_round_white)).mutate();
        DrawableCompat.setTint(drawableWrap, ContextCompat.getColor(context, R.color.colorAccent));
        */
        return unwrappedDrawable;
    }

    //*********** Get placeholder based on gender ********//
    @DrawableRes
    public static int getPlaceholder(String gender) {
        if (gender != null && gender.equalsIgnoreCase("female")) {
            return R.drawable.avatar_woman;
        } else {
            return R.drawable.avatar_man;
        }
    }

    //*********** Load image into imageView ********//
    public static void loadImage(@NonNull ImageView imageView, String imageName, String gender) {
        @DrawableRes int resourceId = Utilities.getPlaceholder(gender);
        String imageUrl = ConstantValues.IMAGE_URL + imageName;
        GlideUrl glideUrl = new GlideUrl(imageUrl, new LazyHeaders.Builder()
                .addHeader("Authorization", ConstantValues.ACCESS_TOKEN)
                .build());
        Object model = imageName != null && !imageName.isEmpty() ? glideUrl : resourceId;
        GlideApp.with(imageView)
                .load(model)
                .placeholder(resourceId)
                .error(resourceId)
                .fallback(resourceId)
                .into(imageView);
    }

    public static Object getImage(String imageName, String gender) {
        @DrawableRes int resourceId = Utilities.getPlaceholder(gender);
        String imageUrl = ConstantValues.IMAGE_URL + imageName;
        return imageName != null && !imageName.isEmpty() ? imageUrl : resourceId;
    }

    /**
     * Turn drawable into byte array.
     *
     * @param drawable data
     * @return byte array
     */
    public static byte[] getByteFromDrawable(Drawable drawable) {
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * Convert
     *
     * @param bytes
     * @return
     */
    public static String toHexString(byte[] bytes) {
        char[] hexArray = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v / 16];
            hexChars[j * 2 + 1] = hexArray[v % 16];
        }
        return new String(hexChars);
    }

    /**
     * Decode hexadecimal text to string
     *
     * @param hexString
     * @return
     */
    public static String hexToString(String hexString) {
        String hex = hexString.replaceAll("[^0-9A-Fa-f]+", "");
        StringBuilder stringbuilder = new StringBuilder();
        char[] hexData = hex.toCharArray();
        for (int count = 0; count < hexData.length - 1; count += 2) {
            int firstDigit = Character.digit(hexData[count], 16);
            int lastDigit = Character.digit(hexData[count + 1], 16);
            int decimal = firstDigit * 16 + lastDigit;
            stringbuilder.append((char) decimal);
        }
        return stringbuilder.toString();
    }

    /**
     * Hex to ASCII Text Converter
     *
     * @param hexString
     * @return
     */
    public static String hexToASCIIText(String hexString) {
        String hex = hexString.replaceAll("[^0-9A-Fa-f]+", "");
        int l = hex.length();
        byte[] data = new byte[l / 2];
        for (int i = 0; i < l; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return new String(data);
    }
}
