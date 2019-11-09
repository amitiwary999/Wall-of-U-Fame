package com.example.amit.uniconnexample

import android.util.Base64

import java.io.UnsupportedEncodingException

/**
 * Created by amit on 31/10/16.
 */

class PostBlogModel(
        val id: String,
        var desc: String? = null,
        var image: String? = null,
        val cityname: String? = null,
        val date: String?=null
) {
}
