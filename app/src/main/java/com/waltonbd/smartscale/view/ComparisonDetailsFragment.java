package com.waltonbd.smartscale.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.waltonbd.smartscale.R;
import com.waltonbd.smartscale.databinding.FragmentComparisonDetailsBinding;
import com.waltonbd.smartscale.databinding.FragmentComparisonListBinding;
import com.waltonbd.smartscale.interfaces.OnFragmentCallback;

public class ComparisonDetailsFragment extends Fragment {

    private FragmentComparisonDetailsBinding binding;

    public static ComparisonDetailsFragment newInstance() {
        return new ComparisonDetailsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((OnFragmentCallback) requireContext()).onCallback(getString(R.string.title_comparison));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentComparisonDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}