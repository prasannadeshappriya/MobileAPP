package com.a14roxgmail.prasanna.mobileapp.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.a14roxgmail.prasanna.mobileapp.Constants.Constants;

/**
 * Created by Prasanna Deshappriya on 1/18/2017.
 */
public class Database extends SQLiteOpenHelper {
    private String name;
    private Context context;
    private String command;

    public Database(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.name = name;
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Create user table
        command = "CREATE TABLE IF NOT EXISTS user (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_index VARCHAR(10), " +
                "first_name VARCHAR(50), " +
                "last_name VARCHAR(50), " +
                "full_name VARCHAR(100), " +
                "login_status VARCHAR(10), " +
                "token VARCHAR(255));";
        Log.i(Constants.LOG_TAG, "Create user table query :- " + command);
        sqLiteDatabase.execSQL(command);

        command = "CREATE TABLE IF NOT EXISTS course (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_index VARCHAR(10), " +
                "course_name VARCHAR(255), " +
                "course_code VARCHAR(50), " +
                "credits VARCHAR(10), " +
                "grade VARCHAR(10), " +
                "semester VARCHAR(10), " +
                "FOREIGN KEY(user_index) REFERENCES user(user_index));";
        Log.i(Constants.LOG_TAG, "Create course table query :- " + command);
        sqLiteDatabase.execSQL(command);

        command = "CREATE TABLE IF NOT EXISTS gpa (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_index VARCHAR(10), " +
                "gpa VARCHAR(10), " +
                "FOREIGN KEY(user_index) REFERENCES user(user_index));";
        Log.i(Constants.LOG_TAG, "Create gpa table query :- " + command);
        sqLiteDatabase.execSQL(command);

        command = "CREATE TABLE IF NOT EXISTS sgpa (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_index VARCHAR(10), " +
                "total_credit VARCHAR(20), " +
                "semester VARCHAR(10), " +
                "sgpa VARCHAR(10), " +
                "FOREIGN KEY(user_index) REFERENCES user(user_index));";
        Log.i(Constants.LOG_TAG, "Create sgpa table query :- " + command);
        sqLiteDatabase.execSQL(command);
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        command = "DROP TABLE IF EXISTS user;";
        Log.i(Constants.LOG_TAG, "Drop user table query :- " + command);
        sqLiteDatabase.execSQL(command);
        onCreate(sqLiteDatabase);
    }

    public String getDatabaseName(){return name;}
    public void setContext(Context context){
        this.context = context;
    }

    public SQLiteDatabase getDatabase(){
        return getWritableDatabase();
    }
}
