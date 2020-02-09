package com.example.stepcounter.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface HistoryDao {

    @Query("SELECT * FROM history ORDER BY day DESC")
    LiveData<List<HistoryEntity>> getHistory();

    @Insert
    void setHistory(HistoryEntity historyEntity);

    @Delete
    void deleteHistory(HistoryEntity historyEntity);

    @Update
    void updateHistory(HistoryEntity historyEntity);

    @Query("DELETE FROM history")
    void deleteAllHistory();

    @Query("SELECT * FROM history WHERE day = :day LIMIT 1")
    LiveData<HistoryEntity> getToday(float day);

}
