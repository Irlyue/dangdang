package com.example.wenfeng.mysecondapp.log;

import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyLogger {
    private static final String LOG_TAG = "MyLogger";
    private File mLogFile;
    private BufferedWriter mWriter;
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

    public MyLogger(String fileName){
        try {
            initLogging(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initLogging(String fileName) throws IOException{
        File appDirectory = new File( Environment.getExternalStorageDirectory() + "/" + "Dangdang");
        mLogFile = new File(appDirectory, fileName + ".txt");

        if ( !appDirectory.exists() ) {
            appDirectory.mkdir();
        }

        if(!mLogFile.exists()){
            mLogFile.createNewFile();
        }
        mWriter = new BufferedWriter(new FileWriter(mLogFile, true));
        info("", "=====================================================================");
    }

    public void info(String msg){
        info(LOG_TAG, msg);
    }

    public void info(String tag, String msg){
        String fmt = "%s %s: %s";
        try {
            mWriter.append(String.format(fmt, tag, mDateFormat.format(new Date()), msg));
            mWriter.newLine();
            mWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onDestroy(){
        try {
            mWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
