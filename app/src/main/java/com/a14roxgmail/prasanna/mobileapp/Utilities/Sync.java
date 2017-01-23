package com.a14roxgmail.prasanna.mobileapp.Utilities;

import android.util.Log;

import com.a14roxgmail.prasanna.mobileapp.Constants.Constants;
import com.a14roxgmail.prasanna.mobileapp.DAO.CourseDAO;
import com.a14roxgmail.prasanna.mobileapp.Model.Course;

import java.util.ArrayList;

/**
 * Created by Prasanna Deshappriya on 1/23/2017.
 */
public class Sync {
    private ArrayList<Course> arrCourseList;
    private String user_index;
    private CourseDAO course_dao;

    public Sync(String user_index, ArrayList<Course> arrCourseList, CourseDAO course_dao) {
        this.user_index = user_index;
        this.arrCourseList = arrCourseList;
        this.course_dao = course_dao;
    }

    public void startSyncProcess(){

        Log.i(Constants.LOG_TAG,Utility.getCurrentDate() + " " + course_dao.getLastSyncDate(user_index));
    }

    public boolean isSyncNeed(){
        return true;
    }
}
