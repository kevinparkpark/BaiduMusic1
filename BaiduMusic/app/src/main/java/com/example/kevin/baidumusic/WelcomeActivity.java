package com.example.kevin.baidumusic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.bartoszlipinski.recyclerviewheader.RecyclerViewHeader;

/**
 * Created by kevin on 16/5/18.
 */
public class WelcomeActivity extends Activity{

    //欢迎页面
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        //3秒跳转
        CountDownTimer timer=new CountDownTimer(3000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                startActivity(new Intent(WelcomeActivity.this,MainActivity.class));
                finish();
            }
        }.start();
    }
}
