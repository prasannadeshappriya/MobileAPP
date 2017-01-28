package com.a14roxgmail.prasanna.mobileapp.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import com.a14roxgmail.prasanna.mobileapp.Constants.Constants;
import com.a14roxgmail.prasanna.mobileapp.Model.Notification;
import java.util.ArrayList;

/**
 * Created by Prasanna Deshappriya on 1/27/2017.
 */
public class NotificationDAO extends DAO {
    private Context context;
    private String tableName = "notification";
    private String command;

    public NotificationDAO(Context context) {
        super(context);
        this.context = context;
    }

    public void addNotification(Notification notification){
        Log.i(Constants.LOG_TAG,"Notification " + notification + " insert method triggered");
        ContentValues cv = new ContentValues();
        cv.put("user_index", notification.getUser_index());
        cv.put("day", notification.getDay());
        cv.put("event", notification.getEvent());
        sqldb.insert(tableName,null,cv);
    }



    public void deleteNotification(String user_index){
        command = "DELETE FROM " + tableName + " WHERE user_index = \"" + user_index + "\";";
        Log.i(Constants.LOG_TAG,"Delete notification, query :- " + command);
        sqldb.execSQL(command);
    }

    public ArrayList<Notification> getNotification(String userIndex){
        command = "SELECT * FROM " + tableName + " WHERE user_index = \"" + userIndex + "\";";
        Log.i(Constants.LOG_TAG,"Get all notifications for user " + userIndex + ", query :- " + command);
        Cursor c = sqldb.rawQuery(command,null);
        ArrayList<Notification> arrNotification = new ArrayList<>();
        Notification notification = null;
        if(c.moveToFirst()) {
            do {
                notification = new Notification(
                        c.getString(c.getColumnIndex("user_index")),
                        c.getString(c.getColumnIndex("day")),
                        c.getString(c.getColumnIndex("event"))
                );
                arrNotification.add(notification);
            } while (c.moveToNext());
        }
        return arrNotification;
    }

    public String getNotificationDate(String userIndex){
        command = "SELECT day FROM " + tableName + " WHERE user_index = \"" + userIndex + "\";";
        Log.i(Constants.LOG_TAG,"Select notification date for user " + userIndex + ", query :- " + command);
        Cursor c = sqldb.rawQuery(command,null);
        if(c.getCount()>0) {
            Log.i(Constants.LOG_TAG, "Table name :- " + tableName + "   Search cursor count :- " + String.valueOf(c.getCount()));
            c.moveToFirst();
            return c.getString(c.getColumnIndex("day"));
        }else {
            return "0";
        }
    }

    public boolean isNotificationAvailable(String userIndex, String date){
        command = "SELECT * FROM " + tableName + " WHERE user_index=\"" + userIndex + "\" AND day=\"" + date + "\";";
        Log.i(Constants.LOG_TAG,"Check for notification exist query :- " + command);
        Cursor c = sqldb.rawQuery(command,null);
        if(c.getCount()>0){
            Log.i(Constants.LOG_TAG, "Table name :- " + tableName + "   Search cursor count :- " + String.valueOf(c.getCount()));
            c.close();
            return true;
        }else{
            c.close();
            return false;
        }
    }
}
