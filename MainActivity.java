package com.example.sgvn89.servicedemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    MyReceiver myReceiver;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Thread.UncaughtExceptionHandler defaultHandler = Thread.getDefaultUncaughtExceptionHandler();

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                // fix our issues for shared preferences
                getSharedPreferences("ourInfo", MODE_PRIVATE).edit().putBoolean("active", false).apply();

                // Handle everything else
                defaultHandler.uncaughtException(thread, throwable);
            }
        });

        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.text);
        sendBroadcast(new Intent(this, AutoStart.class));
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Register BroadcastReceiver
        //to receive event from our service
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MyService.MY_ACTION);
        registerReceiver(myReceiver, intentFilter);

        // Store our shared preference
        getSharedPreferences("ourInfo", MODE_PRIVATE).edit().putBoolean("active", true).apply();
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Store our shared preference
        getSharedPreferences("ourInfo", MODE_PRIVATE).edit().putBoolean("active", false).apply();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Start the service anytime it got killed.
        startService(new Intent(this, MyService.class));
    }

    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {

            if(textView != null) {
                String dataPassed = arg1.getStringExtra("dataPassed");
                String displayString = textView.getText().toString() + "\n" + dataPassed;
                textView.setText(displayString);
            }
        }
    }
}
