package com.example.tornstocksnew.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.tornstocksnew.models.Trigger

@Dao
interface TriggerDao {

    @Query("SELECT * FROM triggers")
    suspend fun getAllTriggers(): List<Trigger>

    @Query("SELECT * FROM triggers WHERE stock_id = :stock_id")
    fun getTriggersByStockId(stock_id: Int): List<Trigger>

    @Insert
    suspend fun insert(trigger: Trigger)

    @Delete
    suspend fun delete(trigger: Trigger)
}