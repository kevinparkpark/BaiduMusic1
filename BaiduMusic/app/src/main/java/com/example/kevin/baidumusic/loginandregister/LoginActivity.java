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
public class LoginActivity extends AppCompatActivity implements View.OnClickListener,LoginFragment.Login2LogOutOnClcikListener {
    private ImageView ivWifi,ivClock,ivDesk,ivScreen,ivBack;
    private TextView tv2reg;
    private EditText etUser,etPw;
    private Button btnLogin;
    private boolean flag;
    private LoginFragment loginFragment;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        BmobUser bmobUser=BmobUser.getCurrentUser(this);
        Log.d("LoginActivity","-------"+ bmobUser.getUsername());

//        String user=getIntent().getStringExtra("user");
        if (bmobUser.getUsername()!=null){
            loginFragment=new LoginFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.relativelayout_login,loginFragment).commit();
            Bundle bundle=new Bundle();
            bundle.putString("user",bmobUser.getUsername());
            loginFragment.setArguments(bundle);
        }

        ivWifi= (ImageView) findViewById(R.id.iv_login_wifi);
        ivClock= (ImageView) findViewById(R.id.iv_login_clock);
        ivDesk= (ImageView) findViewById(R.id.iv_desk);
        ivScreen= (ImageView) findViewById(R.id.iv_login_screen);
        ivBack= (ImageView) findViewById(R.id.iv_login_back);
        tv2reg= (TextView) findViewById(R.id.tv_login_register);
        btnLogin= (Button) findViewById(R.id.btn_login_login);
        etUser= (EditText) findViewById(R.id.et_login_id);
        etPw= (EditText) findViewById(R.id.et_login_pw);

        ivWifi.setOnClickListener(this);
        ivClock.setOnClickListener(this);
        ivDesk.setOnClickListener(this);
        ivScreen.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        tv2reg.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

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
            case R.id.btn_login_login:
                BmobUser bmobUser=new BmobUser();
                bmobUser.setUsername(etUser.getText().toString());
                bmobUser.setPassword(etPw.getText().toString());
                bmobUser.login(this, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                            loginFragment=new LoginFragment();
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.relativelayout_login,loginFragment).commit();
                            Bundle bundle=new Bundle();
                            bundle.putString("user",etUser.getText().toString());
                            loginFragment.setArguments(bundle);
                        etUser.setText("");
                        etPw.setText("");
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }
    }

    @Override
    public void onLogin2LogOutClickListener() {
        getSupportFragmentManager().beginTransaction().hide(loginFragment).commit();
    }
}
