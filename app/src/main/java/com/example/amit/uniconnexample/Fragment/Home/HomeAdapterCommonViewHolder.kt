package com.example.amit.uniconnexample.Fragment.Home

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.amit.uniconnexample.App
import com.example.amit.uniconnexample.Others.CommonString
import com.example.amit.uniconnexample.R
import com.example.amit.uniconnexample.rest.model.PostModel
import com.example.amit.uniconnexample.utils.DateUtils

/**
 * Created by Meera on 01,January,2020
 */
abstract class HomeAdapterCommonViewHolder(itemView :View): RecyclerView.ViewHolder(itemView) {
    abstract fun bindData(post: PostModel?)

    var pImage: ImageView = itemView.findViewById(R.id.pimage)
    var name: TextView = itemView.findViewById(R.id.bname)
    var postDesc: TextView = itemView.findViewById(R.id.post_desc)
    var likeButton: ImageButton = itemView.findViewById(R.id.like)
    var date: TextView = itemView.findViewById(R.id.txtDate)
    var likeCount: TextView = itemView.findViewById(R.id.txtlike)

    fun setData(post: PostModel?){
        post?.let { postModel ->
            postDesc.text = postModel.desc
            name.text = postModel.creatorName
            date.text = DateUtils.getDateFromUTCTimestamp(postModel.date, CommonString.DATE_FORMAT)
            likeCount.text = postModel.like.toString()
            if(postModel.isLiked == 1){
                likeButton.setColorFilter(App.instance.resources.getColor(R.color.yellow))
            }else{
                likeButton.setColorFilter(App.instance.resources.getColor(R.color.Black))
            }

            if(postModel.creatorDp.isNotEmpty()){
                Glide.with(itemView.context).setDefaultRequestOptions(RequestOptions().circleCrop()).load(postModel.creatorDp).into(pImage)
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