package com.example.tornstocksnew.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Error(
    val code: Int,
    val warning: String
) : Parcelable