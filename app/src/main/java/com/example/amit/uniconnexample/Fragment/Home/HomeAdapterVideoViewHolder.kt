package com.example.amit.uniconnexample.Fragment.Home

import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
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
    var imageView: ImageView = itemView.findViewById(R.id.image_view)
    var playerIcon: ImageView = itemView.findViewById(R.id.play_btn)
    override fun bindData(post: PostModel?) {
        info { "set data video" }
        super.setData(post)
        val layoutParam = playerView.layoutParams
        layoutParam.width = frameWidth
        layoutParam.height = frameWidth
        playerView.requestLayout()
        info { "set data video super ${post}" }
        post?.let{postModel ->
            imageView.visibility = View.VISIBLE
            playerIcon.visibility = View.VISIBLE
            Glide.with(itemView).setDefaultRequestOptions(RequestOptions().fitCenter()).load(postModel.mediaUrl).override(frameWidth).into(imageView)
            playerView.visibility = View.GONE

            mediaFrame.setOnClickListener {
                playerView.visibility = View.VISIBLE
                imageView.visibility = View.GONE
                playerIcon.visibility = View.GONE
                videoView.setData(Uri.parse(postModel.mediaUrl), false, playerView)
            }
        }
    }

    override fun detachedView() {
        info { "detached from window" }
        playerView.visibility = View.GONE
        imageView.visibility = View.VISIBLE
        playerIcon.visibility = View.VISIBLE
        videoView.pause()
    }
}