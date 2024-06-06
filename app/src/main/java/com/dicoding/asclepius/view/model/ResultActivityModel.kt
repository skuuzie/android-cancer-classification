package com.dicoding.asclepius.view.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.data.local.database.ClassificationHistory
import com.dicoding.asclepius.data.local.repository.ClassificationHistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResultActivityModel @Inject constructor(
    private val mClassificationHistoryRepository: ClassificationHistoryRepository
) : ViewModel() {

    fun insert(result: ClassificationHistory) {
        viewModelScope.launch {
            mClassificationHistoryRepository.insert(result)
        }
    }
}