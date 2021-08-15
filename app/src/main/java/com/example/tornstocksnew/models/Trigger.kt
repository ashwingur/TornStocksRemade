package com.example.tornstocksnew.models

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize


@Entity(tableName = "triggers")
@Parcelize
data class Trigger(
    val stock_id: Int,
    val name: String,
    val acronym: String,
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var trigger_price: Float,
    var single_use: Boolean
) : Parcelable
