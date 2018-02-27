package com.example.sgvn89.servicedemo;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;

/**
 * JobService to be scheduled by the JobScheduler.
 * start another service
 */
public class TestJobService extends JobService {

    @Override
    public boolean onStartJob(JobParameters params) {
        Intent service = new Intent(getApplicationContext(), MyService.class);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1) {
            getApplicationContext().startForegroundService(service);
        } else {
            getApplicationContext().startService(service);
        }
        Util.scheduleJob(getApplicationContext()); // reschedule the job
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }

}