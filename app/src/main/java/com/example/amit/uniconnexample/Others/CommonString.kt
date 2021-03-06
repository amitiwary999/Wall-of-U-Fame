package com.example.amit.uniconnexample.Others

/**
 * Created by Meera on 10,November,2019
 */
class CommonString {
    companion object{
        val MESSAGE_ID_CONSTANT: String = "message_id_constant"
        val USER_ID = "user_id"
        val base_url = "https://fwalls-dot-expinf.appspot.com/"
        const val USER_NAME = "user_name"
        const val USER_DP = "user_dp"
        const val PAYLOAD_ITEM_LIKE = "payload_item_like"
        const val PAYLOAD_ITEM_UNLIKE = "payload_item_unlike"
        const val USER_EMAIL = "user_email"
        const val DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
        const val MEDIA_SELECTED = "media_selected"
        const val MEDIA_UNSELECTED = "media_unselected"
        const val MEDIA = "media"
        const val MEDIA_PICKER_ACTIVITY = 123
        const val STORAGE_URL = "gs://expinf"
        const val THUMBNAIL_GENERATE = "thumbnail_generate"
        const val ORIGINAL_GENERATE = "original_generate"
        const val EXT_JPEG = "jpeg"
    }


    object MimeType{
        const val NONE ="none"
        const val IMAGE: String = "image"
        const val VIDEO: String = "video"
        const val AUDIO: String = "audio"
        const val DOCS: String = "application"
    }
}