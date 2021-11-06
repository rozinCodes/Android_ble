package com.waltonbd.smartscale.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.waltonbd.smartscale.R;
import com.waltonbd.smartscale.api.UserRegistration;
import com.waltonbd.smartscale.constant.ConstantValues;
import com.waltonbd.smartscale.databinding.ActivityLoginBinding;
import com.waltonbd.smartscale.databinding.ActivityRegisterBinding;
import com.waltonbd.smartscale.util.ToastMaker;
import com.waltonbd.smartscale.viewmodels.LoginViewModel;
import com.waltonbd.smartscale.viewmodels.RegistrationViewModel;
import com.waltonbd.smartscale.webLoader.WebLoaderActivity;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    private final Context mContext = RegisterActivity.this;

    private KProgressHUD progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityRegisterBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        RegistrationViewModel registerViewModel = new ViewModelProvider(this).get(RegistrationViewModel.class);
        binding.setRegisterViewModel(registerViewModel);
        binding.setLifecycleOwner(this);

        // init progress dialog
        progressDialog = KProgressHUD.create(this)
                .setLabel("Loading...")
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE);

        binding.actionBack.setOnClickListener(view -> onBackPressed());
        binding.loginTvLayout.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        });

        binding.privacyPolicyTV.setOnClickListener(view -> {
            Intent intent = WebLoaderActivity.getStartIntent(getApplicationContext(), "Legal & Policies", ConstantValues.POLICIES);
            startActivity(intent);
        });

        binding.feedBack.setOnClickListener(view -> {
            // goto contact us page
            BaseActivity.startActivity(getApplicationContext(), getString(R.string.title_contact));
        });

        registerViewModel.userNotExists.observe(this, userInfo -> {
            if (userInfo != null) {
                startActivity(new Intent(getApplicationContext(), UserRegisterActivity.class)
                        .putExtra("regInfo", userInfo));
            }
        });

        registerViewModel.errorMessage.observe(this, message -> {
            if (message != null) ToastMaker.show(this, message);
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