package com.example.amit.uniconnexample.MediaPicker

/**
 * Created by Meera on 15,December,2019
 */
interface MediaSelected {
    fun onMediaFolderSelected(mediaFolder: MediaFolder)
    fun onMediaSelected(media: ChosenMediaFile)
}