package com.example.sgvn89.servicedemo;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by sgvn89 on 2018/02/26.
 * run when boot completed
 */

public class AutoStart extends BroadcastReceiver {
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        Util.scheduleJob(context);
    }
}
