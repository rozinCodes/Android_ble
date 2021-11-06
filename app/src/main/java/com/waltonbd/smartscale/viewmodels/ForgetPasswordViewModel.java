package com.waltonbd.smartscale.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.waltonbd.smartscale.api.APIClient;
import com.waltonbd.smartscale.api.APIRequests;
import com.waltonbd.smartscale.api.BaseResponse;
import com.waltonbd.smartscale.api.ForgetPassword;
import com.waltonbd.smartscale.utils.Validator;
import com.waltonbd.smartscale.view.OTPVerifyFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPasswordViewModel extends ViewModel {

    private final APIRequests apiRequests = APIClient.getInstance();

    public MutableLiveData<String> email = new MutableLiveData<>();
    public MutableLiveData<String> code = new MutableLiveData<>();
    public MutableLiveData<String> password = new MutableLiveData<>();
    public MutableLiveData<String> confirmPassword = new MutableLiveData<>();
    public MutableLiveData<Boolean> enable = new MutableLiveData<>(true);

    public MutableLiveData<Boolean> loading = new MutableLiveData<>();
    public MutableLiveData<String> toastMessage = new MutableLiveData<>();
    public MutableLiveData<String> isVerified = new MutableLiveData<>();
    public MutableLiveData<Boolean> isPasswordChanged = new MutableLiveData<>();

    public void onSendClicked() {
        ForgetPassword request = new ForgetPassword(email.getValue());
        if (Validator.validateEmail(request.getUsername())) {
            toastMessage.setValue("Invalid email address");
        } else {
            verifyEmail(request);
        }
    }

    public void onNextClicked() {

        if (Validator.validateEmail(email.getValue())) {
            toastMessage.setValue("Invalid email address");
        } else if (Validator.validateText(code.getValue())) {
            toastMessage.setValue("Verification code required");
        } else {
            ForgetPassword request = new ForgetPassword(
                    email.getValue(),
                    Integer.parseInt(code.getValue()));
            verifyOTP(request);
        }
    }

    public void onConfirmClicked() {
        ForgetPassword request = new ForgetPassword(email.getValue(), password.getValue());

        if (Validator.validatePassword(request.getNewPassword())) {
            toastMessage.setValue("Password (6-16 character)");
        } else if (!request.getNewPassword().equals(confirmPassword.getValue())) {
            toastMessage.setValue("Password and confirm password not matched");
        } else {
            forgetPassword(request);
        }
    }

    private void verifyEmail(ForgetPassword request) {
        loading.setValue(true);
        apiRequests.verifyUser(request).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                loading.postValue(false);
                if (response.body() != null) {
                    BaseResponse resp = response.body();
                    if (resp.getStatus()) {
                        toastMessage.postValue("Account does not exist");
                    } else {
                        sendVerificationCode(request);
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                loading.postValue(false);
            }
        });
    }

    private void sendVerificationCode(ForgetPassword request) {
        loading.setValue(true);
        apiRequests.sendOtp(request).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                loading.postValue(false);
                if (response.body() != null) {
                    BaseResponse resp = response.body();
                    enable.postValue(!resp.getStatus());
                    toastMessage.postValue(resp.getMessage());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                loading.postValue(false);
            }
        });
    }

    private void verifyOTP(ForgetPassword request) {
        loading.setValue(true);
        apiRequests.verifyOtp(request).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                loading.postValue(false);
                if (response.body() != null) {
                    BaseResponse resp = response.body();
                    if (resp.getStatus()) {
                        isVerified.postValue(request.getUsername());
                    } else {
                        isVerified.postValue(null);
                        toastMessage.postValue(resp.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                loading.postValue(false);
            }
        });
    }

    private void forgetPassword(ForgetPassword request) {
        loading.setValue(true);
        apiRequests.forgotPassword(request).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                loading.postValue(false);
                if (response.body() != null) {
                    BaseResponse resp = response.body();
                    isPasswordChanged.postValue(resp.getStatus());
                    toastMessage.postValue(resp.getMessage());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                loading.postValue(false);
            }
        });
    }

}
