package com.example.wenfeng.mysecondapp;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    public final static String LOG_TAG = "Root";

    private Intent mServiceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpLogging();

    }


    public void onButtonClicked(View v){
        disableTimeButton();
        startCheckInService();
    }

    private void disableTimeButton(){
        findViewById(R.id.left_text_view).setEnabled(false);
        findViewById(R.id.right_text_view).setEnabled(false);
    }

    private void startCheckInService(){
        if(!isServiceRunning(CheckInService.class)) {
            Toast.makeText(this, "Service starting", Toast.LENGTH_SHORT).show();
            Button left = findViewById(R.id.left_text_view);
            Button right = findViewById(R.id.right_text_view);
            mServiceIntent = new Intent(this, CheckInService.class);
            mServiceIntent.putExtra(CheckInService.E_RANGE_LEFT, left.getText().toString());
            mServiceIntent.putExtra(CheckInService.E_RANGE_RIGHT, right.getText().toString());
            startService(mServiceIntent);
        }else{
            Toast.makeText(this, "Service already started!", Toast.LENGTH_SHORT).show();
        }
    }

    public void showTimePicker(View v){
        Button view = (Button)v;
        TimePickerFragment newFragment = new TimePickerFragment();
        newFragment.setView(view);
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void stopCheckInService(View v){
    }

    private boolean isServiceRunning(Class<?> target){
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (target.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        stopService(mServiceIntent);
        Log.i(LOG_TAG, "onDestroy");
        super.onDestroy();
    }

    private void setUpLogging(){
        Log.i(LOG_TAG, "Setting up logging file...");
        if (isExternalStorageWritable()) {

            File appDirectory = new File( Environment.getExternalStorageDirectory() + "/" + getString(R.string.app_name));
            File logFile = new File( appDirectory, "logcat" + System.currentTimeMillis() + ".txt" );
            Log.i(LOG_TAG, String.format("Logging will be saved to `%s`", logFile.getAbsolutePath()));

            // create app folder
            if ( !appDirectory.exists() ) {
                appDirectory.mkdir();
            }

            // clear the previous logcat and then write the new one to the file
            try {
                Runtime.getRuntime().exec("logcat -c");
                Runtime.getRuntime().exec("logcat -f " + logFile);
            } catch ( IOException e ) {
                e.printStackTrace();
            }
        }
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if ( Environment.MEDIA_MOUNTED.equals( state ) ) {
            return true;
        }
        return false;
    }
}
