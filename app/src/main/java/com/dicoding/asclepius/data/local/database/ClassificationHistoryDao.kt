package com.dicoding.asclepius.data.local.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ClassificationHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(result: ClassificationHistory)

    @Delete
    fun delete(result: ClassificationHistory)

    @Query("SELECT * FROM classification_history")
    fun getAllHistory(): Flow<List<ClassificationHistory>>
}