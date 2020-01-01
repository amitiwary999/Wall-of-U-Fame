package com.example.amit.uniconnexample.utils

import android.view.View
import android.view.ViewGroup

/**
 * Created by Meera on 01,January,2020
 */


object ViewGroupUtils {
    fun getParent(view: View): ViewGroup {
        return view.parent as ViewGroup
    }

    fun removeView(view: View) {
        val parent = getParent(view)
        parent?.removeView(view)
    }

    fun replaceView(currentView: View, newView: View) {
        val parent = getParent(currentView) ?: return
        val index = parent.indexOfChild(currentView)
        removeView(currentView)
        removeView(newView)
        parent.addView(newView, index)
    }
}