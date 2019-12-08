package com.example.amit.uniconnexample.utils

import android.text.format.DateFormat
import java.util.*

/**
 * Created by Meera on 08,December,2019
 */
class DateUtils {
    companion object{
        fun getDateFromUTCTimestamp(mTimestamp: String, dateFormat: String?): String {
            var date: String? = null
            try {
                val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                cal.timeInMillis = mTimestamp.toLong()
                date = DateFormat.format(dateFormat, cal.timeInMillis).toString()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return date?:""
        }
    }
}