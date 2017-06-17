package com.a14roxgmail.prasanna.mobileapp.Service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.a14roxgmail.prasanna.mobileapp.Constants.Constants;
import com.a14roxgmail.prasanna.mobileapp.DAO.CourseDAO;
import com.a14roxgmail.prasanna.mobileapp.DAO.GradeDAO;
import com.a14roxgmail.prasanna.mobileapp.DAO.SyncVerifyDAO;
import com.a14roxgmail.prasanna.mobileapp.DAO.userDAO;
import com.a14roxgmail.prasanna.mobileapp.Model.Course;
import com.a14roxgmail.prasanna.mobileapp.Model.GPA;
import com.a14roxgmail.prasanna.mobileapp.Model.User;
import com.a14roxgmail.prasanna.mobileapp.Utilities.Lock;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by prasanna on 5/17/17.
 */

public class SyncServerService extends Service {
    public static final int SERVICE_ID = 4195;
    private Map<String,String> map;
    private Context context;

    private userDAO user_dao;
    private SyncVerifyDAO syncDAO;
    private CourseDAO course_dao;
    private GradeDAO grade_dao;

    private User user;  //to store the details of the login user
    private String user_index;
    private Lock lock;
    private boolean isLogIn; //To check the user has log in to the app or not

    //Print the log messages related to SyncServerService class
    private void printLog(String message){
        Log.i(Constants.LOG_TAG,"[SyncServerService] " + message);
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        printLog("onBind Method Triggered");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        user_dao = new userDAO(context);

        try {
            user = user_dao.getSignInUserDetails();
            if (user == null) {
                isLogIn = false;
            } else {
                isLogIn = true;
            }
            user_index = user.getUserIndex();
            printLog("onCreate Method Triggered [" + user_index + " is logged in]");
        }catch (Exception e){
            printLog("Error onCreate :- " + e.toString());
        }
    }

    @Override
    public void onDestroy() {
        printLog("onDestroy Method Triggered");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        printLog("onStartCommand Method Triggered");
        printLog("Service is start working");
        if(isLogIn) {
            //This service works only when user logged into the app
            syncDAO = new SyncVerifyDAO(getApplicationContext());
            if(syncDAO.isSyncDetailsExist(user_index)) {
                printLog("Data modified, sending new data to the server");
                CheckInternetAccess internetAccess = new CheckInternetAccess();
                internetAccess.execute();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private class CheckInternetAccess extends AsyncTask<Void,Void,Void>{
        private boolean con;

        @Override
        protected void onPreExecute() {con = false;}

        @Override
        protected Void doInBackground(Void... voids) {
            //Ping is not working for emulator
            //Check weather the app is running on emulator or not
            if(Build.PRODUCT.matches(".*_?sdk_?.*")){
                con = true;
            }else {
                Runtime runtime = Runtime.getRuntime();
                try {
                    Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
                    int exitValue = ipProcess.waitFor();
                    con = (exitValue == 0);
                } catch (Exception e) {
                    printLog("Error :- " + e.toString());
                    con = false;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(con) {
                printLog("Internet connection available");
                //Process should start here
                grade_dao = new GradeDAO(getApplicationContext());
                course_dao = new CourseDAO(getApplicationContext());
                lock = new Lock();
                NotifyServer notifyServer = new NotifyServer();
                notifyServer.execute();
            }else{
                printLog("No internet connection available");
                stopSelf();
            }
        }
    }

    //Each time, when user open the moodle app, app will send the user_id to the server
    //This is to get the statistics from the backend server
    //This process will work when internet connection available only
    private class NotifyServer extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            printLog("Notify Server Async task started");
            lock.setLock();
            map = new HashMap<>();
            map.put("user_id",user_index);
            map.put("first_name",user.getFirstName());
            map.put("last_name",user.getLastName());
            map.put("full_name",user.getFullName());
            PostRequest request = new PostRequest(
                    getApplicationContext(),
                    map,
                    Constants.SERVER_NOTIFY_URL,
                    "NOTIFY_SERVER"
            );
            request.execute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            long start_time = System.currentTimeMillis();
            long duration = 0;
            while (lock.isLock()) {
                long present_time = System.currentTimeMillis();
                duration = (present_time - start_time);
            }
            printLog("Time taken :- " + String.valueOf(duration));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            printLog("Notify Server Async task stopped");
            if(syncDAO.getSyncStatus(user_index).equals("0")) {
                SyncDetailsTask syncCoursesTask = new SyncDetailsTask();
                syncCoursesTask.execute();
            }else{
                printLog("Data is up-to-date, No sync is necessary");
                stopSelf();
            }
        }
    }

    private class SyncDetailsTask extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            printLog("SyncCoursesTask Server Async task started");
            lock.setLock();
            JSONObject object = new JSONObject();

            GPA gpa;
            int maxSemester;
            try {
                maxSemester = Integer.parseInt(course_dao.getMaxSemester(user_index));
            }catch (Exception e){
                maxSemester = 0;
            }
            JSONObject sgpaObject = new JSONObject();
            if(maxSemester>0) {
                try {
                    for (int i = 1; i <=maxSemester; i++){
                        if (grade_dao.isSGPAExist(user_index, String.valueOf(i))) {
                            gpa = grade_dao.getSGPA(user_index, String.valueOf(i));
                            JSONObject sgpaDetailsObject = new JSONObject();
                            if(gpa.getGpa().length()>=9){
                                sgpaDetailsObject.put("sgpa_value", gpa.getGpa().substring(0,8));
                            }else {
                                sgpaDetailsObject.put("sgpa_value", gpa.getGpa());
                            }
                            sgpaDetailsObject.put("semester", gpa.getSemester());
                            sgpaObject.put(String.valueOf(i),sgpaDetailsObject);
                        }
                    }
                    object.put("sgpa_object",sgpaObject);
                    object.put("index_number",user_index);
                }catch (Exception e){
                    printLog("Error on SyncCoursesTask[onPreExecute - courseObject - " + e.toString() + "][" + user_index + "]");
                }
            }
            try {
                if(grade_dao.isGPAExist(user_index)) {
                    gpa = grade_dao.getGPA(user_index);
                    object.put("gpa_object",gpa.getGpa());
                }
            }catch (Exception e){
                printLog("Error on SyncCoursesTask[onPreExecute - gpaObject - " + e.toString() + "][" + user_index + "]");
            }

            map = new HashMap<>();
            map.put("details", String.valueOf(object));
            printLog(String.valueOf(object));
            PostRequest request = new PostRequest(
                    getApplicationContext(),
                    map,
                    Constants.SERVER_SYNC_DATA_URL,
                    "SEND_DETAILS");
            request.execute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            long start_time = System.currentTimeMillis();
            long duration = 0;
            while (lock.isLock()) {
                long present_time = System.currentTimeMillis();
                duration = (present_time - start_time);
            }
            printLog("Time taken for SyncDetailsTask :- " + String.valueOf(duration));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            printLog("SyncDetailsTask Async task stopped");
            SyncCourseTask syncCourseTask = new SyncCourseTask();
            syncCourseTask.execute();
        }
    }
    private class SyncCourseTask extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            printLog("SyncCourseTask Server Async task started");
            lock.setLock();
            ArrayList<Course> arrCourseList;
            if(course_dao.isCoursesExist(user_index)) {
                arrCourseList = course_dao.getAllCoursesByUserId(user_index);
            }else{
                arrCourseList = new ArrayList<>();
            }
            JSONObject object = new JSONObject();
            JSONObject courseObjectArr = new JSONObject();
            int count=0;
            try {
                for (Course course : arrCourseList) {
                    count++;
                    JSONObject courseObject = new JSONObject();
                    courseObject.put("semester", course.getSemester());
                    courseObject.put("index", course.getUserIndex());
                    courseObject.put("credits", course.getCredits());
                    courseObject.put("code", course.getCourseCode());
                    courseObject.put("name", course.getCourseName());
                    courseObject.put("grade", course.getGrade());
                    courseObjectArr.put(String.valueOf(count),courseObject);
                }
                object.put("course_object",courseObjectArr);
            }catch (Exception e){
                printLog("Error on SyncCoursesTask[onPreExecute - courseObject - " + e.toString() + "][" + user_index + "]");
            }
            map = new HashMap<>();
            map.put("details", String.valueOf(object));
            map.put("user_index",user_index);
            PostRequest request = new PostRequest(
                    getApplicationContext(),
                    map,
                    Constants.SERVER_SYNC_COURSE_URL,
                    "SEND_DETAILS");
            request.execute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            long start_time = System.currentTimeMillis();
            long duration = 0;
            while (lock.isLock()) {
                long present_time = System.currentTimeMillis();
                duration = (present_time - start_time);
            }
            printLog("Time taken for SyncCourseTask :- " + String.valueOf(duration));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            syncDAO.updateSyncStatus(user_index,"1");
            printLog("SyncCourseTask Async task stopped");
            printLog("Sync Process is completed successfully");
            stopSelf();
        }
    }

    private class PostRequest extends AsyncTask<Void,Void,Void>{
        private String SERVER_URL;
        private String serverResponse;
        private Context context;
        private Map<String, String> patameters;
        private String METHOD;

        public PostRequest(Context context, Map<String, String> params, String url,String METHOD) {
            this.context = context;
            this.patameters = params;
            this.SERVER_URL = url;
            this.METHOD = METHOD;
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected Void doInBackground(final Void... params) {
            try {
                StringRequest request = new StringRequest(Request.Method.POST, SERVER_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                serverResponse = response;
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                serverResponse = error.toString();
                                printLog("Error caught during request transmission :- " + error.toString());
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> paramMap;
                        paramMap = patameters;
                        return paramMap;
                    }
                };

                request.setTag(Constants.LOG_TAG);
                RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(request);

                long start_time = System.currentTimeMillis();
                long duration = 0;
                while (serverResponse == null) {
                    long present_time = System.currentTimeMillis();
                    duration = (present_time - start_time);
                }
                printLog("Time taken :- " + String.valueOf(duration));
            } catch (Exception e) {
                serverResponse = "Internal Error :- " + e.toString();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            printLog("Response :- " + serverResponse);
            try {
                if (METHOD.equals("NOTIFY_SERVER")) {
                    JSONObject obj = new JSONObject(serverResponse);
                    if (obj.has("status")) {
                        printLog("Message from server :- " + obj.getString("status"));
                    } else {
                        printLog("Connection error, Please try again");
                    }
                    lock.unLock();
                } else if (METHOD.equals("SEND_DETAILS")) {
                    JSONObject obj = new JSONObject(serverResponse);
                    if (obj.has("status")) {
                        printLog("Message from server :- " + obj.getString("status"));
                    } else {
                        printLog("Connection error, Please try again");
                    }
                    lock.unLock();
                }
            } catch (JSONException e) {
                Log.i("TAG", "Invalid response from server :- " + e.toString());
                lock.unLock();
            }

        }
    }
}
