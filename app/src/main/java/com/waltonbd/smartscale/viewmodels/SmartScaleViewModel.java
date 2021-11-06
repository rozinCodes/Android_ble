package com.waltonbd.smartscale.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.waltonbd.smartscale.api.APIClient;
import com.waltonbd.smartscale.api.APIRequests;
import com.waltonbd.smartscale.api.BaseResponse;
import com.waltonbd.smartscale.constant.ConstantValues;
import com.waltonbd.smartscale.entities.MeasurementsTable;
import com.waltonbd.smartscale.models.IndexItem;
import com.waltonbd.smartscale.models.Measurement;
import com.waltonbd.smartscale.repositories.IndexRepo;
import com.waltonbd.smartscale.repositories.MeasurementsRepository;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SmartScaleViewModel extends AndroidViewModel {

    private final APIRequests apiRequests = APIClient.getInstance();

    private final MeasurementsRepository repository;
    private final IndexRepo indexRepo = new IndexRepo();

    public SmartScaleViewModel(@NonNull Application application) {
        super(application);
        repository = new MeasurementsRepository(application);
    }

    public void insert(MeasurementsTable measurementsTable) {
        repository.insertMeasurements(measurementsTable);
    }

    public void updateMeasurements(MeasurementsTable measurementsTable) {
        repository.updateMeasurements(measurementsTable);
    }

    public void deleteMeasurements(MeasurementsTable measurementsTable) {
        repository.deleteMeasurements(measurementsTable);
    }

    public void deleteMeasurementByUserId(long guestId) {
        repository.deleteMeasurementByUserId(guestId);
    }

    public void deleteAllMeasurement() {
        repository.deleteAllMeasurement();
    }

    public void resetSequence() {
        repository.resetSequence();
    }

    public LiveData<List<MeasurementsTable>> getLastMeasurement() {
        return repository.getLastMeasurement();
    }

    public LiveData<List<MeasurementsTable>> getAllMeasurements() {
        return repository.getAllMeasurements();
    }

    public List<MeasurementsTable> getMeasurementsByDate(String date) {
        return repository.getMeasurementsByDate(date);
    }

    public List<MeasurementsTable> getLastMeasurementsGroup() {
        return repository.getLastMeasurementsGroup();
    }

    public List<MeasurementsTable> getLastMeasurementsGroupWeekly() {
        return repository.getLastMeasurementsGroupWeekly();
    }

    public List<MeasurementsTable> getLastMeasurementsGroupMonthly() {
        return repository.getLastMeasurementsGroupMonthly();
    }

    public List<MeasurementsTable> getLastMeasurementsGroupYearly() {
        return repository.getLastMeasurementsGroupYearly();
    }

    public LiveData<String> getLiveWeight() {
        return repository.getLiveWeight();
    }

    public void setLiveWeight(String weight) {
        repository.setLiveWeight(weight);
    }

    public LiveData<String> getState() {
        return repository.getState();
    }

    public void setState(String status) {
        repository.setState(status);
    }

    public LiveData<MeasurementsTable> getMeasurementData() {
        return repository.getMeasurementData();
    }

    public void setMeasurementData(MeasurementsTable data) {
        repository.setMeasurementData(data);
    }

    public LiveData<List<IndexItem>> getIndexItems() {
        return indexRepo.getIndexItems();
    }

    public void sendMeasurementData(MeasurementsTable data) {
        Measurement mData = new Measurement(data.getWeight(), data.getHeartRate(), data.getCardiacIndex(), data.getBmi(), data.getBodyFat(),
                data.getBodyType(), data.getFatFreeBodyWeight(), data.getSubCutaneousFat(), data.getVisceralFat(), data.getSkeletalMuscle(),
                data.getMuscleMass(), data.getBodyWater(), data.getBoneMass(), data.getProtein(), data.getBmr(), data.getMetabolicAge(),
                data.getCreateDateTime(), data.getUpdateDateTime());
        mData.setUserId(ConstantValues.PARENT_ID);
        mData.setGuestId(ConstantValues.USER_ID != ConstantValues.PARENT_ID ? ConstantValues.USER_ID : 0);

        apiRequests.sendMeasurementData(ConstantValues.ACCESS_TOKEN, mData).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(@NotNull Call<BaseResponse> call, @NotNull Response<BaseResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    BaseResponse resp = response.body();
                    Log.d("onResponse", resp.getMessage());
                }
            }

            @Override
            public void onFailure(@NotNull Call<BaseResponse> call, @NotNull Throwable t) {
                Log.e("onFailure", t.getMessage());
            }
        });
    }
}
