package com.waltonbd.smartscale.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.waltonbd.smartscale.R;
import com.waltonbd.smartscale.databinding.FragmentForgetPasswordBinding;
import com.waltonbd.smartscale.databinding.FragmentOtpVerifyBinding;
import com.waltonbd.smartscale.util.ToastMaker;
import com.waltonbd.smartscale.viewmodels.ForgetPasswordViewModel;

import org.jetbrains.annotations.NotNull;

public class ForgetPasswordFragment extends Fragment {

    private static final String TAG = "ForgetPasswordFragment";

    public FragmentForgetPasswordBinding binding;

    private KProgressHUD progressDialog;

    private String username;

    public static ForgetPasswordFragment newInstance() {
        return new ForgetPasswordFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        username = getArguments().getString("email");
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_forget_password, container, false);
        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ForgetPasswordViewModel viewModel = new ViewModelProvider(this).get(ForgetPasswordViewModel.class);
        binding.setViewModel(viewModel);

        // set email to viewModel
        viewModel.email.setValue(username);

        // init progress dialog
        progressDialog = KProgressHUD.create(requireContext())
                .setLabel("Loading...")
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE);

        viewModel.toastMessage.observe(getViewLifecycleOwner(), message -> {
            if (message != null) ToastMaker.show(requireContext(), message);
        });

        viewModel.loading.observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null) {
                if (isLoading) {
                    progressDialog.show();
                } else {
                    progressDialog.dismiss();
                }
            }
        });

        viewModel.isPasswordChanged.observe(getViewLifecycleOwner(), isChanged -> {
            if (isChanged != null && isChanged) requireActivity().finish();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}