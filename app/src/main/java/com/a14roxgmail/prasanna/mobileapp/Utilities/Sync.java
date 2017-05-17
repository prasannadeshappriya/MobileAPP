package com.a14roxgmail.prasanna.mobileapp.Utilities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Toast;

import com.a14roxgmail.prasanna.mobileapp.Constants.Constants;
import com.a14roxgmail.prasanna.mobileapp.Constants.Token;
import com.a14roxgmail.prasanna.mobileapp.DAO.CourseDAO;
import com.a14roxgmail.prasanna.mobileapp.DAO.SettingsDAO;
import com.a14roxgmail.prasanna.mobileapp.DAO.userDAO;
import com.a14roxgmail.prasanna.mobileapp.Model.Course;
import com.a14roxgmail.prasanna.mobileapp.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Created by Prasanna Deshappriya on 1/23/2017.
 */
public class Sync {
    private ArrayList<Course> arrCourseList;
    private ArrayList<Course> arrSyncCourseList;
    private String user_index;
    private CourseDAO course_dao;
    private userDAO user_dao;
    private SettingsDAO settings_dao;
    private Context context;
    private String new_password;
    private Activity activity;
    private ProgressDialog pd;

    public Sync(Context context,
                String user_index,
                ArrayList<Course> arrCourseList,
                CourseDAO course_dao,
                Activity activity,
                String new_password) {
        this.new_password = new_password;
        this.activity = activity;
        this.user_index = user_index;
        this.arrCourseList = arrCourseList;
        this.course_dao = course_dao;
        this.context = context;
        user_dao = new userDAO(context);
        settings_dao = new SettingsDAO(context);
        pd = new ProgressDialog(context, R.style.AppTheme_Dark_Dialog);
    }

    public void startSyncProcess() {
        CheckInternetAccess checkInternetAccess = new CheckInternetAccess("startSyncProcess");
        checkInternetAccess.execute();
    }

    public boolean isSyncNeed(){
        //For autoSS
        return true;
    }

    public class getCourseInfo extends AsyncTask<Void,Void,Void> {
        private String username;
        private String password;
        private Elements elements;
        private ProgressDialog pd;

        public getCourseInfo(ProgressDialog pd){
            username = user_index;
            String pass = user_dao.getUserPassword(username);
            password = user_dao.getUserPassword(username);
            this.pd = pd;
            arrSyncCourseList = new ArrayList<>();
        }

        @Override
        protected void onPreExecute() {
            pd.setIndeterminate(true);
            pd.setCanceledOnTouchOutside(false);
            pd.setMessage("Sync Course Info..");
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Connection.Response response;
                response = Jsoup.connect(Constants.LMS_LOGIN_URL)
                        .method(Connection.Method.GET)
                        .execute();

                String session = response.cookie(Constants.LMS_COOKIE_ID);

                response = Jsoup.connect(Constants.LMS_LOGIN_URL)
                        .cookie(Constants.LMS_COOKIE_ID,session)
                        .data(Constants.LMS_USERNAME_ID,username)
                        .data(Constants.LMS_PASSWORD_ID,password)
                        .data(Constants.LMS_LOGIN_ID,Constants.LMS_LOGIN_ID_VALUE)
                        .cookies(response.cookies())
                        .method(Connection.Method.POST)
                        .execute();

                Document document = Jsoup.connect(Constants.LMS_ENROLMENTS_URL)
                        .cookie(Constants.LMS_COOKIE_ID,session)
                        .cookies(response.cookies()).get();

                elements = document.getElementsByClass(Constants.LMS_SOURCE_TAG);
            } catch (Exception e) {
                Log.i(Constants.LOG_TAG, "Error cought while getting course data :- " + e.toString());
            }

            int i=0;
            Course course = null;
            for (Element element : elements) {
                if (!element.ownText().replace(" ","").equals("")) {
                    if(i==0) {
                        course = new Course(username);
                        String semester = element.ownText().substring(element.ownText().length()-1);
                        Log.i(Constants.LOG_TAG, "Semester :- '" + semester + "'");
                        course.setSemester(semester);
                        i++;
                    }else if(i==1) {
                        Log.i(Constants.LOG_TAG, "Module Code :- " + element.ownText());
                        course.setCourseCode(element.ownText());
                        i++;
                    }else if(i==2) {
                        Log.i(Constants.LOG_TAG, "Module :- " + element.ownText());
                        course.setCourseName(element.ownText());
                        i++;
                    }else if(i==3) {
                        Log.i(Constants.LOG_TAG, "Credits :- " + element.ownText());
                        course.setCredits(element.ownText());
                        arrSyncCourseList.add(course);
                        i=0;
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if(arrSyncCourseList.size()==arrCourseList.size()){
                pd.dismiss();
                Toast.makeText(context,"Data up to date", Toast.LENGTH_SHORT).show();
                String a = Utility.getCurrentDate();
                settings_dao.updateLastSyncDate(user_index,Utility.getCurrentDate());
            }else{
                syncDatabase();
            }
        }

    }

    private void syncDatabase() {
        Log.i(Constants.LOG_TAG, "Sync process started for user " + user_index);
        ArrayList<Integer> arrExistItemIndex = new ArrayList<>();
        for (int i = 0; i <arrSyncCourseList.size(); i++){
            Course course = arrSyncCourseList.get(i);

            if(contains(1,arrCourseList,course,null,0)){
                arrExistItemIndex.add(getIndex(arrCourseList,course));
            }else{
                course_dao.addCourse(course);
                Log.i(Constants.LOG_TAG, course.getCourseName() + " is not in the database");
                arrCourseList.add(course);
                arrExistItemIndex.add(getIndex(arrCourseList,course));
            }
        }
        for(int i=0; i<arrCourseList.size(); i++){
            if(!contains(2,null,null,arrExistItemIndex,i)){
                course_dao.deleteCourse(user_index,arrCourseList.get(i).getCourseCode());
                Log.i(Constants.LOG_TAG, arrCourseList.get(i).getCourseName() + " should not be in the database");
                arrCourseList.remove(i);
            }
        }
        settings_dao.updateLastSyncDate(user_index,Utility.getCurrentDate());
        Log.i(Constants.LOG_TAG, "Sync process completed for user " + user_index);
        pd.dismiss();
        Toast.makeText(context, "Sync completed", Toast.LENGTH_SHORT).show();
    }

    private int getIndex(ArrayList<Course> listCourse, Course course){
        for(int i=0; i<listCourse.size(); i++){
            if(listCourse.get(i).getCourseCode().equals(course.getCourseCode())){
                return i;
            }
        }
        return -1;
    }

    private boolean contains(int method,
                             ArrayList<Course> listCourse, Course course,
                             ArrayList<Integer> listInt, int index) {
        if(method==1) {
            for (Course item : listCourse) {
                if (item.getCourseCode().equals(course.getCourseCode())) {
                    return true;
                }
            }
            return false;
        }else{
            for (int item : listInt) {
                if (index==item) {
                    return true;
                }
            }
            return false;
        }
    }

    public void getToken(){
        final ServerRequest request = new ServerRequest(3, activity);
        request.set_server_url(Constants.SERVER_GET_TOKEN);
        request.setParams(user_index, "username");
        request.setParams(user_dao.getUserPassword(user_index), "password");
        request.setParams("moodle_mobile_app","service");
        try {
            String req = request.sendPostRequest();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        pd.setIndeterminate(true);
        pd.setCanceledOnTouchOutside(false);
        pd.setMessage("Sync user details..");

        CountDownTimer timer = new CountDownTimer(2000, 1000) {
            @Override
            public void onFinish() {
                process_token_response(request);
                Log.i(Constants.LOG_TAG, "Sync Class - OnFinish method triggered");
                //pd.dismiss();
            }

            @Override
            public void onTick(long millisLeft) {}
        };
        timer.start();
    }


    private void process_token_response(ServerRequest request) {
        String response = request.getResponse();
        if (response.equals("")) {
            Toast.makeText(context, "Server timeout", Toast.LENGTH_LONG).show();
        } else {
            Log.i(Constants.LOG_TAG, "Server Response :- " + response);
            JSONObject objResponse = null;
            try {
                objResponse = new JSONObject(response);
                if (objResponse.has("error")){
                    Log.i(Constants.LOG_TAG, "Error :- " + objResponse.getString("error"));
                    Toast.makeText(context,"Password update required",Toast.LENGTH_LONG).show();
                    pd.dismiss();
                }else {
                    if(objResponse.has("token")){
                        Token.setToken(objResponse.getString("token"));
                        Log.i(Constants.LOG_TAG, "Newest Token is :- " + Token.getToken());
                        if(!user_dao.getToken(user_index).equals(Token.getToken())){
                            user_dao.updateToken(Token.getToken(),user_index);
                            Log.i(Constants.LOG_TAG, "New token value received and updated");
                        }
                        getCourseInfo syncCourseInfo = new getCourseInfo(pd);
                        syncCourseInfo.execute();
                    }else{
                        Toast.makeText(context,"Something went wrong",Toast.LENGTH_LONG).show();
                        pd.dismiss();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public void setNew_password(String new_password){
        this.new_password = new_password;
    }

    public void updatePassword(){
        if(new_password.equals(user_dao.getUserPassword(user_index))){
            Toast.makeText(context,"Up to date", Toast.LENGTH_SHORT).show();
            Log.i(Constants.LOG_TAG,"Password update does not required");
        }else {
            CheckInternetAccess checkInternetAccess = new CheckInternetAccess("updatePassword");
            checkInternetAccess.execute();
        }
    }

    private class CheckInternetAccess extends AsyncTask<Void,Void,Void>{
        private boolean con = false;
        private String method;
        public CheckInternetAccess(String method){
            this.method = method;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            if(con) {
                if(method.equals("updatePassword")) {
                    final ServerRequest request = new ServerRequest(3, activity);
                    request.set_server_url(Constants.SERVER_GET_TOKEN);
                    request.setParams(user_index, "username");
                    request.setParams(new_password, "password");
                    request.setParams("moodle_mobile_app", "service");
                    try {
                        String req = request.sendPostRequest();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    pd.setIndeterminate(true);
                    pd.setCanceledOnTouchOutside(false);
                    pd.setMessage("Authenticating..");

                    CountDownTimer timer = new CountDownTimer(2000, 1000) {
                        @Override
                        public void onFinish() {
                            process_updatePassword_response(request);
                            Log.i(Constants.LOG_TAG, "Sync Class - OnFinish method triggered");
                            //pd.dismiss();
                        }

                        @Override
                        public void onTick(long millisLeft) {
                        }
                    };
                    timer.start();
                }else if(method.equals("startSyncProcess")){
                    getToken();
                }
            }else{
                Toast.makeText(context,"No internet connection", Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        }

        @Override
        protected void onPreExecute() {
            pd.setIndeterminate(true);
            pd.setCanceledOnTouchOutside(false);
            pd.setMessage("Connecting..");
            pd.show();
        }

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
                    Log.i(Constants.LOG_TAG, "Error :- " + e.toString());
                    con = false;
                }
            }
            return null;
        }
    }


    private void process_updatePassword_response(ServerRequest request) {
        String response = request.getResponse();
        if (response.equals("")) {
            Toast.makeText(context, "Server timeout", Toast.LENGTH_LONG).show();
        } else {
            Log.i(Constants.LOG_TAG, "Server Response :- " + response);
            JSONObject objResponse = null;
            try {
                objResponse = new JSONObject(response);
                if (objResponse.has("error")){
                    Log.i(Constants.LOG_TAG, "Error :- " + objResponse.getString("error"));
                    Toast.makeText(context,"Invalid password",Toast.LENGTH_LONG).show();
                    pd.dismiss();
                }else {
                    if(objResponse.has("token")){
                        Token.setToken(objResponse.getString("token"));
                        Log.i(Constants.LOG_TAG, "Newest Token is :- " + Token.getToken());

                        user_dao.updateToken(Token.getToken(),user_index);
                        Log.i(Constants.LOG_TAG, "New token value received and updated");

                        user_dao.updatePassword(new_password,user_index);
                        Log.i(Constants.LOG_TAG, "New password value received and updated");

                        Toast.makeText(context,"Password updated",Toast.LENGTH_LONG).show();
                        pd.dismiss();
                    }else{
                        Toast.makeText(context,"Something went wrong",Toast.LENGTH_LONG).show();
                        pd.dismiss();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(context,"Something went wrong",Toast.LENGTH_LONG).show();
                pd.dismiss();
            }

        }
    }

}
