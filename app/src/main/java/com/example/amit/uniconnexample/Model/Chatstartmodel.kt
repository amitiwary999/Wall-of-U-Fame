package com.example.amit.uniconnexample

/**
 * Created by amit on 9/12/16.
 */

class Chatstartmodel {

    var msg1: String? = null
    var msg2: String? = null
    var time1: String? = null
        private set
    val time2: String

    constructor() {

    }

    constructor(msg1: String, time1: String) {
        this.msg1 = msg1
        this.time1 = time1
    }

    constructor(msg1: String, msg2: String, time1: String, time2: String) {
        this.msg1 = msg1
        this.msg2 = msg2
        this.time1 = time1
        this.time2 = time2
    }
}
