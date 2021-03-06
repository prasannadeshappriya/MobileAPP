package com.a14roxgmail.prasanna.mobileapp.Utilities;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.a14roxgmail.prasanna.mobileapp.Constants.Constants;
import com.a14roxgmail.prasanna.mobileapp.Constants.Months;
import com.a14roxgmail.prasanna.mobileapp.Fragment.CalendarFragment;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;

/**
 * Created by Prasanna Deshappriya on 1/17/2017.
 */
public abstract class Utility{
    public static boolean setListViewHeightBasedOnItems(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {
            int numberOfItems = listAdapter.getCount();
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                float px = 500 * (listView.getResources().getDisplayMetrics().density);
                item.measure(View.MeasureSpec.makeMeasureSpec((int)px, View.MeasureSpec.AT_MOST), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                totalItemsHeight += item.getMeasuredHeight();
            }
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);
            int totalPadding = listView.getPaddingTop() + listView.getPaddingBottom();
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight + totalPadding+ 100;
            listView.setLayoutParams(params);
            listView.requestLayout();
            return true;

        } else {
            return false;
        }

    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }
        return true;
    }

    public static HashMap<String,String> XMLPaser(String response){
        HashMap<String,String> map = new HashMap();
        int count = 0;
        boolean con = false;
        for(int i=0; i<response.length(); i++){
            String tmp1 = response.substring(i,i+8).replace(" ","").toLowerCase();
            if (tmp1.equals("keyname")){
                String key="";
                String value="";
                boolean key_or_value = true; //true for key
                for(int j=0; j<response.length(); j++){
                    String ret2 = response.substring(i+j+8,i+j+9);
                    if(ret2.equals(">")){
                        if(key_or_value) {
                            key = response.substring(i + 10, i + 7 + j);
                            Log.i(Constants.LOG_TAG, "Key :- " + key);
                            key_or_value = false;
                        }else{
                            String tmp3 = response.substring(i+j+8);
                            for(int k=0; k<response.length(); k++){
                                ret2 = tmp3.substring(k,k+1);
                                if(ret2.equals("<")){
                                    value = response.substring(i+j+9, i+j+k+8);
                                    Log.i(Constants.LOG_TAG, "Value :- " + value);
                                    break;
                                }
                            }
                            map.put(key,value);
                            break;
                        }
                    }
                }
                count++;
                if(count>=7){con = true;}
            }
            if(con){break;}
        }
        return map;
    }

    public static double roundTwoDecimals(double d)
    {
        DecimalFormat twoDForm = new DecimalFormat("#.####");
        return Double.valueOf(twoDForm.format(d));
    }

    public static double roundInToDecimals(double d)
    {
        DecimalFormat twoDForm = new DecimalFormat("#.#");
        return Double.valueOf(twoDForm.format(d));
    }

    public static String getCurrentDate(){
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String month = String.valueOf(calendar.get(Calendar.MONTH)+1);
        String date = String.valueOf(calendar.get(Calendar.DATE));
        String ret = year + "-" + Months.getMonth(month) + "-" + date;
        return ret;
    }

    public static String getDate(){
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        String date = String.valueOf(calendar.get(Calendar.DATE));
        String ret = date;
        return ret;
    }

    public static String getYear(){
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        String Year = String.valueOf(calendar.get(Calendar.YEAR));
        String ret = Year;
        return ret;
    }

    public static String getMonth(){
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        String Month = String.valueOf(calendar.get(Calendar.MONTH)+1);
        String ret = Month;
        return ret;
    }
}
