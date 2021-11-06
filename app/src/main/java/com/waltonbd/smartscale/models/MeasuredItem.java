package com.waltonbd.smartscale.models;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.neo.expandedrecylerview.model.IExpandData;
import com.waltonbd.smartscale.R;

import org.jetbrains.annotations.NotNull;

public class MeasuredItem implements IExpandData {

    private String label;
    private String value;
    private String subValue;
    private int valueColor = R.color.md_indigo_A200;


    public MeasuredItem(String label, String value, String subValue) {
        this.label = label;
        this.value = value;
        this.subValue = subValue;
    }

    public MeasuredItem(String label, String value, String subValue, int valueColor) {
        this.label = label;
        this.value = value;
        this.subValue = subValue;
        this.valueColor = valueColor;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSubValue() {
        return subValue;
    }

    public void setSubValue(String subValue) {
        this.subValue = subValue;
    }

    public int getValueColor() {
        return valueColor;
    }

    public void setValueColor(int valueColor) {
        this.valueColor = valueColor;
    }

    @NotNull
    @Override
    public String toString() {
        return "MeasuredItem{" +
                ", label='" + label + '\'' +
                ", value='" + value + '\'' +
                ", subValue='" + subValue + '\'' +
                ", valueColor=" + valueColor +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MeasuredItem that = (MeasuredItem) o;
        return getValueColor() == that.getValueColor() &&
                getLabel().equals(that.getLabel()) &&
                getValue().equals(that.getValue());
    }

    public static boolean equalsWithNulls(Object a, Object b) {
        if (a == b) return true;
        if ((a == null) || (b == null)) return false;
        return a.equals(b);
    }

    /*@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MeasuredItem that = (MeasuredItem) o;
        return getValueColor() == that.getValueColor() &&
                equalsWithNulls(getLabel(), that.getLabel()) &&
                equalsWithNulls(getValue(), that.getValue()) &&
                equalsWithNulls(getSubValue(), that.getSubValue());
    }*/

    public static DiffUtil.ItemCallback<MeasuredItem> itemCallback = new DiffUtil.ItemCallback<MeasuredItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull MeasuredItem oldItem, @NonNull MeasuredItem newItem) {
            return oldItem.getLabel().equals(newItem.getLabel());
        }

        @Override
        public boolean areContentsTheSame(@NonNull MeasuredItem oldItem, @NonNull MeasuredItem newItem) {
            return oldItem.equals(newItem);
        }
    };
}
