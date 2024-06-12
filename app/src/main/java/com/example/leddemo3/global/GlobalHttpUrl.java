package com.example.leddemo3.global;

public class GlobalHttpUrl {
    public static String url = "";
    public static Boolean isUrlNull(){
        if (url.length()==0||url==null){

            return false;
        }
        return true;
    }
}
