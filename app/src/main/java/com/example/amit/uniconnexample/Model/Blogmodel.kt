package com.example.amit.uniconnexample

import android.util.Base64

import java.io.UnsupportedEncodingException

/**
 * Created by amit on 31/10/16.
 */

class Blogmodel {
    // private  String title;
    var desc: String? = null
    var image: String? = null
    var name: String? = null
    var propic: String? = null
    var like: Int = 0
    var unlike: Int = 0
    val cityname: String? = null
    val time: String
    val date: String
    val key: String

    constructor() {

    }

    constructor(key: String, desc: String, image: String, uname: String, propic: String, like: Int, unlike: Int, time: String, date: String) {
        // this.title = title;
        this.key = key
        this.desc = desc
        this.image = image
        this.name = uname
        this.propic = propic
        this.like = like
        this.unlike = unlike
        //    this.cityname=cityname;
        this.time = time
        this.date = date
    }
}
