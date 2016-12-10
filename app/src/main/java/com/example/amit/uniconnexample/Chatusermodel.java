package com.example.amit.uniconnexample;

/**
 * Created by amit on 7/12/16.
 */

public class Chatusermodel {
    private String name;
    private String propic;

    public Chatusermodel(){

    }
    public Chatusermodel(String name,String propic){
        this.name=name;
        this.propic=propic;
    }
    public String getName(){
        return name;
    }

    public String getPropic() {
        return propic;
    }
}
