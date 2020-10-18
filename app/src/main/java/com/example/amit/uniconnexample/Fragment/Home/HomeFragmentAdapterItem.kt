package com.example.amit.uniconnexample.Fragment.Home

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.amit.uniconnexample.Others.CommonString
import com.example.amit.uniconnexample.R
import com.example.amit.uniconnexample.View.VideoPlayerViewRecycler
import com.example.amit.uniconnexample.rest.model.PostModel
import com.google.android.exoplayer2.ui.PlayerView
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

/**
 * Created by Meera on 18,October,2020
 */
class HomeFragmentAdapterItem(val context: Context, var videoView: VideoPlayerViewRecycler, var frameWidth: Int, var famousPostsItem: List<PostModel>) : PagerAdapter(), AnkoLogger {
    private var mLayoutInflater: LayoutInflater
//    val famousPostsItem  = ArrayList<PostModel>()
    init {
        mLayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    }

//    fun setData(posts: List<PostModel>) {
//        famousPostsItem.addAll(posts)
//        notifyDataSetChanged()
//    }
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as View

    }

    override fun getCount(): Int {
        return famousPostsItem.size
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val postModel = famousPostsItem[position]
        val itemViewType = postModel.mimeType
        Log.d("item position ", " "+ position+" itemviewtype "+itemViewType?.contains(CommonString.MimeType.IMAGE))

        lateinit var view: View
        if(itemViewType == null){
            view = mLayoutInflater.inflate(R.layout.blog_text_item_view, container, false)
        }

        itemViewType?.let {
            if(it.contains(CommonString.MimeType.IMAGE)){
                view = mLayoutInflater.inflate(R.layout.blog_image_item_view, container, false)
                loadImage(postModel, view)
//                var imageView: ImageView = view.findViewById(R.id.post_image)
//                postModel?.let { postModel->
//                    Log.d("load image ", "view "+postModel.mediaUrl)
//
//                    val thumb = if(postModel.mediaThumbUrl != null && postModel.mediaThumbUrl!!.isNotEmpty()){
//                        postModel.mediaThumbUrl
//                    }else{
//                        postModel.mediaUrl
//                    }
//                    Glide.with(imageView).setDefaultRequestOptions(RequestOptions().fitCenter()).load(postModel.mediaUrl).thumbnail(Glide.with(imageView).load(thumb)).override(frameWidth).into(imageView)
//                }

            }else if(it.contains(CommonString.MimeType.VIDEO)){
                view = mLayoutInflater.inflate(R.layout.blog_video_item_view, container, false)
                loadVideo(postModel, view)
            }else{

            }
        }
        container.addView(view)
        return view
    }

    fun loadImage(post: PostModel?, itemView: View){
        Log.d("load image ", "view "+post)
        var imageView: ImageView = itemView.findViewById(R.id.post_image)
        post?.let { postModel->
            Log.d("load image ", "view "+postModel.mediaUrl)

            val thumb = if(postModel.mediaThumbUrl != null && postModel.mediaThumbUrl!!.isNotEmpty()){
                postModel.mediaThumbUrl
            }else{
                postModel.mediaUrl
            }
            Glide.with(itemView).setDefaultRequestOptions(RequestOptions().fitCenter()).load(postModel.mediaUrl).thumbnail(Glide.with(itemView).load(thumb)).override(frameWidth).into(imageView)
        }
    }

    fun loadVideo(post: PostModel?, itemView: View){
        var mediaFrame: FrameLayout = itemView.findViewById(R.id.post_image)
        var playerView: PlayerView = itemView.findViewById(R.id.player_view)
        var imageView: ImageView = itemView.findViewById(R.id.image_view)
        var playerIcon: ImageView = itemView.findViewById(R.id.play_btn)

        val layoutParam = playerView.layoutParams
        layoutParam.width = frameWidth
        layoutParam.height = frameWidth
        playerView.requestLayout()
        info { "set data video super ${post}" }
        post?.let{postModel ->
            imageView.visibility = View.VISIBLE
            playerIcon.visibility = View.VISIBLE
            val thumb = if(postModel.mediaThumbUrl != null && postModel.mediaThumbUrl!!.isNotEmpty()){
                postModel.mediaThumbUrl
            }else{
                postModel.mediaUrl
            }
            Glide.with(itemView).setDefaultRequestOptions(RequestOptions().fitCenter()).load(thumb).override(frameWidth).into(imageView)
            playerView.visibility = View.GONE

            mediaFrame.setOnClickListener {
                playerView.visibility = View.VISIBLE
                imageView.visibility = View.GONE
                playerIcon.visibility = View.GONE
                videoView.setData(Uri.parse(postModel.mediaUrl), true, playerView)
            }
        }
    }
}