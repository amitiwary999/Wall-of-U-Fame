package com.example.amit.uniconnexample.Activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.amit.uniconnexample.Fragment.Home.GetPostRequestModel
import com.example.amit.uniconnexample.Others.CommonString
import com.example.amit.uniconnexample.rest.RetrofitClientBuilder
import com.example.amit.uniconnexample.rest.model.PostLikeModel
import com.example.amit.uniconnexample.rest.model.PostModel
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Meera on 04,December,2019
 */
class MainViewModel : ViewModel(), AnkoLogger {
    var nextKey: String = ""
    var postLiveData = MutableLiveData<List<PostModel>>()
    var firebaseUser = FirebaseAuth.getInstance().currentUser
    var error = MutableLiveData<String>()

    init {
        getPagedPost()
    }

    fun getPagedPost(){
        firebaseUser?.getToken(false)?.addOnCompleteListener {
            if(it.isSuccessful){
                val getPostRequestModel = GetPostRequestModel(nextKey, 10)
                RetrofitClientBuilder(CommonString.base_url).getmNetworkRepository().getPostPAged("Bearer ${it.result?.token}", getPostRequestModel)
                        .enqueue(object : Callback<List<PostModel>> {
                            override fun onFailure(call: Call<List<PostModel>>, t: Throwable) {
                                error.postValue("failed")
                            }

                            override fun onResponse(call: Call<List<PostModel>>, response: Response<List<PostModel>>) {
                                if(response.isSuccessful && response.body() != null && response.body()!!.isNotEmpty()){
                                    val responseBody = response.body()!!
                                    nextKey = responseBody[responseBody.size-1].postId
                                    postLiveData.postValue(responseBody)
                                }else{
                                    error.postValue("failed")
                                }
                            }
                        })
            }else{
                error.postValue("failed")
            }
        }
    }

    fun postLiked(postId: String){
        FirebaseAuth.getInstance()?.currentUser?.getToken(false)?.addOnCompleteListener {
            if(it.isSuccessful && it.result != null){
                val postLikemodel = PostLikeModel(postId, 1)
                RetrofitClientBuilder(CommonString.base_url).getmNetworkRepository().postLiked("Bearer ${it.result?.token}", postLikemodel)
                        .enqueue(object : Callback<String>{
                            override fun onFailure(call: Call<String>, t: Throwable) {
                                t.printStackTrace()
                            }

                            override fun onResponse(call: Call<String>, response: Response<String>) {
                                if(response.isSuccessful && response.body() != null) {
                                    info { "response like ${response.body()}" }
                                }else{
                                    info { "response like fail" }
                                }
                            }
                        })
            }
        }
    }

    fun postUnlike(postId: String) {
        FirebaseAuth.getInstance()?.currentUser?.getToken(false)?.addOnCompleteListener {
            if(it.isSuccessful && it.result != null){
                val postLikemodel = PostLikeModel(postId, 0)
                RetrofitClientBuilder(CommonString.base_url).getmNetworkRepository().postLiked("Bearer ${it.result?.token}", postLikemodel)
                        .enqueue(object : Callback<String>{
                            override fun onFailure(call: Call<String>, t: Throwable) {
                                t.printStackTrace()
                            }

                            override fun onResponse(call: Call<String>, response: Response<String>) {
                                if(response.isSuccessful && response.body() != null) {
                                    info { "response ${response.body()}" }
                                }else{
                                    info { "response fail" }
                                }
                            }
                        })
            }
        }
    }

    fun refresh(){
        nextKey = ""
        getPagedPost()
    }
}