package com.example.tornstocksnew.repositories

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.tornstocksnew.database.LocalDatabase
import com.example.tornstocksnew.models.StocksResponseObject
import com.example.tornstocksnew.models.Trigger
import com.example.tornstocksnew.network.ApiService
import com.example.tornstocksnew.utils.Constants
import javax.inject.Inject

class Repository @Inject constructor(
    private val apiService: ApiService,
    private val db: LocalDatabase,
    private val sharedPreferences: SharedPreferences
) {

    suspend fun getStocks(key: String): StocksResponseObject {
        return apiService.getStocks(key)
    }

    fun getAllTriggers(): LiveData<List<Trigger>> {
        return db.triggerDao().getAllTriggers()
    }

    suspend fun insertTrigger(trigger: Trigger) {
        db.triggerDao().insert(trigger)
    }

    suspend fun deleteTrigger(trigger: Trigger) {
        db.triggerDao().delete(trigger)
    }

    fun loadApiKey() {
        Constants.API_KEY = sharedPreferences.getString(Constants.STORED_KEY, null)
    }
}