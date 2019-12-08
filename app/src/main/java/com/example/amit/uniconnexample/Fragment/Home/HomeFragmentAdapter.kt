package com.example.amit.uniconnexample.Fragment.Home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.amit.uniconnexample.App
import com.example.amit.uniconnexample.Others.CommonString
import com.example.amit.uniconnexample.R
import com.example.amit.uniconnexample.rest.model.PostModel
import com.example.amit.uniconnexample.utils.DateUtils

/**
 * Created by Meera on 04,December,2019
 */
class HomeFragmentAdapter(var itemOptionsClickListener: ItemOptionsClickListener): RecyclerView.Adapter<HomeFragmentAdapter.HomeAdapterViewHolder>() {
     var postModels = ArrayList<PostModel>()
    var context: Context ?= null
    fun setData(posts: List<PostModel>){
        val size = postModels.size
        postModels.addAll(posts)
        notifyItemRangeInserted(size, posts.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.blog_item, parent, false)
        context = parent.context
        return HomeAdapterViewHolder(view, context)
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
                itemOptionsClickListener.onPostUnlike(postModel.postId)
                notifyItemChanged(position, CommonString.PAYLOAD_ITEM_UNLIKE)
            }else{
                postModel.isLiked = 1
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

    class HomeAdapterViewHolder(itemView: View, var context: Context?) : RecyclerView.ViewHolder(itemView){
        var pImage: ImageView = itemView.findViewById(R.id.pimage)
        var name: TextView = itemView.findViewById(R.id.bname)
        var image: ImageView = itemView.findViewById(R.id.postimage)
        var postDesc: TextView = itemView.findViewById(R.id.post_desc)
        var likeButton: ImageButton = itemView.findViewById(R.id.like)
        var date: TextView = itemView.findViewById(R.id.txtDate)
        var likeCount: TextView = itemView.findViewById(R.id.txtlike)

        fun setData(post: PostModel?){
            post?.let {postModel ->
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
                    if(postModel.imageUrl.isNotEmpty()){
                        Glide.with(it).load(postModel.imageUrl).into(image)
                    }

                    if(postModel.creatorDp.isNotEmpty()){
                        Glide.with(it).load(postModel.creatorDp).into(pImage)
                    }
                }
            }
        }

        fun setData(post: PostModel?, payload: String){
            if(payload == CommonString.PAYLOAD_ITEM_LIKE){
                likeButton.setColorFilter(App.instance.resources.getColor(R.color.yellow))
            }else if(payload == CommonString.PAYLOAD_ITEM_UNLIKE){
                likeButton.setColorFilter(App.instance.resources.getColor(R.color.Black))
            }
        }
    }
}