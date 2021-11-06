package com.waltonbd.smartscale.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.waltonbd.smartscale.R;
import com.waltonbd.smartscale.constant.ConstantValues;
import com.waltonbd.smartscale.databinding.FragmentOtpVerifyBinding;
import com.waltonbd.smartscale.interfaces.OnFragmentCallback;
import com.waltonbd.smartscale.utils.Validator;
import com.waltonbd.smartscale.viewmodels.ForgetPasswordViewModel;

import org.jetbrains.annotations.NotNull;

public class OTPVerifyFragment extends Fragment {

    private static final String TAG = "OTPVerifyFragment";

    private ForgetPasswordViewModel viewModel;
    public FragmentOtpVerifyBinding binding;

    private KProgressHUD progressDialog;

    public static OTPVerifyFragment newInstance() {
        return new OTPVerifyFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((OnFragmentCallback) requireContext()).onCallback(getString(R.string.title_forget_pass));
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_otp_verify, container, false);
        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(ForgetPasswordViewModel.class);
        binding.setViewModel(viewModel);
        binding.setFragment(this);

        // auto fill email
        viewModel.email.setValue(ConstantValues.USER_EMAIL);

        // init progress dialog
        progressDialog = KProgressHUD.create(requireContext())
                .setLabel("Loading...")
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE);

        viewModel.toastMessage.observe(getViewLifecycleOwner(), message -> {
            if (message != null)
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
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

        viewModel.isVerified.observe(getViewLifecycleOwner(), email -> {
            if (email != null) {
                loadFragment(email);
            }
        });
    }

    public void loadFragment(String email) {
        Bundle bundle = new Bundle();
        bundle.putString("email", email);
        Fragment fragment = ForgetPasswordFragment.newInstance();
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}