package com.a14roxgmail.prasanna.mobileapp.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.a14roxgmail.prasanna.mobileapp.Constants.Constants;
import com.a14roxgmail.prasanna.mobileapp.Model.Course;
import com.a14roxgmail.prasanna.mobileapp.Utilities.Utility;

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
                        String.valueOf(c.getPosition()),
                        c.getString(c.getColumnIndex("user_index")),
                        c.getString(c.getColumnIndex("course_name")),
                        c.getString(c.getColumnIndex("course_code")),
                        c.getString(c.getColumnIndex("credits")),
                        c.getString(c.getColumnIndex("semester")),
                        c.getString(c.getColumnIndex("grade")));
            } while (c.moveToNext());
        }
        return course;
    }

    public ArrayList<Course> getAllCourseBySemester(String semester, String userIndex){
        command = "SELECT * FROM "+tableName+" WHERE user_index = \"" + userIndex + "\" AND semester = \"" + semester + "\";";
        Log.i(Constants.LOG_TAG,"Course select all for userIndex and semester query :- " + command);
        Cursor c = sqldb.rawQuery(command,null);
        Log.i(Constants.LOG_TAG, "Table name :- " + tableName + "   Search cursor count :- " + String.valueOf(c.getCount()));
        ArrayList<Course> arrCourse = new ArrayList<>();
        Course course = null;
        if(c.moveToFirst()) {
            do {
                course = new Course(
                        String.valueOf(c.getPosition()),
                        c.getString(c.getColumnIndex("user_index")),
                        c.getString(c.getColumnIndex("course_name")),
                        c.getString(c.getColumnIndex("course_code")),
                        c.getString(c.getColumnIndex("credits")),
                        c.getString(c.getColumnIndex("semester")),
                        c.getString(c.getColumnIndex("grade")));
                arrCourse.add(course);
            } while (c.moveToNext());
        }
        return arrCourse;
    }

    public ArrayList<String> getAllCourseNamesBySemester(String semester, String userIndex){
        command = "SELECT * FROM "+tableName+" WHERE user_index = \"" + userIndex + "\" AND semester = \"" + semester + "\";";
        Log.i(Constants.LOG_TAG,"Course select all course names for userIndex and semester query :- " + command);
        Cursor c = sqldb.rawQuery(command,null);
        Log.i(Constants.LOG_TAG, "Table name :- " + tableName + "   Search cursor count :- " + String.valueOf(c.getCount()));
        ArrayList<String> arrCourse = new ArrayList<>();
        if(c.moveToFirst()) {
            do {
                arrCourse.add(c.getString(c.getColumnIndex("course_name")));
            } while (c.moveToNext());
        }
        return arrCourse;
    }

    public boolean isInt(String value){
        try{
            int check = Integer.parseInt(value);
            Log.i(Constants.LOG_TAG, "Value :- " +String.valueOf(check) + " is a numaric value");
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public String getMaxSemester(String userIndex){
        command = "SELECT semester FROM " + tableName + " WHERE user_index = \"" + userIndex + "\" ORDER BY semester desc;";
        Log.i(Constants.LOG_TAG,"Select max semester number, query :- " + command);
        Cursor c = sqldb.rawQuery(command,null);
        if(c.getCount()>0) {
            Log.i(Constants.LOG_TAG, "Table name :- " + tableName + "   Search cursor count :- " + String.valueOf(c.getCount()));
            c.moveToFirst();
            String max_sem = c.getString(c.getColumnIndex("semester"));
            boolean con = true;
            while(!isInt(max_sem)){
                try {
                    c.moveToNext();
                }catch (Exception e){
                    Log.i(Constants.LOG_TAG,"An error occurred while getting the max semester [course_dao]");
                    return "0";
                }
                max_sem = c.getString(c.getColumnIndex("semester"));
            }
            return max_sem;
        }else {
            return "0";
        }
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
                        String.valueOf(c.getPosition()),
                        c.getString(c.getColumnIndex("user_index")),
                        c.getString(c.getColumnIndex("course_name")),
                        c.getString(c.getColumnIndex("course_code")),
                        c.getString(c.getColumnIndex("credits")),
                        c.getString(c.getColumnIndex("semester")),
                        c.getString(c.getColumnIndex("grade")));
                arrCourse.add(course);
            } while (c.moveToNext());
        }
        return arrCourse;
    }

    public boolean isCoursesExist(String userIndex){
        command = "SELECT * FROM "+tableName+" WHERE user_index = \"" + userIndex + "\";";
        Log.i(Constants.LOG_TAG,"Check for courses exist query :- " + command);
        Cursor c = sqldb.rawQuery(command,null);
        if(c.getCount()>0){
            c.close();
            return true;
        }else{
            c.close();
            return false;
        }
    }

    public void addCourse(Course course){
        ContentValues cv = new ContentValues();

        cv.put("user_index",course.getUserIndex());
        cv.put("course_name",course.getCourseName());
        cv.put("course_code",course.getCourseCode());
        cv.put("credits",course.getCredits());
        cv.put("grade","");
        cv.put("semester",course.getSemester());

        Log.i(Constants.LOG_TAG,"CourseDAO insert method triggered, course id :- " + course.getCourseCode());
        sqldb.insert(tableName,null,cv);
    }

    public void updateGrade(String userIndex, String course_name,String grade){
        command = "UPDATE " + tableName + " SET grade = \"" + grade + "\" WHERE course_name = \"" + course_name + "\" AND user_index = \"" + userIndex + "\";";
        Log.i(Constants.LOG_TAG, "Update grade for user :- " + userIndex + ", course :- " + course_name + ", query :- " + command);
        sqldb.execSQL(command);
    }

    public void addCourseList(ArrayList<Course> arrCourse){
        Log.i(Constants.LOG_TAG,"CourseDAO addCourseList method triggered");
        for(int i=0;i<arrCourse.size(); i++){
            addCourse(arrCourse.get(i));
        }
    }

    public void deleteCourse(String userIndex, String courseID) {
        command = "DELETE FROM " + tableName + " WHERE user_index = \"" + userIndex + "\" AND course_code = \"" + courseID + "\";";
        Log.i(Constants.LOG_TAG,"Delete course " + courseID + ", query :- " + command);
        sqldb.execSQL(command);
    }

    public boolean isGradeExist(String user_index, String course_code){
        command = "SELECT grade FROM " + tableName + " WHERE user_index = \"" + user_index + "\" AND course_code = \"" + course_code + "\";";
        Log.i(Constants.LOG_TAG,"Check existence of a grade , query :- " + command);
        Cursor c = sqldb.rawQuery(command,null);
        if(c.getCount()>0) {
            Log.i(Constants.LOG_TAG, "Table name :- " + tableName + "   Search cursor count :- " + String.valueOf(c.getCount()));
            c.moveToFirst();
            if(c.getString(c.getColumnIndex("grade")).equals("")) {
                return false;
            }else if(c.getString(c.getColumnIndex("grade")).equals("Non - GPA")){
                return false;
            }else{
                return true;
            }
        }else {
            return false;
        }
    }
}
