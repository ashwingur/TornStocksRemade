package com.example.tornstocksnew.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.tornstocksnew.models.Trigger

@Dao
interface TriggerDao {

    @Query("SELECT * FROM triggers")
    fun getAllTriggers(): LiveData<List<Trigger>>

    @Query("SELECT * FROM triggers WHERE stock_id = :stock_id")
    suspend fun getTriggersByStockId(stock_id: Int): List<Trigger>

    @Insert
    suspend fun insert(trigger: Trigger)

    @Delete
    suspend fun delete(trigger: Trigger)

    @Update
    suspend fun updateTrigger(trigger: Trigger)
}