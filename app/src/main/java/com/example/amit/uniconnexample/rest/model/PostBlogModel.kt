package com.example.amit.uniconnexample

import android.util.Base64

import java.io.UnsupportedEncodingException

/**
 * Created by amit on 31/10/16.
 */

class PostBlogModel(
        val postId: String,
        var desc: String? = null,
        var mediaUrl: String? = null,
        var mediaThumbUrl: String?=null,
        var mimeType: String?=null
) {
}
