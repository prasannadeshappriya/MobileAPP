package com.a14roxgmail.prasanna.mobileapp.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.a14roxgmail.prasanna.mobileapp.Constants.Constants;
import com.a14roxgmail.prasanna.mobileapp.Model.AttendanceEntry;

import java.util.ArrayList;

/**
 * Created by prasanna on 2/10/17.
 */

public class AttendanceDAO extends  DAO {
    private Context context;
    private String tableName = "attendance";
    private String command;

    public AttendanceDAO(Context context) {
        super(context);
        this.context = context;
    }

    public void addAttendanceEntry(AttendanceEntry entry){
        ContentValues cv = new ContentValues();

        cv.put("user_index",entry.getUserIndex());
        cv.put("module_name",entry.getModuleName());
        cv.put("year",entry.getYear());
        cv.put("month",entry.getMonth());
        cv.put("day",entry.getDate());
        cv.put("comment",entry.getComment());
        cv.put("value",entry.getValue());

        Log.i(Constants.LOG_TAG,"AttendanceDAO insert method triggered, module_name id :- " + entry.getModuleName());
        sqldb.insert(tableName,null,cv);
    }

    public ArrayList<AttendanceEntry> getAttendanceInfo(String user_index, String module_name){
        command = "SELECT * FROM "+tableName+" WHERE user_index = \"" + user_index + "\" AND module_name =\"" + module_name + "\";";
        Log.i(Constants.LOG_TAG,"Attendance select all for userIndex and module_name query :- " + command);
        Cursor c = sqldb.rawQuery(command,null);
        Log.i(Constants.LOG_TAG, "Table name :- " + tableName + "   Search cursor count :- " + String.valueOf(c.getCount()));
        ArrayList<AttendanceEntry> arrAttendaneEntryList = new ArrayList<>();
        AttendanceEntry entry = null;
        if(c.moveToFirst()) {
            do {
                entry = new AttendanceEntry(
                        String.valueOf(c.getPosition()),
                        c.getString(c.getColumnIndex("user_index")),
                        c.getString(c.getColumnIndex("module_name")),
                        c.getString(c.getColumnIndex("value")),
                        c.getString(c.getColumnIndex("day")),
                        c.getString(c.getColumnIndex("month")),
                        c.getString(c.getColumnIndex("year")),
                        c.getString(c.getColumnIndex("comment")));
                arrAttendaneEntryList.add(entry);
            } while (c.moveToNext());
        }
        return arrAttendaneEntryList;
    }

    public boolean isAttendanceExist(String user_index, String module_name, String year, String month, String day){
        command = "SELECT * FROM "+tableName+" WHERE user_index = \"" + user_index + "\" AND module_name =\"" + module_name + "\"" +
                " AND year =\"" + year + "\" AND month =\"" + month + "\"" +
                " AND day =\"" + day + "\";";
        Log.i(Constants.LOG_TAG,"Attendance select for userIndex, module_name and " + year + "-" + month + "-" + day + ", query :- " + command);
        Cursor c = sqldb.rawQuery(command,null);
        if(c.getCount()>0){
            c.close();
            return true;
        }else{
            c.close();
            return false;
        }
    }

    public void updateAttendanceEntry(String user_index, String module_name, String year, String month, String day, String comment, String value){
        command = "UPDATE "+tableName+ " SET comment=\"" + comment + "\" WHERE user_index = \"" + user_index + "\" AND module_name =\"" + module_name + "\"" +
                " AND year =\"" + year + "\" AND month =\"" + month + "\"" +
                " AND day =\"" + day + "\";";
        sqldb.execSQL(command);
        command = "UPDATE "+tableName+ " SET value=\"" + value + "\" WHERE user_index = \"" + user_index + "\" AND module_name =\"" + module_name + "\"" +
                " AND year =\"" + year + "\" AND month =\"" + month + "\"" +
                " AND day =\"" + day + "\";";
        Log.i(Constants.LOG_TAG,"Update attendane info for " + year + "-" + month + "-" + day + ", query :- " + command);
        sqldb.execSQL(command);
    }

    public void deleteAttendanceEntry(String user_index, String module_name, String year, String month, String day){
        command = "DELETE FROM "+tableName+ " WHERE user_index = \"" + user_index + "\" AND module_name =\"" + module_name + "\"" +
                " AND year =\"" + year + "\" AND month =\"" + month + "\"" +
                " AND day =\"" + day + "\";";
        Log.i(Constants.LOG_TAG,"Delete attendane info for " + year + "-" + month + "-" + day + ", query :- " + command);
        sqldb.execSQL(command);
    }

    public AttendanceEntry getAttendanceViewInfo(String user_index, String module_name, String year, String month, String day){
        command = "SELECT * FROM "+tableName+" WHERE user_index = \"" + user_index + "\" AND module_name =\"" + module_name + "\"" +
                                                                                            " AND year =\"" + year + "\" AND month =\"" + month + "\"" +
                                                                                                " AND day =\"" + day + "\";";
        Log.i(Constants.LOG_TAG,"Attendance select for userIndex, module_name and " + year + "-" + month + "-" + day + ", query :- " + command);
        Cursor c = sqldb.rawQuery(command,null);
        Log.i(Constants.LOG_TAG, "Table name :- " + tableName + "   Search cursor count :- " + String.valueOf(c.getCount()));
        AttendanceEntry entry = null;
        if(c.moveToFirst()) {
                entry = new AttendanceEntry(
                        String.valueOf(c.getPosition()),
                        c.getString(c.getColumnIndex("user_index")),
                        c.getString(c.getColumnIndex("module_name")),
                        c.getString(c.getColumnIndex("value")),
                        c.getString(c.getColumnIndex("day")),
                        c.getString(c.getColumnIndex("month")),
                        c.getString(c.getColumnIndex("year")),
                        c.getString(c.getColumnIndex("comment")));
        }
        return entry;
    }
}
