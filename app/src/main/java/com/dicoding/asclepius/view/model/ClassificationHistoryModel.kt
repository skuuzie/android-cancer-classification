package com.dicoding.asclepius.view.model

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.dicoding.asclepius.MyApplication
import com.dicoding.asclepius.data.local.database.ClassificationHistory
import com.dicoding.asclepius.data.local.repository.ClassificationHistoryRepository
import kotlinx.coroutines.launch
import java.io.File

class ClassificationHistoryModel(
    private val mClassificationHistoryRepository: ClassificationHistoryRepository
) : ViewModel() {

    val histories: LiveData<List<ClassificationHistory>> = liveData {
        emitSource(mClassificationHistoryRepository.getAllHistory().asLiveData())
    }

    fun delete(result: ClassificationHistory) {
        viewModelScope.launch {
            mClassificationHistoryRepository.delete(result)
            val imageFile = File(Uri.parse(result.imageUri).path!!)
            if (imageFile.exists()) imageFile.delete()
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val application = checkNotNull(extras[APPLICATION_KEY])
                val appContainer = (application as MyApplication).appContainer
                return ClassificationHistoryModel(
                    appContainer.classificationHistoryRepository
                ) as T
            }
        }
    }
}