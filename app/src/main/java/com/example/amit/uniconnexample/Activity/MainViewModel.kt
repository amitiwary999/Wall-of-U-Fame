package com.example.amit.uniconnexample.Activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.amit.uniconnexample.Fragment.Home.GetPostRequestModel
import com.example.amit.uniconnexample.Others.CommonString
import com.example.amit.uniconnexample.rest.RetrofitClientBuilder
import com.example.amit.uniconnexample.rest.model.PostModel
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Meera on 04,December,2019
 */
class MainViewModel : ViewModel() {
    var nextKey: String = ""
    var postLiveData = MutableLiveData<List<PostModel>>()
    var firebaseUser = FirebaseAuth.getInstance().currentUser

    fun getPagedPost(){
        firebaseUser?.getToken(false)?.addOnCompleteListener {
            if(it.isSuccessful){
                val getPostRequestModel = GetPostRequestModel(nextKey, 10)
                RetrofitClientBuilder(CommonString.base_url).getmNetworkRepository().getPostPAged("Bearer ${it.result?.token}", getPostRequestModel)
                        .enqueue(object : Callback<List<PostModel>> {
                            override fun onFailure(call: Call<List<PostModel>>, t: Throwable) {
                            }

                            override fun onResponse(call: Call<List<PostModel>>, response: Response<List<PostModel>>) {
                                if(response.isSuccessful && response.body() != null){
                                    val responseBody = response.body()!!
                                    nextKey = responseBody[responseBody.size-1].postId
                                    postLiveData.postValue(responseBody)
                                }else{

                                }
                            }
                        })
            }else{

            }
        }
    }
}