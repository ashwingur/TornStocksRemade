package com.example.tornstocksnew.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Benefit(
    val type: String,
    val frequency: Int,
    val requirement: Int,
    val description: String
) : Parcelable