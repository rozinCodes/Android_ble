package com.waltonbd.smartscale.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.waltonbd.smartscale.R;
import com.waltonbd.smartscale.models.MeasuredItem;

import java.util.ArrayList;
import java.util.List;

public class MeasurementRepo {

    private static final String TAG = "MeasurementRepo";

    private MutableLiveData<List<MeasuredItem>> mutableMeasuredList = new MutableLiveData<>();
    private MutableLiveData<String> mutableLiveWeight = new MutableLiveData<>();

    public LiveData<List<MeasuredItem>> getMeasuredItems() {
        if (mutableMeasuredList.getValue() == null) {
            loadEmptyData();
        }
        return mutableMeasuredList;
    }

    public void setMeasuredItems(List<MeasuredItem> measuredList) {
        if (mutableMeasuredList.getValue() == null) {
            init();
        }
        mutableMeasuredList.setValue(measuredList);
    }

    public void init() {
        mutableMeasuredList.setValue(new ArrayList<MeasuredItem>());
    }

    private void loadEmptyData() {
        List<MeasuredItem> measuredItems = new ArrayList<>();
        measuredItems.add(new MeasuredItem("Weight", "0.0", "kg", R.color.md_indigo_A300));
        measuredItems.add(new MeasuredItem("Heart Rate", "0", "bpm", R.color.md_indigo_A300));
        measuredItems.add(new MeasuredItem("Cardiac index", "0.0", "L/Min/M", R.color.md_indigo_A300));
        measuredItems.add(new MeasuredItem("BMI", "0.0", "", R.color.md_indigo_A300));
        measuredItems.add(new MeasuredItem("Balance", "0.0", "kg", R.color.md_indigo_A300));
        measuredItems.add(new MeasuredItem("Body Fat", "0.0", "%", R.color.md_indigo_A300));
        measuredItems.add(new MeasuredItem("Body Type", "-", "", R.color.md_indigo_A300));
        measuredItems.add(new MeasuredItem("Fat-free Body Weight", "0.0", "kg", R.color.md_indigo_A300));
        measuredItems.add(new MeasuredItem("Subcutaneous Fat", "0.0", "%", R.color.md_indigo_A300));
        measuredItems.add(new MeasuredItem("Visceral Fat", "0", "", R.color.md_indigo_A300));
        measuredItems.add(new MeasuredItem("Skeletal Muscle", "0.0", "%", R.color.md_indigo_A300));
        mutableMeasuredList.setValue(measuredItems);
    }

    public LiveData<String> getLiveWeight() {
        if (mutableLiveWeight.getValue() == null) {
            mutableLiveWeight.setValue("0.0");
        }
        return mutableLiveWeight;
    }

    public void setLiveWeight(String weight) {
        mutableLiveWeight.setValue(weight);
    }
}
