package com.example.tornstocksnew.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.tornstocksnew.repositories.Repository
import com.example.tornstocksnew.utils.Constants
import com.example.tornstocksnew.utils.Resource
import com.example.tornstocksnew.utils.StocksDisplayPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class StocksViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    fun loadStocksDisplayPreference(){
        repository.loadStocksDisplayPreference()
    }

    fun saveStocksDisplayPreference(pref: StocksDisplayPreference){
        repository.saveStocksDisplayPreference(pref)
    }

}