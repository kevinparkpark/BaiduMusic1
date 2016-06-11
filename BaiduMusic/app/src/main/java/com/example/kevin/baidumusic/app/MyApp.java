package com.example.kevin.baidumusic.app;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import cn.bmob.v3.Bmob;

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
        Bmob.initialize(this,"cdcb8445ac642e55c8bdd06b7525fe0a");
    }


}
