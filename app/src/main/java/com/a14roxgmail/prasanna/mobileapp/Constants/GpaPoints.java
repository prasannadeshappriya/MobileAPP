package com.a14roxgmail.prasanna.mobileapp.Constants;

/**
 * Created by Prasanna Deshappriya on 1/18/2017.
 */
public class GpaPoints {
    private static final String A = "4.7";
    private static final String B = "3.1";
    private static final String C = "2.0";
    private static final String D = "1.7";

    public static String getPoint(String pointChar){
        if(pointChar.equals("A")){return A;}
        else if(pointChar.equals("B")){return B;}
        else if(pointChar.equals("C")){return C;}
        else if(pointChar.equals("D")){return D;}
        else{return "";}
    }
}
