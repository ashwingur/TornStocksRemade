package com.example.tornstocksnew.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.tornstocksnew.ui.fragments.TRIGGER_PAGE_MODE
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
) : Parcelable {

    companion object {
        val AlphabeticalAscendingComparator = Comparator<Trigger> { t1, t2 ->
            t1.acronym.compareTo(t2.acronym)
        }
    }
}

enum class TRIGGER_TYPE {
    DEFAULT,
    PERCENTAGE
}
