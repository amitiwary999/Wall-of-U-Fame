package com.example.amit.uniconnexample.View

import android.content.Context
import android.os.Bundle
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.DialogFragment
import com.example.amit.uniconnexample.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * Created by Meera on 12,July,2020
 */
open class BaseBottomSheetDialog() : BottomSheetDialogFragment() {
    private var bottomSheet: FrameLayout? = null
    var behavior: BottomSheetBehavior<FrameLayout>? = null
    override fun onStart() {
        super.onStart()
        val dialog = dialog as BottomSheetDialog?
        bottomSheet = dialog!!.delegate.findViewById(R.id.design_bottom_sheet)
        if (bottomSheet != null) {
            val layoutParams = bottomSheet!!.layoutParams as CoordinatorLayout.LayoutParams
            layoutParams.height = getHeight()
            bottomSheet!!.layoutParams = layoutParams
            behavior = BottomSheetBehavior.from(bottomSheet)
            behavior?.setPeekHeight(getHeight())
            behavior?.setState(BottomSheetBehavior.STATE_EXPANDED)
        }
    }

    fun closeDialog(){
        behavior?.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.MyDialog)
        isCancelable = true
    }

    protected fun getHeight(): Int {
        return resources.displayMetrics.heightPixels/2
    }

    /**
     * dp转px
     *
     * @param context
     * @param dpValue
     * @return
     */
    protected fun dp2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }
}
