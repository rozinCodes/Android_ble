package com.waltonbd.smartscale.view;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Utils;
import com.waltonbd.smartscale.R;
import com.waltonbd.smartscale.adapters.IndexRlyAdapter;
import com.waltonbd.smartscale.custom.MyMarkerView;
import com.waltonbd.smartscale.databinding.FragmentWeightBinding;
import com.waltonbd.smartscale.models.IndexItem;
import com.waltonbd.smartscale.viewmodels.SmartScaleViewModel;

import java.util.ArrayList;
import java.util.List;


public class WeightFragment extends Fragment implements IndexRlyAdapter.OnItemClickListener, OnChartValueSelectedListener {

    private static final String TAG = "WeightFragment";

    private FragmentWeightBinding binding;
    private IndexRlyAdapter indexRlyAdapter;
    private SmartScaleViewModel smartScaleViewModel;

    private LineChart chart;

    public static WeightFragment newInstance() {
        return new WeightFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentWeightBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // default checked 1st position
        binding.daysFilter.setCheckedPosition(0);

        indexRlyAdapter = new IndexRlyAdapter(this, requireContext());
        binding.indexRlv.setAdapter(indexRlyAdapter);

        smartScaleViewModel = new ViewModelProvider(requireActivity()).get(SmartScaleViewModel.class);
        smartScaleViewModel.getIndexItems().observe(getViewLifecycleOwner(), new Observer<List<IndexItem>>() {
            @Override
            public void onChanged(List<IndexItem> indexItems) {
                indexRlyAdapter.submitList(indexItems);
            }
        });

        chart = view.findViewById(R.id.chart);
        setupChart();

        binding.daysFilter.setOnChangeListener(position -> {
            Log.e(TAG, "setOnChangeListener: " + position);
        });
    }

    private void setupChart() {

        // // Chart Style // //

        // background color
        chart.setBackgroundColor(Color.WHITE);

        // disable description text
        chart.getDescription().setEnabled(false);

        // enable touch gestures
        chart.setTouchEnabled(true);

        // enable scaling and dragging
        chart.setDragEnabled(false);
        chart.setScaleEnabled(false);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(false);

        // set listeners
        chart.setOnChartValueSelectedListener(this);
        chart.setDrawGridBackground(false);

        // create marker to display box when values are selected
        /*MyMarkerView mv = new MyMarkerView(requireContext(), R.layout.marker_view_top);

        // Set the marker to the chart
        mv.setChartView(chart);
        chart.setMarker(mv);*/

        // X-Axis Style //
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //xAxis.setDrawGridLines(false);
        // vertical grid lines
        xAxis.enableGridDashedLine(10f, 10f, 0f);

        // Y-Axis Style //
        YAxis yAxis = chart.getAxisLeft();
        // disable dual axis (only use RIGHT axis)
        chart.getAxisLeft().setEnabled(false);
        chart.getAxisRight().setDrawGridLines(false);
        // horizontal grid lines
        yAxis.enableGridDashedLine(10f, 10f, 0f);

        // disable legend
        chart.getLegend().setEnabled(false);

        // add data
        setData(10, 90);

        // draw points over time
        //chart.animateX(1000);
    }

    private void setData(int count, float range) {

        ArrayList<Entry> values = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            float val = (float) (Math.random() * range);
            values.add(new Entry(i, val));
        }

        LineDataSet set1;

        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            set1.notifyDataSetChanged();
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, null);

            set1.setDrawIcons(false);

            // black lines and points
            set1.setColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary));
            set1.setCircleColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary));
            set1.setValueTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary));

            // line thickness and point size
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);

            // draw points as solid circles
            set1.setDrawCircleHole(true);

            // cubic corner
            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);

            // text size of values
            set1.setValueTextSize(9f);

            // draw selection line as dashed
            set1.enableDashedHighlightLine(10f, 5f, 0f);

            // set the filled area
            set1.setDrawFilled(true);
            set1.setFillFormatter(new IFillFormatter() {
                @Override
                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                    return chart.getAxisLeft().getAxisMinimum();
                }
            });

            // set color of filled area
            if (Utils.getSDKInt() >= 18) {
                // drawables only supported on api level 18 and above
                Drawable drawable = ContextCompat.getDrawable(requireContext(), R.drawable.chart_fill_color);
                set1.setFillDrawable(drawable);
            } else {
                set1.setFillColor(Color.BLACK);
            }

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1); // add the data sets

            // create a data object with the data sets
            LineData data = new LineData(dataSets);

            // set data
            chart.setData(data);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemClick(IndexItem IndexItem) {
        Log.e(TAG, "onItemClick: " + IndexItem.getName());
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.i("Entry selected", e.toString());
        Log.i("LOW HIGH", "low: " + chart.getLowestVisibleX() + ", high: " + chart.getHighestVisibleX());
        Log.i("MIN MAX", "xMin: " + chart.getXChartMin() + ", xMax: " + chart.getXChartMax() + ", yMin: " + chart.getYChartMin() + ", yMax: " + chart.getYChartMax());
    }

    @Override
    public void onNothingSelected() {
        Log.i("Nothing selected", "Nothing selected.");
    }
}