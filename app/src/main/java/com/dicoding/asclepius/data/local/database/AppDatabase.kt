package com.dicoding.asclepius.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ClassificationHistory::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun classificationHistoryDao(): ClassificationHistoryDao
}