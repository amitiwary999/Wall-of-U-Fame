package com.example.amit.uniconnexample.rest.model

/**
 * Created by Meera on 09,November,2019
 */
data class PostModel(
        var date: String,
        var description: String,
        var mediaUrl: String?,
        var mediaThumbUrl: String?,
        var creatorId: String,
        var postId: String,
        var userDp :String?,
        var userName: String?,
        var isLiked: String = "0",
        var mimeType: String?=null
) {
}