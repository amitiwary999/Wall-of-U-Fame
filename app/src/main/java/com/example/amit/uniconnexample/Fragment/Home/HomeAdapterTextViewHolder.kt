package com.example.amit.uniconnexample.Fragment.Home

import android.view.View
import com.example.amit.uniconnexample.rest.model.PostModel

/**
 * Created by Meera on 01,January,2020
 */
class HomeAdapterTextViewHolder(itemView: View): HomeAdapterCommonViewHolder(itemView) {
    override fun bindData(post: PostModel?) {
        super.setData(post)
    }

    override fun detachedView() {

    }
}