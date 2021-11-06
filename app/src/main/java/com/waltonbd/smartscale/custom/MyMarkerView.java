
package com.waltonbd.smartscale.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.waltonbd.smartscale.R;
import com.waltonbd.smartscale.constant.ConstantValues;
import com.waltonbd.smartscale.constant.Unit;
import com.waltonbd.smartscale.utils.TimeUtil;
import com.waltonbd.smartscale.widget.TextScriptView;
import com.yolanda.health.qnblesdk.constant.QNUnit;
import com.yolanda.health.qnblesdk.out.QNBleApi;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Custom implementation of the MarkerView.
 *
 * @author Philipp Jahoda
 */
@SuppressLint("ViewConstructor")
public class MyMarkerView extends MarkerView {

    private final TextView chartDataTime;
    private final TextScriptView chartDataValue;

    private final List<String> datesList;
    private final String unit;
    private final QNBleApi mQNBleApi;

    public MyMarkerView(Context context, List<String> datesList, String unit) {
        super(context, R.layout.marker_view_top);
        this.datesList = datesList;
        this.unit = unit;
        mQNBleApi = QNBleApi.getInstance(context);

        chartDataTime = findViewById(R.id.chart_data_time);
        chartDataValue = findViewById(R.id.chart_data_value);
    }

    // runs every time the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        /*if (e instanceof CandleEntry) {
            CandleEntry ce = (CandleEntry) e;
            //tvContent.setText(String.format("%s kg", Utils.formatNumber(ce.getHigh(), 0, true)));
        } else {
            //tvContent.setText(String.format("%s kg", Utils.formatNumber(e.getY(), 0, true)));
        }*/

        int position = Math.round(e.getX());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyy", Locale.getDefault());
        String date = sdf.format(new Date((TimeUtil.getDateInMilliSeconds(datesList.get(position), "yyyy-MM-dd"))));

        if (unit.equals(QNUnit.WEIGHT_UNIT_KG_STR)) {
            chartDataTime.setText(String.format(Locale.getDefault(), "%s", date));
            chartDataValue.setText(getActualUnit(e.getY()));
            chartDataValue.setSubScriptText("");
        } else {
            chartDataTime.setText(String.format(Locale.getDefault(), "%s", date));
            chartDataValue.setText(String.format(Locale.getDefault(), "%.2f", e.getY()));
            chartDataValue.setSubScriptText(String.format(Locale.getDefault(), "%s", unit));
        }
        super.refreshContent(e, highlight);
    }

    private String getActualUnit(float value) {
        if (ConstantValues.WEIGHT_UNIT == QNUnit.WEIGHT_UNIT_KG) {
            return mQNBleApi.convertWeightWithTargetUnit(value, ConstantValues.WEIGHT_UNIT);
        }
        // Convert lb to kg for original value
        return mQNBleApi.convertWeightWithTargetUnit((value / 2.205), ConstantValues.WEIGHT_UNIT);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2.0f), -getHeight());
    }

    /**
     * https://stackoverflow.com/a/33540808/5280371
     */
    @Override
    public void draw(Canvas canvas, float posX, float posY) {
        MPPointF offset = getOffsetForDrawingAtPoint(posX, posY);

        int saveId = canvas.save();
        // translate to the correct position and draw
        canvas.translate(posX + offset.x, 0);
        draw(canvas);
        canvas.restoreToCount(saveId);
    }
}
