package com.waltonbd.smartscale.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.waltonbd.smartscale.api.APIClient;
import com.waltonbd.smartscale.api.SetGoal;
import com.waltonbd.smartscale.constant.ConstantValues;
import com.waltonbd.smartscale.databinding.ActivitySetGoalBinding;
import com.waltonbd.smartscale.models.User;
import com.waltonbd.smartscale.session.SharedPreferencesManager;
import com.waltonbd.smartscale.util.ToastMaker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetGoalActivity extends AppCompatActivity {

    private final Context mContext = SetGoalActivity.this;

    protected ActivitySetGoalBinding binding;

    protected float weightSelectorValue = 60;
    protected float bodyFatSelectorValue = 16;

    private KProgressHUD progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySetGoalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // init progress dialog
        progressDialog = KProgressHUD.create(this)
                .setLabel("Loading...")
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE);

        // set default ruler value
        binding.rulerWeight.setValue(weightSelectorValue, ConstantValues.weightMinValue, ConstantValues.weightMaxValue, ConstantValues.mPerValue);
        binding.rulerBodyFat.setValue(bodyFatSelectorValue, ConstantValues.bodyFatMinValue, ConstantValues.bodyFatMaxValue, ConstantValues.mPerValue);
        binding.tvInfoWeight.setNormalText(String.format(Locale.getDefault(), "%.1f", weightSelectorValue));
        binding.tvInfoBodyFat.setNormalText(String.format(Locale.getDefault(), "%.1f", bodyFatSelectorValue));

        binding.actionBack.setOnClickListener(view -> onBackPressed());
        binding.rulerWeight.setOnValueChangeListener(value -> {
            binding.tvInfoWeight.setNormalText(String.format(Locale.getDefault(), "%.1f", value));
            weightSelectorValue = value;
        });

        binding.rulerBodyFat.setOnValueChangeListener(value -> {
            binding.tvInfoBodyFat.setNormalText(String.format(Locale.getDefault(), "%.1f", value));
            bodyFatSelectorValue = value;
        });

        binding.confirmBtn.setOnClickListener(view -> {
            String strWeight = String.valueOf(weightSelectorValue);
            String strBodyFat = String.valueOf(bodyFatSelectorValue);
            SetGoal userGoal = new SetGoal(ConstantValues.USER_ID, 0, strWeight, strBodyFat);
            setGoat(userGoal);
        });
    }

    private void setGoat(SetGoal goal) {
        progressDialog.show();

        Call<ResponseBody> call = APIClient.getInstance().setGoat(ConstantValues.ACCESS_TOKEN, goal);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();
                if (response.body() != null) {
                    try {
                        Log.e("response: ", response.body().string());

                        ToastMaker.show(mContext, "Goal set successfully");
                        new Handler(Looper.myLooper()).postDelayed(() -> finish(), 500);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }
}