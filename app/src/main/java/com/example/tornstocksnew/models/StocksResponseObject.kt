package com.example.tornstocksnew.models

data class StocksResponseObject(
    val stocks: Map<String, Stock>,
    val error: Error
)