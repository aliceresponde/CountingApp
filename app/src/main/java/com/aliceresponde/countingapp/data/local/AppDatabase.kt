package com.aliceresponde.countingapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CounterEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun counterDao(): CounterDao
}
