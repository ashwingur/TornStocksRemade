package com.example.tornstocksnew.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.tornstocksnew.database.LocalDatabase
import com.example.tornstocksnew.models.StocksResponseObject
import com.example.tornstocksnew.models.Trigger
import com.example.tornstocksnew.network.ApiService
import javax.inject.Inject

class Repository @Inject constructor(private val apiService: ApiService, private val db: LocalDatabase) {

    suspend fun getStocks(key: String) : StocksResponseObject{
        return apiService.getStocks(key)
    }

    fun getAllTriggers() : LiveData<List<Trigger>> {
        return db.triggerDao().getAllTriggers()
    }

    suspend fun insertTrigger(trigger: Trigger) {
        db.triggerDao().insert(trigger)
    }

    suspend fun deleteTrigger(trigger: Trigger){
        db.triggerDao().delete(trigger)
    }
}