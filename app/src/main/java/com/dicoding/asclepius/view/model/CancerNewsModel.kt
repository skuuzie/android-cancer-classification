package com.dicoding.asclepius.view.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.data.remote.ArticlesItem
import com.dicoding.asclepius.data.remote.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CancerNewsModel @Inject constructor(
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
}