package com.example.amit.uniconnexample;

/**
 * Created by amit on 9/12/16.
 */

public class Chatstartmodel {

private String msg1,msg2;
    public Chatstartmodel(){

    }
    public Chatstartmodel(String msg1,String msg2){
        this.msg1=msg1;
        this.msg2=msg2;
    }
    public void setMsg1(String msg1){
        this.msg1=msg1;
    }
    public void setMsg2(String msg2){
        this.msg2=msg2;
    }
    public String getMsg1(){
        return msg1;
    }
    public String getMsg2(){
        return msg2;
    }
}
