package com.waltonbd.smartscale.repositories;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;

import com.waltonbd.smartscale.dao.UnitsDAO;
import com.waltonbd.smartscale.database.SmartScaleDatabase;
import com.waltonbd.smartscale.entities.UnitsTable;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UnitsRepository {
    private UnitsDAO unitsDAO;
    private LiveData<List<UnitsTable>> allMeasurements;

    public UnitsRepository(Application application) {
        SmartScaleDatabase database = SmartScaleDatabase.getInstance(application);
        unitsDAO = database.unitsDAO();
        allMeasurements = unitsDAO.getAllUnits();

    }

    public void insertUnits(UnitsTable unassignedDataTable) {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            //Background work here
            unitsDAO.insertUnits(unassignedDataTable);
//            handler.post(() -> {
//                //UI Thread work here
//            });
        });

    }

    public void updateUnits(UnitsTable unassignedDataTable) {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            //Background work here
            unitsDAO.updateUnits(unassignedDataTable);
//            handler.post(() -> {
//                //UI Thread work here
//            });
        });

    }

    public void deleteUnits(UnitsTable unassignedDataTable) {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            //Background work here
            unitsDAO.deleteUnits(unassignedDataTable);
//            handler.post(() -> {
//                //UI Thread work here
//            });
        });
    }
}
