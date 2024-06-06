package com.dicoding.asclepius.data.remote.repository

import com.dicoding.asclepius.data.remote.ArticlesItem
import com.dicoding.asclepius.data.remote.retrofit.NewsApiConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NewsDataSource @Inject constructor() {
    val allNews: Flow<List<ArticlesItem>?> = flow {
        val _allNews = NewsApiConfig.getApiService().getAllNews("us", "health", "cancer")
        emit(_allNews.articles)
    }
}