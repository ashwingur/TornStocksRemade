package com.example.tornstocksnew.models

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class Stock(
    val stock_id: Int,
    val name: String,
    val acronym: String,
    val current_price: Float,
    val market_cap: Long,
    val total_shares: Long,
    val benefit: Benefit
) : Parcelable {

    companion object {
        val PriceDescendingComparator = Comparator<Stock> { s1, s2 ->
            (s2.current_price - s1.current_price).toInt()
        }
    }

}
