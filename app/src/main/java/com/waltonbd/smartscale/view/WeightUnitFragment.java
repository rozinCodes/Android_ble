package com.waltonbd.smartscale.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.waltonbd.smartscale.R;
import com.waltonbd.smartscale.constant.ConstantValues;
import com.waltonbd.smartscale.databinding.FragmentWeightUnitBinding;
import com.waltonbd.smartscale.interfaces.OnFragmentCallback;
import com.waltonbd.smartscale.session.SharedPreferencesManager;
import com.waltonbd.smartscale.util.Utilities;

public class WeightUnitFragment extends Fragment {

    private FragmentWeightUnitBinding binding;

    public static WeightUnitFragment newInstance() {
        return new WeightUnitFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((OnFragmentCallback) requireContext()).onCallback(getString(R.string.title_weight_unit));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentWeightUnitBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Init shared pref
        SharedPreferencesManager.init(requireContext());
        // set pre-selected unit
        Utilities.setRadioButtonByTag(binding.radioGroup, Utilities.getWeightUnitStr(ConstantValues.WEIGHT_UNIT));

        binding.radioGroup.setOnCheckedChangeListener((group, checkedId) ->{
            // update unit set by user
            RadioButton rb = view.findViewById(checkedId);
            int unit = Utilities.getWeightUnit(rb.getTag().toString());
            SharedPreferencesManager.write(SharedPreferencesManager.WEIGHT_UNIT, unit);
            ConstantValues.WEIGHT_UNIT = unit;

            // redirect to dashboard
            Intent intent = new Intent(requireContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            ActivityCompat.finishAffinity(requireActivity());
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}