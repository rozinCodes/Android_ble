package com.waltonbd.smartscale.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.neo.expandedrecylerview.model.IExpandData;
import com.waltonbd.smartscale.constant.Unit;
import com.waltonbd.smartscale.custom.DividerItemDecoration;
import com.waltonbd.smartscale.databinding.MeasureHistoryRowBinding;
import com.waltonbd.smartscale.databinding.UnassignedListRowBinding;
import com.waltonbd.smartscale.entities.MeasurementsTable;
import com.waltonbd.smartscale.models.IndexItem;
import com.waltonbd.smartscale.models.MeasuredItem;
import com.waltonbd.smartscale.models.unassigned_model.UnassignedData;
import com.waltonbd.smartscale.models.unassigned_model.UnassignedDetails;
import com.waltonbd.smartscale.util.DateUtils;
import com.waltonbd.smartscale.util.Utilities;
import com.waltonbd.smartscale.utils.TimeUtil;
import com.yolanda.health.qnblesdk.out.QNBleApi;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class MeasurementHistoryDataAdapter extends ListAdapter<MeasurementsTable, MeasurementHistoryDataAdapter.ViewHolder> {

    private static final DiffUtil.ItemCallback<MeasurementsTable> itemCallback = new DiffUtil.ItemCallback<MeasurementsTable>() {
        @Override
        public boolean areItemsTheSame(@NonNull MeasurementsTable oldItem, @NonNull MeasurementsTable newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull MeasurementsTable oldItem, @NonNull MeasurementsTable newItem) {
            return oldItem.getUpdateDateTime().equals(newItem.getUpdateDateTime());
        }
    };

    private final Context context;
    private final QNBleApi mQNBleApi;
    private final OnItemDeleteListener listener;

    public interface OnItemDeleteListener {
        void onItemDelete(MeasurementsTable item);
    }

    public MeasurementHistoryDataAdapter(OnItemDeleteListener listener, Context context) {
        super(itemCallback);
        this.context = context;
        this.listener = listener;
        mQNBleApi = QNBleApi.getInstance(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        MeasureHistoryRowBinding binding = MeasureHistoryRowBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MeasurementsTable item = getItem(position);

        holder.binding.dateTv.setText(TimeUtil.getFormattedTime(item.getUpdateDateTime()));

        MeasurementHistoryAdapter adapter = new MeasurementHistoryAdapter();
        holder.binding.unassignedListRv.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL_LIST));
        holder.binding.unassignedListRv.setAdapter(adapter);

        // SubValue for target unit
        String subValueUnit = Utilities.getSubValueWithTargetUnit();

        // add list item
        List<MeasuredItem> measuredData = new ArrayList<>();
        measuredData.add(new MeasuredItem("Weight", Utilities.getValueWithTargetUnit(mQNBleApi, item.getWeight()), subValueUnit));
        measuredData.add(new MeasuredItem("Heart Rate", item.getHeartRate(), Unit.HEART_RATE));
        //measuredData.add(new MeasuredItem("Cardiac index", measuredItems.get(0).getCardiacIndex(), "L/Min/M²"));
        measuredData.add(new MeasuredItem("BMI", item.getBmi(), Unit.BMI));
        measuredData.add(new MeasuredItem("Body Fat", item.getBodyFat(), Unit.BODY_FAT));
        measuredData.add(new MeasuredItem("Fat-free Body Weight", Utilities.getValueWithTargetUnit(mQNBleApi, item.getFatFreeBodyWeight()), subValueUnit));
        measuredData.add(new MeasuredItem("Subcutaneous Fat", item.getSubCutaneousFat(), Unit.SUBCUTANEOUS_FAT));
        measuredData.add(new MeasuredItem("Visceral Fat", item.getVisceralFat(), Unit.VISCERAL_FAT));
        measuredData.add(new MeasuredItem("Skeletal Muscle", item.getSkeletalMuscle(), Unit.SKELETAL_MUSCLE));
        measuredData.add(new MeasuredItem("Muscle Mass", Utilities.getValueWithTargetUnit(mQNBleApi, item.getMuscleMass()), subValueUnit));
        measuredData.add(new MeasuredItem("Body Water", item.getBodyWater(), Unit.BODY_WATER));
        measuredData.add(new MeasuredItem("Bone Mass", Utilities.getValueWithTargetUnit(mQNBleApi, item.getBoneMass()), subValueUnit));
        measuredData.add(new MeasuredItem("Protein", item.getProtein(), Unit.PROTEIN));
        measuredData.add(new MeasuredItem("BMR", item.getBmr(), Unit.BMR));
        measuredData.add(new MeasuredItem("Metabolic Age", item.getMetabolicAge(), Unit.METABOLIC_AGE));
        adapter.submitList(measuredData);

        holder.binding.delete.setOnClickListener(view -> {
            if (listener != null) listener.onItemDelete(item);
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final MeasureHistoryRowBinding binding;

        private ViewHolder(MeasureHistoryRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
