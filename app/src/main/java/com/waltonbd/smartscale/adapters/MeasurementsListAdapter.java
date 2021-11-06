package com.waltonbd.smartscale.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.waltonbd.smartscale.databinding.MeasureRowBinding;
import com.waltonbd.smartscale.models.MeasuredItem;


public class MeasurementsListAdapter extends ListAdapter<MeasuredItem, MeasurementsListAdapter.MeasurementsViewHolder> {

    public MeasurementsListAdapter() {
        super(MeasuredItem.itemCallback);
    }

    @NonNull
    @Override
    public MeasurementsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        MeasureRowBinding measureRowBinding = MeasureRowBinding.inflate(layoutInflater, parent, false);
        return new MeasurementsViewHolder(measureRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MeasurementsViewHolder holder, int position) {
        MeasuredItem measuredItem = getItem(position);
        holder.measureRowBinding.setMeasuredData(measuredItem);
        holder.measureRowBinding.executePendingBindings();
    }

    class MeasurementsViewHolder extends RecyclerView.ViewHolder {

        MeasureRowBinding measureRowBinding;

        public MeasurementsViewHolder(@NonNull MeasureRowBinding binding) {
            super(binding.getRoot());
            this.measureRowBinding = binding;
        }
    }
}
