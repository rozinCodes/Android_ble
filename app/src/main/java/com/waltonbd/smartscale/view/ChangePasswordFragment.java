package com.waltonbd.smartscale.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.waltonbd.smartscale.R;
import com.waltonbd.smartscale.databinding.FragmentChangePasswordBinding;
import com.waltonbd.smartscale.databinding.FragmentChangeUnitBinding;
import com.waltonbd.smartscale.databinding.FragmentOtpVerifyBinding;
import com.waltonbd.smartscale.interfaces.OnFragmentCallback;
import com.waltonbd.smartscale.util.ToastMaker;
import com.waltonbd.smartscale.viewmodels.ChangePasswordViewModel;
import com.waltonbd.smartscale.viewmodels.ForgetPasswordViewModel;

import org.jetbrains.annotations.NotNull;

public class ChangePasswordFragment extends Fragment {


    private ChangePasswordViewModel viewModel;
    private FragmentChangePasswordBinding binding;

    private KProgressHUD progressDialog;

    public static ChangePasswordFragment newInstance() {
        return new ChangePasswordFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((OnFragmentCallback) requireContext()).onCallback(getString(R.string.title_change_pass));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_change_password, container, false);
        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(ChangePasswordViewModel.class);
        binding.setViewModel(viewModel);

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