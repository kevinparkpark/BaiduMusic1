package com.example.kevin.baidumusic.app;

import android.app.Application;
import android.content.Context;

/**
 * Created by kevin on 16/5/23.
 */
public class MyApp extends Application{

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context=this;
    }
}
