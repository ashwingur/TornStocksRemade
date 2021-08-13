package com.example.tornstocksnew.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Entity(tableName = "triggers")
@Parcelize
data class Trigger(
    val stock_id: Int,
    val name: String,
    val acronym: String,
    var trigger_price: Int
) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
