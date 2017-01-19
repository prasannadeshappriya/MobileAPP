package com.a14roxgmail.prasanna.mobileapp.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.a14roxgmail.prasanna.mobileapp.Constants.Constants;
import com.a14roxgmail.prasanna.mobileapp.Model.Course;

import java.util.ArrayList;

/**
 * Created by Prasanna Deshappriya on 1/19/2017.
 */
public class CourseDAO extends DAO{
    private Context context;
    private String tableName = "course";
    private String command;

    public CourseDAO(Context context) {
        super(context);
        this.context = context;
    }

    public Course getCourseByCourseID(String courseID){
        command = "SELECT * FROM "+tableName+" WHERE course_code = \"" + courseID + "\";";
        Log.i(Constants.LOG_TAG,"Course select for courseID query :- " + command);
        Cursor c = sqldb.rawQuery(command,null);
        Log.i(Constants.LOG_TAG, "Table name :- " + tableName + "   Search cursor count :- " + String.valueOf(c.getCount()));
        Course course = null;
        if(c.moveToFirst()) {
            do {
                course = new Course(
                        c.getString(c.getColumnIndex("user_index")),
                        c.getString(c.getColumnIndex("course_name")),
                        c.getString(c.getColumnIndex("course_code")),
                        c.getString(c.getColumnIndex("credits")),
                        c.getString(c.getColumnIndex("semester")));
            } while (c.moveToNext());
        }
        return course;
    }

    public ArrayList<Course> getAllCoursesByUserId(String userIndex){
        command = "SELECT * FROM "+tableName+" WHERE user_index = \"" + userIndex + "\";";
        Log.i(Constants.LOG_TAG,"Course select all for userIndex query :- " + command);
        Cursor c = sqldb.rawQuery(command,null);
        Log.i(Constants.LOG_TAG, "Table name :- " + tableName + "   Search cursor count :- " + String.valueOf(c.getCount()));
        ArrayList<Course> arrCourse = new ArrayList<>();
        Course course = null;
        if(c.moveToFirst()) {
            do {
                course = new Course(
                        c.getString(c.getColumnIndex("user_index")),
                        c.getString(c.getColumnIndex("course_name")),
                        c.getString(c.getColumnIndex("course_code")),
                        c.getString(c.getColumnIndex("credits")),
                        c.getString(c.getColumnIndex("semester")));
                arrCourse.add(course);
            } while (c.moveToNext());
        }
        return arrCourse;
    }

    public void addCourse(Course course){
        ContentValues cv = new ContentValues();

        cv.put("user_index",course.getUserIndex());
        cv.put("course_name",course.getCourseName());
        cv.put("course_code",course.getCourseCode());
        cv.put("credits",course.getCredits());
        cv.put("semester",course.getSemester());

        Log.i(Constants.LOG_TAG,"CourseDAO insert method triggered, course id :- " + course.getCourseCode());
        sqldb.insert(tableName,null,cv);
    }

    public void addCourseList(ArrayList<Course> arrCourse){
        Log.i(Constants.LOG_TAG,"CourseDAO addCourseList method triggered");
        for(int i=0;i<arrCourse.size(); i++){
            addCourse(arrCourse.get(i));
        }
    }
}
