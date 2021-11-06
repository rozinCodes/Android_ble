package com.waltonbd.smartscale.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.waltonbd.smartscale.entities.UsersTable;
import com.waltonbd.smartscale.repositories.UsersRepository;

import java.util.List;

public class UserViewModel extends AndroidViewModel {

    private final UsersRepository repository;
    private final LiveData<List<UsersTable>> allUsers;

    public UserViewModel(@NonNull Application application) {
        super(application);
        repository = new UsersRepository(application);
        allUsers = repository.getAllUsers();
    }

    public void insert(UsersTable usersTable) {
        repository.insertUser(usersTable);
    }

    public void updateUser(UsersTable usersTable) {
        repository.updateUser(usersTable);
    }

    public void deleteAllUsers() {
        repository.deleteAllUser();
    }

    public void deleteUser(UsersTable usersTable) {
        repository.deleteUser(usersTable);
    }

    public void resetSequence() {
        repository.resetSequence();
    }

    public UsersTable getUser(long id) {
        return repository.getUser(id);
    }

    public LiveData<UsersTable> getMutableUser(long id) {
        return repository.getMutableUser(id);
    }

    public UsersTable getMainUser() {
        return repository.getMainUser();
    }

    public LiveData<List<UsersTable>> getAllUsers() {
        return allUsers;
    }

    public int getLastUserId() {
        return repository.getLastUserId();
    }

    public LiveData<UsersTable> getSelectedUser() {
        return repository.getSelectedUser();
    }

    public void setSelectedUser(UsersTable user) {
        repository.setSelectedUser(user);
    }

}