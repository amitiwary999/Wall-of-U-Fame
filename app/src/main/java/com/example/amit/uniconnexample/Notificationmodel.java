package com.example.amit.uniconnexample;

/**
 * Created by amit on 21/1/17.
 */

public class Notificationmodel {
    String img,txt,key,post_key;
    public Notificationmodel(String img,String txt,String key,String post_key){
        this.img=img;
        this.txt=txt;
        this.key=key;
        this.post_key=post_key;
    }

    public Notificationmodel() {
    }

    public String getImg() {
        return img;
    }

    public String getTxt() {
        return txt;
    }

    public String getKey() {
        return key;
    }

    public String getPost_key() {
        return post_key;
    }
}
