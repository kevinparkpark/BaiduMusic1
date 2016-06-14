package com.example.kevin.baidumusic.loginandregister;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kevin.baidumusic.R;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by kevin on 16/6/8.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener,LoginFragment.Login2LogOutOnClcikListener
,Login2LogFragment.Login2LogedOnClickListener{
    private ImageView ivWifi,ivClock,ivDesk,ivScreen,ivBack;
    private boolean flag;
    private LoginFragment loginFragment;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        BmobUser bmobUser=BmobUser.getCurrentUser(this);

//        String user=getIntent().getStringExtra("user");
        if (bmobUser.getUsername()!=null){
            loginFragment=new LoginFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.framelayout_login,loginFragment).commit();
            Bundle bundle=new Bundle();
            bundle.putString("user",bmobUser.getUsername());
            loginFragment.setArguments(bundle);
        }else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.framelayout_login,new Login2LogFragment()).commit();
        }

        ivWifi= (ImageView) findViewById(R.id.iv_login_wifi);
        ivClock= (ImageView) findViewById(R.id.iv_login_clock);
        ivDesk= (ImageView) findViewById(R.id.iv_desk);
        ivScreen= (ImageView) findViewById(R.id.iv_login_screen);
        ivBack= (ImageView) findViewById(R.id.iv_login_back);

        ivWifi.setOnClickListener(this);
        ivClock.setOnClickListener(this);
        ivDesk.setOnClickListener(this);
        ivScreen.setOnClickListener(this);
        ivBack.setOnClickListener(this);

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
        }
    }

    @Override
    public void onLogin2LogOutClickListener() {
        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_login,new Login2LogFragment()).commit();
    }

    @Override
    public void onLogin2LogedClickListener() {
        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_login,new LoginFragment()).commit();
    }
}
