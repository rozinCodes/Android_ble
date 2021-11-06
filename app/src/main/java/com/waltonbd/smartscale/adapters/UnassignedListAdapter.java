package com.waltonbd.smartscale.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.waltonbd.smartscale.databinding.UnassignedListItemRowBinding;
import com.waltonbd.smartscale.models.unassigned_model.UnassignedData;
import com.waltonbd.smartscale.models.unassigned_model.UnassignedDetails;
import com.waltonbd.smartscale.utils.NumberUtil;
import com.waltonbd.smartscale.utils.TimeUtil;

import java.util.List;


public class UnassignedListAdapter extends RecyclerView.Adapter<UnassignedListAdapter.ViewHolder> {

    private OnItemSelectListener mListener;

    public interface OnItemSelectListener {
        void onItemSelect();
    }

    public void setOnItemSelectListener(OnItemSelectListener listener) {
        mListener = listener;
    }

    private final List<UnassignedDetails> itemsList;
    private Context context;

    public UnassignedListAdapter(List<UnassignedDetails> itemsList, Context context) {
        this.itemsList = itemsList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        UnassignedListItemRowBinding binding = UnassignedListItemRowBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UnassignedDetails data = itemsList.get(position);

        holder.binding.weightTv.setText(NumberUtil.toStringAsFixed(data.getWeight()));
        holder.binding.dateTv.setText(TimeUtil.getFormattedTimestamp(data.getUpdateDateTime()));

        holder.binding.checkBox.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            data.setChecked(isChecked);

            if (mListener != null) {
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemSelect();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public List<UnassignedDetails> getItems() {
        return itemsList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final UnassignedListItemRowBinding binding;

        private ViewHolder(UnassignedListItemRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            /*itemView.setOnClickListener(v -> {
                if (mListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mListener.onItemClick(itemsList, position);
                    }
                }
            });*/
        }
    }
}
