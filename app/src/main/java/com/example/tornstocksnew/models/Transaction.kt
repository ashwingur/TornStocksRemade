package com.example.tornstocksnew.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Transaction(
    val shares: Long,
    val bought_price: Float,
    val time_bought: Long
): Parcelable
