package com.waltonbd.smartscale.repositories;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.waltonbd.smartscale.constant.ConstantValues;
import com.waltonbd.smartscale.dao.MeasurementsDAO;
import com.waltonbd.smartscale.database.SmartScaleDatabase;
import com.waltonbd.smartscale.entities.MeasurementsTable;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MeasurementsRepository {

    private final MeasurementsDAO measurementsDAO;
    private final MutableLiveData<String> mutableLiveWeight = new MutableLiveData<>();
    private final MutableLiveData<String> mutableState = new MutableLiveData<>();
    private final MutableLiveData<MeasurementsTable> measurementData = new MutableLiveData<>();

    public MeasurementsRepository(Application application) {
        SmartScaleDatabase database = SmartScaleDatabase.getInstance(application);
        measurementsDAO = database.measurementsDAO();
    }

    public void insertMeasurements(MeasurementsTable measurementsTable) {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            //Background work here
            measurementsDAO.insertMeasurement(measurementsTable);
//            handler.post(() -> {
//                //UI Thread work here
//            });
        });

    }

    public void updateMeasurements(MeasurementsTable measurementsTable) {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            //Background work here
            measurementsDAO.updateMeasurement(measurementsTable);
//            handler.post(() -> {
//                //UI Thread work here
//            });
        });
    }

    public void deleteMeasurements(MeasurementsTable measurementsTable) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            //Background work here
            measurementsDAO.deleteMeasurement(measurementsTable);
        });
    }

    public void deleteMeasurementByUserId(long userId) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            //Background work here
            measurementsDAO.deleteMeasurementByUserId(userId);
        });
    }

    public void deleteAllMeasurement() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(measurementsDAO::deleteAllMeasurement);
    }

    public void resetSequence() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(measurementsDAO::resetSequence);
    }

    public LiveData<List<MeasurementsTable>> getAllMeasurements() {
        return measurementsDAO.getAllMeasurements(ConstantValues.USER_ID);
    }

    public List<MeasurementsTable> getMeasurementsByDate(String date) {
        return measurementsDAO.getMeasurementsByDate(ConstantValues.USER_ID, date);
    }

    public List<MeasurementsTable> getLastMeasurementsGroup() {
        return measurementsDAO.getLastMeasurementsGroup();
    }

    public List<MeasurementsTable> getLastMeasurementsGroupWeekly() {
        return measurementsDAO.getLastMeasurementsGroupWeekly(ConstantValues.USER_ID);
    }

    public List<MeasurementsTable> getLastMeasurementsGroupMonthly() {
        return measurementsDAO.getLastMeasurementsGroupMonthly(ConstantValues.USER_ID);
    }

    public List<MeasurementsTable> getLastMeasurementsGroupYearly() {
        return measurementsDAO.getLastMeasurementsGroupYearly(ConstantValues.USER_ID);
    }

    public LiveData<List<MeasurementsTable>> getLastMeasurement() {
        return measurementsDAO.getLastMeasurement(ConstantValues.USER_ID);
    }

    public LiveData<String> getLiveWeight() {
        if (mutableLiveWeight.getValue() == null) {
            mutableLiveWeight.setValue("0.0");
        }
        return mutableLiveWeight;
    }

    public void setLiveWeight(String weight) {
        mutableLiveWeight.setValue(weight);
    }

    public LiveData<String> getState() {
        if (mutableState.getValue() == null) {
            mutableState.setValue("Measuring...");
        }
        return mutableState;
    }

    public void setState(String status) {
        mutableState.setValue(status);
    }

    public LiveData<MeasurementsTable> getMeasurementData() {
        return measurementData;
    }

    public void setMeasurementData(MeasurementsTable data) {
        measurementData.setValue(data);
    }
}
