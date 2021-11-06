package com.waltonbd.smartscale.repositories;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;

import com.waltonbd.smartscale.dao.UnassignedDataDAO;
import com.waltonbd.smartscale.database.SmartScaleDatabase;
import com.waltonbd.smartscale.entities.UnassignedDataTable;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UnassignedDataRepository {
    private UnassignedDataDAO unassignedDataDAO;
    private LiveData<List<UnassignedDataTable>> allMeasurements;

    public UnassignedDataRepository(Application application) {
        SmartScaleDatabase database = SmartScaleDatabase.getInstance(application);
        unassignedDataDAO = database.unassignedDataDAO();
        allMeasurements = unassignedDataDAO.getAllUnassignedData();

    }

    public void insertUnassignedData(UnassignedDataTable unassignedDataTable) {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            //Background work here
            unassignedDataDAO.insertUnassignedData(unassignedDataTable);
//            handler.post(() -> {
//                //UI Thread work here
//            });
        });

    }

    public void updateUnassignedData(UnassignedDataTable unassignedDataTable) {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            //Background work here
            unassignedDataDAO.updateUnassignedData(unassignedDataTable);
//            handler.post(() -> {
//                //UI Thread work here
//            });
        });

    }

    public void deleteUnassignedData(UnassignedDataTable unassignedDataTable) {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            //Background work here
            unassignedDataDAO.deleteUnassignedData(unassignedDataTable);
//            handler.post(() -> {
//                //UI Thread work here
//            });
        });
    }
}
