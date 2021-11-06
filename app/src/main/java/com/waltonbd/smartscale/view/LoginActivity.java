package com.waltonbd.smartscale.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.waltonbd.smartscale.R;
import com.waltonbd.smartscale.api.APIClient;
import com.waltonbd.smartscale.constant.ConstantValues;
import com.waltonbd.smartscale.databinding.ActivityLoginBinding;
import com.waltonbd.smartscale.entities.MeasurementsTable;
import com.waltonbd.smartscale.entities.UsersTable;
import com.waltonbd.smartscale.models.GuestUser;
import com.waltonbd.smartscale.models.Measurement;
import com.waltonbd.smartscale.models.UserMap;
import com.waltonbd.smartscale.session.SharedPreferencesManager;
import com.waltonbd.smartscale.util.ToastMaker;
import com.waltonbd.smartscale.viewmodels.LoginViewModel;
import com.waltonbd.smartscale.viewmodels.SmartScaleViewModel;
import com.waltonbd.smartscale.viewmodels.UserViewModel;
import com.yolanda.health.qnblesdk.constant.QNUnit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private final Context mContext = LoginActivity.this;

    private SmartScaleViewModel smartScaleViewModel;
    private KProgressHUD progressDialog;

    private final List<UserMap> userMap = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLoginBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        LoginViewModel loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        smartScaleViewModel = new ViewModelProvider(this).get(SmartScaleViewModel.class);
        binding.setLoginViewModel(loginViewModel);
        binding.setLifecycleOwner(this);

        // auto fill field
        loginViewModel.email.setValue(ConstantValues.USER_EMAIL);
        loginViewModel.password.setValue(ConstantValues.USER_PASS);

        // init progress dialog
        progressDialog = KProgressHUD.create(this)
                .setLabel("Loading...")
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE);

        binding.actionBack.setOnClickListener(view -> onBackPressed());

        binding.useRegisterBtn.setOnClickListener(view -> {
            startActivity(new Intent(mContext, RegisterActivity.class));
            finish();
        });

        binding.forgetPwdTv.setOnClickListener(view -> {
            // redirect to forget pass page
            BaseActivity.startActivity(getApplicationContext(), getString(R.string.title_forget_pass));
        });

        binding.feedBack.setOnClickListener(view -> {
            // redirect to contact us page
            BaseActivity.startActivity(getApplicationContext(), getString(R.string.title_contact));
        });

        loginViewModel.getLoginResponseLiveData().observe(this, response -> {
            if (response != null) {
                userMap.clear();

                //----------- Insert Main User to Room DB ---------------
                UsersTable userInfo = new UsersTable();
                userInfo.setId(response.getId());
                userInfo.setGuestId(0L);
                userInfo.setUserName(response.getUsername());
                userInfo.setFullName(response.getFullName());
                userInfo.setGender(response.getGender());
                userInfo.setImage(response.getImage());
                userInfo.setBirthDate(response.getBirthdate());
                userInfo.setWeightUnit(response.getWeightUnit());
                userInfo.setHeightUnit(response.getHeightUnit());
                userInfo.setHeight(response.getHeight());
                userInfo.setWeight(response.getWeight());
                userInfo.setPhone(response.getPhone());
                userInfo.setAthleteMode(response.getAthlateMode());
                userInfo.setCreateDateTime(response.getCreateDateTime());
                userInfo.setUpdateDateTime(response.getUpdateDateTime());
                userViewModel.insert(userInfo);

                // get user measurement data and insert to Room db
                userMap.add(new UserMap(userInfo.getId(), userInfo.getGuestId()));

                //----------- Insert Guest User to Room DB ---------------
                List<GuestUser> guestUserList = response.getGuestUsers();
                for (GuestUser user : guestUserList) {
                    UsersTable guestUser = new UsersTable();
                    guestUser.setId(user.getId());
                    guestUser.setGuestId(userInfo.getId());
                    guestUser.setFullName(user.getFullName());
                    guestUser.setGender(user.getGender());
                    guestUser.setImage(user.getImage());
                    guestUser.setBirthDate(user.getBirthdate());
                    guestUser.setWeightUnit(QNUnit.WEIGHT_UNIT_KG_STR);
                    guestUser.setWeight("0.0");
                    guestUser.setHeightUnit("cm");
                    guestUser.setHeight(user.getHeight());
                    guestUser.setAthleteMode(user.getAthlateMode());
                    guestUser.setCreateDateTime(user.getCreateDateTime());
                    guestUser.setUpdateDateTime(user.getUpdateDateTime());
                    userViewModel.insert(guestUser);

                    // get guest measurement data and insert to Room db
                    userMap.add(new UserMap(userInfo.getId(), guestUser.getId()));
                }

                //----------------- Save user info in Shared Pref ---------------------
                SharedPreferencesManager.init(getApplicationContext());
                SharedPreferencesManager.write(SharedPreferencesManager.PARENT_ID, response.getId());
                SharedPreferencesManager.write(SharedPreferencesManager.USER_ID, response.getId());
                SharedPreferencesManager.write(SharedPreferencesManager.USER_EMAIL, response.getUsername());
                SharedPreferencesManager.write(SharedPreferencesManager.USER_PASS, loginViewModel.password.getValue());

                String TOKEN = response.getTokenType() + " " + response.getAccessToken();
                SharedPreferencesManager.writeToken(SharedPreferencesManager.ACCESS_TOKEN, TOKEN);
                if (binding.remindCheck.isChecked()) {
                    SharedPreferencesManager.writeLoginStatus(SharedPreferencesManager.AUTO_LOGIN, true);
                }
                // save for further use
                ConstantValues.PARENT_ID = response.getId();
                ConstantValues.USER_ID = response.getId();
                ConstantValues.USER_EMAIL = response.getUsername();
                ConstantValues.USER_PASS = loginViewModel.password.getValue();
                ConstantValues.ACCESS_TOKEN = TOKEN;
                ConstantValues.IS_USER_LOGGED_IN = true;

                // request for measurement data
                getUserMeasurement();
            }
        });

        loginViewModel.errorMessage.observe(this, message -> {
            if (message != null) ToastMaker.show(this, message);
        });

        loginViewModel.loading.observe(this, isLoading -> {
            if (isLoading != null) {
                if (isLoading) {
                    progressDialog.show();
                } else {
                    progressDialog.dismiss();
                }
            }
        });
    }

    private void getUserMeasurement() {
        progressDialog.setLabel("Syncing data...");
        progressDialog.show();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            for (UserMap user : userMap) {
                getMeasurementById(user.getUserId(), user.getGuestId());
            }
            handler.post(() -> {
                progressDialog.dismiss();
                startActivity(new Intent(mContext, MainActivity.class));
                finishAffinity();
            });
        });
    }

    private void getMeasurementById(long userId, long guestId) {

        Map<String, Long> params = new HashMap<>();
        params.put("userId", userId);
        params.put("guestId", guestId);

        Call<List<Measurement>> call = APIClient.getInstance().getMeasurementById(ConstantValues.ACCESS_TOKEN, params);
        try {
            Response<List<Measurement>> response = call.execute();
            if (response.isSuccessful() && response.body() != null) {
                // insert data into database
                List<Measurement> measurementList = response.body();

                for (Measurement measurement : measurementList) {
                    MeasurementsTable mData = new MeasurementsTable();
                    mData.setUserId(guestId == 0 ? userId : guestId);
                    mData.setWeight(measurement.getWeight());
                    mData.setHeartRate(measurement.getHeartRate());
                    mData.setCardiacIndex(measurement.getCardiacIndex());
                    mData.setBmi(measurement.getBmi());
                    mData.setBodyFat(measurement.getBodyFat());
                    mData.setBodyType(measurement.getBodyType());
                    mData.setFatFreeBodyWeight(measurement.getFatFreeBodyWeight());
                    mData.setSubCutaneousFat(measurement.getSubcutaneousFat());
                    mData.setVisceralFat(measurement.getVisceralFat());
                    mData.setSkeletalMuscle(measurement.getSkeletalMuscle());
                    mData.setMuscleMass(measurement.getMuscleMass());
                    mData.setBodyWater(measurement.getBodyWater());
                    mData.setBoneMass(measurement.getBoneMass());
                    mData.setProtein(measurement.getProtein());
                    mData.setBmr(measurement.getBmr());
                    mData.setMetabolicAge(measurement.getMetabolicAge());
                    mData.setCreateDateTime(measurement.getCreateDateTime());
                    mData.setUpdateDateTime(measurement.getUpdateDateTime());
                    smartScaleViewModel.insert(mData);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}