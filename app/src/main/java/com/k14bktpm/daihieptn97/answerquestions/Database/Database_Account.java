package com.k14bktpm.daihieptn97.answerquestions.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


/**
 * Created by Hiep Tran on 4/22/2017.
 */

public class Database_Account {
    SQLiteDatabase database;
    DatabaseHelper databaseHelper;
    private static Database_Account datbase_account;

    public Database_Account(Context context) {
        databaseHelper = DatabaseHelper.getDatabaseHelper(context);
    }

    public static Database_Account getDatbase_account(Context context) {
        if (datbase_account == null) {
            datbase_account = new Database_Account(context);
        }
        return datbase_account;
    }

    public void Open_Database() {
        databaseHelper.getWritableDatabase();
    }

    public void Insert_Datbase_Account(Account account) {
        database = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(databaseHelper.Key_login_Email, account.getMail());
        contentValues.put(databaseHelper.Key_login_Password, account.getPassword());
        database.insert(databaseHelper.Table_login_Name, null, contentValues);
    }

    public boolean isEmpty() {
        Cursor cursor = databaseHelper.getReadableDatabase().rawQuery("select * from " + databaseHelper.Table_login_Name + "", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            return false;
        } else return true;
    }

    public boolean isExits(String temp) {
        Cursor cursor = databaseHelper.getReadableDatabase().rawQuery("select email from Account where email =  '" + temp + "'", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public void Delete_Account() {
        Cursor cursor = databaseHelper.getWritableDatabase().rawQuery("delete from " + databaseHelper.Table_login_Name + " ", null);
        cursor.moveToFirst();
    }

    public Account LoadAccount() {
        Account account = new Account();
        Cursor cursor = databaseHelper.getReadableDatabase().rawQuery("select * from " + databaseHelper.Table_login_Name + "", null);
        cursor.moveToFirst();
        account.setMail(cursor.getString(cursor.getColumnIndex(databaseHelper.Key_login_Email)));
        account.setPassword(cursor.getString(cursor.getColumnIndex(databaseHelper.Key_login_Password)));

        return account;
    }
}
