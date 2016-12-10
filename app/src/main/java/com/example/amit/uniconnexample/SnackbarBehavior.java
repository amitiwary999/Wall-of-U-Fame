package com.example.amit.uniconnexample;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by amit on 9/12/16.
 */

public class SnackbarBehavior extends CoordinatorLayout.Behavior<ViewGroup>{

    public SnackbarBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, ViewGroup child, View dependency) {
        return dependency instanceof Snackbar.SnackbarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, ViewGroup child, View dependency) {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        params.bottomMargin = parent.getHeight()- (int) dependency.getY();
        child.setLayoutParams(params);
        return true;
    }

    @Override
    public void onDependentViewRemoved(CoordinatorLayout parent, ViewGroup child, View dependency) {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        params.bottomMargin = 0;
        child.setLayoutParams(params);
    }
}
