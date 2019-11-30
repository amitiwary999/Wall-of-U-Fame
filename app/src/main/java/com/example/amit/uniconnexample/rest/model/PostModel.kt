package com.example.amit.uniconnexample.rest.model

/**
 * Created by Meera on 09,November,2019
 */
data class PostModel(
        var date: String,
        var desc: String,
        var imageUrl: String,
        var creatorId: String,
        var postId: String,
        var creatorDp :String,
        var creatorName: String,
        var like : Int,
        var unlike: Int
) {
}