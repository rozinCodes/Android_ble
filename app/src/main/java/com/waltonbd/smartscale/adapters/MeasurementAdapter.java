package com.waltonbd.smartscale.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.neo.expandedrecylerview.adapters.BaseExpandedGridViewHolder;
import com.neo.expandedrecylerview.adapters.ExpandedGridAdapter;
import com.neo.expandedrecylerview.model.IExpandData;
import com.waltonbd.smartscale.bean.IndicateBean;
import com.waltonbd.smartscale.constant.Parameter;
import com.waltonbd.smartscale.databinding.BarBmiBinding;
import com.waltonbd.smartscale.databinding.BarBodyFatBinding;
import com.waltonbd.smartscale.databinding.BarHeartRateBinding;
import com.waltonbd.smartscale.databinding.BarNoneBinding;
import com.waltonbd.smartscale.databinding.BarWeightBinding;
import com.waltonbd.smartscale.databinding.MeasureRowBinding;
import com.waltonbd.smartscale.databinding.MeasureRowExpBinding;
import com.waltonbd.smartscale.models.MeasuredItem;
import com.yolanda.health.qnblesdk.constant.QNIndicator;

import java.util.List;


public class MeasurementAdapter extends ExpandedGridAdapter {

    private int columnNumber;

    public void setColumnNumber(int columnNumber) {
        this.columnNumber = columnNumber;
    }

    @Override
    public void onParentClicked(int position) {
        //Log.i("Clicked On", "" + position);
    }

    @Override
    public int getColumnCount() {
        return columnNumber;
    }

    private List<IExpandData> parentItemModels;

    @Override
    public List<IExpandData> getData() {
        return parentItemModels;
    }

    public void setData(List<IExpandData> parentItemModels) {
        this.parentItemModels = parentItemModels;
    }

    @Override
    public ChildView getChildView(ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        MeasureRowExpBinding binding = MeasureRowExpBinding.inflate(layoutInflater, parent, false);
        return new ChildView(binding);
    }

    @Override
    public BaseExpandedGridViewHolder getParentView(ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        MeasureRowBinding measureRowBinding = MeasureRowBinding.inflate(layoutInflater, parent, false);
        return new Parent(measureRowBinding);
    }

    @Override
    public void setParentViewData(BaseExpandedGridViewHolder parentViewHolder, int position) {
        ((Parent) parentViewHolder).setData(iExpandDatas.get(position));
    }


    @Override
    public void setChildViewData(BaseExpandedGridViewHolder childViewHolder, int position) {
        //((ChildView) childViewHolder).setData(iExpandDatas.get(position));
    }


    static class Parent extends BaseExpandedGridViewHolder {

        MeasureRowBinding measureRowBinding;

        public Parent(MeasureRowBinding binding) {
            super(binding.getRoot());
            this.measureRowBinding = binding;
        }

        @Override
        public void initWidgets(View view) {
        }

        public void setData(IExpandData data) {
            MeasuredItem measuredItem = ((MeasuredItem) data);
            measureRowBinding.setMeasuredData(measuredItem);
            measureRowBinding.executePendingBindings();
        }
    }

    static class ChildView extends BaseExpandedGridViewHolder {

        private final MeasureRowExpBinding binding;
        private LayoutInflater layoutInflater;
        private RelativeLayout layout;

        public ChildView(MeasureRowExpBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            layout = binding.dynamicContent;
        }

        @Override
        public void initWidgets(View view) {
            layoutInflater = LayoutInflater.from(view.getContext());
        }

        public void setData(IExpandData data) {
            String label = ((MeasuredItem) data).getLabel();
            View childView = BarNoneBinding.inflate(layoutInflater, layout, false).getRoot();

            switch (label.toLowerCase()) {
                case Parameter.WEIGHT:
                    childView = BarWeightBinding.inflate(layoutInflater, layout, false).getRoot();
                    break;
                case Parameter.HEART_RATE:
                    childView = BarHeartRateBinding.inflate(layoutInflater, layout, false).getRoot();
                    break;
                case Parameter.BMI:
                    childView = BarBmiBinding.inflate(layoutInflater, layout, false).getRoot();
                    break;
                case Parameter.BODY_FAT:
                    childView = BarBodyFatBinding.inflate(layoutInflater, layout, false).getRoot();
                    break;
            }
            layout.removeAllViews();
            layout.addView(childView);
        }
    }
}
