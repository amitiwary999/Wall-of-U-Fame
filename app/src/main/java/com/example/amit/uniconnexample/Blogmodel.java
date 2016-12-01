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
    private int like;
    private int unlike;
    private String cityname;
    private String time,date;
    public Blogmodel(){

    }
    public Blogmodel( String desc, String image,String uname,String propic,int like,int unlike,String cityname,String time,String date) {
       // this.title = title;
        this.desc = desc;
        this.image = image;
        this.uname=uname;
        this.propic=propic;
        this.like=like;
        this.unlike=unlike;
        this.cityname=cityname;
        this.time=time;
        this.date=date;
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
    public void setLike(int like){
        this.like=like;
    }
 public int getLike(){
     return like;
 }
    public void setUnlike(int like){
        this.unlike=like;
    }
    public int getUnlike(){
        return unlike;
    }

    public String getCityname() {
        return cityname;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
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
