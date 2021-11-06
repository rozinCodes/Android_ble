package com.waltonbd.smartscale.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.waltonbd.smartscale.api.APIClient;
import com.waltonbd.smartscale.api.APIRequests;
import com.waltonbd.smartscale.api.UserLogin;
import com.waltonbd.smartscale.models.User;
import com.waltonbd.smartscale.utils.Validator;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends ViewModel {

    private final APIRequests apiRequests = APIClient.getInstance();

    private final MutableLiveData<User> loginResponseLiveData = new MutableLiveData<>();

    public MutableLiveData<String> email = new MutableLiveData<>();
    public MutableLiveData<String> password = new MutableLiveData<>();
    public MutableLiveData<Boolean> loading = new MutableLiveData<>();
    public MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LiveData<User> getLoginResponseLiveData() {
        return loginResponseLiveData;
    }

    public void onLoginClicked() {
        UserLogin user = new UserLogin(email.getValue(), password.getValue());

        if (Validator.validateEmail(user.getUsername())) {
            errorMessage.setValue("Invalid email");
        } else if (Validator.validateLoginPassword(user.getPassword())) {
            errorMessage.setValue("Password required");
        } else {
            processLogin(user);
        }
    }

    private void processLogin(UserLogin user) {
        loading.setValue(true);
        apiRequests.login(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NotNull Call<User> call, @NotNull Response<User> response) {
                loading.postValue(false);
                if (response.body() != null) {
                    User userInfo = response.body();
                    Log.e( "onResponse: ", userInfo.toString());
                    if (userInfo.getStatus()) {
                        loginResponseLiveData.postValue(userInfo);
                    } else {
                        errorMessage.postValue(userInfo.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                loading.postValue(false);
                loginResponseLiveData.postValue(null);
            }
        });
    }
}
