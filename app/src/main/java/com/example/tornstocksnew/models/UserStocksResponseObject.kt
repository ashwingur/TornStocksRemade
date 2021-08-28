package com.example.tornstocksnew.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserStocksResponseObject(
    val stocks: Map<String, UserStock>,
    val error: Error
) : Parcelable
