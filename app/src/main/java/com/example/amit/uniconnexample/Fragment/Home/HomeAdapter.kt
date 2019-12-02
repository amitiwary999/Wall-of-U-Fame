package com.example.amit.uniconnexample.Fragment.Home

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.amit.uniconnexample.R
import com.example.amit.uniconnexample.rest.model.PostModel
import kotlinx.android.synthetic.main.activity_global_frag.view.*

/**
 * Created by Meera on 26,November,2019
 */
class HomeAdapter(var itemOptionsClickListener: ItemOptionsClickListener) : PagedListAdapter<PostModel, HomeAdapter.HomeAdapterViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.blog_item, parent, false)
        return HomeAdapterViewHolder(view, itemOptionsClickListener)
    }

    override fun onBindViewHolder(holder: HomeAdapterViewHolder, position: Int) {
        holder.setData(getItem(position))
    }

    class HomeAdapterViewHolder(itemView: View, var itemOptionsClickListener: ItemOptionsClickListener) : RecyclerView.ViewHolder(itemView){
        var pImage: ImageView = itemView.findViewById(R.id.pimage)
        var name: TextView = itemView.findViewById(R.id.bname)
        var image: ImageView = itemView.findViewById(R.id.postimage)
        var postDesc: TextView = itemView.findViewById(R.id.post_desc)
        var likeButton: ImageButton = itemView.findViewById(R.id.like)
        var unlikeButton: ImageButton = itemView.findViewById(R.id.unlike)

        fun setData(post: PostModel?){
            post?.let {postModel ->
                postDesc.text = postModel.desc
                postModel.date
                postModel.desc
                postModel.imageUrl

                likeButton.setOnClickListener {
                    if(postModel.liked == 1){
                        postModel.liked = 0
                        itemOptionsClickListener.onPostUnlike(postModel.postId)
                    }else{
                        postModel.liked = 1
                        itemOptionsClickListener.onPostLike(postModel.postId)
                    }
                }
            }
        }
    }

    companion object {

        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PostModel>() {
            override fun areItemsTheSame(oldItem: PostModel, newItem: PostModel): Boolean {
                Log.d("post model diff","same item ${oldItem.postId} ${newItem.postId}")
                return oldItem.postId.equals(newItem.postId)
            }

            override fun areContentsTheSame(oldItem: PostModel, newItem: PostModel): Boolean {
                Log.d("post model diff","same content item ${oldItem.liked} ${newItem.liked}")
                if(oldItem.liked != newItem.liked){
                    return false
                }
                return true
            }

        }
    }
}