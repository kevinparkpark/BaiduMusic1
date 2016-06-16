package com.example.kevin.baidumusic.loginandregister;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kevin.baidumusic.R;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by kevin on 16/6/9.
 */
public class RegisterActivity extends AppCompatActivity {
    private EditText etUser, etPw;
    private Button btnRegister;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        etUser = (EditText) findViewById(R.id.et_register_username);
        etPw = (EditText) findViewById(R.id.et_register_pw);
        btnRegister = (Button) findViewById(R.id.btn_register_register);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etUser.length() != 0 && etPw.length() != 0) {
                    BmobUser bmobUser = new BmobUser();
                    bmobUser.setUsername(etUser.getText().toString());
                    bmobUser.setPassword(etPw.getText().toString());
                    bmobUser.signUp(RegisterActivity.this, new SaveListener() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(RegisterActivity.this, R.string.register_success, Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                            intent.putExtra(getString(R.string.user_register),etUser.getText().toString());
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            Toast.makeText(RegisterActivity.this, R.string.register_fail, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });


    }
}
