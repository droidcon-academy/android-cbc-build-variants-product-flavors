package com.droidcon.wealthbuddy.data.local.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import com.droidcon.wealthbuddy.data.local.database.AppDatabase
import com.droidcon.wealthbuddy.data.local.database.WealthItemDao
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    fun provideWealthItemDao(appDatabase: AppDatabase): WealthItemDao {
        return appDatabase.wealthItemDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "WealthItem"
        ).build()
    }
}
