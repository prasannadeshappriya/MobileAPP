package com.a14roxgmail.prasanna.mobileapp.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.a14roxgmail.prasanna.mobileapp.Constants.Constants;
import com.a14roxgmail.prasanna.mobileapp.Model.GPA;
import com.a14roxgmail.prasanna.mobileapp.Model.Setting;

import java.util.ArrayList;

/**
 * Created by Prasanna Deshappriya on 1/24/2017.
 "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
 "user_index VARCHAR(10), " +
 "gpa_operation VARCHAR(20), " +
 "last_edit_date VARCHAR(10), " +
 */
public class SettingsDAO extends DAO {
    private Context context;
    private String tableName = "settings";
    private String command;

    public SettingsDAO(Context context) {
        super(context);
        this.context = context;
    }

    public void addSettings(Setting settings){
        Log.i(Constants.LOG_TAG,"Setting insert method triggered");
        ContentValues cv = new ContentValues();

        cv.put("user_index", settings.getUser_index());
        cv.put("gpa_operation", settings.getGpa_operation());
        cv.put("last_edit_date", settings.getLast_edit_date());

        sqldb.insert(tableName,null,cv);
    }

    public String getGpaOperation(String userIndex){
        command = "SELECT gpa_operation FROM " + tableName + " WHERE user_index = \"" + userIndex + "\";";
        Log.i(Constants.LOG_TAG,"Select gpa calculation operation for user " + userIndex + ", query :- " + command);
        Cursor c = sqldb.rawQuery(command,null);
        if(c.getCount()>0) {
            Log.i(Constants.LOG_TAG, "Table name :- " + tableName + "   Search cursor count :- " + String.valueOf(c.getCount()));
            c.moveToFirst();
            return c.getString(c.getColumnIndex("gpa_operation"));
        }else {
            return "0";
        }
    }

    public String getLastSyncDate(String userIndex){
        command = "SELECT last_edit_date FROM " + tableName + " WHERE user_index = \"" + userIndex + "\";";
        Log.i(Constants.LOG_TAG,"Select last edit date for user " + userIndex + ", query :- " + command);
        Cursor c = sqldb.rawQuery(command,null);
        if(c.getCount()>0) {
            Log.i(Constants.LOG_TAG, "Table name :- " + tableName + "   Search cursor count :- " + String.valueOf(c.getCount()));
            c.moveToFirst();
            return c.getString(c.getColumnIndex("last_edit_date"));
        }else {
            return "";
        }
    }

    public void updateLastSyncDate(String userIndex, String updateValue){
        command = "UPDATE " + tableName + " SET last_edit_date = \"" + updateValue + "\" WHERE user_index = \"" + userIndex + "\";";
        Log.i(Constants.LOG_TAG, "Update last sync date for user :- " + userIndex + ", value :- " + updateValue + ", query :- " + command);
        sqldb.execSQL(command);
    }

    public void updateGpaCalcOperation(String userIndex, String updateValue){
        command = "UPDATE " + tableName + " SET gpa_operation = \"" + updateValue + "\" WHERE user_index = \"" + userIndex + "\";";
        Log.i(Constants.LOG_TAG, "Update gpa calc option for user :- " + userIndex + ", value :- " + updateValue + ", query :- " + command);
        sqldb.execSQL(command);
    }

}
