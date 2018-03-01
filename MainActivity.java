package com.admin.coinAlert;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.admin.coinAlert.data.Alert;
import com.admin.coinAlert.data.AlertDatabaseHandler;
import com.admin.coinAlert.data.Coin;
import com.admin.coinAlert.data.CoinDatabaseHandler;
import com.admin.coinAlert.data.CoinNameSyncJob;
import com.admin.coinAlert.data.IntervalService;
import com.admin.coinAlert.data.MyJobCreator;
import com.admin.coinAlert.data.ServiceUtil;
import com.evernote.android.job.JobManager;

import java.util.List;

public class MainActivity extends AppCompatActivity implements AddAlertActivity.OnFirstFragmentListener, IntervalService.AsyncListener{

    static AlertDatabaseHandler alertDatabase;
    static CoinDatabaseHandler coinDatabase;
    AlertListActivity alertListScreen;

    MyReceiver myReceiver;
    Intent intent;
    IntentFilter intentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        /*
      The {@link android.support.v4.view.PagerAdapter} that will provide
      fragments for each of the sections. We use a
      {@link FragmentPagerAdapter} derivative, which will keep every
      loaded fragment in memory. If this becomes too memory intensive, it
      may be best to switch to a
      {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        /*
      The {@link ViewPager} that will host the section contents.
     */
        ViewPager mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        alertDatabase = new AlertDatabaseHandler(this);
        coinDatabase = new CoinDatabaseHandler(this);

        JobManager.create(this).addJobCreator(new MyJobCreator());
        //Register BroadcastReceiver
        //to receive event from our service
        myReceiver = new MyReceiver();
        intentFilter = new IntentFilter();
        intentFilter.addAction(CoinNameSyncJob.MY_ACTION);
        registerReceiver(myReceiver, intentFilter);
    }

    public static AlertDatabaseHandler getAlertDatabase(){return alertDatabase;}

    public static CoinDatabaseHandler getCoinDatabase(){return coinDatabase;}

    @Override
    protected void onDestroy() {
        ServiceUtil.jobScheduler.cancelAll();
        alertDatabase = null;
        coinDatabase = null;
        alertListScreen = null;
        unregisterReceiver(myReceiver);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_login) {
            return true;
        }
        else if (id == R.id.action_help) {
            return true;
        }
        else if (id == R.id.action_contact) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // returning the current tab
            switch (position)
            {
                case 0:
                    return new AddAlertActivity();
                case 1:
                    alertListScreen = new AlertListActivity();
                    return alertListScreen;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }
    }

    @Override
    public void onTransmitAlert(Alert alert) {
        if(alertListScreen != null) {
            alertListScreen.addNewAlert(alert);
        }
    }

    @Override
    public void updateCoinPrice() {
        if(alertListScreen != null) {
            alertListScreen.updateCoinPrice();
        }
    }

    @Override
    public void updateNewCoinAvailable(List<Coin> newCoinList) {
        if(alertListScreen != null) {
            alertListScreen.updateNewCoinList(newCoinList);
        }
    }

    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            String dataPassed = arg1.getStringExtra("dataPassed");
            Toast.makeText(getApplicationContext(), dataPassed, Toast.LENGTH_SHORT).show();
//            updateCoinPrice();
//            updateNewCoinAvailable();
        }
    }
}
