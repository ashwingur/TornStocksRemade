package com.example.tornstocksnew.viewmodels

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.tornstocksnew.database.LocalDatabase
import com.example.tornstocksnew.database.TriggerDao
import com.example.tornstocksnew.models.Trigger
import com.example.tornstocksnew.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    val sharedPreferences: SharedPreferences,
    val triggerDao: TriggerDao
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

    fun testDb() {
        GlobalScope.launch {
            withContext(Dispatchers.IO){
                Log.d("DATABASE", "${triggerDao.getAllTriggers()} ")
                triggerDao.insert(Trigger(1, "Test stock", "TES", 420))
                Log.d("DATABASE", "${triggerDao.getAllTriggers()} ")
            }
        }


    }
}