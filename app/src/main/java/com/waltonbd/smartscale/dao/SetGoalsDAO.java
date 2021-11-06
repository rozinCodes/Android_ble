package com.waltonbd.smartscale.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.waltonbd.smartscale.entities.SetGoalsTable;

import java.util.List;

@Dao
public interface SetGoalsDAO {
    @Insert
    void insertGoal(SetGoalsTable goalsTable);

    @Update
    void updateGoal(SetGoalsTable goalsTable);

    @Delete
    void deleteGoal(SetGoalsTable goalsTable);

    @Query("DELETE FROM tbl_set_goal")
    void deleteAllGoals();

    @Query("DELETE FROM tbl_set_goal WHERE ID = :goalID")
    void deleteGoalsByID(int goalID);

    @Query("SELECT * FROM tbl_set_goal ORDER BY ID DESC")
    LiveData<List<SetGoalsTable>> getAllGoals();
}
