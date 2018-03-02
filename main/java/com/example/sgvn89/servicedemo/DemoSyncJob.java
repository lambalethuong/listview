package com.example.sgvn89.servicedemo;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DemoSyncJob extends Job {

    public static final String TAG = "job_demo_tag";
    final static String MY_ACTION = "MY_ACTION";
    final static String DATA_NAME = "DATA_NAME";

    @Override
    @NonNull
    protected Result onRunJob(Params params) {
        // run your job here
        String data = new Date().toString();
        Intent shareIntent = new Intent();
        shareIntent.setAction(MY_ACTION);
        shareIntent.putExtra(DATA_NAME, data);
        getContext().sendBroadcast(shareIntent);
        return Result.SUCCESS;
    }

    public static int scheduleJob() {
        return new JobRequest.Builder(DemoSyncJob.TAG)
                //.setExecutionWindow(5_000L, 10_000L)
                .setPeriodic(TimeUnit.MINUTES.toMillis(15))
                .build()
                .schedule();
    }
}
