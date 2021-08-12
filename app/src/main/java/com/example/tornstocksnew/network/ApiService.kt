package com.example.tornstocksnew.network

import com.example.tornstocksnew.models.StocksResponseObject
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    // https://api.torn.com/torn/?selections=stocks&key={API_KEY}
    @GET("/torn/?selections=stocks")
    suspend fun getStocks(
        @Query("key") key: String
    ) : StocksResponseObject
}