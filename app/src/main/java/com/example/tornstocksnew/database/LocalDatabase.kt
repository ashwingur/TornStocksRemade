package com.example.tornstocksnew.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.tornstocksnew.models.Trigger

@Database(entities = arrayOf(Trigger::class), version = 1)
abstract class LocalDatabase() : RoomDatabase() {
    abstract fun triggerDao(): TriggerDao
}