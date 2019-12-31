package com.example.amit.uniconnexample.View

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.lifecycle.LifecycleObserver
import org.jetbrains.anko.AnkoLogger

/**
 * Created by Meera on 31,December,2019
 */
class ImageView : FrameLayout, AnkoLogger, LifecycleObserver {

    constructor(context: Context): this(context, null){

    }

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0){
        //initialize()
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        //initialize()
    }

    fun initialize(){

    }
}