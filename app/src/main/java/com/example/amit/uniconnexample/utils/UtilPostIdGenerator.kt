package com.example.amit.uniconnexample.utils

import com.example.amit.uniconnexample.Others.CommonString

/**
 * Created by Meera on 10,November,2019
 */
class UtilPostIdGenerator {
    companion object{
        private const val MAX_ID = 9999

        @Synchronized
        fun getIncrement(): String{
            val returnedId = PrefManager.getInt(CommonString.MESSAGE_ID_CONSTANT, 0)
            val nextId = if (returnedId + 1 > MAX_ID) 0 else returnedId + 1

            PrefManager.putInt(CommonString.MESSAGE_ID_CONSTANT, nextId)
            val format = String.format("%04d", returnedId)
            return format
        }

        fun generatePostId():String{
            return ""+System.currentTimeMillis()+"_"+ getIncrement()+"_"+PrefManager.getString(CommonString.USER_ID, "user")
        }
    }
}