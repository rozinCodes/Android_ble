package com.waltonbd.smartscale.view;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.waltonbd.smartscale.R;
import com.waltonbd.smartscale.api.APIClient;
import com.waltonbd.smartscale.api.BaseResponse;
import com.waltonbd.smartscale.api.UserProfile;
import com.waltonbd.smartscale.constant.ConstantValues;
import com.waltonbd.smartscale.databinding.ActivityEditUserBinding;
import com.waltonbd.smartscale.entities.UsersTable;
import com.waltonbd.smartscale.models.User;
import com.waltonbd.smartscale.util.DateUtils;
import com.waltonbd.smartscale.util.HeightPickerDialog;
import com.waltonbd.smartscale.util.ToastMaker;
import com.waltonbd.smartscale.util.Utilities;
import com.waltonbd.smartscale.utils.Validator;
import com.waltonbd.smartscale.viewmodels.UserViewModel;
import com.yolanda.health.qnblesdk.constant.QNUnit;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import id.zelory.compressor.Compressor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditUserActivity extends AppCompatActivity {

    public static final String EXTRA_FLAG_ADD_MEMBER = "add_member";

    private final Context mContext = EditUserActivity.this;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private boolean addNewMember = false;
    protected ActivityEditUserBinding binding;
    protected UserViewModel userViewModel;

    private KProgressHUD progressDialog;
    private final int PICK_IMAGE_ID = 110;

    private String birthday;
    private int heightFeet = 3, heightInch = 11;    // default value

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        addNewMember = getIntent().getBooleanExtra(EXTRA_FLAG_ADD_MEMBER, false);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // init progress dialog
        progressDialog = KProgressHUD.create(this)
                .setLabel("Loading...")
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE);

        if (addNewMember) {
            binding.titleToolbar.setText(String.format("%s", "New Member"));
            binding.lPwd.setVisibility(View.GONE);
        } else {
            binding.titleToolbar.setText(String.format("%s", "Profile"));
            binding.lPwd.setVisibility(View.VISIBLE);
            getUserInfoFromRoom();
        }
        binding.actionBack.setOnClickListener(view -> onBackPressed());

        binding.ageTv.setOnClickListener(view -> {

            final Calendar c = Calendar.getInstance();      // current day
            if (!addNewMember && birthday != null) {
                c.setTime(DateUtils.stringToDate(birthday, "dd MMM, yyyy"));   // birthday
            }
            int mYear = c.get(Calendar.YEAR);        // current year
            int mMonth = c.get(Calendar.MONTH);      // current month
            int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

            // date picker dialog
            DatePickerDialog dialog = new DatePickerDialog(mContext, (view1, year, monthOfYear, dayOfMonth) -> {
                // set day of month , month and year value in the edit text
                Date birthday = DateUtils.getDate(year, monthOfYear + 1, dayOfMonth);
                binding.ageTv.setText(DateUtils.dateToString(birthday, "dd MMM, yyyy"));
            }, mYear, mMonth, mDay);

            // show year first
            dialog.getDatePicker().getTouchables().get(0).performClick();
            dialog.getDatePicker().setMaxDate(new Date().getTime());
            dialog.show();
        });

        binding.heightTv.setOnClickListener(view -> {
            // default height 3 feet 11 inch
            HeightPickerDialog.showDialog(this, heightFeet, heightInch);
        });

        // set picked height
        HeightPickerDialog.setOnCheckedChangeListener(height -> {
            binding.heightTv.setText(height);
            binding.heightCm.setText(String.format("(%s cm)", Utilities.getCmFromHeight(height)));
        });

        binding.lPwd.setOnClickListener(view -> {
            // goto change password activity
            BaseActivity.startActivity(getApplicationContext(), getString(R.string.title_change_pass));
        });

        binding.userHead.setOnClickListener(view -> {
            if (addNewMember) {
                ToastMaker.show(this, "Image can be uploaded later from profile");
                return;
            }
            pickImage();
        });

        binding.confirmBtn.setOnClickListener(view -> validateInput());
    }

    /**
     * Update profile or add new member
     */
    private void validateInput() {
        String gender = Utilities.getTagFromSelectedRadioButton(binding.genderRg);
        String fullName = binding.nameEt.getText().toString().trim();
        String birthday = binding.ageTv.getText().toString();
        String height = Utilities.getCmFromHeight(binding.heightTv.getText().toString());
        boolean athleteMode = binding.athleteSb.isChecked();

        // Validate all input
        if (Validator.validateName(fullName)) {
            ToastMaker.show(mContext, "Name required");
            return;
        } else if (Validator.validateBirthday(birthday)) {
            ToastMaker.show(mContext, "Invalid birthday");
            return;
        } else if (Validator.validateHeight(height)) {
            ToastMaker.show(mContext, "Invalid height");
            return;
        }

        if (addNewMember) {
            UserProfile newMember = new UserProfile(ConstantValues.USER_EMAIL, gender, fullName, birthday, height, athleteMode);
            addNewMember(newMember);
        } else {
            UserProfile userProfile = new UserProfile(gender, fullName, birthday, height, athleteMode);
            updateProfile(userProfile);
        }
    }

    private void getUserInfoFromRoom() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            UsersTable user = userViewModel.getUser(ConstantValues.USER_ID);
            Log.i("userInfo", user.toString());
            handler.post(() -> {
                Utilities.setRadioButtonByTag(binding.genderRg, user.getGender());
                Utilities.loadImage(binding.userHead, user.getImage(), user.getGender());
                binding.nameEt.setText(user.getFullName());
                binding.ageTv.setText(user.getBirthDate());
                binding.heightTv.setText(Utilities.getHeightFromCm(user.getHeight()));
                binding.heightCm.setText(String.format("(%s cm)", user.getHeight()));
                binding.athleteSb.setChecked(user.getAthleteMode());

                // assign user birthday
                birthday = user.getBirthDate();
                // assign height into feet and inches
                int cm = (int) Double.parseDouble(user.getHeight());
                heightFeet = (int) Math.floor((cm / 2.54) / 12);
                heightInch = (int) Math.ceil((cm / 2.54) - (heightFeet * 12));
            });
        });
    }

    /**
     * Picks User Profile Image from Gallery
     */
    private void pickImage() {
        ImagePicker.create(this)
                .single()
                .folderMode(true)
                .showCamera(false)
                .returnMode(ReturnMode.ALL) // Whether pick immediate return result
                .toolbarFolderTitle("Choose Image")
                .toolbarImageTitle("Tap to select")
                .start(PICK_IMAGE_ID);
    }

    //*********** Receives the result from a previous call of startActivityForResult(Intent, int) ********//
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE_ID) {
            // Get a single image only
            Image image = ImagePicker.getFirstImageOrNull(data);
            if (image == null) return;

            // Compress image asynchronously with RxJava
            File actualImage = new File(image.getPath());
            Disposable disposable = new Compressor(this)
                    .setMaxWidth(320)
                    .setMaxHeight(240)
                    .setQuality(50)
                    .compressToFileAsFlowable(actualImage)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::processUploadImage, Throwable::printStackTrace);
            compositeDisposable.add(disposable);
        }
    }

    /**
     * Handle profile image uploading
     */
    private void processUploadImage(File file) {

        Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        binding.userHead.setImageBitmap(myBitmap);

        // Show progress dialog
        progressDialog.show();

        // Create a request body with file and image media type
        RequestBody requestFile = RequestBody.create(file, MediaType.parse("multipart/form-data"));
        //RequestBody requestFile = RequestBody.create(Utilities.getByteFromDrawable(binding.userHead.getDrawable()), MediaType.parse("image/*"));

        // Create MultipartBody.Part using file request-body,file name and part name
        MultipartBody.Part image = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        Call<BaseResponse> call = APIClient.getInstance().uploadPicture(ConstantValues.ACCESS_TOKEN, ConstantValues.USER_ID, image);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(@NotNull Call<BaseResponse> call, @NotNull Response<BaseResponse> response) {
                progressDialog.dismiss();
                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        // update image url in user info table
                        String imageUrl = response.body().getMessage();
                        ExecutorService executor = Executors.newSingleThreadExecutor();
                        Handler handler = new Handler(Looper.getMainLooper());
                        executor.execute(() -> {
                            UsersTable user = userViewModel.getUser(ConstantValues.USER_ID);
                            user.setImage(imageUrl);
                            userViewModel.updateUser(user);
                            handler.post(() -> ToastMaker.show(mContext, "Image uploaded successfully"));
                        });
                    } else {
                        ToastMaker.show(mContext, response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<BaseResponse> call, @NotNull Throwable t) {
                ToastMaker.show(mContext, t.getMessage());
                progressDialog.dismiss();
            }
        });
    }

    private void addNewMember(UserProfile user) {
        progressDialog.show();

        Call<User> call = APIClient.getInstance().addMember(ConstantValues.ACCESS_TOKEN, user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NotNull Call<User> call, @NotNull Response<User> response) {
                progressDialog.dismiss();
                if (response.body() != null) {

                    User user = response.body();
                    // insert new user into database
                    UsersTable userInfo = new UsersTable();
                    userInfo.setId(user.getId());
                    userInfo.setGuestId(ConstantValues.USER_ID);
                    userInfo.setFullName(user.getFullName());
                    userInfo.setGender(user.getGender());
                    userInfo.setBirthDate(user.getBirthdate());
                    userInfo.setWeightUnit(QNUnit.WEIGHT_UNIT_KG_STR);
                    userInfo.setWeight("0.0");
                    userInfo.setHeightUnit("cm");
                    userInfo.setHeight(user.getHeight());
                    userInfo.setAthleteMode(user.getAthlateMode());
                    userInfo.setCreateDateTime(user.getCreateDateTime());
                    userInfo.setUpdateDateTime(user.getUpdateDateTime());
                    userViewModel.insert(userInfo);

                    ToastMaker.show(mContext, "New member successfully added.");
                    // finish the activity
                    new Handler(Looper.getMainLooper()).postDelayed(() -> finish(), 300);
                }
            }

            @Override
            public void onFailure(@NotNull Call<User> call, @NotNull Throwable t) {
                ToastMaker.show(mContext, t.getMessage());
                progressDialog.dismiss();
            }
        });
    }

    private void updateProfile(UserProfile userProfile) {
        progressDialog.show();

        Call<BaseResponse> call = APIClient.getInstance().updateProfile(ConstantValues.ACCESS_TOKEN, ConstantValues.USER_ID, userProfile);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(@NotNull Call<BaseResponse> call, @NotNull Response<BaseResponse> response) {
                progressDialog.dismiss();
                if (response.body() != null) {
                    ToastMaker.show(mContext, response.body().getMessage());

                    if (response.body().getStatus()) {
                        ExecutorService executor = Executors.newSingleThreadExecutor();
                        Handler handler = new Handler(Looper.getMainLooper());
                        executor.execute(() -> {
                            // update user
                            UsersTable userInfo = userViewModel.getUser(ConstantValues.USER_ID);
                            userInfo.setGender(userProfile.getGender());
                            userInfo.setFullName(userProfile.getFullName());
                            userInfo.setBirthDate(userProfile.getBirthdate());
                            userInfo.setHeight(userProfile.getHeight());
                            userInfo.setAthleteMode(userProfile.isAthlateMode());
                            userViewModel.updateUser(userInfo);

                            // finish the activity
                            handler.postDelayed(() -> finish(), 500);
                        });
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<BaseResponse> call, @NotNull Throwable t) {
                ToastMaker.show(mContext, t.getMessage());
                progressDialog.dismiss();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }
}