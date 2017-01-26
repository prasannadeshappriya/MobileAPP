package com.a14roxgmail.prasanna.mobileapp.Service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.a14roxgmail.prasanna.mobileapp.Constants.Constants;
import com.a14roxgmail.prasanna.mobileapp.DAO.CourseDAO;
import com.a14roxgmail.prasanna.mobileapp.DAO.userDAO;
import com.a14roxgmail.prasanna.mobileapp.ListAdapter.CalendarAdapter;
import com.a14roxgmail.prasanna.mobileapp.Model.Entry;
import com.a14roxgmail.prasanna.mobileapp.Model.User;
import com.a14roxgmail.prasanna.mobileapp.R;
import com.a14roxgmail.prasanna.mobileapp.Utilities.Sync;
import com.a14roxgmail.prasanna.mobileapp.Utilities.Utility;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Prasanna Deshappriya on 1/26/2017.
 */
public class SyncService extends Service {
    public static final int SERVICE_ID = 4097;
    private userDAO user_dao;
    private CourseDAO course_dao;
    private Sync sync;
    private String user_index;
    private Context context;
    private boolean isLogIn;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        user_dao = new userDAO(context);
        course_dao = new CourseDAO(context);

        User user = user_dao.getSignInUserDetails();
        if(user==null){
            isLogIn = false;
        }else{
            isLogIn = true;
        }
        user_index = user.getUserIndex();
        printlog("Service onCreate method triggered");
    }

    public boolean CheckInternetAccess(){
        //Ping is not working for emulator
        //Check weather the app is running on emulator or not
        if(Build.PRODUCT.matches(".*_?sdk_?.*")){
            return true;
        }else {
            return Utility.CheckInternetAccess();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(context, SyncService.class);
        final PendingIntent pIntent =
                PendingIntent.getService(context,
                        SyncService.SERVICE_ID,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pIntent);
        printlog("Service onDestroy method triggered");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        printlog("Check weather sync is needed");
        if(isLogIn) {
            Toast.makeText(context, user_index, Toast.LENGTH_LONG).show();
            if(CheckInternetAccess()){
                showEventNotification();
            }
        }
        return Service.START_STICKY;
    }

    public void showEventNotification(){
        getCalendarInfo calender_task = new getCalendarInfo(2016,11);
        calender_task.execute();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        printlog("Service onBind method triggered");
        return null;
    }

    public void printlog(String message){
        Log.i(Constants.LOG_TAG,message);
    }

    public class getCalendarInfo extends AsyncTask<Void,Void,Void> {
        private int month;
        private int year;
        private String password;
        private ProgressDialog pd;
        private ArrayList<Entry> arrAdapterArray;

        public getCalendarInfo(int year, int month){
            this.month = month; this.year = year;
            this.password = user_dao.getUserPassword(user_index);
            arrAdapterArray = new ArrayList<>();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Connection.Response response;
                response = Jsoup.connect(Constants.MOODLE_LOGIN_URL)
                        .method(Connection.Method.GET)
                        .execute();

                String session = response.cookie(Constants.MOODLE_COOKIE_ID);
                Map<String,String> map = response.cookies();

                response = Jsoup.connect(Constants.MOODLE_LOGIN_URL)
                        .cookie(Constants.MOODLE_COOKIE_ID,session)
                        .data(Constants.MOODLE_USERNAME_ID,user_index)
                        .data(Constants.MOODLE_PASSWORD_ID,password)
                        .cookies(response.cookies())
                        .method(Connection.Method.POST)
                        .execute();

                String url = Constants.MOODLE_CALENDAR_URL + "?view=month&course=1&cal_d=1&cal_m=" + month + "&cal_y=" + year;
                Log.i(Constants.LOG_TAG,"Moodle calendar view url, " + url);
                Document document = Jsoup.connect(url)
                        .cookie(Constants.MOODLE_COOKIE_ID,session)
                        .cookies(response.cookies()).get();

                Elements elements = document.select("a");
                boolean con = true;
                HashMap<Integer,Integer> list = new HashMap<>();
                ArrayList<String> arrList = new ArrayList<>();
                int date = 0;
                int count = 0;
                for (Element element : elements) {
                    if(!element.ownText().replace(" ","").equals("")) {
                        if (element.ownText().equals("Skip Navigation")) {
                            list.put(date,count);
                            break;
                        }
                        if(con){
                            if(Utility.isInteger(element.ownText())){
                                con = false;
                                date = Integer.parseInt(element.ownText());
                            }
                        }else{
                            if(Utility.isInteger(element.ownText())){
                                list.put(date,count);
                                count = 0;
                                date = Integer.parseInt(element.ownText());
                            }else{
                                arrList.add(element.ownText());
                                count++;
                            }
                        }
                    }
                }

                for (int n=0;n<32;n++) {
                    int c = 0;
                    if(list.containsKey(n)) {
                        for (int i = 0; i < 32; i++) {
                            if (i == n) {
                                int list_count = list.get(n);
                                Entry entry = new Entry(
                                        String.valueOf(n),
                                        String.valueOf(month)
                                );
                                ArrayList<String> arrEntry = new ArrayList<>();
                                for (int k = c; k < (c + list_count); k++) {
                                    Log.i(Constants.LOG_TAG, "Day " + n + " have " + arrList.get(k));
                                    arrEntry.add(arrList.get(k));
                                }
                                entry.setEvents(arrEntry);
                                arrAdapterArray.add(entry);
                                break;
                            }
                            if (list.containsKey(i)) {
                                int list_count = list.get(i);
                                c+=list_count;
                            }
                        }
                    }
                }

                

            } catch (Exception e) {
                pd.dismiss();
                Log.i(Constants.LOG_TAG, "Error caught :- " + e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(Constants.LOG_TAG,"Completed calendar Sync Process");
        }

    }
}
