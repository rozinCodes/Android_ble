package com.waltonbd.smartscale.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.waltonbd.smartscale.BuildConfig;
import com.waltonbd.smartscale.R;
import com.waltonbd.smartscale.databinding.FragmentAboutBinding;
import com.waltonbd.smartscale.interfaces.OnFragmentCallback;
import com.waltonbd.smartscale.util.Utilities;

public class AboutFragment extends Fragment {

    private FragmentAboutBinding binding;
    private int pressedCounter = 0;

    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((OnFragmentCallback) requireContext()).onCallback(getString(R.string.title_about));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAboutBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set app name & version
        binding.versionEt.setText(String.format("%s %s", getString(R.string.app_name), BuildConfig.VERSION_NAME));
        binding.devLyt.setOnClickListener(view1 -> {
            if (pressedCounter >= 10) {
                String ascii = Utilities.hexToASCIIText(getResources().getString(R.string.hex_string));
                Toast.makeText(requireContext(), ascii, Toast.LENGTH_SHORT).show();
                pressedCounter = 0;
            }
            pressedCounter++;
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}