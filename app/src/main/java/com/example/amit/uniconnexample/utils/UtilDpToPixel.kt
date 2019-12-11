package com.example.amit.uniconnexample.utils

import android.content.Context
import android.util.DisplayMetrics

/**
 * Created by Meera on 12,December,2019
 */
class UtilDpToPixel {
    companion object {
        @JvmStatic fun convertDpToPixel(dp: Float, context: Context): Float {
            val resources = context.resources
            val metrics = resources.displayMetrics
            return dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
        }
    }
}