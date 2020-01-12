package com.example.amit.uniconnexample.Fragment.Home

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.amit.uniconnexample.R
import com.example.amit.uniconnexample.rest.model.PostModel
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

/**
 * Created by Meera on 01,January,2020
 */
class HomeAdapterImageViewHolder(itemView: View, var itemHeight : Int): HomeAdapterCommonViewHolder(itemView), AnkoLogger {
    var imageView: ImageView = itemView.findViewById(R.id.image_view)
    override fun bindData(post: PostModel?) {
        info { "set data image" }
        super.setData(post)
        info { "set data image super" }
        post?.let { postModel->
            val thumb = if(postModel.mediaThumbUrl != null && postModel.mediaThumbUrl!!.isNotEmpty()){
                postModel.mediaThumbUrl
            }else{
                postModel.mediaUrl
            }
            Glide.with(itemView).setDefaultRequestOptions(RequestOptions().fitCenter()).load(postModel.mediaUrl).thumbnail(Glide.with(itemView).load(thumb)).override(itemHeight).into(imageView)
        }
    }

    override fun detachedView() {

    }
}