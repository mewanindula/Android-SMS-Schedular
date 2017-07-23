package com.example.sampath.smsapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.util.Calendar;

/**
 * Created by SAMPATH on 6/11/2017.
 */

public class DB_Controller extends SQLiteOpenHelper
{
    Calendar calendar = Calendar.getInstance();
    DateFormat formatDateTime = DateFormat.getDateTimeInstance();


    public DB_Controller(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, "SMS_SCHEDULAR", factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL("CREATE TABLE SMS(PNUM TEXT, MSG TEXT, TIME TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS SMS);");
        onCreate(sqLiteDatabase);
    }


    public void insert_msg(String pnum, String msg, String time)
    {
        ContentValues contentValues = new ContentValues();

        contentValues.put("PNUM", pnum);
        contentValues.put("MSG", msg);
        contentValues.put("TIME", time);

        this.getWritableDatabase().insertOrThrow("SMS", "", contentValues);
    }


    public void delete_msg()
    {
        this.getWritableDatabase().delete("SMS",null, null);
    }

    public String select_pnum()
    {
        String pnum;

        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM SMS", null);

        if(cursor.getCount() == 1)
        {
            cursor.moveToFirst();
            pnum = cursor.getString(0);
        }
        else
        {
            pnum = "There Has No Records";
        }

        return pnum;
    }


    public String select_msg()
    {
        String msg;

        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM SMS", null);

        if(cursor.getCount() == 1)
        {
            cursor.moveToFirst();
            msg = cursor.getString(1);
        }
        else
        {
            msg = "There Has No Records";
        }

        return msg;
    }


    public String select_time()
    {
        String time;

        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM SMS", null);

        if(cursor.getCount() == 1)
        {
            cursor.moveToFirst();
            time = cursor.getString(2);
        }
        else
        {
            time = formatDateTime.format(calendar.getTime()).toString();
        }

        return time;
    }


}
