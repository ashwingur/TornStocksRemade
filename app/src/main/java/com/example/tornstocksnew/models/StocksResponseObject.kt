package com.example.tornstocksnew.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StocksResponseObject(
    val stocks: Map<String, Stock>,
    val error: Error
) : Parcelable