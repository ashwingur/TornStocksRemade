package com.example.tornstocksnew.utils

class Constants {

    companion object {
        const val BASE_URL = "https://api.torn.com"
        const val TEST_API = "0QvdYr3CeeLjimul"
        var API_KEY : String? = null

        const val SHARED_PREFS = "SHARED_PREFS"
        const val STORED_KEY = "STORED_KEY"
        const val STORED_STOCKS_DISPLAY_PREFERENCE = "STORED_STOCKS_DISPLAY_PREFERENCE"
        var STOCKS_DISPLAY_PREFERENCE = StocksDisplayPreference.DEFAULT

        const val DATABASE_NAME = "LOCAL_DATABASE"

        const val PARCEL_STOCK = "PARCEL_STOCK"
        const val PARCEL_TRIGGER = "PARCEL TRIGGER"
    }
}
