package com.waltonbd.smartscale.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.waltonbd.smartscale.databinding.UnassignedListRowBinding;
import com.waltonbd.smartscale.models.unassigned_model.UnassignedData;
import com.waltonbd.smartscale.models.unassigned_model.UnassignedDetails;

import java.util.ArrayList;
import java.util.List;


public class UnassignedListDataAdapter extends RecyclerView.Adapter<UnassignedListDataAdapter.ViewHolder> {

    private OnItemSelectListener mListener;

    public interface OnItemSelectListener {
        void onItemSelect(List<UnassignedDetails> itemsList, boolean isChecked);
    }

    public void setOnItemSelectListener(OnItemSelectListener listener) {
        mListener = listener;
    }

    private List<UnassignedData> dataList;
    private Context context;

    public UnassignedListDataAdapter(List<UnassignedData> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        UnassignedListRowBinding binding = UnassignedListRowBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UnassignedData data = dataList.get(position);

        holder.binding.dateTv.setText(data.getDate());

        UnassignedListAdapter itemListDataAdapter = new UnassignedListAdapter(data.getDetails(), context);
        holder.binding.unassignedListRv.setAdapter(itemListDataAdapter);
        holder.binding.unassignedListRv.setNestedScrollingEnabled(false);
        itemListDataAdapter.setOnItemSelectListener(() -> {
            if (mListener != null) {
                mListener.onItemSelect(getItems(), isAnyItemChecked());
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public List<UnassignedDetails> getItems() {
        List<UnassignedDetails> itemsList = new ArrayList<>();
        for (UnassignedData data : dataList) {
            itemsList.addAll(data.getDetails());
        }
        return itemsList;
    }

    public List<Integer> getCheckedItemsId() {
        List<Integer> list = new ArrayList<>();
        for (UnassignedDetails data : getItems()) {
            if (data.isChecked()) list.add(data.getId());
        }
        return list;
    }

    public boolean isAnyItemChecked() {
        boolean isChecked = false;
        for (UnassignedData data : dataList) {
            for (UnassignedDetails item : data.getDetails()) {
                isChecked |= item.isChecked();
            }
        }
        return isChecked;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final UnassignedListRowBinding binding;

        private ViewHolder(UnassignedListRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
