package com.waltonbd.smartscale.viewmodels;

import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingListener;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.suke.widget.SwitchButton;
import com.waltonbd.smartscale.api.APIClient;
import com.waltonbd.smartscale.api.APIRequests;
import com.waltonbd.smartscale.api.BaseResponse;
import com.waltonbd.smartscale.api.UserRegistration;
import com.waltonbd.smartscale.models.User;
import com.waltonbd.smartscale.util.Utilities;
import com.waltonbd.smartscale.utils.Validator;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationViewModel extends ViewModel {

    private final APIRequests apiRequests = APIClient.getInstance();

    private final MutableLiveData<User> registerResponse = new MutableLiveData<>();

    public MutableLiveData<String> email = new MutableLiveData<>();
    public MutableLiveData<String> password = new MutableLiveData<>();
    public MutableLiveData<String> surePassword = new MutableLiveData<>();
    public MutableLiveData<String> gender = new MutableLiveData<>();
    public MutableLiveData<String> fullName = new MutableLiveData<>();
    public MutableLiveData<String> birthday = new MutableLiveData<>();
    public MutableLiveData<String> height = new MutableLiveData<>();
    public MutableLiveData<Boolean> athleteMode = new MutableLiveData<>();

    public MutableLiveData<Boolean> loading = new MutableLiveData<>();
    public MutableLiveData<String> errorMessage = new MutableLiveData<>();
    public MutableLiveData<UserRegistration> userNotExists = new MutableLiveData<>();
    public MutableLiveData<Boolean> registerSuccess = new MutableLiveData<>();

    public LiveData<User> getRegisterResponse() {
        return registerResponse;
    }

    public void onNextClicked() {

        UserRegistration user = new UserRegistration(email.getValue(), password.getValue());

        if (Validator.validateEmail(user.getUsername())) {
            errorMessage.setValue("Enter a valid email address");
        } else if (Validator.validatePassword(user.getPassword())) {
            errorMessage.setValue("Password (6-16 character)");
        } else if (!user.getPassword().equals(surePassword.getValue())) {
            errorMessage.setValue("Password and confirm password not matched");
        } else {
            checkUserExists(user);
        }
    }

    public void onRegisterClicked() {

        // convert feetInch to (cm)
        String height = Utilities.getCmFromHeight(this.height.getValue());
        UserRegistration user = new UserRegistration(email.getValue(), password.getValue(), gender.getValue(), fullName.getValue(), birthday.getValue(), height, athleteMode.getValue());

        if (Validator.validateName(user.getFullName())) {
            errorMessage.setValue("Name required");
        } else if (Validator.validateBirthday(user.getBirthdate())) {
            errorMessage.setValue("Invalid birthday");
        } else if (Validator.validateHeight(user.getHeight())) {
            errorMessage.setValue("Invalid height");
        } else {
            processRegistration(user);
        }
    }

    private void checkUserExists(UserRegistration user) {

        loading.setValue(true);
        apiRequests.isUserExists(user).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(@NotNull Call<BaseResponse> call, @NotNull Response<BaseResponse> response) {
                loading.postValue(false);
                if (response.body() != null) {
                    BaseResponse resp = response.body();
                    if (resp.getStatus()) {
                        userNotExists.postValue(user);
                    } else {
                        userNotExists.postValue(null);
                        errorMessage.postValue(resp.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<BaseResponse> call, @NotNull Throwable t) {
                loading.postValue(false);
            }
        });
    }

    private void processRegistration(UserRegistration user) {
        loading.setValue(true);
        apiRequests.registration(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NotNull Call<User> call, @NotNull Response<User> response) {
                loading.postValue(false);
                if (response.body() != null) {
                    User userInfo = response.body();
                    if (userInfo.getStatus()) {
                        registerSuccess.postValue(true);
                    } else {
                        errorMessage.postValue(userInfo.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<User> call, @NotNull Throwable t) {
                loading.postValue(false);
                registerSuccess.postValue(false);
            }
        });
    }

    public void onGenderChanged(RadioGroup radioGroup, int id) {
        RadioButton radioButton = radioGroup.findViewById(id);
        gender.setValue(radioButton.getTag().toString());
    }

    public void onAthleteModeChanged(boolean checked) {
        athleteMode.setValue(checked);
    }

    @BindingAdapter(value = {"android:onCheckedChanged", "android:checkedAttrChanged"}, requireAll = false)
    public static void setListeners(SwitchButton view, final SwitchButton.OnCheckedChangeListener listener,
                                    final InverseBindingListener attrChange) {
        if (attrChange == null) {
            view.setOnCheckedChangeListener(listener);
        } else {
            view.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (listener != null) {
                    listener.onCheckedChanged(buttonView, isChecked);
                }
                attrChange.onChange();
            });
        }
    }
}
