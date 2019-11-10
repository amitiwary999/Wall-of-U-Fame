package com.example.amit.uniconnexample.rest

import com.example.amit.uniconnexample.PostBlogModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * Created by Meera on 09,November,2019
 */
interface NetworkRepository {
    @POST("setPost")
    fun sendPost(@Header("Authorization") authToken:String, @Body postBlogModel: PostBlogModel) : Call<String>
}