package com.example.tornstocksnew.viewmodels

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.tornstocksnew.database.TriggerDao
import com.example.tornstocksnew.models.Trigger
import com.example.tornstocksnew.repositories.Repository
import com.example.tornstocksnew.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    val sharedPreferences: SharedPreferences,
    val triggerDao: TriggerDao,
    val repository: Repository
) : ViewModel() {

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

    fun insertTrigger(trigger: Trigger){
        GlobalScope.launch {
            withContext(Dispatchers.IO){
                triggerDao.insert(trigger)
            }
        }
    }

    fun getAllTriggers() = liveData<List<Trigger>>(Dispatchers.IO) {
        emit(repository.getAllTriggers())
    }

    fun testDb() {
        GlobalScope.launch {
            withContext(Dispatchers.IO){
                Log.d("DATABASE", "${triggerDao.getAllTriggers()} ")
            }
        }


    }
}