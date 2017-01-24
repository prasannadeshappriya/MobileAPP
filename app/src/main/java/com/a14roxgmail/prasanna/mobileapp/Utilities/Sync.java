package com.a14roxgmail.prasanna.mobileapp.Utilities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.a14roxgmail.prasanna.mobileapp.Constants.Constants;
import com.a14roxgmail.prasanna.mobileapp.DAO.CourseDAO;
import com.a14roxgmail.prasanna.mobileapp.DAO.SettingsDAO;
import com.a14roxgmail.prasanna.mobileapp.DAO.userDAO;
import com.a14roxgmail.prasanna.mobileapp.Model.Course;
import com.a14roxgmail.prasanna.mobileapp.R;

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
    private ProgressDialog pd;

    public Sync(Context context, String user_index, ArrayList<Course> arrCourseList, CourseDAO course_dao) {
        this.user_index = user_index;
        this.arrCourseList = arrCourseList;
        this.course_dao = course_dao;
        this.context = context;
        user_dao = new userDAO(context);
        settings_dao = new SettingsDAO(context);
        pd = new ProgressDialog(context, R.style.AppTheme_Dark_Dialog);
    }

    public void startSyncProcess() {
        if (checkInternetAccess()) {
            getCourseInfo courseInfo = new getCourseInfo(pd);
            courseInfo.execute();
        } else {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show();
        }
    }

    private boolean checkInternetAccess(){
        if(Build.PRODUCT.matches(".*_?sdk_?.*")) {
            return true;
        }else{
            return Utility.CheckInternetAccess();
        }
    }

    public boolean isSyncNeed(){
        //For autoSync Process
        //Sync Details automatically after 30 days

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
            pd.setMessage("Getting Course Info..");
            pd.show();
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

}
