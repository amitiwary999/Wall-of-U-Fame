package com.example.amit.uniconnexample.Others;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by amit on 30/10/16.
 */

public class UserData implements Parcelable {
    public String phone,  name;
    public String email , photo ,clg; //extras


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.phone);
        dest.writeString(this.name);
      //  dest.writeString(this.location);
        dest.writeString(this.photo);
        dest.writeString(this.clg);
    }

    public UserData() {
    }

    protected UserData(Parcel in) {
        this.phone = in.readString();
        this.name = in.readString();
      //  this.location = in.readString();
        this.photo = in.readString();
        this.clg=in.readString();
    }

    public static final Creator<UserData> CREATOR = new Parcelable.Creator<UserData>() {
        @Override
        public UserData createFromParcel(Parcel source) {
            return new UserData(source);
        }

        @Override
        public UserData[] newArray(int size) {
            return new UserData[size];
        }
    };
}