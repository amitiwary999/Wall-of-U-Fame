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
import com.example.amit.uniconnexample.rest.model.PostModel
import com.example.amit.uniconnexample.utils.DateUtils
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

/**
 * Created by Meera on 04,December,2019
 */
class HomeFragmentAdapter(var itemOptionsClickListener: ItemOptionsClickListener, var itemHeight : Int, var videoPlayerView: VideoPlayerView): RecyclerView.Adapter<HomeFragmentAdapter.HomeAdapterViewHolder>() {
     var postModels = ArrayList<PostModel>()
    var context: Context ?= null
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.blog_item, parent, false)
        context = parent.context
        return HomeAdapterViewHolder(view, context, itemHeight, videoPlayerView)
    }

    override fun getItemCount(): Int {
        return postModels.size
    }

    override fun onBindViewHolder(holder: HomeAdapterViewHolder, position: Int) {
        val postModel = postModels[position]
        holder.setData(postModel)

        holder.likeButton.setOnClickListener {
            if(postModel.isLiked == 1){
                postModel.isLiked = 0
                postModel.like = postModel.like-1
                itemOptionsClickListener.onPostUnlike(postModel.postId)
                notifyItemChanged(position, CommonString.PAYLOAD_ITEM_UNLIKE)
            }else{
                postModel.isLiked = 1
                postModel.like = postModel.like+1
                itemOptionsClickListener.onPostLike(postModel.postId)
                notifyItemChanged(position, CommonString.PAYLOAD_ITEM_LIKE)
            }
        }
    }

    override fun onBindViewHolder(holder: HomeAdapterViewHolder, position: Int, payloads: MutableList<Any>) {
        if(payloads.isNotEmpty()){
            holder.setData(postModels[position], (payloads[0] as String))
        }
        super.onBindViewHolder(holder, position, payloads)
    }

    class HomeAdapterViewHolder(itemView: View, var context: Context?,var itemHeight : Int, var view: VideoPlayerView) : RecyclerView.ViewHolder(itemView), AnkoLogger{
        var pImage: ImageView = itemView.findViewById(R.id.pimage)
        var name: TextView = itemView.findViewById(R.id.bname)
        var image: ImageView = itemView.findViewById(R.id.postimage)
        var postDesc: TextView = itemView.findViewById(R.id.post_desc)
        var likeButton: ImageButton = itemView.findViewById(R.id.like)
        var date: TextView = itemView.findViewById(R.id.txtDate)
        var likeCount: TextView = itemView.findViewById(R.id.txtlike)
        var mediaFrame: FrameLayout = itemView.findViewById(R.id.media_frame)

        fun setData(post: PostModel?){
            post?.let {postModel ->
                info { "post model in set data ${postModel.imageUrl}" }
                mediaFrame.visibility = View.GONE
                postDesc.text = postModel.desc
                name.text = postModel.creatorName
                date.text = DateUtils.getDateFromUTCTimestamp(postModel.date, CommonString.DATE_FORMAT)
                likeCount.text = postModel.like.toString()
                if(postModel.isLiked == 1){
                    likeButton.setColorFilter(App.instance.resources.getColor(R.color.yellow))
                }else{
                    likeButton.setColorFilter(App.instance.resources.getColor(R.color.Black))
                }
                context?.let {
                    postModel.mimeType?.let {mimeType ->
                        mediaFrame.visibility = View.VISIBLE
                        mediaFrame.removeView(view)
                        if(mimeType.contains(CommonString.MimeType.IMAGE)){
                            if(postModel.imageUrl.isNotEmpty()){
                                image.visibility = View.VISIBLE
                                Glide.with(it).setDefaultRequestOptions(RequestOptions().fitCenter()).load(postModel.imageUrl).override(itemHeight).into(image)
                            }else{
                                image.visibility = View.GONE
                            }
                        }else if(mimeType.contains(CommonString.MimeType.VIDEO) && postModel.imageUrl.isNotEmpty()){
                            image.visibility = View.GONE
                            mediaFrame.addView(view)
                            view.setData(Uri.parse(postModel.imageUrl), false)
                        }else{
                            //no other mimetype right now
                        }
                    }

                    if(postModel.creatorDp.isNotEmpty()){
                        Glide.with(it).setDefaultRequestOptions(RequestOptions().circleCrop()).load(postModel.creatorDp).into(pImage)
                    }


                }
            }
        }

        fun setData(postModel: PostModel?, payload: String){
            if(payload == CommonString.PAYLOAD_ITEM_LIKE){
                likeButton.setColorFilter(App.instance.resources.getColor(R.color.yellow))
            }else if(payload == CommonString.PAYLOAD_ITEM_UNLIKE){
                likeButton.setColorFilter(App.instance.resources.getColor(R.color.Black))
            }
            postModel?.let {
                likeCount.text = it.like.toString()
            }
        }
    }
}