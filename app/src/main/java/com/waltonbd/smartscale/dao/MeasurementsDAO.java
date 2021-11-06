package com.waltonbd.smartscale.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.waltonbd.smartscale.entities.MeasurementsTable;

import java.util.List;

@Dao
public interface MeasurementsDAO {
    @Insert
    void insertMeasurement(MeasurementsTable measurementsTable);

    @Update
    void updateMeasurement(MeasurementsTable measurementsTable);

    @Delete
    void deleteMeasurement(MeasurementsTable measurementsTable);

    @Query("DELETE FROM tbl_measurements WHERE userId = :userId")
    void deleteMeasurementByUserId(long userId);

    @Query("DELETE FROM tbl_measurements")
    void deleteAllMeasurement();

    @Query("UPDATE sqlite_sequence SET seq = 0 WHERE name = 'tbl_measurements'")
    void resetSequence();

    @Query("SELECT * FROM tbl_measurements WHERE userId = :userId ORDER BY id ASC")
    LiveData<List<MeasurementsTable>> getAllMeasurements(long userId);

    @Query("SELECT * FROM tbl_measurements WHERE userId = :userId AND createDateTime = :date ORDER BY id ASC")
    List<MeasurementsTable> getMeasurementsByDate(long userId, String date);

    @Query("SELECT * FROM tbl_measurements WHERE userId = :userId ORDER BY id DESC")
    LiveData<List<MeasurementsTable>> getLastMeasurement(long userId);

    @Query("SELECT m1.* FROM tbl_measurements m1 LEFT JOIN tbl_measurements m2" +
            " ON (m1.createDateTime = m2.createDateTime AND m1.id < m2.id)" +
            " WHERE m2.id IS NULL ORDER BY m1.createDateTime ASC")
    List<MeasurementsTable> getLastMeasurementsGroup();

    @Query("SELECT m1.* FROM tbl_measurements m1 LEFT JOIN tbl_measurements m2" +
            " ON (m1.createDateTime = m2.createDateTime AND m1.id < m2.id)" +
            " WHERE m2.id IS NULL AND m1.userId = :userId ORDER BY m1.createDateTime ASC")
    List<MeasurementsTable> getLastMeasurementsGroupByUserId(int userId);


    /**
     * https://stackoverflow.com/questions/10504218/query-last-day-last-week-last-month-sqlite
     */
    @Query("SELECT m1.* FROM tbl_measurements m1 LEFT JOIN tbl_measurements m2 ON (m1.createDateTime = m2.createDateTime AND m1.id < m2.id" +
            " AND m1.userId = m2.userId) WHERE m2.id IS NULL AND m1.createDateTime >= DATETIME('now', '-7 days')" +
            " AND m1.userId = :userId ORDER BY m1.createDateTime ASC")
    List<MeasurementsTable> getLastMeasurementsGroupWeekly(long userId);

    @Query("SELECT m1.* FROM tbl_measurements m1 LEFT JOIN tbl_measurements m2 ON (m1.createDateTime = m2.createDateTime AND m1.id < m2.id" +
            " AND m1.userId = m2.userId) WHERE m2.id IS NULL AND m1.createDateTime >= DATETIME('now', '-1 month')" +
            " AND m1.userId = :userId ORDER BY m1.createDateTime ASC")
    List<MeasurementsTable> getLastMeasurementsGroupMonthly(long userId);

    @Query("SELECT m1.* FROM tbl_measurements m1 LEFT JOIN tbl_measurements m2 ON (m1.createDateTime = m2.createDateTime AND m1.id < m2.id" +
            " AND m1.userId = m2.userId) WHERE m2.id IS NULL AND m1.createDateTime >= DATETIME('now', '-1 year')" +
            " AND m1.userId = :userId ORDER BY m1.createDateTime ASC")
    List<MeasurementsTable> getLastMeasurementsGroupYearly(long userId);

}
