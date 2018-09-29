package com.example.wenfeng.mysecondapp;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.example.wenfeng.mysecondapp.log.MyLogManager;
import com.example.wenfeng.mysecondapp.log.MyLogger;
import com.example.wenfeng.mysecondapp.task.StartDingDingTask;


public class MainActivity extends AppCompatActivity {

    private static final MyLogger log = MyLogManager.getLogger();

    private Intent mServiceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        log.info("Start CheckInService...");
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
        log.info(String.format("On %s onDestroy!", getClass().getSimpleName()));
        stopService(mServiceIntent);
        super.onDestroy();
    }
}
