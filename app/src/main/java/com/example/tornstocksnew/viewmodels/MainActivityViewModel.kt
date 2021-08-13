package com.example.tornstocksnew.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.example.tornstocksnew.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(val sharedPreferences: SharedPreferences) : ViewModel() {

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
}