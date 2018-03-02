package com.example.sgvn89.servicedemo;

/*
 * Created by sgvn89 on 2018/02/27.
 * define schedule
 */

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;

class Util {

    static final int JOB_ID = 1;
    static JobScheduler jobScheduler;

    // schedule the start of the service every 10 - 30 seconds
    static void scheduleJob(Context context) {
        ComponentName serviceComponent = new ComponentName(context, TestJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, serviceComponent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.setMinimumLatency(5000); // periodically run when meet condition
        } else {
            builder.setPeriodic(5000);
        }
        //builder.setOverrideDeadline(10000); // maximum delay when condition not meet
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY); // require network
        //builder.setRequiresDeviceIdle(true); // device should be idle
        //builder.setRequiresCharging(false); // we don't care if the device is charging or not
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            jobScheduler = context.getSystemService(JobScheduler.class);
        }else{
            jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        }
        if (jobScheduler != null) {
            jobScheduler.schedule(builder.build());
        }
    }

}