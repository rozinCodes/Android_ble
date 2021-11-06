package com.waltonbd.smartscale.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.waltonbd.smartscale.entities.UnitsTable;

import java.util.List;

@Dao
public interface UnitsDAO {
    @Insert
    void insertUnits(UnitsTable unitsTable);

    @Update
    void updateUnits(UnitsTable unitsTable);

    @Delete
    void deleteUnits(UnitsTable unitsTable);

    @Query("DELETE FROM tbl_units")
    void deleteAllUnits();

    @Query("DELETE FROM tbl_units WHERE ID = :unitID")
    void deleteUnitsByID(int unitID);

    @Query("SELECT * FROM tbl_units ORDER BY ID DESC")
    LiveData<List<UnitsTable>> getAllUnits();
}
