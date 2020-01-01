package com.example.amit.uniconnexample.Fragment.Home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.amit.uniconnexample.Activity.AddBlogActivity
import com.example.amit.uniconnexample.Activity.MainViewModel
import com.example.amit.uniconnexample.Activity.NewTabActivity
import com.example.amit.uniconnexample.Others.CommonString
import com.example.amit.uniconnexample.R
import com.example.amit.uniconnexample.View.ImageView
import com.example.amit.uniconnexample.View.VideoPlayerView
import com.example.amit.uniconnexample.View.VideoPlayerViewRecycler
import com.example.amit.uniconnexample.rest.RetrofitClientBuilder
import com.example.amit.uniconnexample.rest.model.PostLikeModel
import com.example.amit.uniconnexample.utils.EndlessScrollListener
import com.example.amit.uniconnexample.utils.UtilDpToPixel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_global_frag.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.roundToInt

/**
 * Created by Meera on 26,November,2019
 */
class HomeFragment : Fragment(), ItemOptionsClickListener,AnkoLogger {
    var homeAdapter: HomeFragmentAdapter ?= null
    var mContext: Context ?= null
    lateinit var scrollListener: EndlessScrollListener
    var activity: NewTabActivity? = null
    var mainViewModel: MainViewModel ?= null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        activity = context as NewTabActivity
        getActivity()?.let {
            mainViewModel = ViewModelProviders.of(it).get(MainViewModel::class.java)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_global_frag, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mContext?.let {
            val dm = resources.displayMetrics
            val paddingFrame = UtilDpToPixel.convertDpToPixel(30f, it).roundToInt()
            val frameWidth = dm.widthPixels
            val linearLayoutManager = LinearLayoutManager(it)
            mblog_list.layoutManager = linearLayoutManager
            val videoViewRecycler = VideoPlayerViewRecycler(it)
            lifecycle.addObserver(videoViewRecycler)
            homeAdapter = HomeFragmentAdapter(this, frameWidth, videoViewRecycler)
            mblog_list.adapter = homeAdapter

            scrollListener = object : EndlessScrollListener(linearLayoutManager){
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                    mainViewModel?.getPagedPost()
                }
            }

            mblog_list.addOnScrollListener(scrollListener)

            mainViewModel?.postLiveData?.observe(it as NewTabActivity, Observer {
                refresh?.let {
                    if(refresh.isRefreshing) {
                        refresh.isRefreshing = false
                    }
                }
                    homeAdapter?.setData(it)
            })

            mainViewModel?.refreshLiveData?.observe(it as NewTabActivity, Observer {
                refresh?.let {
                    if(refresh.isRefreshing) {
                        refresh.isRefreshing = false
                    }
                }
                homeAdapter?.setRefreshedData(it)
            })

            mainViewModel?.error?.observe(it as NewTabActivity, Observer {
                refresh?.let {
                    if(refresh.isRefreshing) {
                        refresh.isRefreshing = false
                    }
                }
            })

            fab?.setOnClickListener {
                val intent = Intent(mContext, AddBlogActivity::class.java)
                startActivity(intent)
            }

            refresh?.setOnRefreshListener {
                mainViewModel?.refresh()
            }
        }

    }

    override fun onPostLike(postId: String) {
        mainViewModel?.postLiked(postId)
    }

    override fun onPostUnlike(postId: String) {
        mainViewModel?.postUnlike(postId)
    }

    override fun onChatClick(userId: String) {

    }
}