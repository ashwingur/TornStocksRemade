package com.example.tornstocksnew.injection

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.tornstocksnew.database.LocalDatabase
import com.example.tornstocksnew.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalDataModule {

    @Provides
    fun provideSharedPrefs(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(Constants.SHARED_PREFS, Context.MODE_PRIVATE)
    }

    @Singleton
    @Provides
    fun provideRoomDatabase(@ApplicationContext context: Context): LocalDatabase{
        return Room.databaseBuilder(
            context,
            LocalDatabase::class.java,
            Constants.DATABASE_NAME
        ).build()
    }

    @Singleton
    @Provides
    fun provideTriggerDao(db: LocalDatabase) = db.triggerDao()

}