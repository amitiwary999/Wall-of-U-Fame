package com.example.amit.uniconnexample.utils

import android.content.Context
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.util.Log
import android.view.animation.Interpolator
import androidx.viewpager.widget.ViewPager
import java.lang.ref.WeakReference

/**
 * Created by Meera on 17,October,2020
 */
class AutoScrollViewPager : ViewPager {
    private var interval = DEFAULT_INTERVAL.toLong()
    private var direction = RIGHT
    private var isCycle = true
    private var stopScrollWhenTouch = true
    private var slideBorderMode = SLIDE_BORDER_MODE_NONE
    private var isBorderAnimation = true
    private var autoScrollFactor = 1.0
    private var swipeScrollFactor = 1.0
    private var myHandler: Handler? = null
    private var scroller: DurationScroller? = null

    constructor(paramContext: Context?) : super(paramContext!!) {
        init()
    }

    constructor(paramContext: Context?, paramAttributeSet: AttributeSet?) : super(paramContext!!, paramAttributeSet) {
        init()
    }

    private fun init() {
        myHandler = MyHandler(this)
        setViewPagerScroller()
    }

    /**
     * start auto scroll, first scroll delay time is [.getInterval].
     */
    fun startAutoScroll() {
        scroller?.let {
            sendScrollMessage(
                    (interval + it.duration / autoScrollFactor * swipeScrollFactor).toLong())
        }
    }

    /**
     * start auto scroll.
     *
     * @param delayTimeInMills first scroll delay time.
     */
    fun startAutoScroll(delayTimeInMills: Int) {
        sendScrollMessage(delayTimeInMills.toLong())
    }

    /**
     * stop auto scroll.
     */
    fun stopAutoScroll() {
        myHandler?.removeMessages(SCROLL_WHAT)
    }

    /**
     * set the factor by which the duration of sliding animation will change while swiping.
     */
    fun setSwipeScrollDurationFactor(scrollFactor: Double) {
        swipeScrollFactor = scrollFactor
    }

    /**
     * set the factor by which the duration of sliding animation will change while auto scrolling.
     */
    fun setAutoScrollDurationFactor(scrollFactor: Double) {
        autoScrollFactor = scrollFactor
    }

    private fun sendScrollMessage(delayTimeInMills: Long) {
        /** remove messages before, keeps one message is running at most  */
        myHandler?.removeMessages(SCROLL_WHAT)
        myHandler?.sendEmptyMessageDelayed(SCROLL_WHAT, delayTimeInMills)
    }

    /**
     * set ViewPager scroller to change animation duration when sliding.
     */
    private fun setViewPagerScroller() {
        try {
            val scrollerField = ViewPager::class.java.getDeclaredField("mScroller")
            scrollerField.isAccessible = true
            val interpolatorField = ViewPager::class.java.getDeclaredField("sInterpolator")
            interpolatorField.isAccessible = true
            scroller = DurationScroller(context, interpolatorField[null] as Interpolator)
            scrollerField[this] = scroller
        } catch (e: IllegalAccessException) {
            Log.e(TAG, "setViewPagerScroller: ", e)
            //  Timber.e(e);
        } catch (e: NoSuchFieldException) {
            Log.e(TAG, "setViewPagerScroller: ", e)
            // Timber.e(e);
        }
    }

    /**
     * scroll only once.
     */
    fun scrollOnce() {
        val adapter = adapter
        var currentItem = currentItem
        val totalCount = adapter?.count ?: -100
        if (adapter == null || totalCount <= 1) {
            return
        }
        val nextItem = if (direction == LEFT) --currentItem else ++currentItem
        if (nextItem < 0) {
            if (isCycle) {
                setCurrentItem(totalCount - 1, isBorderAnimation)
            }
        } else if (nextItem == totalCount) {
            if (isCycle) {
                setCurrentItem(0, isBorderAnimation)
            }
        } else {
            setCurrentItem(nextItem, true)
        }
    }

    private class MyHandler(autoScrollViewPager: AutoScrollViewPager) : Handler() {
        private val autoScrollViewPager: WeakReference<AutoScrollViewPager>
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == SCROLL_WHAT) {
                val pager = autoScrollViewPager.get()
                pager?.let {
                    it.scroller?.setScrollDurationFactor(pager.autoScrollFactor)
                    it.scrollOnce()
                    it.scroller?.setScrollDurationFactor(pager.swipeScrollFactor)
                    it.scroller?.let {durationScroller ->
                        it.sendScrollMessage(pager.interval + durationScroller.duration)
                    }
                }
            }
        }

        init {
            this.autoScrollViewPager = WeakReference(autoScrollViewPager)
        }
    }

    /**
     * get auto scroll time in milliseconds, default is [.DEFAULT_INTERVAL].
     *
     * @return the interval.
     */
    fun getInterval(): Long {
        return interval
    }

    /**
     * set auto scroll time in milliseconds, default is [.DEFAULT_INTERVAL].
     *
     * @param interval the interval to set.
     */
    fun setInterval(interval: Long) {
        this.interval = interval
    }

    /**
     * get auto scroll direction.
     *
     * @return [.LEFT] or [.RIGHT], default is [.RIGHT]
     */
    fun getDirection(): Int {
        return if (direction == LEFT) LEFT else RIGHT
    }

    /**
     * set auto scroll direction.
     *
     * @param direction [.LEFT] or [.RIGHT], default is [.RIGHT]
     */
    fun setDirection(direction: Int) {
        this.direction = direction
    }

    /**
     * whether automatic cycle when auto scroll reaching the last or first item, default is true.
     *
     * @return the isCycle.
     */
    fun isCycleScroll(): Boolean {
        return isCycle
    }

    /**
     * set whether automatic cycle when auto scroll reaching the last or first item, default is true.
     *
     * @param isCycle the isCycle to set.
     */
    fun setCycle(isCycle: Boolean) {
        this.isCycle = isCycle
    }

    /**
     * whether stop auto scroll when touching, default is true.
     *
     * @return the stopScrollWhenTouch.
     */
    fun isStopScrollWhenTouch(): Boolean {
        return stopScrollWhenTouch
    }

    /**
     * set whether stop auto scroll when touching, default is true.
     */
    fun setStopScrollWhenTouch(stopScrollWhenTouch: Boolean) {
        this.stopScrollWhenTouch = stopScrollWhenTouch
    }

    /**
     * get how to process when sliding at the last or first item.
     *
     * @return the slideBorderMode [.SLIDE_BORDER_MODE_NONE],
     * [.SLIDE_BORDER_MODE_TO_PARENT],
     * [.SLIDE_BORDER_MODE_CYCLE], default is [.SLIDE_BORDER_MODE_NONE]
     */
    fun getSlideBorderMode(): Int {
        return slideBorderMode
    }

    /**
     * set how to process when sliding at the last or first item.
     *
     * @param slideBorderMode [.SLIDE_BORDER_MODE_NONE], [.SLIDE_BORDER_MODE_TO_PARENT],
     * [.SLIDE_BORDER_MODE_CYCLE], default is [.SLIDE_BORDER_MODE_NONE]
     */
    fun setSlideBorderMode(slideBorderMode: Int) {
        this.slideBorderMode = slideBorderMode
    }

    /**
     * whether animating when auto scroll at the last or first item, default is true.
     */
    fun isBorderAnimationEnabled(): Boolean {
        return isBorderAnimation
    }

    /**
     * set whether animating when auto scroll at the last or first item, default is true.
     */
    fun setBorderAnimation(isBorderAnimation: Boolean) {
        this.isBorderAnimation = isBorderAnimation
    }

    companion object {
        const val DEFAULT_INTERVAL = 4000
        const val LEFT = 0
        const val RIGHT = 1
        private const val TAG = "AutoScrollViewPager"
        const val SLIDE_BORDER_MODE_NONE = 0
        const val SLIDE_BORDER_MODE_CYCLE = 1
        const val SLIDE_BORDER_MODE_TO_PARENT = 2
        const val SCROLL_WHAT = 0
    }
}