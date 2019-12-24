package com.example.amit.uniconnexample.MediaPicker

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Created by Meera on 13,December,2019
 */
class MediaPickerViewModel() : ViewModel() {
    var folders: MutableLiveData<List<MediaFolder>>
    val selectedMedia: HashMap<String, ChosenMediaFile> = HashMap()
    var bucketMedia: MutableLiveData<List<ChosenMediaFile>>
    val mediapickerRepository = MediapickerRepository()
    init {
        folders = MutableLiveData()
        bucketMedia = MutableLiveData()
    }
    fun getFolders(context: Context): LiveData<List<MediaFolder>> {
        mediapickerRepository.getFolders(context, folders)
        return folders
    }

    fun setSelectedMedia(mediaSelected: ChosenMediaFile){
        mediaSelected.id?.let {
            selectedMedia.put(it, mediaSelected)
        }
    }

    fun removeSelectedMedia(media: ChosenMediaFile){
        media.id?.let {
            selectedMedia.remove(it)
        }
    }

    fun getBucketMedia(context: Context, bucketId: String): LiveData<List<ChosenMediaFile>>{
        mediapickerRepository.getMediaInBucket(context, bucketId, bucketMedia)
        return bucketMedia
    }
}