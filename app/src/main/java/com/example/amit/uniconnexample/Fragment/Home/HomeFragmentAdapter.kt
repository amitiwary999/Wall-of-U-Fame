package com.example.amit.uniconnexample.Fragment.Home

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.amit.uniconnexample.App
import com.example.amit.uniconnexample.Others.CommonString
import com.example.amit.uniconnexample.R
import com.example.amit.uniconnexample.View.VideoPlayerView
import com.example.amit.uniconnexample.View.VideoPlayerViewRecycler
import com.example.amit.uniconnexample.rest.model.PostModel
import com.example.amit.uniconnexample.utils.DateUtils
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

/**
 * Created by Meera on 04,December,2019
 */
class HomeFragmentAdapter(var itemOptionsClickListener: ItemOptionsClickListener, var itemHeight : Int, var videoPlayerView: VideoPlayerViewRecycler): RecyclerView.Adapter<HomeAdapterCommonViewHolder>() {
     var postModels = ArrayList<PostModel>()
    var context: Context ?= null
    companion object{
        const val IMAGE_VIEW = 1
        const val VIDEO_VIEW = 2
        const val TEXT_VIEW = 0
    }

    fun setData(posts: List<PostModel>){
        val size = postModels.size
        postModels.addAll(posts)
        notifyItemRangeInserted(size, posts.size)
    }

    fun setRefreshedData(posts: List<PostModel>){
        val size = postModels.size
        notifyItemRangeRemoved(0, size)
        postModels.clear()
        postModels.addAll(posts)
        notifyItemRangeInserted(0, posts.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeAdapterCommonViewHolder {
        lateinit var view: View
        context = parent.context
        view =  LayoutInflater.from(context).inflate(R.layout.famous_post_item, parent, false);
        return HomeAdapterCommonViewHolder(view, videoPlayerView, itemHeight)
    }

    override fun getItemCount(): Int {
        return postModels.size
    }

    override fun onBindViewHolder(holder: HomeAdapterCommonViewHolder, position: Int) {
        val postModel = postModels[position]
        val mediaUrl = postModel.mediaUrl
        val mediaThumbUrl = postModel.mediaThumbUrl
        val postId = postModel.postId
        val date = postModel.date
        val description = postModel.description
        val mimeType = postModel.mimeType
        val userName = postModel.userName
        val userDp = postModel.userDp
        val userId = postModel.creatorId
        val isLikeds = postModel.isLiked

        val mediaUrls =  mediaUrl?.split(",")
        val mediaThumbUrls = mediaThumbUrl?.split(",")
        val postIds = postId.split(",")
        val dates = date.split(",")
        val descriptions = description.split(",")
        val mimeTypes = mimeType?.split(",")
        val isLiked = isLikeds.split(",")

        var posts = ArrayList<PostModel>()

        val length = descriptions.size
        for(i in 0 until length){
            var mediaUrl = ""
            var mimeType: String ?= null
            var date = ""
            var mediaThumbUrl = ""
            var postId = ""
            var description = descriptions[i]
            var isLikedPost = isLiked[i]
            mediaUrls?.let {
                mediaUrl = it[i]
            }
            mediaThumbUrls?.let {
                mediaThumbUrl = it[i]
            }

            postIds.let {
                postId = it[i]
            }

            dates.let {
                date = it[i]
            }
            mimeTypes?.let {
                mimeType = it[i]
            }
            val post = PostModel(date, description, mediaUrl, mediaThumbUrl, userId, postId, userDp, userName, isLikedPost, mimeType)
            posts.add(post)
        }

        holder.setDatas(posts)

//        holder.likeButton.setOnClickListener {
//            if(postModel.isLiked == 1){
//                postModel.isLiked = 0
////                postModel.like = if(postModel.like != null){
////                    0
////                }else{
////                    postModel.like!!-1
////                }
//                itemOptionsClickListener.onPostUnlike(postModel.postId)
//                notifyItemChanged(position, CommonString.PAYLOAD_ITEM_UNLIKE)
//            }else{
//                postModel.isLiked = 1
//                //TODO handle it when sql added properly for post like
////                postModel.like = if(postModel.like != null){
////                    0
////                }else{
////                    postModel.like!!+1
////                }
//                itemOptionsClickListener.onPostLike(postModel.postId)
//                notifyItemChanged(position, CommonString.PAYLOAD_ITEM_LIKE)
//            }
//        }
    }

//    override fun onBindViewHolder(holder: HomeAdapterCommonViewHolder, position: Int, payloads: MutableList<Any>) {
//        if(payloads.isNotEmpty()){
//            holder.setData(postModels[position], (payloads[0] as String))
//        }
//        super.onBindViewHolder(holder, position, payloads)
//    }

//    override fun getItemViewType(position: Int): Int {
//        val postModel = postModels[position]
//        if(postModel.mimeType == null){
//            return TEXT_VIEW
//        }else{
//            postModel.mimeType?.let{
//                if(it.contains(CommonString.MimeType.IMAGE)){
//                    return IMAGE_VIEW
//                }else if(it.contains(CommonString.MimeType.VIDEO)){
//                    return VIDEO_VIEW
//                }
//            }
//        }
//        return TEXT_VIEW
//    }

    override fun onViewDetachedFromWindow(holder: HomeAdapterCommonViewHolder) {
    }

//    class HomeAdapterViewHolder(itemView: View, var context: Context?,var itemHeight : Int, var videoView: VideoPlayerView, var imageView: com.example.amit.uniconnexample.View.ImageView) : RecyclerView.ViewHolder(itemView), AnkoLogger{
//        var pImage: ImageView = itemView.findViewById(R.id.pimage)
//        var name: TextView = itemView.findViewById(R.id.bname)
//        var postDesc: TextView = itemView.findViewById(R.id.post_desc)
//        var likeButton: ImageButton = itemView.findViewById(R.id.like)
//        var date: TextView = itemView.findViewById(R.id.txtDate)
//        var likeCount: TextView = itemView.findViewById(R.id.txtlike)
//        var mediaFrame: FrameLayout = itemView.findViewById(R.id.media_frame)
//
//        fun setData(post: PostModel?){
//            post?.let {postModel ->
//                info { "post model in set data ${postModel.imageUrl}" }
//                mediaFrame.visibility = View.GONE
//                postDesc.text = postModel.desc
//                name.text = postModel.creatorName
//                date.text = DateUtils.getDateFromUTCTimestamp(postModel.date, CommonString.DATE_FORMAT)
//                likeCount.text = postModel.like.toString()
//                if(postModel.isLiked == 1){
//                    likeButton.setColorFilter(App.instance.resources.getColor(R.color.yellow))
//                }else{
//                    likeButton.setColorFilter(App.instance.resources.getColor(R.color.Black))
//                }
//
//
//                context?.let {
//                    postModel.mimeType?.let { mimeType ->
//                        mediaFrame.visibility = View.VISIBLE
//                        mediaFrame.removeAllViews()
//                        if (mimeType.contains(CommonString.MimeType.IMAGE)) {
//                            mediaFrame.addView(imageView)
//                            imageView.setData(postModel.imageUrl, itemHeight)
//                        } else if (mimeType.contains(CommonString.MimeType.VIDEO)) {
//                            mediaFrame.addView(videoView)
//                            videoView.setData(Uri.parse(postModel.imageUrl), false)
//                        } else {
//                            //no other mimetype right now
//                        }
//                    }
//
//                    if(postModel.creatorDp.isNotEmpty()){
//                        Glide.with(it).setDefaultRequestOptions(RequestOptions().circleCrop()).load(postModel.creatorDp).into(pImage)
//                    }
//
//
//                }
//            }
//        }
//
//        fun setData(postModel: PostModel?, payload: String){
//            if(payload == CommonString.PAYLOAD_ITEM_LIKE){
//                likeButton.setColorFilter(App.instance.resources.getColor(R.color.yellow))
//            }else if(payload == CommonString.PAYLOAD_ITEM_UNLIKE){
//                likeButton.setColorFilter(App.instance.resources.getColor(R.color.Black))
//            }
//            postModel?.let {
//                likeCount.text = it.like.toString()
//            }
//        }
//    }
}