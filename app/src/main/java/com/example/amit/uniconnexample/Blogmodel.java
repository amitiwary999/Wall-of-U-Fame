package com.example.amit.uniconnexample;

import android.util.Base64;

import java.io.UnsupportedEncodingException;

/**
 * Created by amit on 31/10/16.
 */

public class Blogmodel {
   // private  String title;
    private String desc;
    private String image;
    private String uname;
    private String propic;

    public Blogmodel(){

    }
    public Blogmodel( String desc, String image,String uname,String propic) {
       // this.title = title;
        this.desc = desc;
        this.image = image;
        this.uname=uname;
        this.propic=propic;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

  /*  public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }*/

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return uname;
    }

    public void setName(String uname) {
        this.uname = uname;
    }

    public String getPropic() {
      /*  byte[] data = null;
        try {
            data = propic.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        String profile= Base64.encodeToString(data,Base64.DEFAULT);*/
        return propic;
    }

    public void setPropic(String propic) {
        this.propic = propic;
    }
}
