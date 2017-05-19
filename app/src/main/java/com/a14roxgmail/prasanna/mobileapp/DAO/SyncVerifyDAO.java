package com.a14roxgmail.prasanna.mobileapp.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.a14roxgmail.prasanna.mobileapp.Constants.Constants;
import com.a14roxgmail.prasanna.mobileapp.Model.Setting;

/**
 * Created by prasanna on 5/19/17.
 */

public class SyncVerifyDAO extends DAO {
    private Context context;
    private String tableName = "sync_verify";
    private String command;

    public SyncVerifyDAO(Context context) {
        super(context);
        this.context = context;
    }

    public void initialize(String last_sync_date, String status, String user_index){
        Log.i(Constants.LOG_TAG,"SyncVerifyDAO initialize method triggered");
        ContentValues cv = new ContentValues();

        cv.put("user_index", user_index);
        cv.put("last_sync_date", last_sync_date);
        cv.put("status", status);

        sqldb.insert(tableName,null,cv);
    }

    public boolean isSyncDetailsExist(String userIndex){
        command = "SELECT * FROM " + tableName + " WHERE user_index=\"" + userIndex + "\";";
        Log.i(Constants.LOG_TAG,"Check for sync details exist query :- " + command);
        Cursor c = sqldb.rawQuery(command,null);
        if(c.getCount()>0){
            c.close();
            return true;
        }else{
            c.close();
            return false;
        }
    }

    public String getSyncStatus(String userIndex){
        command = "SELECT status FROM " + tableName + " WHERE user_index = \"" + userIndex + "\";";
        Log.i(Constants.LOG_TAG,"Select sync status for user " + userIndex + ", query :- " + command);
        Cursor c = sqldb.rawQuery(command,null);
        if(c.getCount()>0) {
            Log.i(Constants.LOG_TAG, "Table name :- " + tableName + "   Search cursor count :- " + String.valueOf(c.getCount()));
            c.moveToFirst();
            return c.getString(c.getColumnIndex("status"));
        }else {
            return "0";
        }
    }

    public void updateSyncStatus(String userIndex, String updateValue){
        command = "UPDATE " + tableName + " SET status = \"" + updateValue + "\" WHERE user_index = \"" + userIndex + "\";";
        Log.i(Constants.LOG_TAG, "Update sync status for user :- " + userIndex + ", value :- " + updateValue + ", query :- " + command);
        sqldb.execSQL(command);
    }

}
