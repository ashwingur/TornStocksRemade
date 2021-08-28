package com.example.tornstocksnew.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Dividend(
    val ready: Int,
    val increment: Int,
    val progress: Int,
    val frequency: Int
) : Parcelable
