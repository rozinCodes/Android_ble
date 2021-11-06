package com.waltonbd.smartscale.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.waltonbd.smartscale.R;
import com.waltonbd.smartscale.databinding.FragmentReminderBinding;
import com.waltonbd.smartscale.interfaces.OnFragmentCallback;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class ReminderFragment extends Fragment {

    private FragmentReminderBinding binding;

    private final SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());

    public static ReminderFragment newInstance() {
        return new ReminderFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((OnFragmentCallback) requireContext()).onCallback(getString(R.string.title_reminders));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentReminderBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.tvReminder1.setOnClickListener(view1 -> showHourPicker(binding.tvReminder1));
        binding.tvReminder2.setOnClickListener(view1 -> showHourPicker(binding.tvReminder2));
        binding.tvReminder3.setOnClickListener(view1 -> showHourPicker(binding.tvReminder3));

        binding.reminderSw1.setOnCheckedChangeListener((view1, isChecked) -> {
            if (isChecked) {
                String time = binding.tvReminder1.getText().toString();
                Toast.makeText(requireContext(), "Reminder is ON for: " + time, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Reminder is OFF", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*private void showTimePickerDialog() {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minutes = c.get(Calendar.MINUTE);
        // time picker dialog
        TimePickerDialog picker = new TimePickerDialog(requireContext(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                (timePicker, sHour, sMinute) -> {
                    binding.tvReminder1.setText(sHour + ":" + sMinute);
                }, hour, minutes, false);
        picker.setTitle("Choose hour:");
        picker.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        picker.show();
    }*/


    public void showHourPicker(TextView textView) {
        int color = ContextCompat.getColor(requireContext(), R.color.colorPrimary);
        TimePickerDialog mDialogHourMinute = new TimePickerDialog.Builder()
                .setCancelStringId("Cancel")
                .setSureStringId("Confirm")
                .setTitleStringId(null)
                .setHourText(null)
                .setMinuteText(null)
                .setCyclic(false)
                .setCurrentMillseconds(milliseconds(textView.getText().toString()))
                .setThemeColor(R.color.colorPrimary)
                .setWheelItemTextSelectorColor(R.color.colorPrimary)
                .setWheelItemTextSize(16)
                .setType(Type.HOURS_MINS)
                .setCallBack((timePickerView, milliseconds) -> {
                    textView.setText(getTimeToString(milliseconds));
                }).build();
        mDialogHourMinute.show(getChildFragmentManager(), "hour_minute");
    }

    private String getTimeToString(long milliseconds) {
        Date date = new Date(milliseconds);
        return sdf.format(date);
    }

    private long milliseconds(String time) {
        try {
            Date mDate = sdf.parse(time);
            return mDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}