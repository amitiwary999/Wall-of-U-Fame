package com.example.amit.uniconnexample;

/**
 * Created by amit on 21/1/17.
 */

public class Notificationmodel {
    String img,txt;
    public Notificationmodel(String img,String txt){
        this.img=img;
        this.txt=txt;
    }

    public Notificationmodel() {
    }

    public String getImg() {
        return img;
    }

    public String getTxt() {
        return txt;
    }
}
