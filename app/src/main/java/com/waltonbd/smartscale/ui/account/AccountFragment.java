package com.waltonbd.smartscale.ui.account;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.jaeger.library.StatusBarUtil;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.waltonbd.smartscale.R;
import com.waltonbd.smartscale.adapters.UserListAdapter;
import com.waltonbd.smartscale.api.APIClient;
import com.waltonbd.smartscale.api.BaseResponse;
import com.waltonbd.smartscale.constant.ConstantValues;
import com.waltonbd.smartscale.custom.SwipeHelper;
import com.waltonbd.smartscale.databinding.AddMeasurementUserSheetBinding;
import com.waltonbd.smartscale.databinding.FragmentAccountBinding;
import com.waltonbd.smartscale.entities.UsersTable;
import com.waltonbd.smartscale.session.SharedPreferencesManager;
import com.waltonbd.smartscale.util.ToastMaker;
import com.waltonbd.smartscale.util.Utilities;
import com.waltonbd.smartscale.view.BabyEditActivity;
import com.waltonbd.smartscale.view.BaseActivity;
import com.waltonbd.smartscale.view.EditUserActivity;
import com.waltonbd.smartscale.view.SettingsActivity;
import com.waltonbd.smartscale.viewmodels.SmartScaleViewModel;
import com.waltonbd.smartscale.viewmodels.UserViewModel;
import com.waltonbd.smartscale.webLoader.WebLoaderActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.waltonbd.smartscale.view.EditUserActivity.EXTRA_FLAG_ADD_MEMBER;


public class AccountFragment extends Fragment {

    private static final String TAG = "AccountFragment";

    private SmartScaleViewModel smartScaleViewModel;
    private UserViewModel userViewModel;
    private FragmentAccountBinding binding;

    private KProgressHUD progressDialog;
    private NavController navController;

    private UserListAdapter mAdapter;
    private final List<UsersTable> userList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setColor(requireActivity(), ContextCompat.getColor(requireContext(), R.color.colorPrimary), 0);
        StatusBarUtil.setDarkMode(requireActivity());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAccountBinding.inflate(inflater, container, false);
        binding.setFragment(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // init progress dialog
        progressDialog = KProgressHUD.create(requireContext())
                .setLabel("Loading...")
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE);

        navController = Navigation.findNavController(view);

        smartScaleViewModel = new ViewModelProvider(requireActivity()).get(SmartScaleViewModel.class);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        mAdapter = new UserListAdapter(userList, requireContext());
        binding.userRecyclerView.setAdapter(mAdapter);
        swipeHelper.attachToRecyclerView(binding.userRecyclerView);

        userViewModel.getAllUsers().observe(getViewLifecycleOwner(), users -> {
            if (users.size() > 0) {
                userList.clear();
                userList.addAll(users);
                mAdapter.notifyDataSetChanged();
            }
        });

        mAdapter.setOnItemClickListener(user -> {
            // update all variable and local storage
            updateUser(user);
            // config for selected user
            userViewModel.setSelectedUser(user);
            // redirect to dashboard
            navController.popBackStack();
        });
    }

    private final SwipeHelper swipeHelper = new SwipeHelper(getActivity()) {
        @Override
        public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
            underlayButtons.add(new SwipeHelper.UnderlayButton(
                    "Delete",
                    Color.parseColor("#FF3C30"),
                    position -> {
                        // User delete management
                        new MaterialAlertDialogBuilder(requireContext(), R.style.MaterialAlertDialogRounded)
                                .setMessage("Are you sure you want to delete this?")
                                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                                .setPositiveButton("Confirm", (dialog, which) -> {
                                    dialog.dismiss();
                                    // request to server
                                    deleteMember(position);
                                }).show();
                    }
            ));
        }
    };

    private void deleteMember(int position) {
        progressDialog.show();

        // get user information from selected row
        final UsersTable user = mAdapter.getData().get(position);

        Call<BaseResponse> call = APIClient.getInstance().deleteMember(ConstantValues.ACCESS_TOKEN, user.getId());
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(@NotNull Call<BaseResponse> call, @NotNull Response<BaseResponse> response) {
                progressDialog.dismiss();
                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        // delete from room database
                        userViewModel.deleteUser(user);
                        mAdapter.removeItem(position);
                        // delete all measurement data against this user
                        smartScaleViewModel.deleteMeasurementByUserId(user.getId());
                        // if selected others user not main user
                        if (ConstantValues.USER_ID == user.getId()) {
                            // Get main user info from database and select primary user
                            ExecutorService executor = Executors.newSingleThreadExecutor();
                            Handler handler = new Handler(Looper.getMainLooper());
                            executor.execute(() -> {
                                UsersTable mainUser = userViewModel.getMainUser();
                                updateUser(mainUser);
                                handler.post(() -> {
                                    // config for selected user & goto dashboard
                                    userViewModel.setSelectedUser(mainUser);
                                    navController.popBackStack();
                                });
                            });
                        }
                    }
                    // show toast message to user
                    ToastMaker.show(requireContext(), response.body().getMessage());
                }
            }

            @Override
            public void onFailure(@NotNull Call<BaseResponse> call, @NotNull Throwable t) {
                ToastMaker.show(requireContext(), t.getMessage());
                progressDialog.dismiss();
            }
        });
    }

    private void getUserInfoFromRoom() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            UsersTable user = userViewModel.getUser(ConstantValues.USER_ID);
            handler.post(() -> {
                Utilities.loadImage(binding.profileImage, user.getImage(), user.getGender());
                binding.userName.setText(user.getFullName());
                binding.userEmail.setText(user.getUserName());
            });
        });
    }

    private void updateUser(UsersTable user) {
        //------------------- Update user id ---------------------
        SharedPreferencesManager.init(requireContext());
        SharedPreferencesManager.write(SharedPreferencesManager.USER_ID, user.getId());
        ConstantValues.USER_ID = user.getId();

        // update last measured weight value
        float currentWeight = Float.parseFloat(user.getWeight() != null ? user.getWeight() : "0.0");
        SharedPreferencesManager.write(SharedPreferencesManager.LAST_MEASURED_WEIGHT, currentWeight);
        ConstantValues.LAST_MEASURED_WEIGHT = currentWeight;
    }

    public void onProfileClicked() {
        startActivity(new Intent(requireContext(), EditUserActivity.class)
                .putExtra(EXTRA_FLAG_ADD_MEMBER, false));
    }

    public void onFaqClicked() {
        Intent intent = WebLoaderActivity.getStartIntent(requireContext(), "FAQ", "faq");
        startActivity(intent);
    }

    public void onContactClicked() {
        BaseActivity.startActivity(requireContext(), getString(R.string.title_contact));
    }

    public void onAddUserClicked() {
        openDialog();
    }

    public void onSettingsClicked(View view) {
        startActivity(new Intent(requireContext(), SettingsActivity.class));
    }

    private void openDialog() {
        // Limit adding ned member
        if(userList.size() >= ConstantValues.MAX_LIMIT) {
            ToastMaker.show(requireContext(), R.string.max_limit_text);
            return;
        }

        AddMeasurementUserSheetBinding binding = AddMeasurementUserSheetBinding.inflate(LayoutInflater.from(requireContext()));
        BottomSheetDialog dialog = new BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme);
        dialog.setContentView(binding.getRoot());

        binding.measurementUser.setOnClickListener(view -> {
            dialog.dismiss();
            startActivity(new Intent(requireContext(), EditUserActivity.class)
                    .putExtra(EXTRA_FLAG_ADD_MEMBER, true));
        });
        binding.babyUser.setOnClickListener(view -> {
            dialog.dismiss();
            startActivity(new Intent(requireContext(), BabyEditActivity.class));
        });
        binding.cancelTv.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        getUserInfoFromRoom();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}