package com.example.amit.uniconnexample

/**
 * Created by amit on 23/12/16.
 */

class Message_model {
    var image: String? = null
        internal set
    var msg: String? = null
        internal set
    var name: String? = null
        internal set
    var key: String? = null
        internal set

    constructor() {

    }

    constructor(image: String, msg: String, name: String, key: String) {
        this.image = image
        this.msg = msg
        this.name = name
        this.key = key
    }
}
