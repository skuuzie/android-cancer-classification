package com.dicoding.asclepius.data.local.repository

import com.dicoding.asclepius.data.local.database.ClassificationHistory
import com.dicoding.asclepius.data.local.database.ClassificationHistoryDao
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ClassificationHistoryRepository(
    private val mClassificationHistoryDao: ClassificationHistoryDao
) {
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    fun getAllHistory(): Flow<List<ClassificationHistory>> {
        return mClassificationHistoryDao.getAllHistory()
    }

    fun insert(result: ClassificationHistory): Unit = executorService.execute {
        mClassificationHistoryDao.insert(result)
    }

    fun delete(result: ClassificationHistory): Unit = executorService.execute {
        mClassificationHistoryDao.delete(result)
    }
}