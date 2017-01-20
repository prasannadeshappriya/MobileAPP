package com.a14roxgmail.prasanna.mobileapp.Constants;

import java.util.ArrayList;

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



    public static ArrayList<String> getGradeList(){
        ArrayList<String> arrGrades = new ArrayList<>();
        arrGrades.add("Non - GPA");
        arrGrades.add("A+");
        arrGrades.add("A");
        arrGrades.add("A-");
        arrGrades.add("B+");
        arrGrades.add("B");
        arrGrades.add("B-");
        arrGrades.add("C+");
        arrGrades.add("C");
        arrGrades.add("C-");
        arrGrades.add("D");
        arrGrades.add("F");
        return arrGrades;
    }

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

    public static int getIndex(String pointChar){
        if(pointChar.equals("A+")){return 1;}
        else if(pointChar.equals("A")){return 2;}
        else if(pointChar.equals("A-")){return 3;}
        else if(pointChar.equals("B+")){return 4;}
        else if(pointChar.equals("B")){return 5;}
        else if(pointChar.equals("B-")){return 6;}
        else if(pointChar.equals("C+")){return 7;}
        else if(pointChar.equals("C")){return 8;}
        else if(pointChar.equals("C-")){return 9;}
        else if(pointChar.equals("D")){return 10;}
        else if(pointChar.equals("F")){return 11;}
        else if(pointChar.equals("Non - GPA")){return 0;}
        else{return 0;}
    }
}
