package com.waltonbd.smartscale.view;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.suke.widget.SwitchButton;
import com.waltonbd.smartscale.R;
import com.waltonbd.smartscale.api.UserRegistration;
import com.waltonbd.smartscale.custom.MyNumberPicker;
import com.waltonbd.smartscale.databinding.ActivityUserRegisterBinding;
import com.waltonbd.smartscale.util.DateUtils;
import com.waltonbd.smartscale.util.HeightPickerDialog;
import com.waltonbd.smartscale.util.ToastMaker;
import com.waltonbd.smartscale.util.Utilities;
import com.waltonbd.smartscale.utils.NumberUtil;
import com.waltonbd.smartscale.utils.TimeUtil;
import com.waltonbd.smartscale.viewmodels.RegistrationViewModel;

import java.util.Calendar;
import java.util.Date;

public class UserRegisterActivity extends AppCompatActivity {

    private static final String TAG = "UserRegisterActivity";

    private final Context mContext = UserRegisterActivity.this;

    private KProgressHUD progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityUserRegisterBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_user_register);
        RegistrationViewModel registerViewModel = new ViewModelProvider(this).get(RegistrationViewModel.class);
        binding.setRegisterViewModel(registerViewModel);
        binding.setLifecycleOwner(this);

        // set some default values for user
        UserRegistration regInfo = (UserRegistration) getIntent().getSerializableExtra("regInfo");
        registerViewModel.email.setValue(regInfo.getUsername());
        registerViewModel.password.setValue(regInfo.getPassword());
        registerViewModel.gender.setValue("male");
        registerViewModel.athleteMode.setValue(false);

        // init progress dialog
        progressDialog = KProgressHUD.create(this)
                .setLabel("Loading...")
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE);

        binding.actionBack.setOnClickListener(view -> onBackPressed());

        binding.birthdayEt.setOnClickListener(view -> {
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);        // current year
            int mMonth = c.get(Calendar.MONTH);      // current month
            int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

            // date picker dialog
            DatePickerDialog dialog = new DatePickerDialog(mContext, (view1, year, monthOfYear, dayOfMonth) -> {
                // set day of month , month and year value in the edit text
                Date birthday = DateUtils.getDate(year, monthOfYear + 1, dayOfMonth);
                binding.birthdayEt.setText(DateUtils.dateToString(birthday, "dd MMM, yyyy"));
            }, mYear, mMonth, mDay);

            // show year first
            dialog.getDatePicker().getTouchables().get(0).performClick();
            dialog.getDatePicker().setMaxDate(new Date().getTime());
            dialog.show();
        });

        binding.heightEt.setOnClickListener(view -> {
            // default height 3 feet 11 inch
            HeightPickerDialog.showDialog(this, 3, 11);
        });

        // set picked height
        HeightPickerDialog.setOnCheckedChangeListener(height -> {
            binding.heightEt.setText(height);
            binding.heightCm.setText(String.format("(%s cm)", Utilities.getCmFromHeight(height)));
        });

        registerViewModel.registerSuccess.observe(this, isSuccess -> {
            if (isSuccess) {
                ToastMaker.show(mContext, "Registration successful. Please login.");
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finishAffinity();
            }
        });

        registerViewModel.errorMessage.observe(this, message -> {
            if (message != null) ToastMaker.show(mContext, message);
        });

        registerViewModel.loading.observe(this, isLoading -> {
            if (isLoading != null) {
                if (isLoading) {
                    progressDialog.show();
                } else {
                    progressDialog.dismiss();
                }
            }
        });
    }
}