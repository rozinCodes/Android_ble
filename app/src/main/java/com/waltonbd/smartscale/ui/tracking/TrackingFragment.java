package com.waltonbd.smartscale.ui.tracking;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.jaeger.library.StatusBarUtil;
import com.waltonbd.smartscale.R;
import com.waltonbd.smartscale.adapters.IndexRlyAdapter;
import com.waltonbd.smartscale.constant.ConstantValues;
import com.waltonbd.smartscale.constant.Parameter;
import com.waltonbd.smartscale.constant.Unit;
import com.waltonbd.smartscale.custom.MyMarkerView;
import com.waltonbd.smartscale.custom.XAxisValueFormatter;
import com.waltonbd.smartscale.custom.YAxisValueFormatter;
import com.waltonbd.smartscale.databinding.FragmentTrackingBinding;
import com.waltonbd.smartscale.entities.MeasurementsTable;
import com.waltonbd.smartscale.models.IndexItem;
import com.waltonbd.smartscale.view.MeasurementHistory;
import com.waltonbd.smartscale.viewmodels.SmartScaleViewModel;
import com.yolanda.health.qnblesdk.constant.QNUnit;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TrackingFragment extends Fragment implements IndexRlyAdapter.OnItemClickListener {

    private static final String TAG = "TrackingFragment";

    protected SmartScaleViewModel smartScaleViewModel;
    private FragmentTrackingBinding binding;
    private IndexRlyAdapter indexRlyAdapter;

    List<MeasurementsTable> measurementList = new ArrayList<>();
    private IndexItem item;
    private LineChart chart;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setColor(requireActivity(), Color.WHITE, 0);
        StatusBarUtil.setLightMode(requireActivity());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTrackingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        smartScaleViewModel = new ViewModelProvider(requireActivity()).get(SmartScaleViewModel.class);

        initView();
        setupChart();

        // by default load weekly data and wight filter selected
        Executors.newSingleThreadExecutor().execute(() -> {
            measurementList = smartScaleViewModel.getLastMeasurementsGroupWeekly();
            setWeight();
        });

        smartScaleViewModel.getIndexItems().observe(getViewLifecycleOwner(), indexItems -> {
            indexRlyAdapter.submitList(indexItems);
        });

        binding.daysFilter.setOnChangeListener(position -> {
            // clear previous data from list
            measurementList.clear();
            switch (position) {
                case 0:
                    getWeeklyData();
                    break;
                case 1:
                    getMonthlyData();
                    break;
                case 2:
                    getYearlyData();
                    break;
            }
        });
    }

    private void initView() {

        chart = binding.chart;

        // default checked 1st position
        binding.daysFilter.setCheckedPosition(0);

        indexRlyAdapter = new IndexRlyAdapter(this, requireContext());
        binding.indexRlv.setAdapter(indexRlyAdapter);

        binding.history.setOnClickListener(view -> {
            // Goto Measurement History Activity
            startActivity(new Intent(requireContext(), MeasurementHistory.class));
        });
    }

    private void setupChart() {

        // // Chart Style // //
        chart.setNoDataText("No Data Available");
        Paint paint = chart.getPaint(Chart.PAINT_INFO);
        paint.setColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary));
        chart.invalidate(); // refresh

        // background color
        chart.setBackgroundColor(Color.WHITE);
        // disable description text
        chart.getDescription().setEnabled(false);
        // enable touch gestures
        chart.setTouchEnabled(true);
        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(false);
        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(false);
        chart.setDrawGridBackground(false);
        // disable legend
        chart.getLegend().setEnabled(false);

        // add data
        //setData(10, 90);

        // draw points over time
        //chart.animateX(1000);
    }

    // --------------------- Database Operation ---------------------//
    private void getWeeklyData() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            measurementList = smartScaleViewModel.getLastMeasurementsGroupWeekly();
            handler.post(() -> onItemClick(item));
        });
    }

    private void getMonthlyData() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            measurementList = smartScaleViewModel.getLastMeasurementsGroupMonthly();
            handler.post(() -> onItemClick(item));
        });
    }

    private void getYearlyData() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            measurementList = smartScaleViewModel.getLastMeasurementsGroupYearly();
            handler.post(() -> onItemClick(item));
        });
    }

    private double convertUnit(String strValue) {
        double value = Double.parseDouble(strValue);
        if (ConstantValues.WEIGHT_UNIT == QNUnit.WEIGHT_UNIT_KG) {
            return value;
        }
        return value * 2.205; // Convert kg to lb for others case
    }

    // --------------------- Filter Data ---------------------//
    private void setWeight() {
        List<String> dates = new ArrayList<>();
        List<Double> listOfData = new ArrayList<>();
        for (MeasurementsTable data : measurementList) {
            if (data.getWeight() != null) {
                dates.add(data.getCreateDateTime());
                listOfData.add(convertUnit(data.getWeight()));
            }
        }
        setData(dates, listOfData, Unit.WEIGHT);
    }

    private void setHeartRate() {
        List<String> dates = new ArrayList<>();
        List<Double> listOfData = new ArrayList<>();
        for (MeasurementsTable data : measurementList) {
            if (data.getHeartRate() != null) {
                dates.add(data.getCreateDateTime());
                listOfData.add(Double.parseDouble(data.getHeartRate()));
            }
        }
        setData(dates, listOfData, Unit.HEART_RATE);
    }

    private void setCardiacIndex() {
        List<String> dates = new ArrayList<>();
        List<Double> listOfData = new ArrayList<>();
        for (MeasurementsTable data : measurementList) {
            if (data.getCardiacIndex() != null) {
                dates.add(data.getCreateDateTime());
                listOfData.add(Double.parseDouble(data.getCardiacIndex()));
            }
        }
        setData(dates, listOfData, Unit.CARDIAC_INDEX);
    }

    private void setBmi() {
        List<String> dates = new ArrayList<>();
        List<Double> listOfData = new ArrayList<>();
        for (MeasurementsTable data : measurementList) {
            if (data.getBmi() != null) {
                dates.add(data.getCreateDateTime());
                listOfData.add(Double.parseDouble(data.getBmi()));
            }
        }
        setData(dates, listOfData, Unit.BMI);
    }

    private void setBodyFat() {
        List<String> dates = new ArrayList<>();
        List<Double> listOfData = new ArrayList<>();
        for (MeasurementsTable data : measurementList) {
            if (data.getBodyFat() != null) {
                dates.add(data.getCreateDateTime());
                listOfData.add(Double.parseDouble(data.getBodyFat()));
            }
        }
        setData(dates, listOfData, Unit.BODY_FAT);
    }

    private void setFatFreeBodyWeight() {
        List<String> dates = new ArrayList<>();
        List<Double> listOfData = new ArrayList<>();
        for (MeasurementsTable data : measurementList) {
            if (data.getFatFreeBodyWeight() != null) {
                dates.add(data.getCreateDateTime());
                listOfData.add(convertUnit(data.getFatFreeBodyWeight()));
            }
        }
        setData(dates, listOfData, Unit.FAT_FREE_BODY_WEIGHT);
    }

    private void setSubcutaneousFat() {
        List<String> dates = new ArrayList<>();
        List<Double> listOfData = new ArrayList<>();
        for (MeasurementsTable data : measurementList) {
            if (data.getSubCutaneousFat() != null) {
                dates.add(data.getCreateDateTime());
                listOfData.add(Double.parseDouble(data.getSubCutaneousFat()));
            }
        }
        setData(dates, listOfData, Unit.SUBCUTANEOUS_FAT);
    }

    private void setVisceralFat() {
        List<String> dates = new ArrayList<>();
        List<Double> listOfData = new ArrayList<>();
        for (MeasurementsTable data : measurementList) {
            if (data.getVisceralFat() != null) {
                dates.add(data.getCreateDateTime());
                listOfData.add(Double.parseDouble(data.getVisceralFat()));
            }
        }
        setData(dates, listOfData, Unit.VISCERAL_FAT);
    }

    private void setBodyWater() {
        List<String> dates = new ArrayList<>();
        List<Double> listOfData = new ArrayList<>();
        for (MeasurementsTable data : measurementList) {
            if (data.getBodyWater() != null) {
                dates.add(data.getCreateDateTime());
                listOfData.add(Double.parseDouble(data.getBodyWater()));
            }
        }
        setData(dates, listOfData, Unit.BODY_WATER);
    }

    private void setSkeletalMuscle() {
        List<String> dates = new ArrayList<>();
        List<Double> listOfData = new ArrayList<>();
        for (MeasurementsTable data : measurementList) {
            if (data.getSkeletalMuscle() != null) {
                dates.add(data.getCreateDateTime());
                listOfData.add(Double.parseDouble(data.getSkeletalMuscle()));
            }
        }
        setData(dates, listOfData, Unit.SKELETAL_MUSCLE);
    }

    private void setMuscleMass() {
        List<String> dates = new ArrayList<>();
        List<Double> listOfData = new ArrayList<>();
        for (MeasurementsTable data : measurementList) {
            if (data.getMuscleMass() != null) {
                dates.add(data.getCreateDateTime());
                listOfData.add(convertUnit(data.getMuscleMass()));
            }
        }
        setData(dates, listOfData, Unit.MUSCLE_MASS);
    }

    private void setBoneMass() {
        List<String> dates = new ArrayList<>();
        List<Double> listOfData = new ArrayList<>();
        for (MeasurementsTable data : measurementList) {
            if (data.getBoneMass() != null) {
                dates.add(data.getCreateDateTime());
                listOfData.add(convertUnit(data.getBoneMass()));
            }
        }
        setData(dates, listOfData, Unit.BONE_MASS);
    }

    private void setProtein() {
        List<String> dates = new ArrayList<>();
        List<Double> listOfData = new ArrayList<>();
        for (MeasurementsTable data : measurementList) {
            if (data.getProtein() != null) {
                dates.add(data.getCreateDateTime());
                listOfData.add(Double.parseDouble(data.getProtein()));
            }
        }
        setData(dates, listOfData, Unit.PROTEIN);
    }

    private void setBmr() {
        List<String> dates = new ArrayList<>();
        List<Double> listOfData = new ArrayList<>();
        for (MeasurementsTable data : measurementList) {
            if (data.getBmr() != null) {
                dates.add(data.getCreateDateTime());
                listOfData.add(Double.parseDouble(data.getBmr()));
            }
        }
        setData(dates, listOfData, Unit.BMR);
    }

    private void setMetabolicAge() {
        List<String> dates = new ArrayList<>();
        List<Double> listOfData = new ArrayList<>();
        for (MeasurementsTable data : measurementList) {
            if (data.getMetabolicAge() != null) {
                dates.add(data.getCreateDateTime());
                listOfData.add(Double.parseDouble(data.getMetabolicAge()));
            }
        }
        setData(dates, listOfData, Unit.METABOLIC_AGE);
    }

    // --------------------- Show Data On Graph ---------------------//
    private void setData(List<String> dates, List<Double> dataList, String unit) {

        // check the data available for showing or not
        if (dataList.size() <= 0) {
            chart.setData(null);
            return;
        }

        // create marker to display box when values are selected
        MyMarkerView mv = new MyMarkerView(requireContext(), dates, unit);
        // Set the marker to the chart
        mv.setChartView(chart);
        chart.setMarker(mv);

        // X-Axis Style //
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        xAxis.setGridColor(getResources().getColor(R.color.md_grey_350));
        xAxis.setAxisMaximum(dates.size());
        xAxis.setAxisMinimum(-1f);  // add 1 bar gap from first position
        //xAxis.setLabelCount(dates.size(), true);
        xAxis.setGranularityEnabled(true);
        xAxis.setGranularity(1f);   // set label after 1 bar interval
        xAxis.setValueFormatter(new XAxisValueFormatter(dates));
        //xAxis.setCenterAxisLabels(true);

        // Y-Axis Style //
        YAxis yAxis = chart.getAxisRight();
        // disable dual axis (only use RIGHT axis)
        chart.getAxisLeft().setEnabled(false);
        yAxis.setDrawGridLines(false);
        yAxis.setValueFormatter(new YAxisValueFormatter());

        ArrayList<Entry> values = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            values.add(new Entry(i, dataList.get(i).floatValue()));
        }

        LineDataSet set1;
        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();

            float visibleLabelCount = xAxis.getLabelCount() + 1;
            // now modify viewport (enable scrolling / dragging)
            chart.setVisibleXRangeMinimum(7); // allow 7 values to be displayed at once on the x-axis, not more
            chart.setVisibleXRangeMaximum(8);
            // If the chart contains more values, it will automatically allow scrolling.
            chart.moveViewToX(visibleLabelCount); // set the left edge of the chart to x-index 10
            // moveViewToX(...) also calls invalidate()
        } else {
            int color = ContextCompat.getColor(requireContext(), R.color.colorPrimary);

            // create a data set and give it a type
            set1 = new LineDataSet(values, null);
            set1.setDrawIcons(false);
            // black lines and points
            set1.setDrawCircles(true);
            set1.setColor(color);
            set1.setCircleColor(color);
            set1.setValueTextColor(color);
            set1.setHighLightColor(color);
            set1.setDrawHorizontalHighlightIndicator(false);
            set1.setHighlightLineWidth(1.0f);
            // line thickness and point size
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);
            // draw points as solid circles
            set1.setDrawCircleHole(true);
            // cubic corner
            set1.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
            set1.setCubicIntensity(0.2f);
            // text size of values
            set1.setValueTextSize(9f);
            // draw selection line as dashed
            set1.enableDashedHighlightLine(10f, 5f, 0f);
            // set the filled area
            set1.setDrawFilled(true);

            // set color of filled area
            Drawable drawable = ContextCompat.getDrawable(requireContext(), R.drawable.chart_fill_color);
            set1.setFillDrawable(drawable);
            set1.setDrawValues(true);
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1); // add the data sets
            // create a data object with the data sets
            LineData data = new LineData(dataSets);
            data.setValueFormatter(new DefaultValueFormatter(2));

            // set data
            chart.setData(data);

            float visibleLabelCount = xAxis.getLabelCount() + 1;
            // now modify viewport (enable scrolling / dragging)
            chart.setVisibleXRangeMinimum(7); // allow 7 values to be displayed at once on the x-axis, not more
            chart.setVisibleXRangeMaximum(8);
            // If the chart contains more values, it will automatically allow scrolling.
            chart.moveViewToX(visibleLabelCount); // set the left edge of the chart to x-index 10
            // moveViewToX(...) also calls invalidate()
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemClick(IndexItem item) {
        this.item = item;   // store selected item

        setupChart();       // re-init chart
        if (item != null) {
            switch (item.getName().toLowerCase()) {
                case Parameter.WEIGHT:
                    setWeight();
                    break;
                case Parameter.HEART_RATE:
                    setHeartRate();
                    break;
                case Parameter.CARDIAC_INDEX:
                    setCardiacIndex();
                    break;
                case Parameter.BMI:
                    setBmi();
                    break;
                case Parameter.BODY_FAT:
                    setBodyFat();
                    break;
                case Parameter.FAT_FREE_BODY_WEIGHT:
                    setFatFreeBodyWeight();
                    break;
                case Parameter.SUBCUTANEOUS_FAT:
                    setSubcutaneousFat();
                    break;
                case Parameter.VISCERAL_FAT:
                    setVisceralFat();
                    break;
                case Parameter.BODY_WATER:
                    setBodyWater();
                    break;
                case Parameter.SKELETAL_MUSCLE:
                    setSkeletalMuscle();
                    break;
                case Parameter.MUSCLE_MASS:
                    setMuscleMass();
                    break;
                case Parameter.BONE_MASS:
                    setBoneMass();
                    break;
                case Parameter.PROTEIN:
                    setProtein();
                    break;
                case Parameter.BMR:
                    setBmr();
                    break;
                case Parameter.METABOLIC_AGE:
                    setMetabolicAge();
                    break;
                default:
                    // set empty data
                    chart.setData(null);
                    break;
            }
        } else {
            setWeight();
        }
    }
}