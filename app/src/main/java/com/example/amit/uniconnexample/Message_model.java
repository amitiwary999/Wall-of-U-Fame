package com.example.amit.uniconnexample;

/**
 * Created by amit on 23/12/16.
 */

public class Message_model {
    String image,msg;

    public Message_model(String image, String msg){
        this.image=image;
        this.msg=msg;
    }

    public String getImage() {
        return image;
    }

    public String getMsg() {
        return msg;
    }
}
