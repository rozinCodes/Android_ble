package com.waltonbd.smartscale.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.waltonbd.smartscale.entities.UsersTable;

import java.util.List;

@Dao
public interface UsersDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(UsersTable usersTable);

    @Update
    void updateUser(UsersTable usersTable);

    @Delete
    void deleteUser(UsersTable usersTable);

    @Query("DELETE FROM tbl_users")
    void deleteAllUsers();

    @Query("UPDATE sqlite_sequence SET seq = 0 WHERE name = 'tbl_users'")
    void resetSequence();

    @Query("SELECT * FROM tbl_users WHERE id = :id")
    UsersTable getUser(long id);

    @Query("SELECT * FROM tbl_users WHERE id = :id")
    LiveData<UsersTable> getMutableUser(long id);

    @Query("SELECT * FROM tbl_users WHERE guestId = 0")
    UsersTable getMainUser();

    @Query("DELETE FROM tbl_users WHERE id = :id")
    void deleteUserByID(int id);

    @Query("SELECT * FROM tbl_users ORDER BY ID ASC")
    LiveData<List<UsersTable>> getAllUsers();

    @Query("SELECT id FROM tbl_users ORDER BY id DESC")
    int getLastUserId();
}
