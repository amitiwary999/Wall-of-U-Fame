package com.example.amit.uniconnexample.Fragment.Home

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.amit.uniconnexample.R
import com.example.amit.uniconnexample.View.VideoPlayerViewRecycler
import com.example.amit.uniconnexample.rest.model.PostModel
import com.example.amit.uniconnexample.utils.AutoScrollViewPager
import com.google.android.material.tabs.TabLayout

/**
 * Created by Meera on 01,January,2020
 */
class HomeAdapterCommonViewHolder(itemView: View, var videoView: VideoPlayerViewRecycler, var frameWidth: Int): RecyclerView.ViewHolder(itemView) {

    var adapter:HomeFragmentAdapterItem?=null
    val pager = itemView.findViewById<AutoScrollViewPager>(R.id.viewPager)
    val tab = itemView.findViewById<TabLayout>(R.id.tab)

    //pager for media and text view, video call request and like button
    fun setDatas(famousPosts: List<PostModel>){
        Log.d("posts famous size ", " " + famousPosts.size)
        adapter = HomeFragmentAdapterItem(itemView.context, videoView, frameWidth, famousPosts)
//        adapter?.setData(famousPosts)
        pager.adapter = adapter
        tab.setupWithViewPager(pager)
        val params: ViewGroup.LayoutParams = pager.layoutParams
        params.height = frameWidth
        pager.layoutParams = params
//        adapter?.let {
//            if(it.count<=1) tab.visibility = View.GONE
//        }
//        pager.startAutoScroll();
//        pager.setCycle(true);
    }

//    fun setData(post: PostModel?){
//        post?.let { postModel ->
//            postDesc.text = postModel.description
//            name.text = postModel.userName
//            date.text = DateUtils.getDateFromUTCTimestamp(postModel.date, CommonString.DATE_FORMAT)
//            likeCount.text = postModel.like?.toString()
//            if(postModel.isLiked == 1){
//                likeButton.setColorFilter(App.instance.resources.getColor(R.color.yellow))
//            }else{
//                likeButton.setColorFilter(App.instance.resources.getColor(R.color.Black))
//            }
//            postModel.userDp?.let {
//                if(it.isNotEmpty()){
//                    Glide.with(itemView.context).setDefaultRequestOptions(RequestOptions().circleCrop()).load(it).into(pImage)
//                }
//            }
//        }
//    }
//
//    fun setData(postModel: PostModel?, payload: String){
//        if(payload == CommonString.PAYLOAD_ITEM_LIKE){
//            likeButton.setColorFilter(App.instance.resources.getColor(R.color.yellow))
//        }else if(payload == CommonString.PAYLOAD_ITEM_UNLIKE){
//            likeButton.setColorFilter(App.instance.resources.getColor(R.color.Black))
//        }
//        postModel?.let {
//            likeCount.text = it.like?.toString()
//        }
//    }
}