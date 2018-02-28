package com.example.sgvn89.servicedemo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

    MyReceiver myReceiver;
    TextView textView;
    Button start;
    Button stop;
    Intent intent;
    IntentFilter intentFilter;

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

        intent = new Intent(this, AutoStart.class);

        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.text);
        start = findViewById(R.id.startBtn);
        stop = findViewById(R.id.stopBtn);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendBroadcast(intent);
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.jobScheduler.cancelAll();
            }
        });

        //Register BroadcastReceiver
        //to receive event from our service
        myReceiver = new MyReceiver();
        intentFilter = new IntentFilter();
        intentFilter.addAction(MyService.MY_ACTION);
        registerReceiver(myReceiver, intentFilter);

    }

    @Override
    protected void onStart() {
        super.onStart();

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
        unregisterReceiver(myReceiver);
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
