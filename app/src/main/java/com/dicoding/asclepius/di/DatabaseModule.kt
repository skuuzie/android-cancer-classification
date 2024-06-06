package com.dicoding.asclepius.di

import android.content.Context
import androidx.room.Room
import com.dicoding.asclepius.data.local.database.AppDatabase
import com.dicoding.asclepius.data.local.database.ClassificationHistoryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    private const val DB_NAME = "history.db"

    @Provides
    fun provideClassificationHistoryDao(database: AppDatabase): ClassificationHistoryDao {
        return database.classificationHistoryDao()
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext.applicationContext,
            AppDatabase::class.java,
            DB_NAME
        ).build()
    }
}