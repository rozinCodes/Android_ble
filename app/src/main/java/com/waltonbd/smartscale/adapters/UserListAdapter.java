package com.waltonbd.smartscale.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.waltonbd.smartscale.R;
import com.waltonbd.smartscale.constant.ConstantValues;
import com.waltonbd.smartscale.databinding.UserRowBinding;
import com.waltonbd.smartscale.entities.UsersTable;
import com.waltonbd.smartscale.util.Utilities;
import com.waltonbd.smartscale.utils.TimeUtil;
import com.yolanda.health.qnblesdk.out.QNBleApi;

import java.util.List;


public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserViewHolder> {

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(UsersTable user);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    private final List<UsersTable> userList;
    private final Context context;
    private final QNBleApi mQNBleApi;

    public UserListAdapter(List<UsersTable> userList, Context context) {
        this.userList = userList;
        this.context = context;
        mQNBleApi = QNBleApi.getInstance(context);
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        UserRowBinding userRowBinding = UserRowBinding.inflate(layoutInflater, parent, false);
        return new UserViewHolder(userRowBinding);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        UsersTable user = userList.get(position);

        Utilities.loadImage(holder.binding.profileImage, user.getImage(), user.getGender());
        holder.binding.userName.setText(user.getFullName());
        holder.binding.lastMeasuredDate.setText(TimeUtil.updatedDateTimeToString(user.getUpdateDateTime(), "dd MMM, yyyy"));
        String weight = user.getWeight();

        if (!TextUtils.isEmpty(weight) && !weight.equals("null") && !weight.equals("0.0"))
            holder.binding.lastMeasuredWeight.setText(
                    mQNBleApi.convertWeightWithTargetUnit(Double.parseDouble(weight), ConstantValues.WEIGHT_UNIT));

        // highlight selected user (match primary id)
        if (ConstantValues.USER_ID == user.getId()) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorRipple));
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void removeItem(int position) {
        userList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(UsersTable item, int position) {
        userList.add(position, item);
        notifyItemInserted(position);
    }

    public List<UsersTable> getData() {
        return userList;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        private final UserRowBinding binding;

        private UserViewHolder(UserRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            itemView.setOnClickListener(v -> {
                if (mListener != null) {
                    int position = getAbsoluteAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mListener.onItemClick(userList.get(position));
                    }
                }
            });
        }
    }
}
