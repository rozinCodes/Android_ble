package com.waltonbd.smartscale.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.waltonbd.smartscale.R;
import com.waltonbd.smartscale.api.APIClient;
import com.waltonbd.smartscale.api.BaseResponse;
import com.waltonbd.smartscale.api.Feedback;
import com.waltonbd.smartscale.api.SetGoal;
import com.waltonbd.smartscale.constant.ConstantValues;
import com.waltonbd.smartscale.databinding.FragmentAboutBinding;
import com.waltonbd.smartscale.databinding.FragmentContactUsBinding;
import com.waltonbd.smartscale.interfaces.OnFragmentCallback;
import com.waltonbd.smartscale.util.ToastMaker;
import com.waltonbd.smartscale.utils.Validator;

import java.io.IOException;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactUsFragment extends Fragment {

    private FragmentContactUsBinding binding;

    private KProgressHUD progressDialog;

    public static ContactUsFragment newInstance() {
        return new ContactUsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((OnFragmentCallback) requireContext()).onCallback(getString(R.string.title_contact));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentContactUsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // init progress dialog
        progressDialog = KProgressHUD.create(requireContext())
                .setLabel("Loading...")
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE);

        boolean isUserLogin = ConstantValues.IS_USER_LOGGED_IN;
        binding.emailFly.setVisibility(isUserLogin ? View.GONE : View.VISIBLE);

        binding.feedbackEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence text, int i, int i1, int i2) {
                binding.textLimitTv.setText(String.format(Locale.getDefault(), "%s/%d", text.length(), getResources().getInteger(R.integer.max_feedback_length)));
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        binding.confirmBtn.setOnClickListener(view1 -> {

            String email = binding.emailAddressEt.getText().toString().trim();
            String problems = binding.feedbackEt.getText().toString().trim();

            Feedback feedback = new Feedback();
            feedback.setProblems(problems);
            if (ConstantValues.IS_USER_LOGGED_IN) {
                feedback.setUsername(ConstantValues.USER_EMAIL);
            } else {
                feedback.setUsername(email);
            }

            if (Validator.validateEmail(feedback.getUsername())) {
                ToastMaker.show(requireContext(), "Invalid email");
                return;
            } else if (Validator.validateText(feedback.getProblems())) {
                ToastMaker.show(requireContext(), "Invalid email");
                return;
            }

            sendFeedback(feedback);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void sendFeedback(Feedback feedback) {
        progressDialog.show();

        APIClient.getInstance().sendFeedback(feedback).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                progressDialog.dismiss();
                if (response.body() != null) {
                    ToastMaker.show(requireContext(), response.body().getMessage());
                    if (response.body().getStatus()) {
                        new Handler(Looper.myLooper()).postDelayed(() -> requireActivity().finish(), 500);
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }
}