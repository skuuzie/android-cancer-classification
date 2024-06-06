package com.dicoding.asclepius.data.remote.repository

import com.dicoding.asclepius.data.remote.ArticlesItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepository @Inject constructor(
    private val newsDataSource: NewsDataSource
) {
    val allNews: Flow<List<ArticlesItem>?> =
        newsDataSource.allNews
}