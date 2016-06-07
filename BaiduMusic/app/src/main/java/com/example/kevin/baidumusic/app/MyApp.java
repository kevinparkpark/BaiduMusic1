package com.example.kevin.baidumusic.app;

import android.app.Application;
import android.content.Context;
import android.util.Log;

/**
 * Created by kevin on 16/5/23.
 */
public class MyApp extends Application{

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context=this;
        Log.d("MyApp", "onCreate");
    }


}
