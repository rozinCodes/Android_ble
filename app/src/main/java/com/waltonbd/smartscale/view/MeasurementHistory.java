package com.waltonbd.smartscale.view;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.applandeo.materialcalendarview.EventDay;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.waltonbd.smartscale.R;
import com.waltonbd.smartscale.adapters.MeasurementHistoryDataAdapter;
import com.waltonbd.smartscale.api.APIClient;
import com.waltonbd.smartscale.api.BaseResponse;
import com.waltonbd.smartscale.constant.ConstantValues;
import com.waltonbd.smartscale.databinding.ActivityMeasurementHistoryBinding;
import com.waltonbd.smartscale.entities.MeasurementsTable;
import com.waltonbd.smartscale.util.DateUtils;
import com.waltonbd.smartscale.util.ToastMaker;
import com.waltonbd.smartscale.viewmodels.SmartScaleViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MeasurementHistory extends AppCompatActivity implements MeasurementHistoryDataAdapter.OnItemDeleteListener {

    private final Handler handler = new Handler(Looper.getMainLooper());
    private ActivityMeasurementHistoryBinding binding;
    private SmartScaleViewModel viewModel;

    private MeasurementHistoryDataAdapter adapter;

    private KProgressHUD progressDialog;
    private boolean isDateUpdated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMeasurementHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // init progress dialog
        progressDialog = KProgressHUD.create(this)
                .setLabel("Loading...")
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE);

        viewModel = new ViewModelProvider(this).get(SmartScaleViewModel.class);
        binding.actionBack.setOnClickListener(view -> onBackPressed());

        adapter = new MeasurementHistoryDataAdapter(this, getApplicationContext());
        binding.rvHistory.setNestedScrollingEnabled(false);
        binding.rvHistory.setAdapter(adapter);

        // Custom calender view
        binding.calendarView.setCalendarDayLayout(R.layout.custom_calendar_day_row);

        viewModel.getAllMeasurements().observe(this, measurements -> {
            List<EventDay> events = new ArrayList<>();

            for (MeasurementsTable measurement : measurements) {
                Calendar calendar = toCalendar(DateUtils.stringToDate(measurement.getCreateDateTime()));
                events.add(new EventDay(calendar, R.drawable.round_circle));
            }

            // Set all measurements date with dot indicator
            binding.calendarView.setEvents(events);

            // Set selected at last measured date
            if (measurements.size() > 0 && !isDateUpdated) {
                Date lastDate = DateUtils.stringToDate(measurements.get(measurements.size() - 1).getCreateDateTime());
                binding.calendarView.setDate(lastDate);
                // Show measurement of last date
                showMeasurementData(lastDate);
                isDateUpdated = true;
            }
        });

        binding.calendarView.setOnDayClickListener(eventDay -> {
            // Show measurement of selected date
            showMeasurementData(eventDay.getCalendar().getTime());
        });
    }

    private Calendar toCalendar(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    private void showMeasurementData(Date date) {
        String mDate = DateUtils.dateToString(date);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            List<MeasurementsTable> measurements = viewModel.getMeasurementsByDate(mDate);
            handler.post(() -> adapter.submitList(measurements));
        });
    }

    @Override
    public void onItemDelete(MeasurementsTable data) {
        new MaterialAlertDialogBuilder(this, R.style.MaterialAlertDialogRounded)
                .setMessage("Delete the data?")
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .setPositiveButton("Confirm", (dialog, which) -> {
                    dialog.dismiss();
                    //deleteData(data);

                    List<MeasurementsTable> tempList = new ArrayList<>(adapter.getCurrentList());
                    tempList.remove(data);
                    adapter.submitList(tempList);
                    // delete from room database
                    viewModel.deleteMeasurements(data);
                }).show();
    }

    private void deleteData(MeasurementsTable data) {
        // Show progress dialog
        progressDialog.show();

        APIClient.getInstance().deleteMeasurement(ConstantValues.ACCESS_TOKEN, data.getId()).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(@NotNull Call<BaseResponse> call, @NotNull Response<BaseResponse> response) {
                progressDialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    BaseResponse rsp = response.body();
                    if (rsp.getStatus()) {
                        List<MeasurementsTable> tempList = new ArrayList<>(adapter.getCurrentList());
                        tempList.remove(data);
                        adapter.submitList(tempList);
                        // delete from room database
                        viewModel.deleteMeasurements(data);
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<BaseResponse> call, @NotNull Throwable t) {
                progressDialog.dismiss();
                ToastMaker.show(getApplicationContext(), t.getMessage());
            }
        });
    }
}