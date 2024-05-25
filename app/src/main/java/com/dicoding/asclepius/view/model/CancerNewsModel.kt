package com.dicoding.asclepius.view.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.dicoding.asclepius.MyApplication
import com.dicoding.asclepius.data.remote.ArticlesItem
import com.dicoding.asclepius.data.remote.repository.NewsRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class CancerNewsModel(
    private val mNewsRepository: NewsRepository
) : ViewModel() {

    private var _news: MutableLiveData<List<ArticlesItem>?> = MutableLiveData<List<ArticlesItem>?>()

    private val _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isFailure = MutableLiveData(false)
    val isFailure: LiveData<Boolean> = _isFailure

    fun fetchNews(): LiveData<List<ArticlesItem>?> {
        viewModelScope.launch {
            _isFailure.value = false
            _isLoading.value = true
            mNewsRepository.allNews
                .catch { e ->
                    _isFailure.value = true
                }
                .collect {
                    _news.value = it
                }
            _isLoading.value = false
        }
        return _news
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val application =
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                val appContainer = (application as MyApplication).appContainer
                return CancerNewsModel(
                    appContainer.newsRepository
                ) as T
            }
        }
    }
}