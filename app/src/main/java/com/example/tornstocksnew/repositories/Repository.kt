package com.example.tornstocksnew.repositories

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.tornstocksnew.database.LocalDatabase
import com.example.tornstocksnew.models.StocksResponseObject
import com.example.tornstocksnew.models.Trigger
import com.example.tornstocksnew.network.ApiService
import com.example.tornstocksnew.utils.Constants
import com.example.tornstocksnew.utils.StocksDisplayPreference
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

    suspend fun updateTrigger(trigger: Trigger){
        db.triggerDao().updateTrigger(trigger)
    }

    fun loadApiKey() {
        Constants.API_KEY = sharedPreferences.getString(Constants.STORED_KEY, null)
    }

    fun saveApiKey(apiKey: String) {
        if (apiKey.isEmpty()) {
            sharedPreferences.edit().putString(Constants.STORED_KEY, null).apply()
        } else {
            sharedPreferences.edit().putString(Constants.STORED_KEY, apiKey).apply()
        }

        Constants.API_KEY = apiKey
    }

    fun loadStocksDisplayPreference(){
        val pref = sharedPreferences.getInt(Constants.STORED_STOCKS_DISPLAY_PREFERENCE, 0)
        when (pref) {
            StocksDisplayPreference.DEFAULT.ordinal -> {
                Constants.STOCKS_DISPLAY_PREFERENCE = StocksDisplayPreference.DEFAULT
            }
            StocksDisplayPreference.CONCISE.ordinal -> {
                Constants.STOCKS_DISPLAY_PREFERENCE = StocksDisplayPreference.CONCISE
            }
            StocksDisplayPreference.DETAILED.ordinal -> {
                Constants.STOCKS_DISPLAY_PREFERENCE = StocksDisplayPreference.DETAILED
            }
        }
    }

    fun saveStocksDisplayPreference(pref: StocksDisplayPreference){
        Constants.STOCKS_DISPLAY_PREFERENCE = pref
        sharedPreferences.edit().putInt(Constants.STORED_STOCKS_DISPLAY_PREFERENCE, pref.ordinal).apply()
    }
}