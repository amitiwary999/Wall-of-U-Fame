package com.example.amit.uniconnexample.Fragment.Home

import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.example.amit.uniconnexample.Others.CommonString
import com.example.amit.uniconnexample.rest.RetrofitClientBuilder
import com.example.amit.uniconnexample.rest.model.PostModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Meera on 26,November,2019
 */
class HomeDataSource : PageKeyedDataSource<String, PostModel>() {
    val firebaseUser: FirebaseUser?
    var retrofitClientBuilder: RetrofitClientBuilder
    init {
        firebaseUser = FirebaseAuth.getInstance().currentUser
        retrofitClientBuilder = RetrofitClientBuilder(CommonString.base_url)
    }

    override fun loadInitial(params: LoadInitialParams<String>, callback: LoadInitialCallback<String, PostModel>) {
        firebaseUser?.getToken(false)?.addOnCompleteListener {
            if(it.isSuccessful){
                val getPostRequestModel = GetPostRequestModel("", 10)
                retrofitClientBuilder.getmNetworkRepository().getPostPAged("Bearer ${it.result?.token}", getPostRequestModel)
                        .enqueue(object : Callback<List<PostModel>> {
                            override fun onFailure(call: Call<List<PostModel>>, t: Throwable) {
                                t.printStackTrace()
                            }

                            override fun onResponse(call: Call<List<PostModel>>, response: Response<List<PostModel>>) {
                                Log.d("response retrofit","posts ${response.body()?.size}")
                                if(response.isSuccessful && response.body() != null){
                                    val responseBody = response.body()!!
                                    val nextKey = response.body()!![response.body()!!.size - 1].postId
                                    callback.onResult(responseBody, "", nextKey)
                                }else{

                                }
                            }
                        })
            }else{

            }
        }
    }

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, PostModel>) {
       Log.d("homedatasource","load after ${params.key}")
        firebaseUser?.getToken(false)?.addOnCompleteListener {
            if(it.isSuccessful){
                val getPostRequestModel = GetPostRequestModel(params.key, 10)
                retrofitClientBuilder.getmNetworkRepository().getPostPAged("Bearer ${it.result?.token}", getPostRequestModel)
                        .enqueue(object : Callback<List<PostModel>> {
                            override fun onFailure(call: Call<List<PostModel>>, t: Throwable) {
                            }

                            override fun onResponse(call: Call<List<PostModel>>, response: Response<List<PostModel>>) {
                                if(response.isSuccessful && response.body() != null){
                                    val responseBody = response.body()!!
                                    var nextKey: String ?= params.key
                                    callback.onResult(responseBody,  nextKey)
                                }else{

                                }
                            }
                        })
            }else{

            }
        }
    }

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<String, PostModel>) {
        Log.d("homedatasource","load before ${params.key}")
    }
}