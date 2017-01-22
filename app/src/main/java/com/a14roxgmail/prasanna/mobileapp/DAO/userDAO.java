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

    public void deleteUser(String userIndex){
        command = "DELETE FROM " + tableName + " WHERE user_index = \"" + userIndex + "\";";
        Log.i(Constants.LOG_TAG, "Delete user '" + userIndex + "' query :- " + command);
        sqldb.execSQL(command);
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
                        c.getString(c.getColumnIndex("first_name")),
                        c.getString(c.getColumnIndex("last_name")),
                        c.getString(c.getColumnIndex("full_name")),
                        c.getString(c.getColumnIndex("user_index")),
                        c.getString(c.getColumnIndex("token")),
                        c.getString(c.getColumnIndex("login_status")),
                        c.getString(c.getColumnIndex("password")));
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
        cv.put("login_status", "1");
        cv.put("password",user.getPassword());

        Log.i(Constants.LOG_TAG,"UserDAO insert method triggered");
        sqldb.insert(tableName,null,cv);
    }

    public User getSignInUserDetails(){
        command = "SELECT * FROM "+tableName+" WHERE login_status = \"" + Constants.USER_LOGIN_FLAG + "\";";
        Log.i(Constants.LOG_TAG,"Check sign in user query :- " + command);
        Cursor c = sqldb.rawQuery(command,null);
        Log.i(Constants.LOG_TAG, "Table name :- " + tableName + "   Search cursor count :- " + String.valueOf(c.getCount()));
        User user = null;
        if(c.moveToFirst()) {
            user = new User(
                    c.getString(c.getColumnIndex("first_name")),
                    c.getString(c.getColumnIndex("last_name")),
                    c.getString(c.getColumnIndex("full_name")),
                    c.getString(c.getColumnIndex("user_index")),
                    c.getString(c.getColumnIndex("token")),
                    c.getString(c.getColumnIndex("login_status")),
                    c.getString(c.getColumnIndex("password")));
        }
        return user;
    }

    public String getLoginStatus(String userIndex){
        command = "SELECT login_status FROM "+tableName+" WHERE user_index = \"" + userIndex + "\";";
        Log.i(Constants.LOG_TAG,"Check login_status for index query :- " + command);
        Cursor c = sqldb.rawQuery(command,null);
        Log.i(Constants.LOG_TAG, "Table name :- " + tableName + "   Search cursor count :- " + String.valueOf(c.getCount()));
        String login_status_value = "";
        if(c.moveToFirst()) {
            do {
                login_status_value = c.getString(c.getColumnIndex("login_status"));
            } while (c.moveToNext());
        }
        return login_status_value;
    }

    public void updateLoginStatus(String new_login_status_value, String userIndex){
        command = "UPDATE " + tableName + " SET login_status = \"" + new_login_status_value + "\" WHERE user_index = \""+ userIndex + "\";";
        Log.i(Constants.LOG_TAG,"Update login_status value query :- " + command);
        sqldb.execSQL(command);
    }

    public String getUserPassword(String userIndex){
        command = "SELECT password FROM "+tableName+" WHERE user_index = \"" + userIndex + "\";";
        Cursor c = sqldb.rawQuery(command,null);
        Log.i(Constants.LOG_TAG, "Table name :- " + tableName + "   Search cursor count :- " + String.valueOf(c.getCount()));
        String password = "";
        if(c.moveToFirst()) {
            do {
                password = c.getString(c.getColumnIndex("password"));
            } while (c.moveToNext());
        }
        return password;
    }

}
