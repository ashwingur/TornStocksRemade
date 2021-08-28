package com.example.tornstocksnew.viewmodels

import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.*
import com.example.tornstocksnew.database.TriggerDao
import com.example.tornstocksnew.models.Stock
import com.example.tornstocksnew.models.StocksResponseObject
import com.example.tornstocksnew.models.Trigger
import com.example.tornstocksnew.repositories.Repository
import com.example.tornstocksnew.utils.Constants
import com.example.tornstocksnew.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import java.lang.Runnable
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    val sharedPreferences: SharedPreferences,
    val repository: Repository
) : ViewModel() {

    val refreshStockDelay = 30000L
    var cachedStocks: MutableList<Stock> = mutableListOf()
    val refreshStockBool = MutableLiveData(true)

    fun refreshStockBoolTask() {
        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(object : Runnable {
            override fun run() {
                refreshStockBool.value = true
                mainHandler.postDelayed(this, refreshStockDelay)

            }
        })
    }

    fun getCachedStockById(stockId: Int): Stock? {
        for (stock in cachedStocks) {
            if (stock.stock_id == stockId) {
                return stock
            }
        }
        return null
    }

    fun loadApiKey() {
        Constants.API_KEY = sharedPreferences.getString(Constants.STORED_KEY, null)
    }

    fun saveApiKey(apiKey: String) {
        repository.saveApiKey(apiKey)
    }

    fun insertTrigger(trigger: Trigger) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.insertTrigger(trigger)
            }
        }
    }

    fun updateTrigger(trigger: Trigger) {
        viewModelScope.launch {
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