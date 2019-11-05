package com.example.amit.uniconnexample

import android.util.Base64

import java.io.UnsupportedEncodingException

/**
 * Created by amit on 31/10/16.
 */

class Blogmodel(
        var desc: String? = null,
        var image: String? = null,
        var name: String? = null,
        var propic: String? = null,
        var like: Int = 0,
        var unlike: Int = 0,
        val cityname: String? = null,
        val time: String ?=null,
        val date: String?=null,
        val key: String?=null
) {
}
