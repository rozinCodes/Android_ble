package com.waltonbd.smartscale.database;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.waltonbd.smartscale.dao.DeviceManagementDAO;
import com.waltonbd.smartscale.dao.MeasurementsDAO;
import com.waltonbd.smartscale.dao.SetGoalsDAO;
import com.waltonbd.smartscale.dao.UnassignedDataDAO;
import com.waltonbd.smartscale.dao.UnitsDAO;
import com.waltonbd.smartscale.dao.UsersDAO;
import com.waltonbd.smartscale.entities.DeviceManagementTable;
import com.waltonbd.smartscale.entities.GuestUsersTable;
import com.waltonbd.smartscale.entities.MeasurementsTable;
import com.waltonbd.smartscale.entities.SetGoalsTable;
import com.waltonbd.smartscale.entities.UnassignedDataTable;
import com.waltonbd.smartscale.entities.UnitsTable;
import com.waltonbd.smartscale.entities.UsersTable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {DeviceManagementTable.class, GuestUsersTable.class, MeasurementsTable.class, SetGoalsTable.class, UnassignedDataTable.class, UnitsTable.class, UsersTable.class}, version = 1, exportSchema = false)
public abstract class SmartScaleDatabase extends RoomDatabase {

    private static final RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            // Auto insert dummy data
            //PopulateDbAsyncTask(instance);
        }
    };

    private static volatile SmartScaleDatabase instance;

    public static SmartScaleDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (SmartScaleDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            SmartScaleDatabase.class, "smart_scale_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(roomCallback)
                            .build();
                }
            }
        }
        return instance;
    }

    public abstract UsersDAO usersDAO();

    public abstract DeviceManagementDAO deviceManagementDAO();

    public abstract MeasurementsDAO measurementsDAO();

    public abstract SetGoalsDAO setGoalsDAO();

    public abstract UnassignedDataDAO unassignedDataDAO();

    public abstract UnitsDAO unitsDAO();

    private static void PopulateDbAsyncTask(SmartScaleDatabase db) {
        UsersDAO usersDAO = db.usersDAO();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        UsersTable usersTable = new UsersTable();
        usersTable.setId(1);
        usersTable.setUserName("");
        usersTable.setFullName("");
        usersTable.setGender("");
        usersTable.setImage("");
        usersTable.setBirthDate("");
        usersTable.setWeightUnit("");
        usersTable.setHeight("");
        usersTable.setHeight("");
        usersTable.setWeight("");
        usersTable.setPhone("");
        usersTable.setAthleteMode(false);

        executor.execute(() -> {
            //Background work here
            usersDAO.insertUser(usersTable);
            handler.post(() -> {
                Log.e("Populated DB", "Created");
                //UI Thread work here
            });
        });
    }
}
