package com.waltonbd.smartscale.repositories;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.waltonbd.smartscale.dao.UsersDAO;
import com.waltonbd.smartscale.database.SmartScaleDatabase;
import com.waltonbd.smartscale.entities.UsersTable;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UsersRepository {

    private static final String TAG = "UsersRepository";

    private final UsersDAO usersDAO;
    private final LiveData<List<UsersTable>> allUsers;
    private final MutableLiveData<UsersTable> mutableUser = new MutableLiveData<>();

    public UsersRepository(Application application) {
        SmartScaleDatabase database = SmartScaleDatabase.getInstance(application);
        usersDAO = database.usersDAO();
        allUsers = usersDAO.getAllUsers();
    }

    public void insertUser(UsersTable usersTable) {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            //Background work here
            usersDAO.insertUser(usersTable);
            handler.post(() -> {
                Log.d(TAG, "Data Inserted");
            });
        });
    }

    public void updateUser(UsersTable usersTable) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            //Background work here
            usersDAO.updateUser(usersTable);
            handler.post(() -> {
                Log.d(TAG, "Data Updated");
            });
        });
    }

    public void deleteAllUser() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        //Background work here
        executor.execute(usersDAO::deleteAllUsers);
    }

    public void resetSequence() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        //Background work here
        executor.execute(usersDAO::resetSequence);
    }

    public UsersTable getUser(long id) {
        return usersDAO.getUser(id);
    }

    public LiveData<UsersTable> getMutableUser(long id) {
        return usersDAO.getMutableUser(id);
    }

    public UsersTable getMainUser() {
        return usersDAO.getMainUser();
    }

    public void deleteUser(UsersTable usersTable) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            //Background work here
            usersDAO.deleteUser(usersTable);
        });
    }

    public LiveData<List<UsersTable>> getAllUsers() {
        return allUsers;
    }

    public int getLastUserId() {
        return usersDAO.getLastUserId();
    }

    public LiveData<UsersTable> getSelectedUser() {
        return mutableUser;
    }

    public void setSelectedUser(UsersTable user) {
        mutableUser.setValue(user);
    }
}