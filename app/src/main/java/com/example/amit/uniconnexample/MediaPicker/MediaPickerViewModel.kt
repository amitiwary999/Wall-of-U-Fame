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
class MediaPickerViewModel() : ViewModel() {
    var folders: MutableLiveData<List<MediaFolder>>
    val selectedMedia: HashMap<String, Media> = HashMap()
    var bucketMedia: MutableLiveData<List<Media>>
    val mediapickerRepository = MediapickerRepository()
    init {
        folders = MutableLiveData()
        bucketMedia = MutableLiveData()
    }
    fun getFolders(context: Context): LiveData<List<MediaFolder>> {
        mediapickerRepository.getFolders(context, folders)
        return folders
    }

    fun setSelectedMedia(mediaSelected: Media){
        mediaSelected.id?.let {
            selectedMedia.put(it, mediaSelected)
        }
    }

    fun removeSelectedMedia(media: Media){
        media.id?.let {
            selectedMedia.remove(it)
        }
    }

    fun getBucketMedia(context: Context, bucketId: String): LiveData<List<Media>>{
        mediapickerRepository.getMediaInBucket(context, bucketId, bucketMedia)
        return bucketMedia
    }
}