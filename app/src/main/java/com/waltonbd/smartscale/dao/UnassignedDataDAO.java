package com.waltonbd.smartscale.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.waltonbd.smartscale.entities.UnassignedDataTable;

import java.util.List;

@Dao
public interface UnassignedDataDAO {
    @Insert
    void insertUnassignedData(UnassignedDataTable unassignedDataTable);

    @Update
    void updateUnassignedData(UnassignedDataTable unassignedDataTable);

    @Delete
    void deleteUnassignedData(UnassignedDataTable unassignedDataTable);

    @Query("DELETE FROM tbl_unassigned_data")
    void deleteAllUnassignedData();

    @Query("DELETE FROM tbl_unassigned_data WHERE ID = :unassignedDataID")
    void deleteUnassignedDataByID(int unassignedDataID);

    @Query("SELECT * FROM tbl_unassigned_data ORDER BY ID DESC")
    LiveData<List<UnassignedDataTable>> getAllUnassignedData();
}
