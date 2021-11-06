package com.waltonbd.smartscale.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.waltonbd.smartscale.R;
import com.yolanda.health.qnblesdk.out.QNBleDevice;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder> {

    private final List<QNBleDevice> mDevicesList;
    private OnItemClickListener mListener;

    public DeviceAdapter(List<QNBleDevice> devicesList) {
        mDevicesList = devicesList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NotNull
    @Override
    public DeviceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.device_row, parent, false);
        return new DeviceViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceViewHolder holder, int position) {
        QNBleDevice device = mDevicesList.get(position);

        String deviceName;
        switch (device.getModeId()) {
            case "0431":
                deviceName = "Walton SA1B";
                break;
            case "0432":
                deviceName = "Walton SA1W";
                break;
            case "042F":
                deviceName = "Walton SA2B";
                break;
            case "0430":
                deviceName = "Walton SA2W";
                break;
            default:
                deviceName = "Unknown Device";
                break;
        }
        holder.deviceName.setText(deviceName);
    }

    @Override
    public int getItemCount() {
        return mDevicesList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(QNBleDevice device, int position);
    }

    public class DeviceViewHolder extends RecyclerView.ViewHolder {
        private final TextView deviceName;

        private DeviceViewHolder(final View itemView, final OnItemClickListener listener) {
            super(itemView);
            deviceName = itemView.findViewById(R.id.deviceName);
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAbsoluteAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(mDevicesList.get(position), position);
                    }
                }
            });
        }
    }
}
