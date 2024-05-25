package com.dicoding.asclepius.data.remote.repository

import com.dicoding.asclepius.data.remote.ArticlesItem
import kotlinx.coroutines.flow.Flow

class NewsRepository(
    private val newsDataSource: NewsDataSource
) {
    val allNews: Flow<List<ArticlesItem>?> =
        newsDataSource.allNews
}