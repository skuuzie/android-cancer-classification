package com.dicoding.asclepius.view.model

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.data.local.database.ClassificationHistory
import com.dicoding.asclepius.data.local.repository.ClassificationHistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ClassificationHistoryModel @Inject constructor(
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
}