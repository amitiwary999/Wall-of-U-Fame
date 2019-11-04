package com.example.amit.uniconnexample.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory

import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.util.Base64
import android.view.View

import com.afollestad.materialdialogs.MaterialDialog
import com.example.amit.uniconnexample.R

import java.io.ByteArrayOutputStream

/**
 * Created by amit on 30/10/16.
 */

object Utils {
    fun encodeToBase64(image: Bitmap, compressFormat: Bitmap.CompressFormat, quality: Int): String {
        val byteArrayOS = ByteArrayOutputStream()
        image.compress(compressFormat, quality, byteArrayOS)
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT)
    }

    fun decodeBase64(input: String): Bitmap {

        val decodedBytes = Base64.decode(input, 0)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }

    fun loadingDialog(context: Context): MaterialDialog {
        return MaterialDialog.Builder(context)
                .progress(true, 100)
                .content("Loading..")
                .build()
    }

    fun setUpToolbarBackButton(activity: AppCompatActivity, toolbar: Toolbar) {
        activity.setSupportActionBar(toolbar)
        activity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        activity.supportActionBar!!.setDisplayShowHomeEnabled(true)
        toolbar.navigationIcon = ContextCompat.getDrawable(activity, R.drawable.ic_arrow_back_white_18dp)
        toolbar.setNavigationOnClickListener { activity.onBackPressed() }
    }

}