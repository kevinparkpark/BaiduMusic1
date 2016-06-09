package com.example.kevin.baidumusic.loginandregister;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kevin.baidumusic.R;

/**
 * Created by kevin on 16/6/8.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView ivWifi,ivClock,ivDesk,ivScreen,ivBack;
    private TextView tv2reg;
    private boolean flag;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ivWifi= (ImageView) findViewById(R.id.iv_login_wifi);
        ivClock= (ImageView) findViewById(R.id.iv_login_clock);
        ivDesk= (ImageView) findViewById(R.id.iv_desk);
        ivScreen= (ImageView) findViewById(R.id.iv_login_screen);
        ivBack= (ImageView) findViewById(R.id.iv_login_back);
        tv2reg= (TextView) findViewById(R.id.tv_login_register);

        ivWifi.setOnClickListener(this);
        ivClock.setOnClickListener(this);
        ivDesk.setOnClickListener(this);
        ivScreen.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        tv2reg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_login_wifi:
                if (flag=!flag){
                    ivWifi.setImageResource(R.mipmap.bt_setup_wifi_hl);
                }else {
                    ivWifi.setImageResource(R.mipmap.bt_setup_wifi_nor);
                }
                break;
            case R.id.iv_login_clock:
                if (flag=!flag){
                    ivClock.setImageResource(R.mipmap.bt_setup_auto_close_hl);
                }else {
                    ivClock.setImageResource(R.mipmap.bt_setup_auto_close_nor);
                }
                break;
            case R.id.iv_desk:
                if (flag=!flag){
                    ivDesk.setImageResource(R.mipmap.bt_setup_desktoplyrics_hl);
                }else {
                    ivDesk.setImageResource(R.mipmap.bt_setup_desktoplyrics_nor);
                }
                break;
            case R.id.iv_login_screen:
                if (flag=!flag){
                    ivScreen.setImageResource(R.mipmap.bt_setup_lockscreen_hl);
                }else {
                    ivScreen.setImageResource(R.mipmap.bt_setup_lockscreen_nor);
                }
                break;
            case R.id.iv_login_back:
                finish();
                break;
            case R.id.tv_login_register:
                startActivity(new Intent(this,RegisterActivity.class));
                break;
        }
    }
}
