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

    public void addGpa(GPA gpa){
        Log.i(Constants.LOG_TAG,"GPA " + gpa.getType() + " insert method triggered");
        ContentValues cv = new ContentValues();

        cv.put("user_index", gpa.getUserIndex());
        if(gpa.getType().equals("sgpa")) {
            cv.put("semester", gpa.getSemester());
            cv.put("sgpa", gpa.getGpa());
            cv.put("total_credit", gpa.getTotal_credits());
            sqldb.insert(tableSGpa,null,cv);
        }else{
            cv.put("gpa", gpa.getGpa());
            sqldb.insert(tableGpa,null,cv);
        }
    }

    public String getTotalCredit(String semester, String userIndex){
        command = "SELECT total_credit FROM " + tableSGpa + " WHERE user_index = \"" + userIndex + "\" AND semester = \"" + semester + "\";";
        Log.i(Constants.LOG_TAG,"Select total credits for semester " + semester + ", query :- " + command);
        Cursor c = sqldb.rawQuery(command,null);
        if(c.getCount()>0) {
            Log.i(Constants.LOG_TAG, "Table name :- " + tableSGpa + "   Search cursor count :- " + String.valueOf(c.getCount()));
            c.moveToFirst();
            return c.getString(c.getColumnIndex("total_credit"));
        }else {
            return "0.0";
        }
    }

    public ArrayList<GPA> getSGPAArray(String userIndex){
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
                        c.getString(c.getColumnIndex("user_index")),
                        c.getString(c.getColumnIndex("total_credit"))
                );
                arrGPA.add(gpa);
            } while (c.moveToNext());
        }
        return arrGPA;
    }

    public GPA getSGPA(String userIndex, String semester){
        command = "SELECT * FROM " + tableSGpa + " WHERE user_index = \"" + userIndex + "\" AND semester = \"" + semester + "\";";
        Log.i(Constants.LOG_TAG,"get sgpa for user " + userIndex + " query " + command);
        Cursor c = sqldb.rawQuery(command,null);
        GPA gpa = null;
        if(c.moveToFirst()) {
            do {
                gpa = new GPA(
                        Constants.SGPA_FLAG,
                        c.getString(c.getColumnIndex("semester")),
                        c.getString(c.getColumnIndex("sgpa")),
                        c.getString(c.getColumnIndex("user_index")),
                        c.getString(c.getColumnIndex("total_credit"))
                );
            } while (c.moveToNext());
        }
        return gpa;
    }

    public GPA getGPA(String userIndex){
        command = "SELECT * FROM " + tableGpa + " WHERE user_index = \"" + userIndex + "\";";
        Log.i(Constants.LOG_TAG,"get gpa for user " + userIndex + " query " + command);
        Cursor c = sqldb.rawQuery(command,null);
        if(c.getCount()>0){
            if(c.moveToFirst()) {
                return new GPA(
                        Constants.GPA_FLAG,
                        "-",
                        c.getString(c.getColumnIndex("gpa")),
                        c.getString(c.getColumnIndex("user_index"))
                );
            }else{
                return null;
            }
        }else{
            return null;
        }
    }

    public void updateGPA(String userIndex, String nGPA){
        command = "UPDATE " + tableGpa + " SET gpa =\"" + nGPA + "\" WHERE user_index = \"" + userIndex + "\";";
        Log.i(Constants.LOG_TAG,"Update gpa of user " + userIndex + " to " + nGPA + ", query :- " + command);
        sqldb.execSQL(command);
    }

    public void deleteSGPA(String userIndex, String semester){
        command = "DELETE FROM " + tableSGpa + " WHERE user_index = \"" + userIndex + "\" AND semester = \"" + semester + "\";";
        Log.i(Constants.LOG_TAG,"Delete semester " + semester + " sgpa of user " +userIndex + ", query :- " + command);
        sqldb.execSQL(command);
     }

    public void updateSGPA(String userIndex, String nSGPA, String semester, String total_credit){
        command = "UPDATE " + tableSGpa + " SET sgpa =\"" + nSGPA + "\" WHERE user_index = \"" + userIndex + "\" AND semester = \"" + semester + "\";";
        Log.i(Constants.LOG_TAG,"Update semester " + semester + " sgpa of user " + userIndex + " to " + nSGPA + ", query :- " + command);
        sqldb.execSQL(command);
        command = "UPDATE " + tableSGpa + " SET total_credit =\"" + total_credit + "\" WHERE user_index = \"" + userIndex + "\" AND semester = \"" + semester + "\";";
        Log.i(Constants.LOG_TAG,"Update semester " + semester + " total_credit of user " + userIndex + " to " + total_credit + ", query :- " + command);
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
        command = "SELECT * FROM " + tableSGpa + " WHERE user_index=\"" + userIndex + "\" AND semester = \"" + semester + "\";";
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

    public boolean isSGPAAvailable(String userIndex){
        command = "SELECT * FROM " + tableSGpa + " WHERE user_index=\"" + userIndex + "\";";
        Log.i(Constants.LOG_TAG,"Check for sgpa available query :- " + command);
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
