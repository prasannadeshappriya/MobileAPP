package com.a14roxgmail.prasanna.mobileapp.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import com.a14roxgmail.prasanna.mobileapp.Constants.Constants;
import com.a14roxgmail.prasanna.mobileapp.Model.GPA;

import java.util.ArrayList;

/**
 * Created by Prasanna Deshappriya on 1/19/2017.
 */
public class GradeDAO extends DAO {
    private Context context;
    private String tableGpa = "gpa";
    private String tableSGpa = "sgpa";
    private String command;

    public GradeDAO(Context context) {
        super(context);
        this.context = context;
    }

    public void addOveralGpa(GPA gpa){
        Log.i(Constants.LOG_TAG,"GPA " + gpa.getType() + " insert method triggered");
        ContentValues cv = new ContentValues();

        cv.put("user_index", gpa.getUserIndex());
        if(gpa.getType().equals("sgpa")) {
            cv.put("semester", gpa.getSemester());
            cv.put("sgpa", gpa.getGpa());
            sqldb.insert(tableSGpa,null,cv);
        }else{
            cv.put("gpa", gpa.getGpa());
            sqldb.insert(tableGpa,null,cv);
        }
    }

    public ArrayList<GPA> getSGPA(String userIndex){
        command = "SELECT * FROM " + tableSGpa + " WHERE user_index = \"" + userIndex + "\";";
        Log.i(Constants.LOG_TAG,"get sgpa for user " + userIndex + " query " + command);
        Cursor c = sqldb.rawQuery(command,null);
        ArrayList<GPA> arrGPA = new ArrayList<>();
        GPA gpa = null;
        if(c.moveToFirst()) {
            do {
                gpa = new GPA(
                        Constants.SGPA_FLAG,
                        c.getString(c.getColumnIndex("semester")),
                        c.getString(c.getColumnIndex("sgpa")),
                        c.getColumnName(c.getColumnIndex("user_index"))
                );
                arrGPA.add(gpa);
            } while (c.moveToNext());
        }
        return arrGPA;
    }

    public GPA getGPA(String userIndex){
        command = "SELECT * FROM " + tableGpa + " WHERE user_index = \"" + userIndex + "\";";
        Log.i(Constants.LOG_TAG,"get gpa for user " + userIndex + " query " + command);
        Cursor c = sqldb.rawQuery(command,null);
        if(c.getCount()>0){
            c.close();
            return new GPA(
                    Constants.GPA_FLAG,
                    c.getString(c.getColumnIndex("semester")),
                    c.getString(c.getColumnIndex("gpa")),
                    c.getColumnName(c.getColumnIndex("user_index"))
            );
        }else{
            c.close();
            return null;
        }
    }

    public void updateGPA(String userIndex, String nGPA){
        command = "UPDATE " + tableGpa + " SET gpa =\"" + nGPA + "\" WHERE user_index = \"" + userIndex + "\";";
        Log.i(Constants.LOG_TAG,"Update gpa of user " + userIndex + " to " + nGPA + ", query :- " + command);
        sqldb.execSQL(command);
    }

    public void updateSGPA(String userIndex, String nSGPA){
        command = "UPDATE " + tableSGpa + " SET sgpa =\"" + nSGPA + "\" WHERE user_index = \"" + userIndex + "\";";
        Log.i(Constants.LOG_TAG,"Update sgpa of user " + userIndex + " to " + nSGPA + ", query :- " + command);
        sqldb.execSQL(command);
    }

    public boolean isGPAExist(String userIndex){
        command = "SELECT * FROM " + tableGpa + " WHERE user_index=\"" + userIndex + "\";";
        Log.i(Constants.LOG_TAG,"Check for gpa exist query :- " + command);
        Cursor c = sqldb.rawQuery(command,null);
        if(c.getCount()>0){
            c.close();
            return true;
        }else{
            c.close();
            return false;
        }
    }

    public boolean isSGPAExist(String userIndex, String semester){
        command = "SELECT * FROM " + tableSGpa + " WHERE user_index=\"" + userIndex + "\" + AND semester = \"" + semester + "\";";
        Log.i(Constants.LOG_TAG,"Check for sgpa exist query :- " + command);
        Cursor c = sqldb.rawQuery(command,null);
        if(c.getCount()>0){
            c.close();
            return true;
        }else{
            c.close();
            return false;
        }
    }
}
