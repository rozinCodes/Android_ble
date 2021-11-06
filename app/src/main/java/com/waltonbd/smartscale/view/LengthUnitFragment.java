package com.waltonbd.smartscale.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import com.waltonbd.smartscale.R;
import com.waltonbd.smartscale.databinding.FragmentLengthUnitBinding;
import com.waltonbd.smartscale.databinding.FragmentWeightUnitBinding;
import com.waltonbd.smartscale.interfaces.OnFragmentCallback;
import com.waltonbd.smartscale.util.ToastMaker;

public class LengthUnitFragment extends Fragment {

    private FragmentLengthUnitBinding binding;

    public static LengthUnitFragment newInstance() {
        return new LengthUnitFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((OnFragmentCallback) requireContext()).onCallback(getString(R.string.title_length_unit));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLengthUnitBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            // checkedId is the RadioButton selected
            RadioButton rb = view.findViewById(checkedId);
            Log.d("LengthUnit", rb.getText().toString());
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}