package com.example.kevin.baidumusic.loginandregister;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kevin.baidumusic.R;
import com.example.kevin.baidumusic.base.BaseFragment;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by kevin on 16/6/14.
 */
public class Login2LogFragment extends BaseFragment {
    private Button button;
    private EditText etUser, etPw;
    private TextView tvReg;

    @Override
    public int setlayout() {
        return R.layout.fragment_login_login;
    }

    @Override
    protected void initView(View view) {
        button = (Button) view.findViewById(R.id.btn_login_login);
        etUser = (EditText) view.findViewById(R.id.et_login_id);
        etPw = (EditText) view.findViewById(R.id.et_login_pw);
        tvReg= (TextView) view.findViewById(R.id.tv_login_register);
    }

    @Override
    protected void initData() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobUser bmobUser=new BmobUser();
                bmobUser.setUsername(etUser.getText().toString());
                bmobUser.setPassword(etPw.getText().toString());
                bmobUser.login(context, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(context, "登录成功", Toast.LENGTH_SHORT).show();
                        ((Login2LogedOnClickListener)getActivity()).onLogin2LogedClickListener();
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Toast.makeText(context, "登录失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        tvReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context,RegisterActivity.class));
                getActivity().finish();
            }
        });
    }
    public interface Login2LogedOnClickListener{
        void onLogin2LogedClickListener();
    }

}