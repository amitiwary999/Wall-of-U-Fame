package com.example.amit.uniconnexample

/**
 * Created by amit on 21/1/17.
 */

class Notificationmodel {
    var img: String? = null
        internal set
    var txt: String? = null
        internal set
    var key: String? = null
        internal set
    var post_key: String? = null
        internal set

    constructor(img: String, txt: String, key: String, post_key: String) {
        this.img = img
        this.txt = txt
        this.key = key
        this.post_key = post_key
    }

    constructor() {}
}
