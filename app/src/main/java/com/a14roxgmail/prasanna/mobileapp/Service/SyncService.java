package com.a14roxgmail.prasanna.mobileapp.Service;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import com.a14roxgmail.prasanna.mobileapp.Constants.Constants;
import com.a14roxgmail.prasanna.mobileapp.DAO.CourseDAO;
import com.a14roxgmail.prasanna.mobileapp.DAO.NotificationDAO;
import com.a14roxgmail.prasanna.mobileapp.DAO.userDAO;
import com.a14roxgmail.prasanna.mobileapp.Model.Entry;
import com.a14roxgmail.prasanna.mobileapp.Model.Notification;
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
    private NotificationDAO notification_dao;
    private Sync sync;
    private String user_index;
    private Context context;
    private boolean isLogIn;
    private ArrayList<Entry> arrAdapterArray;
    private static boolean Lock = false;

    private static boolean setLock(){
        if(Lock){
            return false;
        }else{
            Lock = true;
            return true;
        }
    }

    private static boolean unLock(){
        if(Lock){
            Lock = false;
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        user_dao = new userDAO(context);
        course_dao = new CourseDAO(context);
        notification_dao = new NotificationDAO(context);

        try {
            User user = user_dao.getSignInUserDetails();
            if (user == null) {
                isLogIn = false;
            } else {
                isLogIn = true;
            }
            user_index = user.getUserIndex();
            printlog("Service onCreate method triggered");
        }catch (Exception e){
            printlog("Error onCreate :- " + e.toString());
        }
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
        String date = Utility.getDate();
        if (notification_dao.isNotificationAvailable(user_index, date)) {
            AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarm.set(
                    alarm.RTC_WAKEUP,
                    System.currentTimeMillis() + (1000 * 60 * 60 * 2),
                    PendingIntent.getService(context, SERVICE_ID, new Intent(context, SyncService.class), 0)
            );
            printlog("Service schedule to run in  2 hrs again");
        }else{
            AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarm.set(
                    alarm.RTC_WAKEUP,
                    System.currentTimeMillis() + (1000 * 10),
                    PendingIntent.getService(context, SERVICE_ID, new Intent(context, SyncService.class), 0)
            );
            printlog("Service schedule to run in 10 sec again");
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        printlog("Service started");
        String date = Utility.getDate();
        try {
            synchronized (this) {
                if (isLogIn) {
                    //service will only work when some user already sign in only
                    //print log for more information
                    Log.i(Constants.LOG_TAG, "Server is working [Log in user_id :- " + user_index + "]");

                    //check for internet connection
                    if (CheckInternetAccess()) {
                        int year = Integer.parseInt(Utility.getYear());
                        int month = Integer.parseInt(Utility.getMonth());
                        printlog("Current status, month :- " + month + ", year :- " + year);
                        getCalendarInfo calender_task = new getCalendarInfo(year, month);
                        calender_task.execute();
                    }

                    //stop the service
                    stopSelf();
                }
            }
        }catch (Exception e){
            printlog("Error :- " + e.toString());
        }

        return Service.START_STICKY;
    }

    public void showEventNotification(String date){
        synchronized (this) {
            if(setLock()) {
                ArrayList<String> eventList = new ArrayList<>();
                for (Entry entry : arrAdapterArray) {
                    if (date.equals(entry.getDay())) {
                        eventList = entry.getEvents();
                        break;
                    }
                }
                if (notification_dao.isNotificationAvailable(user_index, date)) {
                    ArrayList<Notification> arrNotification = notification_dao.getNotification(user_index);

                    if (!eventList.isEmpty()) {
                        if (arrNotification.size() != eventList.size()) {
                            ArrayList<String> newEventList = new ArrayList<>();
                            for (String event : eventList) {
                                boolean con = true;
                                for (Notification dbEvent : arrNotification) {
                                    if (dbEvent.getEvent().equals(event)) {
                                        con = false;
                                        break;
                                    }
                                }
                                if (con) {
                                    newEventList.add(event);
                                }
                            }
                            if (!newEventList.isEmpty()) {
                                for (String event : newEventList) {
                                    notification_dao.addNotification(
                                            new Notification(
                                                    user_index,
                                                    date,
                                                    event
                                            )
                                    );
                                }
                                //Show notification method for newEventList
                                showNotification(newEventList);
                            }
                        }
                    }
                } else {
                    notification_dao.deleteNotification(user_index);
                    if (eventList.isEmpty()) {
                        notification_dao.addNotification(
                                new Notification(
                                        user_index,
                                        date,
                                        "No Events Available"
                                )
                        );
                    } else {
                        for (String event : eventList) {
                            notification_dao.addNotification(
                                    new Notification(
                                            user_index,
                                            date,
                                            event
                                    )
                            );
                        }
                        //Show notification method for newEventList
                        showNotification(eventList);
                    }
                }
                unLock();
            }
        }
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
                    Map<String, String> map = response.cookies();

                    response = Jsoup.connect(Constants.MOODLE_LOGIN_URL)
                            .cookie(Constants.MOODLE_COOKIE_ID, session)
                            .data(Constants.MOODLE_USERNAME_ID, user_index)
                            .data(Constants.MOODLE_PASSWORD_ID, password)
                            .cookies(response.cookies())
                            .method(Connection.Method.POST)
                            .execute();

                    String url = Constants.MOODLE_CALENDAR_URL + "?view=month&course=1&cal_d=1&cal_m=" + month + "&cal_y=" + year;
                    Log.i(Constants.LOG_TAG, "Moodle calendar view url, " + url);
                    Document document = Jsoup.connect(url)
                            .cookie(Constants.MOODLE_COOKIE_ID, session)
                            .cookies(response.cookies()).get();

                    Elements elements = document.select("a");
                    boolean con = true;
                    HashMap<Integer, Integer> list = new HashMap<>();
                    ArrayList<String> arrList = new ArrayList<>();
                    int date = 0;
                    int count = 0;
                    for (Element element : elements) {
                        if (!element.ownText().replace(" ", "").equals("")) {
                            if (element.ownText().equals("Skip Navigation")) {
                                list.put(date, count);
                                break;
                            }
                            if (con) {
                                if (Utility.isInteger(element.ownText())) {
                                    con = false;
                                    date = Integer.parseInt(element.ownText());
                                }
                            } else {
                                if (Utility.isInteger(element.ownText())) {
                                    list.put(date, count);
                                    count = 0;
                                    date = Integer.parseInt(element.ownText());
                                } else {
                                    arrList.add(element.ownText());
                                    count++;
                                }
                            }
                        }
                    }

                    for (int n = 0; n < 32; n++) {
                        int c = 0;
                        if (list.containsKey(n)) {
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
                                    c += list_count;
                                }
                            }
                        }
                    }

                } catch (Exception e) {
                    Log.i(Constants.LOG_TAG, "Error caught :- " + e.toString());
                }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(Constants.LOG_TAG,"Completed calendar Sync Process");
            String date = Utility.getDate();
            showEventNotification(date);
        }

    }

    private void showNotification(ArrayList<String> events){
        if(!events.isEmpty()){
            NotificationCompat.Builder builder =
                    (NotificationCompat.Builder) new NotificationCompat.Builder(getApplicationContext())
                            .setSmallIcon(R.drawable.ic_launcher_theme);

            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
            inboxStyle.setBigContentTitle("Moodle Events");

            boolean con = true;
            for (int i=0; i < events.size(); i++) {
                if(events.get(i).equals("No Events Available")) {
                    if(events.size()==1){con=false;}
                }else{
                    inboxStyle.addLine(events.get(i));
                }
            }

            if(con) {
                builder.setStyle(inboxStyle);

                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                manager.notify(0, builder.build());
            }
        }
    }
}
