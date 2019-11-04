package com.example.amit.uniconnexample.Model

/**
 * Created by amit on 25/2/17.
 */

class BlogModel {
    var desc: String? = null
    var image: String? = null
    var name: String? = null
    var propic: String? = null
    var like: Int = 0
    var unlike: Int = 0
    val cityname: String
    val time: String
    val date: String
    val key: String
    val emailflag: String

    constructor() {}

    constructor(key: String, desc: String, image: String, uname: String, propic: String, like: Int, unlike: Int, time: String, date: String, emailflag: String, cityname: String) {
        this.desc = desc
        this.image = image
        this.name = uname
        this.propic = propic
        this.like = like
        this.unlike = unlike
        this.cityname = cityname
        this.time = time
        this.date = date
        this.key = key
        this.emailflag = emailflag
    }
}
