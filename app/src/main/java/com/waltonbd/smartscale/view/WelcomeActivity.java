package com.waltonbd.smartscale.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.jaeger.library.StatusBarUtil;
import com.waltonbd.smartscale.databinding.ActivityWelcomeBinding;

public class WelcomeActivity extends AppCompatActivity {

    private final Context mContext = WelcomeActivity.this;
    ActivityWelcomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setTranslucentForImageView(this, null);
        binding = ActivityWelcomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.registerBtn.setOnClickListener(view -> startActivity(new Intent(mContext, RegisterActivity.class)));

        binding.loginBtn.setOnClickListener(view -> startActivity(new Intent(mContext, LoginActivity.class)));
    }
}