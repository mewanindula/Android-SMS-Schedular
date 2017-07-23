package com.example.sampath.smsapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.widget.Toast;

/**
 * Created by SAMPATH on 6/11/2017.
 */

public class AlarmReciever extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent)
    {
        MainActivity ma = new MainActivity();

        DB_Controller controller = new DB_Controller(context, "", null, 1);

        String pnum = controller.select_pnum();
        String msg = controller.select_msg();

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(pnum, null, msg, null, null);

        controller.delete_msg();

        Toast.makeText(context, "SMS Sent", Toast.LENGTH_LONG).show();

    }
}
