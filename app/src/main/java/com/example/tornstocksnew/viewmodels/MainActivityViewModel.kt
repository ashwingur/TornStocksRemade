package com.example.tornstocksnew.viewmodels

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.*
import com.example.tornstocksnew.database.TriggerDao
import com.example.tornstocksnew.models.Stock
import com.example.tornstocksnew.models.Trigger
import com.example.tornstocksnew.repositories.Repository
import com.example.tornstocksnew.utils.Constants
import com.example.tornstocksnew.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    val sharedPreferences: SharedPreferences,
    val repository: Repository
) : ViewModel() {

    var cachedStocks: MutableList<Stock> = mutableListOf()

    fun getCachedStockById(stockId: Int): Stock? {
        for (stock in cachedStocks){
            if (stock.stock_id == stockId){
                return stock
            }
        }
        return null
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

    fun insertTrigger(trigger: Trigger) {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                repository.insertTrigger(trigger)
            }
        }
    }

    fun updateTrigger(trigger: Trigger) {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                repository.updateTrigger(trigger)
            }
        }
    }


    fun getStocks(key: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(data = repository.getStocks(key)))
        } catch (exception: Exception) {
            emit(Resource.error(exception.message ?: "Error Occurred!", data = null))
        }
    }
}