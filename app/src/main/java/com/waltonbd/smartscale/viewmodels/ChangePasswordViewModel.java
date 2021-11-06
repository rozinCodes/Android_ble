package com.waltonbd.smartscale.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.waltonbd.smartscale.api.APIClient;
import com.waltonbd.smartscale.api.APIRequests;
import com.waltonbd.smartscale.api.BaseResponse;
import com.waltonbd.smartscale.api.ChangePassword;
import com.waltonbd.smartscale.constant.ConstantValues;
import com.waltonbd.smartscale.utils.Validator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordViewModel extends ViewModel {

    private final APIRequests apiRequests = APIClient.getInstance();

    public MutableLiveData<String> oldPassword = new MutableLiveData<>();
    public MutableLiveData<String> newPassword = new MutableLiveData<>();
    public MutableLiveData<String> confirmNewPassword = new MutableLiveData<>();

    public MutableLiveData<Boolean> loading = new MutableLiveData<>();
    public MutableLiveData<String> toastMessage = new MutableLiveData<>();
    public MutableLiveData<Boolean> isPasswordChanged = new MutableLiveData<>();

    public void onConfirmClicked() {
        ChangePassword request = new ChangePassword(ConstantValues.USER_EMAIL, oldPassword.getValue(), newPassword.getValue());

        if (Validator.validateLoginPassword(request.getOldPassword())) {
            toastMessage.setValue("Invalid old password");
        } else if (Validator.validatePassword(request.getNewPassword())) {
            toastMessage.setValue("Password (6-16 character)");
        } else if (!request.getNewPassword().equals(confirmNewPassword.getValue())) {
            toastMessage.setValue("Password and confirm password not matched");
        } else {
            changePassword(request);
        }
    }

    private void changePassword(ChangePassword request) {
        loading.setValue(true);
        apiRequests.changePassword(ConstantValues.ACCESS_TOKEN, request).enqueue(new Callback<BaseResponse>() {
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
