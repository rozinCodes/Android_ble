package com.waltonbd.smartscale.repositories;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;

import com.waltonbd.smartscale.dao.SetGoalsDAO;
import com.waltonbd.smartscale.database.SmartScaleDatabase;
import com.waltonbd.smartscale.entities.SetGoalsTable;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SetGoalsRepository {
    private SetGoalsDAO setGoalsDAO;
    private LiveData<List<SetGoalsTable>> allMeasurements;

    public SetGoalsRepository(Application application) {
        SmartScaleDatabase database = SmartScaleDatabase.getInstance(application);
        setGoalsDAO = database.setGoalsDAO();
        allMeasurements = setGoalsDAO.getAllGoals();

    }

    public void insertGoals(SetGoalsTable goalsTable) {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            //Background work here
            setGoalsDAO.insertGoal(goalsTable);
//            handler.post(() -> {
//                //UI Thread work here
//            });
        });

    }

    public void updateGoals(SetGoalsTable goalsTable) {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            //Background work here
            setGoalsDAO.updateGoal(goalsTable);
//            handler.post(() -> {
//                //UI Thread work here
//            });
        });

    }

    public void deleteGoals(SetGoalsTable goalsTable) {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            //Background work here
            setGoalsDAO.deleteGoal(goalsTable);
//            handler.post(() -> {
//                //UI Thread work here
//            });
        });
    }
}
