package com.example.amit.uniconnexample.Fragment.Home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.amit.uniconnexample.Others.CommonString
import com.example.amit.uniconnexample.R
import com.example.amit.uniconnexample.rest.RetrofitClientBuilder
import com.example.amit.uniconnexample.rest.model.PostLikeModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_global_frag.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Meera on 26,November,2019
 */
class HomeFragment : Fragment(), ItemOptionsClickListener,AnkoLogger {
    var homeAdapter: HomeAdapter ?= null
    var mContext: Context ?= null
    var homeFragmentViewModel: HomeFragmentViewModel ?= null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_global_frag, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mContext?.let {
            mblog_list.layoutManager = LinearLayoutManager(mContext)
            homeAdapter = HomeAdapter(this)
            mblog_list.adapter = homeAdapter
            homeFragmentViewModel = ViewModelProviders.of(this).get(HomeFragmentViewModel::class.java)
            homeFragmentViewModel?.getListLivedata()?.observe(this, Observer {
                homeAdapter?.submitList(it)
            })
        }

    }

    override fun onPostLike(postId: String) {
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

    override fun onPostUnlike(postId: String) {
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

    override fun onChatClick(userId: String) {

    }
}