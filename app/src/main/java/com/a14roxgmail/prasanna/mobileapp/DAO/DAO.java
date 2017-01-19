package com.a14roxgmail.prasanna.mobileapp.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.a14roxgmail.prasanna.mobileapp.Constants.Constants;
import com.a14roxgmail.prasanna.mobileapp.Database.Database;

/**
 * Created by Prasanna Deshappriya on 1/18/2017.
 */
public class DAO {
    public Database database;
    public SQLiteDatabase sqldb;

    public DAO(Context context){
        database = new Database(
                context,
                Constants.DATABASE_NAME,
                null,
                Constants.DATABASE_VERSION
        );
        sqldb = database.getDatabase();
    }
}
