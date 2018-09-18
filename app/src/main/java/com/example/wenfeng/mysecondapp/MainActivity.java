package com.example.wenfeng.mysecondapp;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private Intent mServiceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    public void onButtonClicked(View v){
        startCheckInService();
    }
    private void startCheckInService(){
        if(!isServiceRunning(CheckInService.class)) {
            Toast.makeText(this, "Service starting", Toast.LENGTH_SHORT).show();
            EditText editText = (EditText)findViewById(R.id.time_edittext);
            mServiceIntent = new Intent(this, CheckInService.class);
            mServiceIntent.putExtra(CheckInService.E_CHECK_IN_TIMER, editText.getText().toString());
            mServiceIntent.putExtra(CheckInService.E_RESET_TIME, "23:30:00");
            startService(mServiceIntent);
        }else{
            Toast.makeText(this, "Service already started!", Toast.LENGTH_SHORT).show();
        }
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
        Log.i("MainActivity", "onDestroy");
        super.onDestroy();
    }
}
