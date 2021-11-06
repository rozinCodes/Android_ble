package com.waltonbd.smartscale.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.waltonbd.smartscale.R;
import com.waltonbd.smartscale.databinding.ActivityBaseBinding;
import com.waltonbd.smartscale.interfaces.OnFragmentCallback;

public class BaseActivity extends AppCompatActivity implements OnFragmentCallback {

    private static final String TAG = "BaseActivity";

    private static final String EXTRA_NAME = "fragment";
    protected ActivityBaseBinding binding;

    public static void startActivity(Context context, String extraName) {
        context.startActivity(new Intent(context, BaseActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .putExtra(EXTRA_NAME, extraName));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBaseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String name = getIntent().getStringExtra(EXTRA_NAME);

        // Load general info fragment
        assert name != null;
        loadFragment(name);

        binding.actionBack.setOnClickListener(view -> onBackPressed());
    }

    private void loadFragment(String name) {
        Bundle bundle = new Bundle();
        Fragment fragment = new Fragment();

        if (name.equalsIgnoreCase(getString(R.string.title_unassigned))) {
            fragment = UnassignedDataFragment.newInstance();
        } else if (name.equalsIgnoreCase(getString(R.string.title_change_unit))) {
            fragment = ChangeUnitFragment.newInstance();
        } else if (name.equalsIgnoreCase(getString(R.string.title_reminders))) {
            fragment = ReminderFragment.newInstance();
        } else if (name.equalsIgnoreCase(getString(R.string.title_comparison))) {
            fragment = ComparisonListFragment.newInstance();
        } else if (name.equalsIgnoreCase(getString(R.string.title_change_pass))) {
            fragment = ChangePasswordFragment.newInstance();
        } else if (name.equalsIgnoreCase(getString(R.string.title_about))) {
            fragment = AboutFragment.newInstance();
        } else if (name.equalsIgnoreCase(getString(R.string.title_contact))) {
            fragment = ContactUsFragment.newInstance();
        } else if (name.equalsIgnoreCase(getString(R.string.title_forget_pass))) {
            fragment = OTPVerifyFragment.newInstance();
        }

        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_NONE)
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    @Override
    public void onCallback(String title) {
        if (!title.isEmpty()) binding.actionTitle.setText(title);
    }
}