package com.example.amit.uniconnexample.Activity

import android.util.Log
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
    var refreshLiveData = MutableLiveData<List<PostModel>>()
    var firebaseUser = FirebaseAuth.getInstance().currentUser
    var error = MutableLiveData<String>()

    init {
        getPagedPost()
    }

    fun getPagedPost(){
        Log.d("main view model ","get pos "+firebaseUser)
        if(firebaseUser == null){
            val getPostRequestModel = GetPostRequestModel(nextKey, 10)
            RetrofitClientBuilder(CommonString.base_url).getmNetworkRepository().getPostPagedWithoutLogin( getPostRequestModel)
                    .enqueue(object : Callback<List<PostModel>> {
                        override fun onFailure(call: Call<List<PostModel>>, t: Throwable) {
                            error.postValue("failed")
                        }

                        override fun onResponse(call: Call<List<PostModel>>, response: Response<List<PostModel>>) {
                            if(response.isSuccessful && response.body() != null && response.body()!!.isNotEmpty()){
                                val responseBody = response.body()!!
                                if(nextKey.isEmpty()){
                                    refreshLiveData.postValue(responseBody)
                                }else{
                                    postLiveData.postValue(responseBody)
                                }
                                nextKey = responseBody[responseBody.size-1].postId
                            }else{
                                error.postValue("failed")
                            }
                        } })
        }else{
            firebaseUser?.getIdToken(false)?.addOnCompleteListener {
                Log.d("view ","task "+it.result)
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
                                        if(nextKey.isEmpty()){
                                            refreshLiveData.postValue(responseBody)
                                        }else{
                                            postLiveData.postValue(responseBody)
                                        }
                                        nextKey = responseBody[responseBody.size-1].postId
                                    }else{
                                        error.postValue("failed")
                                    }
                                }
                            })
                }
            }
        }

    }

    fun postLikeUnLike(postId: String, incrVal: Int){
        FirebaseAuth.getInstance()?.currentUser?.getIdToken(false)?.addOnCompleteListener {
            if(it.isSuccessful && it.result != null){
                val postLikemodel = PostLikeModel(postId, incrVal)
                RetrofitClientBuilder(CommonString.base_url).getmNetworkRepository().postLikeUnlike("Bearer ${it.result?.token}", postLikemodel)
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

    fun refresh(){
        nextKey = ""
        getPagedPost()
    }
}