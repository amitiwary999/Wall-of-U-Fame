package com.example.amit.uniconnexample;

/**
 * Created by amit on 23/12/16.
 */

public class Message_model {
    String image,msg,name;

    public Message_model(String image, String msg,String name){
        this.image=image;
        this.msg=msg;
        this.name=name;
    }

    public String getImage() {
        return image;
    }

    public String getMsg() {
        return msg;
    }

    public String getName() {
        return name;
    }
}
