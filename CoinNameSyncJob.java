package com.admin.coinAlert.data;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.admin.coinAlert.AddCoinAlertActivity;
import com.admin.coinAlert.MainActivity;
import com.admin.coinAlert.R;
import com.admin.coinAlert.definition.Common;
import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Admin on 3/1/2018.
 * get coin name list, run one time
 */

public class CoinNameSyncJob extends Job {

    static final String TAG = "coin_name_job_tag";
    final public static String MY_ACTION = "MY_ACTION";
    final public static String DATA_NAME = "JSONArray";

    @Override
    @NonNull
    protected Result onRunJob(@NonNull Params params) {

        int status; // 0: success, 1:IOException, 2:JSONException
        JSONArray retObj = null;

        try {
            URL urlObj = new URL(Common.JSON_URL);
            HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
            conn.setDoOutput(false);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept-Charset", Common.charset);
            conn.setConnectTimeout(Common.timeOut);
            conn.connect();

            //Receive the response from the server
            InputStream in = new BufferedInputStream(conn.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            retObj = new JSONArray(result.toString());
            status = 0;

        } catch (IOException e) {
            status = 1; // maybe internet interrupt
            Toast.makeText(getContext(), getContext().getResources().getString(R.string.internet_information), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } catch (JSONException e){
            status = 2; // website load fail or API change
            Toast.makeText(getContext(), getContext().getResources().getString(R.string.web_information), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        if(status == 0) {
            Intent obj_intent = new Intent(getContext(), MainActivity.class);
            Bundle b = new Bundle();
            b.putString(DATA_NAME,retObj.toString());
            obj_intent.putExtras(b);
            obj_intent.setAction(MY_ACTION);
            getContext().sendBroadcast(obj_intent);
//            MainActivity.alertListScreen
            return Result.SUCCESS;
        }else {
            // something strange happened, try again later
            return Result.RESCHEDULE;
        }
    }


    @Override
    protected void onReschedule(int newJobId) {
        // the rescheduled job has a new ID
    }

    public static void scheduleJob() {
        new JobRequest.Builder(CoinNameSyncJob.TAG)
                .setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
                .setExecutionWindow(1000, Common.intervalTime)
                .build()
                .schedule();
    }
}
