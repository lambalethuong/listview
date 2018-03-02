package com.example.sgvn89.servicedemo;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import java.util.Date;
import java.util.PriorityQueue;

/**
 * Created by sgvn89 on 2018/02/26.
 * periodically service
 */

public class MyService extends Service {

    public static final String NOTIFICATION_CHANNEL_ID_SERVICE = "com.example.sgvn89.servicedemo";

    final static String MY_ACTION = "MY_ACTION";
    final static String DATA_NAME = "DATA_NAME";
    static int count = 0; // count to 12, 1 minute

    @Override
    public void onCreate() {
        super.onCreate();
        startForeground();
        //startForeground(Util.JOB_ID, new Notification());
    }

    private void startForeground() {
        String channelId = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channelId = createNotificationChannel();
        } else {
            // If earlier version channel ID is not used
            // https://developer.android.com/reference/android/support/v4/app/NotificationCompat.Builder.html#NotificationCompat.Builder(android.content.Context)
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId );
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(Util.JOB_ID, notification);
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private String createNotificationChannel(){
        String channelId = "my_service";
        String channelName = "My Background Service";
        NotificationChannel chan = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager service = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        if (service != null) {
            service.createNotificationChannel(chan);
        }
        return channelId;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Query the database and show alarm if it applies
        String data = new Date().toString();
        Intent shareIntent = new Intent();
        shareIntent.setAction(MY_ACTION);
        shareIntent.putExtra(DATA_NAME, data);
        sendBroadcast(shareIntent);

//        count++;
//        if(count == 6)
//        {
//            showNotification();
//            count = 0;
//        }
//        else{ // write to SD card
//            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/date.txt");
//            try {
//                if(!file.exists()) {
//                    file.createNewFile();
//                }
//                FileOutputStream os = new FileOutputStream(file, true);
//                PrintStream printstream = new PrintStream(os);
//                printstream.append(new Date().toString()).append("\n");
//                printstream.close();
//                os.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

        // I don't want this service to stay in memory, so I stop it
        // immediately after doing what I wanted it to do.
//        stopSelf();

        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    void showNotification(){
        //Creating Notification Builder
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_HIGH);

            // Configure the notification channel.
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
//            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
//            notificationChannel.enableVibration(true);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);

        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.time)
                .setTicker("Hearty365")
                //     .setPriority(Notification.PRIORITY_MAX)
                .setContentTitle("Default notification")
                .setContentText("Lorem ipsum dolor sit amet, consectetur adipiscing elit.")
                .setContentInfo("Info");

        if (notificationManager != null) {
            notificationManager.notify(/*notification id*/1, notificationBuilder.build());
        }

        // ringtone here
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        r.play();

        // Vibration
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        if(vibrator!=null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(150, 10));
            } else {
                vibrator.vibrate(150);
            }
        }
    }
}