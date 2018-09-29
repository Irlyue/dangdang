package com.example.wenfeng.mysecondapp.log;

import java.util.HashMap;

public class MyLogManager {
    private final static String ROOT_LOGGER = "Root";
    private static HashMap<String, MyLogger> loggers = new HashMap<>();

    public static MyLogger getLogger(String name){
        MyLogger logger;
        if(loggers.containsKey(name)){
            logger = loggers.get(name);
        }else{
            logger = new MyLogger(name);
            loggers.put(name, logger);
        }
        return logger;
    }

    public static MyLogger getLogger(Class<?> cls){
        return getLogger(cls.getSimpleName());
    }

    public static MyLogger getLogger(){
        return getLogger(ROOT_LOGGER);
    }
}
