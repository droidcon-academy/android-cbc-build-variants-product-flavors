package com.droidcon.wealthbuddy.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [WealthItem::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun wealthItemDao(): WealthItemDao
}
