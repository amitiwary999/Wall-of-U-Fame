package com.example.amit.uniconnexample.MediaPicker

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Meera on 14,December,2019
 */
data class Media(
        val uri:String?=null,
        val mimeType: String?=null,
        val date: Long=0,
        val width: Int=0,
        val height: Int=0,
        val size: Long=0,
        val bucketId: String

) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString()!!,
            parcel.readString(),
            parcel.readLong(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readLong(),
            parcel.readString()) {
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest!!.writeString(uri)
        dest.writeString(mimeType)
        dest.writeLong(date)
        dest.writeInt(width)
        dest.writeInt(height)
        dest.writeLong(size)
        dest.writeString(bucketId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR: Parcelable.Creator<Media> {
        override fun createFromParcel(source: Parcel): Media {
            return Media(source)
        }

        override fun newArray(size: Int): Array<Media?> {
            return arrayOfNulls<Media>(size)
        }
    }
}