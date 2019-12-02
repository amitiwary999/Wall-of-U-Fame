package com.example.amit.uniconnexample.Fragment.Home

/**
 * Created by Meera on 02,December,2019
 */
interface ItemOptionsClickListener {
    fun onPostLike(postId: String)
    fun onPostUnlike(postId :String)
    fun onChatClick(userId: String)
}