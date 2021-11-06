package com.waltonbd.smartscale.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.waltonbd.smartscale.R;
import com.waltonbd.smartscale.databinding.FragmentChangeUnitBinding;
import com.waltonbd.smartscale.interfaces.OnFragmentCallback;

public class ChangeUnitFragment extends Fragment {

    private FragmentActivity mContext;
    private FragmentChangeUnitBinding binding;

    public static ChangeUnitFragment newInstance() {
        return new ChangeUnitFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = requireActivity();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChangeUnitBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.weightUnit.setOnClickListener(view1 -> loadWeightFragment());
        binding.lengthUnit.setOnClickListener(view1 -> loadLengthFragment());
    }

    @Override
    public void onResume() {
        super.onResume();
        // Update title with default text
        ((OnFragmentCallback) mContext).onCallback(getString(R.string.title_change_unit));
    }

    private void loadWeightFragment() {
        Fragment fragment = WeightUnitFragment.newInstance();
        FragmentManager fragmentManager = mContext.getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void loadLengthFragment() {
        Fragment fragment = LengthUnitFragment.newInstance();
        FragmentManager fragmentManager = mContext.getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}