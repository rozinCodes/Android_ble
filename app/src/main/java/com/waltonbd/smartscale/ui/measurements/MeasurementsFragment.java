package com.waltonbd.smartscale.ui.measurements;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;

import com.jaeger.library.StatusBarUtil;
import com.neo.expandedrecylerview.model.IExpandData;
import com.waltonbd.smartscale.R;
import com.waltonbd.smartscale.adapters.MeasurementAdapter;
import com.waltonbd.smartscale.constant.ConstantValues;
import com.waltonbd.smartscale.constant.Unit;
import com.waltonbd.smartscale.custom.PopUpClass;
import com.waltonbd.smartscale.databinding.FragmentMeasurementsBinding;
import com.waltonbd.smartscale.databinding.LayoutMeasureDialogBinding;
import com.waltonbd.smartscale.databinding.LayoutMeasureHeaderBinding;
import com.waltonbd.smartscale.databinding.LayoutScaleMeasureBinding;
import com.waltonbd.smartscale.entities.MeasurementsTable;
import com.waltonbd.smartscale.entities.UsersTable;
import com.waltonbd.smartscale.models.MeasuredItem;
import com.waltonbd.smartscale.session.SharedPreferencesManager;
import com.waltonbd.smartscale.util.Utilities;
import com.waltonbd.smartscale.utils.TimeUtil;
import com.waltonbd.smartscale.view.BaseActivity;
import com.waltonbd.smartscale.view.SetGoalActivity;
import com.waltonbd.smartscale.viewmodels.SmartScaleViewModel;
import com.waltonbd.smartscale.viewmodels.UserViewModel;
import com.yolanda.health.qnblesdk.constant.QNUnit;
import com.yolanda.health.qnblesdk.out.QNBleApi;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MeasurementsFragment extends Fragment {

    private static final String TAG = "MeasurementsFragment";
    protected SmartScaleViewModel smartScaleViewModel;
    protected UserViewModel userViewModel;

    private FragmentMeasurementsBinding binding;
    private LayoutMeasureHeaderBinding headerBinding;
    private MeasurementAdapter measurementAdapter;

    private AlertDialog alertDialog;
    //private AlertDialog alertDialog;
    private QNBleApi mQNBleApi;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Change status bar color
        StatusBarUtil.setColor(requireActivity(), ContextCompat.getColor(requireContext(), R.color.colorPrimary), 0);
        StatusBarUtil.setDarkMode(requireActivity());

        // Inflate the layout for this fragment
        binding = FragmentMeasurementsBinding.inflate(inflater, container, false);
        headerBinding = binding.layoutMeasureHeader;
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Init shared pref
        SharedPreferencesManager.init(requireContext());
        mQNBleApi = QNBleApi.getInstance(requireContext());

        smartScaleViewModel = new ViewModelProvider(requireActivity()).get(SmartScaleViewModel.class);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        measurementAdapter = new MeasurementAdapter();
        measurementAdapter.setColumnNumber(3);
        binding.measuredDataRecyclerView.setNestedScrollingEnabled(false);  // disable scrolling
        binding.measuredDataRecyclerView.setAdapter(measurementAdapter);

        // Observe last measured data from room database
        smartScaleViewModel.getLastMeasurement().observe(getViewLifecycleOwner(), measuredItems -> {

            // show/hide text
            binding.emptyDataTv.setVisibility(measuredItems.size() > 0 ? View.GONE : View.VISIBLE);

            // Update with last measured data
            if (measuredItems.size() > 0) {

                double weight = Double.parseDouble(measuredItems.get(0).getWeight());
                String weightStr = mQNBleApi.convertWeightWithTargetUnit(weight, ConstantValues.WEIGHT_UNIT);
                String updateDateTime = measuredItems.get(0).getUpdateDateTime();

                // update user info table
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.execute(() -> {
                    UsersTable userInfo = userViewModel.getUser(ConstantValues.USER_ID);
                    userInfo.setWeight(String.valueOf(weight));   // update with default value (kg)
                    userInfo.setUpdateDateTime(updateDateTime);
                    userViewModel.updateUser(userInfo);
                });

                String value = Utilities.getValueWithTargetUnit(mQNBleApi, weight);
                String subValueUnit = Utilities.getSubValueWithTargetUnit();

                // update measurement header view
                headerBinding.tvLiveWeight.setNormalText(value);
                headerBinding.tvLiveWeight.setSubScriptText(subValueUnit);
                headerBinding.weightProgress.setValue((int) weight);

                // update last measured date
                String formattedTimestamp = TimeUtil.getFormattedTimestamp(updateDateTime);
                headerBinding.lastMeasureTimeTv.setText(String.format("From %s", formattedTimestamp));

                // update list item
                List<IExpandData> measuredData = new ArrayList<>();
                measuredData.add(new MeasuredItem("Weight", value, subValueUnit));
                measuredData.add(new MeasuredItem("Heart Rate", measuredItems.get(0).getHeartRate(), Unit.HEART_RATE));
                measuredData.add(new MeasuredItem("BMI", measuredItems.get(0).getBmi(), Unit.BMI));
                measuredData.add(new MeasuredItem("Body Fat", measuredItems.get(0).getBodyFat(), Unit.BODY_FAT));
                measuredData.add(new MeasuredItem("Fat-free Body Weight", Utilities.getValueWithTargetUnit(mQNBleApi, measuredItems.get(0).getFatFreeBodyWeight()), subValueUnit));
                measuredData.add(new MeasuredItem("Subcutaneous Fat", measuredItems.get(0).getSubCutaneousFat(), Unit.SUBCUTANEOUS_FAT));
                measuredData.add(new MeasuredItem("Visceral Fat", measuredItems.get(0).getVisceralFat(), Unit.VISCERAL_FAT));
                measuredData.add(new MeasuredItem("Skeletal Muscle", measuredItems.get(0).getSkeletalMuscle(), Unit.SKELETAL_MUSCLE));
                measuredData.add(new MeasuredItem("Muscle Mass", Utilities.getValueWithTargetUnit(mQNBleApi, measuredItems.get(0).getMuscleMass()), subValueUnit));
                measuredData.add(new MeasuredItem("Body Water", measuredItems.get(0).getBodyWater(), Unit.BODY_WATER));
                measuredData.add(new MeasuredItem("Bone Mass", Utilities.getValueWithTargetUnit(mQNBleApi, measuredItems.get(0).getBoneMass()), subValueUnit));
                measuredData.add(new MeasuredItem("Protein", measuredItems.get(0).getProtein(), Unit.PROTEIN));
                measuredData.add(new MeasuredItem("BMR", measuredItems.get(0).getBmr(), Unit.BMR));
                measuredData.add(new MeasuredItem("Metabolic Age", measuredItems.get(0).getMetabolicAge(), Unit.METABOLIC_AGE));
                //measurementAdapter.submitList(measuredData);
                measurementAdapter.setData(measuredData);
                measurementAdapter.notifyDataSetChanged();
            }
        });

        smartScaleViewModel.getLiveWeight().observe(getViewLifecycleOwner(), weight -> {
            Log.d(TAG, "getLiveWeight");

            if (getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {

                // Show Measuring Animation with live weight
                if (alertDialog == null) alertDialog = createMeasuringDialog(requireContext());
                if (!alertDialog.isShowing()) alertDialog.show();
                // update text with live weight data
                TextView weightTv = alertDialog.findViewById(R.id.weight_tv);
                if (weightTv != null) weightTv.setText(weight);
            }
        });

        smartScaleViewModel.getState().observe(getViewLifecycleOwner(), state -> {
            if (alertDialog != null && alertDialog.isShowing()) {
                // update text with live state
                TextView stateTv = alertDialog.findViewById(R.id.status_tv);
                if (stateTv != null) stateTv.setText(state);
            }
        });

        smartScaleViewModel.getMeasurementData().observe(getViewLifecycleOwner(), mData -> {
            Log.i(TAG, "getMeasurementData");
            if (getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {
                // Close the dialog if is showing
                if (alertDialog != null) {
                    alertDialog.dismiss();
                    alertDialog = null;
                }

                if (mData != null) {
                    // Show a dialog if weight difference is 5kg or greater
                    float NOW_MEASURED_WEIGHT = Float.parseFloat(mData.getWeight());
                    if (ConstantValues.LAST_MEASURED_WEIGHT != 0f && (ConstantValues.LAST_MEASURED_WEIGHT + ConstantValues.WEIGHT_DIFFERENCE <= NOW_MEASURED_WEIGHT ||
                            ConstantValues.LAST_MEASURED_WEIGHT - ConstantValues.WEIGHT_DIFFERENCE >= NOW_MEASURED_WEIGHT)) {
                        showDialogDiff(mData);
                    } else {
                        insertAndSendToServer(mData);
                    }
                }
            }
        });

        binding.addDevice.setOnClickListener(view1 -> {
            PopUpClass popUpClass = new PopUpClass();
            popUpClass.showPopupWindow(view1);
        });

        headerBinding.unknownBtn.setOnClickListener(view12 -> {
            BaseActivity.startActivity(requireContext(), getString(R.string.title_unassigned));
        });

        headerBinding.btnGoal.setOnClickListener(view1 -> {
            startActivity(new Intent(requireContext(), SetGoalActivity.class));
        });

        headerBinding.btnBodyFatGoal.setOnClickListener(view1 -> {
            startActivity(new Intent(requireContext(), SetGoalActivity.class));
        });
    }

    private void getUserInfoFromRoom() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            UsersTable user = userViewModel.getUser(ConstantValues.USER_ID);
            handler.post(() -> {
                binding.userName.setText(user.getFullName());
            });
        });
    }

    public AlertDialog createMeasuringDialog(Context context) {
        Log.d(TAG, "showMeasurementDialog");

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.FullScreenDialogTheme);
        LayoutScaleMeasureBinding binding = LayoutScaleMeasureBinding.inflate(LayoutInflater.from(context));
        builder.setCancelable(false);
        builder.setView(binding.getRoot());

        final Animation loadAnimation = AnimationUtils.loadAnimation(context, R.anim.anim_measure_sun_rotate);
        loadAnimation.setFillAfter(true);
        final Animation loadAnimation2 = AnimationUtils.loadAnimation(context, R.anim.anim_measure_inner_circle_rotate);
        final Animation loadAnimation3 = AnimationUtils.loadAnimation(context, R.anim.anim_measure_out_circle_rotate);
        final Animation loadAnimation4 = AnimationUtils.loadAnimation(context, R.anim.anim_measure_alpha_delay);
        binding.measureRl.setAnimation(loadAnimation4);
        binding.heartbeat.start();

        binding.closeBtn.setOnClickListener(view -> {
            alertDialog.dismiss();
            alertDialog = null;
        });

        loadAnimation4.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.secondIv.startAnimation(loadAnimation);
                binding.thirdIv.startAnimation(loadAnimation2);
                binding.fourthIv.startAnimation(loadAnimation2);
                binding.fifthIv.startAnimation(loadAnimation3);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        return builder.create();
    }

    /**
     * Show the dialog when weight difference is 5kg
     *
     * @param mData
     */
    private void showDialogDiff(MeasurementsTable mData) {
        LayoutMeasureDialogBinding binding = LayoutMeasureDialogBinding.inflate(LayoutInflater.from(requireContext()));
        Dialog dialog = new Dialog(requireContext(), R.style.DialogTheme);
        dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        dialog.setCancelable(false);
        dialog.setContentView(binding.getRoot());

        binding.notMineTv.setOnClickListener(view -> dialog.dismiss());
        binding.mineTv.setOnClickListener(view -> {
            dialog.dismiss();
            insertAndSendToServer(mData);
        });
        dialog.show();
    }

    private void insertAndSendToServer(MeasurementsTable mData) {
        // insert data into local db
        smartScaleViewModel.insert(mData);
        // send data to server
        smartScaleViewModel.sendMeasurementData(mData);

        // update last measured weight value
        float currentWeight = Float.parseFloat(mData.getWeight());
        SharedPreferencesManager.write(SharedPreferencesManager.LAST_MEASURED_WEIGHT, currentWeight);
        ConstantValues.LAST_MEASURED_WEIGHT = currentWeight;
    }

    @Override
    public void onResume() {
        super.onResume();
        getUserInfoFromRoom();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        smartScaleViewModel.getLastMeasurement().removeObservers(requireActivity());
        smartScaleViewModel.getLiveWeight().removeObservers(requireActivity());
        smartScaleViewModel.getState().removeObservers(requireActivity());
        smartScaleViewModel.getMeasurementData().removeObservers(requireActivity());
        binding = null;
    }
}