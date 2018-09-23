package com.example.wenfeng.mysecondapp.utility;

import android.app.Service;
import android.content.Intent;

public class AndroidUtility {
    public static void startApplication(Service service, String packageName){
        Intent launchIntent = service.getPackageManager().getLaunchIntentForPackage(packageName);
        service.startActivity(launchIntent);
    }


    public static void waitForSeconds(int seconds){
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
