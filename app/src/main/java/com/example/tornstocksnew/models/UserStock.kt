package com.example.tornstocksnew.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserStock(
    val stock_id: Int,
    val total_shares: Long,
    val dividend: Dividend,
    val transactions: Map<String, Transaction>
) : Parcelable
