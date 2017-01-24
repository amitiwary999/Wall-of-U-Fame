package com.example.amit.uniconnexample;

/**
 * Created by amit on 24/1/17.
 */

public class Notifmsgmodel {
    //private int txt,name;
    private String txt,name;

    public Notifmsgmodel(String txt, String name) {
        this.txt = txt;
        this.name = name;
    }

    public Notifmsgmodel() {
    }

    public String getName() {
        return name;
    }

    public String getTxt() {
        return txt;
    }

}
