package com.a14roxgmail.prasanna.mobileapp.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.a14roxgmail.prasanna.mobileapp.Constants.Constants;
import com.a14roxgmail.prasanna.mobileapp.Model.User;

/**
 * Created by Prasanna Deshappriya on 1/18/2017.
 */
public class userDAO extends DAO{
    private Context context;
    private String tableName = "user";
    private String command;

    public userDAO(Context context) {
        super(context);
        this.context = context;
    }

    public boolean isUserExist(String userIndex){
        command = "SELECT * FROM " + tableName + " WHERE user_index=\"" + userIndex + "\";";
        Log.i(Constants.LOG_TAG,"Check for user exist query :- " + command);
        Cursor c = sqldb.rawQuery(command,null);
        if(c.getCount()>0){
            c.close();
            return true;
        }else{
            c.close();
            return false;
        }
    }

    public User getUser(String userIndex){
        command = "SELECT * FROM "+tableName+" WHERE user_index = \"" + userIndex + "\";";
        Log.i(Constants.LOG_TAG,"User select for index query :- " + command);
        Cursor c = sqldb.rawQuery(command,null);
        Log.i(Constants.LOG_TAG, "Table name :- " + tableName + "   Search cursor count :- " + String.valueOf(c.getCount()));
        User user = null;
        if(c.moveToFirst()) {
            do {
                user = new User(
                        c.getString(c.getColumnIndex("ID")),
                        c.getString(c.getColumnIndex("first_name")),
                        c.getString(c.getColumnIndex("last_name")),
                        c.getString(c.getColumnIndex("full_name")),
                        c.getString(c.getColumnIndex("user_index")),
                        c.getString(c.getColumnIndex("token")));
            } while (c.moveToNext());
        }
        return user;
    }

    public void addUser(User user){
        ContentValues cv = new ContentValues();

        cv.put("first_name",user.getFirstName());
        cv.put("last_name",user.getLastName());
        cv.put("full_name",user.getFullName());
        cv.put("user_index",user.getUserIndex());
        cv.put("token",user.getToken());

        Log.i(Constants.LOG_TAG,"UserDAO insert method triggered");
        sqldb.insert(tableName,null,cv);
    }


}
