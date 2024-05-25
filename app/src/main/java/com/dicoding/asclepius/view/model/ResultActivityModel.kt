package com.dicoding.asclepius.view.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.dicoding.asclepius.MyApplication
import com.dicoding.asclepius.data.local.database.ClassificationHistory
import com.dicoding.asclepius.data.local.repository.ClassificationHistoryRepository
import kotlinx.coroutines.launch

class ResultActivityModel(
    private val mClassificationHistoryRepository: ClassificationHistoryRepository
) : ViewModel() {

    fun insert(result: ClassificationHistory) {
        viewModelScope.launch {
            mClassificationHistoryRepository.insert(result)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val application =
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                val appContainer = (application as MyApplication).appContainer
                return ResultActivityModel(
                    appContainer.classificationHistoryRepository
                ) as T
            }
        }
    }
}