package com.example.amit.uniconnexample.Fragment.Home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
class HomeAdapter : PagedListAdapter<PostModel, HomeAdapter.HomeAdapterViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.blog_item, parent, false)
        return HomeAdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeAdapterViewHolder, position: Int) {
        holder.setData(getItem(position))
    }

    class HomeAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var pImage: ImageView = itemView.findViewById(R.id.pimage)
        var name: TextView = itemView.findViewById(R.id.bname)
        var image: ImageView = itemView.findViewById(R.id.postimage)
        var postDesc: TextView = itemView.findViewById(R.id.post_desc)

        fun setData(post: PostModel?){
            postDesc.text = post?.desc
            post?.dat
            post?.desc
            post?.imageUrl
        }
    }

    companion object {

        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PostModel>() {
            override fun areItemsTheSame(oldItem: PostModel, newItem: PostModel): Boolean {
                return oldItem.postId.equals(newItem.postId)
            }

            override fun areContentsTheSame(oldItem: PostModel, newItem: PostModel): Boolean {
                return false
            }

        }
    }
}