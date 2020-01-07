package com.example.amit.uniconnexample.rest

import com.example.amit.uniconnexample.Fragment.Home.GetPostRequestModel
import com.example.amit.uniconnexample.PostBlogModel
import com.example.amit.uniconnexample.rest.model.ModelResponseMessage
import com.example.amit.uniconnexample.rest.model.PostLikeModel
import com.example.amit.uniconnexample.rest.model.PostModel
import com.example.amit.uniconnexample.rest.model.UserDetailRequestModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * Created by Meera on 09,November,2019
 */
interface NetworkRepository {
    @POST("setPostSql")
    fun sendPost(@Header("Authorization") authToken:String, @Body postBlogModel: PostBlogModel) : Call<ModelResponseMessage>

    @POST("setUserSql")
    fun sendUser(@Header("Authorization") authToken:String, @Body userDetailRequestModel: UserDetailRequestModel) : Call<ModelResponseMessage>

    @POST("getBlogSql")
    fun getPostPAged(@Header("Authorization") authToken:String, @Body request: GetPostRequestModel): Call<List<PostModel>>

    @POST("setPostLikeSql")
    fun postLikeUnlike(@Header("Authorization") authToken:String, @Body request: PostLikeModel): Call<String>
}