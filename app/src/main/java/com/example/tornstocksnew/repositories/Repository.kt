package com.example.tornstocksnew.repositories

import android.util.Log
import com.example.tornstocksnew.models.StocksResponseObject
import com.example.tornstocksnew.network.ApiService
import javax.inject.Inject

class Repository @Inject constructor(private val apiService: ApiService) {

    suspend fun getStocks(key: String) : StocksResponseObject{
        val response = apiService.getStocks(key)
        return apiService.getStocks(key)
    }
}