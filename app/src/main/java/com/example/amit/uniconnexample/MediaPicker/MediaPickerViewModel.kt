package com.example.amit.uniconnexample.MediaPicker

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory

/**
 * Created by Meera on 13,December,2019
 */
class MediaPickerViewModel(val application: Application, val mediapickerRepository: MediapickerRepository) : ViewModel() {
    var folders: MutableLiveData<List<MediaFolder>>
    init {
      folders = MutableLiveData()
    }
    fun getFolders(context: Context): LiveData<List<MediaFolder>> {
        mediapickerRepository.getFolders(context, folders)
        return folders
    }

    class Factory(private val application: Application, repository: MediapickerRepository) : NewInstanceFactory() {
        private val repository: MediapickerRepository
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return modelClass.cast(MediaPickerViewModel(application, repository))
        }

        init {
            this.repository = repository
        }
    }
}