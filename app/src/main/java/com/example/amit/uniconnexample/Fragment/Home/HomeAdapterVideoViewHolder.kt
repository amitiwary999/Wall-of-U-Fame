package com.example.amit.uniconnexample.Fragment.Home

import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.example.amit.uniconnexample.R
import com.example.amit.uniconnexample.View.VideoPlayerView
import com.example.amit.uniconnexample.View.VideoPlayerViewRecycler
import com.example.amit.uniconnexample.rest.model.PostModel
import com.google.android.exoplayer2.ui.PlayerView
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

/**
 * Created by Meera on 01,January,2020
 */
class HomeAdapterVideoViewHolder(itemView: View, var videoView: VideoPlayerViewRecycler, var frameWidth: Int): HomeAdapterCommonViewHolder(itemView), AnkoLogger {
    var mediaFrame: FrameLayout = itemView.findViewById(R.id.media_frame)
    var playerView: PlayerView = itemView.findViewById(R.id.player_view)
    override fun bindData(post: PostModel?) {
        info { "set data video" }
        super.setData(post)
        val layoutParam = playerView.layoutParams
        layoutParam.width = frameWidth
        layoutParam.height = frameWidth
        playerView.requestLayout()
        info { "set data video super ${post}" }
        post?.let{postModel ->
            videoView.setData(Uri.parse(postModel.imageUrl), false, playerView)
        }
    }
}