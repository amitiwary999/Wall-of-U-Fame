package com.example.amit.uniconnexample.utils

import android.content.Context
import android.view.animation.Interpolator
import android.widget.Scroller

/**
 * Created by Meera on 17,October,2020
 */
class DurationScroller : Scroller {
    private var scrollFactor = 1.0

    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, interpolator: Interpolator?) : super(context, interpolator) {}

    fun setScrollDurationFactor(scrollFactor: Double) {
        this.scrollFactor = scrollFactor
    }

    override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int, duration: Int) {
        super.startScroll(startX, startY, dx, dy, (duration * scrollFactor).toInt())
    }
}