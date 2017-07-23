package com.example.sampath.smsapp;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class MainActivity extends Activity
{

    TextView dis;
    public EditText num,mmg;
    public String pnum, msg, tim;

    public Button setTime, setDate, send, cancel;

    Intent intentAlarm;
    AlarmManager alarmManager;

    Calendar calendarS = Calendar.getInstance();

    DateFormat formatDateTime = DateFormat.getDateTimeInstance();

    DB_Controller controller;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        num = (EditText)findViewById(R.id.tel);
        mmg = (EditText)findViewById(R.id.emsg);

        setTime = (Button)findViewById(R.id.setTime);
        setDate = (Button)findViewById(R.id.setDate);
        send = (Button)findViewById(R.id.mSend);
        cancel = (Button)findViewById(R.id.cancel);

        dis = (TextView)findViewById(R.id.textView5);

        controller = new DB_Controller(this, "", null, 1);

        pnum = controller.select_pnum();
        msg = controller.select_msg();
        tim = controller.select_time();

        num.setText(pnum);
        mmg.setText(msg);
        dis.setText(tim);

        num.setEnabled(false);
        mmg.setEnabled(false);
        setTime.setEnabled(false);
        setDate.setEnabled(false);
        send.setEnabled(false);

        updateTextLable();
    }


    public void updateTextLable()
    {
        dis.setText(formatDateTime.format(calendarS.getTime()));
    }

    public void selectDate(View v)
    {
        new DatePickerDialog(this, d, calendarS.get(Calendar.YEAR), calendarS.get(Calendar.MONTH), calendarS.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void selectTime(View v)
    {
        new TimePickerDialog(this, t, calendarS.get(Calendar.HOUR_OF_DAY), calendarS.get(Calendar.MINUTE), true).show();
    }

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener()
    {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
        {
            calendarS.set(Calendar.YEAR, year);
            calendarS.set(Calendar.MONTH, monthOfYear);
            calendarS.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            updateTextLable();
        }
    };


    TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener()
    {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute)
        {
            calendarS.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendarS.set(Calendar.MINUTE, minute);
            calendarS.set(Calendar.SECOND, 00);

            updateTextLable();
        }
    };



    public  void scheduleSMS(View v)
    {
        pnum = (num.getText().toString());
        msg = (mmg.getText().toString());
        tim = formatDateTime.format(calendarS.getTime()).toString();

        controller.insert_msg(pnum, msg, tim);

        num.setEnabled(false);
        mmg.setEnabled(false);
        setTime.setEnabled(false);
        setDate.setEnabled(false);
        send.setEnabled(false);

        long dif = calendarS.getTimeInMillis() - System.currentTimeMillis();

        String sm = "SMS Will Be Sent At " + formatDateTime.format(calendarS.getTime()) + "";

        Long time = new GregorianCalendar().getTimeInMillis()+(dif);
        intentAlarm = new Intent(this, AlarmReciever.class);

        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        alarmManager.set(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(this, 1, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));

        Toast.makeText(this, sm, Toast.LENGTH_LONG).show();
    }

    public void cancelSMS(View v)
    {
        /*PendingIntent.getBroadcast(this, 1, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT).cancel();
        alarmManager.cancel(PendingIntent.getBroadcast(this, 1, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));*/

        num.setEnabled(true);
        mmg.setEnabled(true);
        setTime.setEnabled(true);
        setDate.setEnabled(true);
        send.setEnabled(true);

        pnum = controller.select_pnum();
        msg = controller.select_msg();
        tim = controller.select_time();

        num.setText(pnum);
        mmg.setText(msg);
        dis.setText(tim);

        num.requestFocus();

        Toast.makeText(this, "Schedule SMS Was Canceled!", Toast.LENGTH_LONG).show();

        controller.delete_msg();
    }

}
