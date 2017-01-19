package com.a14roxgmail.prasanna.mobileapp.Constants;

/**
 * Created by Prasanna Deshappriya on 1/18/2017.
 */
public class GpaPoints {
    private static final String A_PLUS = "4.2";
    private static final String A = "4.0";
    private static final String A_MINUS = "3.7";
    private static final String B_PLUS = "3.3";
    private static final String B = "3.0";
    private static final String B_MINUS = "2.7";
    private static final String C_PLUS = "2.3";
    private static final String C = "2.0";
    private static final String C_MINUS = "1.5";
    private static final String D = "1.0";
    private static final String F = "0.0";

    public static String getPoint(String pointChar){
        if(pointChar.equals("A+")){return A_PLUS;}
        else if(pointChar.equals("A")){return A;}
        else if(pointChar.equals("A-")){return A_MINUS;}
        else if(pointChar.equals("B+")){return B_PLUS;}
        else if(pointChar.equals("B")){return B;}
        else if(pointChar.equals("B-")){return B_MINUS;}
        else if(pointChar.equals("C+")){return C_PLUS;}
        else if(pointChar.equals("C")){return C;}
        else if(pointChar.equals("C-")){return C_MINUS;}
        else if(pointChar.equals("D")){return D;}
        else if(pointChar.equals("F")){return F;}
        else{return "0.0";}
    }
}
