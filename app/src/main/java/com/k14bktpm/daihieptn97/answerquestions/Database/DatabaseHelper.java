package com.k14bktpm.daihieptn97.answerquestions.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Hiep Tran on 10/14/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper databaseHelper;

    public static final String Table_login_Name = "Account";
    public static final String Key_login_Email = "email";
    public static final String Key_login_Password = "password";

    public static synchronized DatabaseHelper getDatabaseHelper(Context context) {
        if (databaseHelper == null) {
            databaseHelper = new DatabaseHelper(context);
        }
        return databaseHelper;
    }

    public static final String Database_Name = "QuanLyAccount";
    public static final int version = 1;


    public DatabaseHelper(Context context) {
        super(context, Database_Name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE  TABLE \"main\".\"Account\" (\"id\" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL ," +
                " \"email\" TEXT NOT NULL , \"password\" TEXT NOT NULL )");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
