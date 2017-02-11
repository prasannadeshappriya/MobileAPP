package com.a14roxgmail.prasanna.mobileapp.Constants;

/**
 * Created by Prasanna Deshappriya on 1/22/2017.
 */
public abstract class Months {
    public static String getMonth(String value){
        int index = Integer.parseInt(value);
        if(index==1){return "January";}
        else if(index==2){return "February";}
        else if(index==3){return "March";}
        else if(index==4){return "April";}
        else if(index==5){return "May";}
        else if(index==6){return "June";}
        else if(index==7){return "July";}
        else if(index==8){return "August";}
        else if(index==9){return "September";}
        else if(index==10){return "October";}
        else if(index==11){return "November";}
        else if(index==12){return "December";}
        else{return"";}
    }

    public static int getMonthIndex(String value){
        if(value.equals("January")){return 1;}
        else if(value.equals("February")){return 2;}
        else if(value.equals("March")){return 3;}
        else if(value.equals("April")){return 4;}
        else if(value.equals("May")){return 5;}
        else if(value.equals("June")){return 6;}
        else if(value.equals("July")){return 7;}
        else if(value.equals("August")){return 8;}
        else if(value.equals("September")){return 9;}
        else if(value.equals("October")){return 10;}
        else if(value.equals("November")){return 11;}
        else if(value.equals("December")){return 12;}
        else{return 0;}
    }
}
