package com.example.amit.uniconnexample.View

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.lifecycle.LifecycleObserver
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.amit.uniconnexample.R
import kotlinx.android.synthetic.main.image_view_layout.view.*
import org.jetbrains.anko.AnkoLogger

/**
 * Created by Meera on 31,December,2019
 */
class ImageView : FrameLayout, AnkoLogger, LifecycleObserver {

    constructor(context: Context): this(context, null){
        initialize()
    }

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0){
        //initialize()
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        //initialize()
    }

    fun initialize(){
        inflate(context, R.layout.image_view_layout, this)
    }

    fun setData(imageUrl: String, itemHeight: Int){
        Glide.with(context).setDefaultRequestOptions(RequestOptions().fitCenter()).load(imageUrl).override(itemHeight).into(image_view)
    }
}