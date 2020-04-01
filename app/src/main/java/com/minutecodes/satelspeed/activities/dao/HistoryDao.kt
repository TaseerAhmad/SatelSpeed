package com.minutecodes.satelspeed.activities.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.minutecodes.satelspeed.activities.model.SpeedHistory

@Dao
interface HistoryDao {
    @Insert
    fun insert(speedHistory: SpeedHistory)

    @Query("SELECT * FROM MAIN_TABLE ORDER BY historyId DESC")
    fun getAll(): LiveData<List<SpeedHistory>>

    @Update
    fun update(speedHistory: SpeedHistory)

    @Delete
    fun delete(speedHistory: SpeedHistory)

    @Query("DELETE FROM main_table")
    fun deleteAll()
}