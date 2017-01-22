package com.a14roxgmail.prasanna.mobileapp.Constants;

/**
 * Created by Prasanna Deshappriya on 1/22/2017.
 */
public class Months {
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
}
