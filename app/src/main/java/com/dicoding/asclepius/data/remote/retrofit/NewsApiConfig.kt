package com.dicoding.asclepius.data.remote.retrofit

import com.dicoding.asclepius.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NewsApiConfig {

    companion object {
        fun getApiService(): NewsApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.NEWS_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(NewsApiService::class.java)
        }
    }
}