package com.example.sgvn89.servicedemo;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;

/**
 * Created by sgvn89 on 2018/02/26.
 * periodically service
 */

public class MyService extends Service {


    final static String MY_ACTION = "MY_ACTION";


    @Override
    public void onCreate() {
        super.onCreate();
        startForeground(Util.JOB_ID,new Notification());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Query the database and show alarm if it applies
        boolean isActivityActive = getSharedPreferences("ourInfo", MODE_PRIVATE).getBoolean("active",false);
        if(isActivityActive){ // update to screen
            String data = new Date().toString();
            Intent shareIntent = new Intent();
            shareIntent.setAction(MY_ACTION);
            shareIntent.putExtra("dataPassed", data);
            sendBroadcast(shareIntent);
        }else{ // write to SD card
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/date.txt");
            try {
                if(!file.exists()) {
                    file.createNewFile();
                }
                FileOutputStream os = new FileOutputStream(file, true);
                PrintStream printstream = new PrintStream(os);
                printstream.append(new Date().toString()).append("\n");
                printstream.close();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // I don't want this service to stay in memory, so I stop it
        // immediately after doing what I wanted it to do.
//        stopSelf();

        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}