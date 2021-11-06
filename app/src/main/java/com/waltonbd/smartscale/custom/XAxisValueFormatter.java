package com.waltonbd.smartscale.custom;

import android.util.Log;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.waltonbd.smartscale.utils.TimeUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * https://medium.com/@makkenasrinivasarao1/line-chart-implementation-with-mpandroidchart-af3dd11804a7
 */
public class XAxisValueFormatter extends ValueFormatter {

    List<String> datesList;

    public XAxisValueFormatter(List<String> arrayOfDates) {
        this.datesList = arrayOfDates;
    }

    @Override
    public String getAxisLabel(float value, AxisBase axis) {
        //return datesList.get((int) value);
        /*
        Depends on the position number on the X axis, we need to display the label, Here, this is the logic to convert the float value to integer so that I can get the value from array based on that integer and can convert it to the required value here, month and date as value. This is required for my data to show properly, you can customize according to your needs.
        */
        int position = Math.round(value);
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd", Locale.getDefault());
        if (position >= 0 && position < datesList.size())
            return sdf.format(new Date((TimeUtil.getDateInMilliSeconds(datesList.get(position), "yyyy-MM-dd"))));
        return "";
    }
}
