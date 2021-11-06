package com.waltonbd.smartscale.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.waltonbd.smartscale.databinding.MeasureHistoryItemRowBinding;
import com.waltonbd.smartscale.models.MeasuredItem;

import org.jetbrains.annotations.NotNull;

public class MeasurementHistoryAdapter extends ListAdapter<MeasuredItem, MeasurementHistoryAdapter.ViewHolder> {

    public MeasurementHistoryAdapter() {
        super(MeasuredItem.itemCallback);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        MeasureHistoryItemRowBinding binding = MeasureHistoryItemRowBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NotNull ViewHolder holder, int position) {
        MeasuredItem data = getItem(position);
        holder.binding.setData(data);
        holder.binding.executePendingBindings();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final MeasureHistoryItemRowBinding binding;

        private ViewHolder(MeasureHistoryItemRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
