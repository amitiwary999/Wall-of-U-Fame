package com.example.amit.uniconnexample.Fragment.Home

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.example.amit.uniconnexample.R
import com.example.amit.uniconnexample.rest.model.PostModel
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

/**
 * Created by Meera on 01,January,2020
 */
class HomeAdapterImageViewHolder(itemView: View, var itemHeight : Int,var imageView: com.example.amit.uniconnexample.View.ImageView): HomeAdapterCommonViewHolder(itemView), AnkoLogger {
    var mediaFrame: FrameLayout = itemView.findViewById(R.id.media_frame)
    override fun bindData(post: PostModel?) {
        info { "set data image" }
        super.setData(post)
        info { "set data image super" }
        post?.let { postModel->
            imageView.parent?.let {
                (it as ViewGroup).removeView(imageView)
            }
            mediaFrame.addView(imageView)
            imageView.setData(postModel.imageUrl, itemHeight)
        }
    }
}