package com.example.tornstocksnew.models

data class Stock(
    val stock_id: Int,
    val name: String,
    val acronym: String,
    val market_cap: Long,
    val total_shares: Long,
    val benefit: Benefit
)
