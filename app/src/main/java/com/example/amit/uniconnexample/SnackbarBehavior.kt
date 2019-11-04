package com.example.amit.uniconnexample

import android.content.Context
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.snackbar.Snackbar
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

/**
 * Created by amit on 29/04/18.
 */

class SnackbarBehavior(context: Context, attrs: AttributeSet) : CoordinatorLayout.Behavior<ViewGroup>(context, attrs) {

    override fun layoutDependsOn(parent: CoordinatorLayout, child: ViewGroup, dependency: View): Boolean {
        return dependency is Snackbar.SnackbarLayout
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: ViewGroup, dependency: View): Boolean {
        val params = child.layoutParams as CoordinatorLayout.LayoutParams
        params.bottomMargin = parent.height - dependency.y.toInt()
        child.layoutParams = params
        return true
    }

    override fun onDependentViewRemoved(parent: CoordinatorLayout, child: ViewGroup, dependency: View) {
        val params = child.layoutParams as CoordinatorLayout.LayoutParams
        params.bottomMargin = 0
        child.layoutParams = params
    }
}