package com.dicoding.asclepius

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.dicoding.asclepius.data.local.database.AppDatabase
import com.dicoding.asclepius.data.local.repository.ClassificationHistoryRepository
import com.dicoding.asclepius.data.remote.repository.NewsDataSource
import com.dicoding.asclepius.data.remote.repository.NewsRepository

class AppContainer {

    private val mDatabase = getDatabase(MyApplication.instance)
    private val mClassificationHistoryDao = mDatabase.classificationHistoryDao()
    val classificationHistoryRepository = ClassificationHistoryRepository(mClassificationHistoryDao)

    private val newsDataSource = NewsDataSource()
    val newsRepository = NewsRepository(newsDataSource)

    companion object {
        private const val DB_NAME = "history.db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): AppDatabase {
            if (INSTANCE == null) {
                Log.d("AppContainer", "history.db initialized!")
                synchronized(AppDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        DB_NAME
                    ).build()
                }
            }
            return INSTANCE as AppDatabase
        }
    }
}