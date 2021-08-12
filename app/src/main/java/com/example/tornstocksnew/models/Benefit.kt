package com.example.tornstocksnew.models

data class Benefit(
    val type: String,
    val frequency: Int,
    val requirement: Int,
    val description: String
)