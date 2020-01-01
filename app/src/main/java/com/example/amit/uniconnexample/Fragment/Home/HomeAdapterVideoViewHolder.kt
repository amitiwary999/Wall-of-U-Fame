package com.example.amit.uniconnexample.Fragment.Home

import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.example.amit.uniconnexample.R
import com.example.amit.uniconnexample.View.VideoPlayerView
import com.example.amit.uniconnexample.rest.model.PostModel
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

/**
 * Created by Meera on 01,January,2020
 */
class HomeAdapterVideoViewHolder(itemView: View, var videoView: VideoPlayerView): HomeAdapterCommonViewHolder(itemView), AnkoLogger {
    var mediaFrame: FrameLayout = itemView.findViewById(R.id.media_frame)
    override fun bindData(post: PostModel?) {
        info { "set data video" }
        super.setData(post)
        info { "set data video super ${post}" }
        post?.let{postModel ->
            videoView.parent?.let {
                (it as ViewGroup).removeView(videoView)
            }
            mediaFrame.addView(videoView)
            videoView.setData(Uri.parse(postModel.imageUrl), false)
        }
    }
}