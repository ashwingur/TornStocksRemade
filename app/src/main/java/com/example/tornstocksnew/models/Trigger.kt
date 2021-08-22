package com.example.tornstocksnew.models

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.streamplate.streamplateandroidapp.ui.fragments.TRIGGER_PAGE_MODE
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize


@Entity(tableName = "triggers")
@Parcelize
data class Trigger(
    var trigger_type: TRIGGER_TYPE,
    val stock_id: Int,
    var stock_price: Float,
    val name: String,
    val acronym: String,
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var trigger_price: Float,
    var trigger_percentage: Float,
    var single_use: Boolean,
    var mode: TRIGGER_PAGE_MODE
) : Parcelable

enum class TRIGGER_TYPE {
    DEFAULT,
    PERCENTAGE
}
