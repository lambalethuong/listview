package com.example.sgvn89.servicedemo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.Date;

/**
 * Created by sgvn89 on 2018/02/26.
 * periodically service
 */

public class MyService extends Service {


    final static String MY_ACTION = "MY_ACTION";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Query the database and show alarm if it applies
        boolean isActivityActive = getSharedPreferences("ourInfo", MODE_PRIVATE).getBoolean("active",false);
        if(isActivityActive){
            String data = new Date().toString();
            Intent shareIntent = new Intent();
            shareIntent.setAction(MY_ACTION);
            shareIntent.putExtra("dataPassed", data);
            sendBroadcast(shareIntent);
        }

        // I don't want this service to stay in memory, so I stop it
        // immediately after doing what I wanted it to do.
        stopSelf();

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        // I want to restart this service again in one hour
        AlarmManager alarm = (AlarmManager)getSystemService(ALARM_SERVICE);
        if (alarm != null) {
            alarm.set(
                    AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis() + 5000,
                    PendingIntent.getService(this, 0, new Intent(this, MyService.class), 0)
            );
        }
    }
}
