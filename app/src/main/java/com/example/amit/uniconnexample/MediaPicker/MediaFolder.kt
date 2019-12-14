package com.example.amit.uniconnexample.MediaPicker

/**
 * Created by Meera on 14,December,2019
 */
data class MediaFolder(
        val thumbnailUri: String,
        val title: String?,
        val itemCount: Int,
        val bucketId: String,
        val folderType: FolderType
) {
    enum class FolderType {
        NORMAL, CAMERA
    }
}