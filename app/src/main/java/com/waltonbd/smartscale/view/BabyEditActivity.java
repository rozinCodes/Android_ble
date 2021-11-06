package com.waltonbd.smartscale.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.jaeger.library.StatusBarUtil;
import com.waltonbd.smartscale.databinding.ActivityBabyEditBinding;
import com.waltonbd.smartscale.util.Utilities;

public class BabyEditActivity extends AppCompatActivity {

    protected ActivityBabyEditBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setTransparentForImageView(this, null);
        binding = ActivityBabyEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.actionBack.setOnClickListener(view -> onBackPressed());
        binding.confirmBtn.setOnClickListener(view -> {
            String gender = Utilities.getTagFromSelectedRadioButton(binding.bayGenderRg);
            Toast.makeText(this, "Gender: " + gender, Toast.LENGTH_SHORT).show();
        });
    }
}