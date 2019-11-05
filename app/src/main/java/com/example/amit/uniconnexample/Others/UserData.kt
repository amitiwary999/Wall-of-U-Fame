package com.example.amit.uniconnexample.Others

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by amit on 30/10/16.
 */

class UserData : Parcelable {
    var phone: String? = null
    var name: String? = null
    var email: String? = null
    var photo: String? = null
    var clg: String? = null //extras


    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(this.phone)
        dest.writeString(this.name)
        //  dest.writeString(this.location);
        dest.writeString(this.photo)
        dest.writeString(this.clg)
    }

    constructor() {}

    protected constructor(`in`: Parcel) {
        this.phone = `in`.readString()
        this.name = `in`.readString()
        //  this.location = in.readString();
        this.photo = `in`.readString()
        this.clg = `in`.readString()
    }

    companion object {

        @JvmField val CREATOR: Parcelable.Creator<UserData> = object : Parcelable.Creator<UserData> {
            override fun createFromParcel(source: Parcel): UserData {
                return UserData(source)
            }

            override fun newArray(size: Int): Array<UserData?> {
                return arrayOfNulls(size)
            }
        }
    }
}