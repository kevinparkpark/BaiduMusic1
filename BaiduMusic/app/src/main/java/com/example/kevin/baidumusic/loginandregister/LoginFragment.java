package com.example.kevin.baidumusic.loginandregister;

import android.view.View;
import android.widget.TextView;

import com.example.kevin.baidumusic.R;
import com.example.kevin.baidumusic.base.BaseFragment;

import cn.bmob.v3.BmobUser;

/**
 * Created by kevin on 16/6/11.
 */
public class LoginFragment extends BaseFragment{
    private TextView tvUsername,tvLogOut;
    @Override
    public int setlayout() {
        return R.layout.fragment_replace_login;
    }

    @Override
    protected void initView(View view) {
        tvUsername= (TextView) view.findViewById(R.id.tv_login_replace_username);
        tvLogOut= (TextView) view.findViewById(R.id.tv_login_replace_logout);
    }

    @Override
    protected void initData() {
//        String user=getArguments().getString("user");
        final BmobUser bmobUser=BmobUser.getCurrentUser(context);

        tvUsername.setText(context.getString(R.string.welcome_login)+bmobUser.getUsername());
        tvLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Login2LogOutOnClcikListener)getActivity()).onLogin2LogOutClickListener();

            }
        });
    }

    public interface Login2LogOutOnClcikListener{
        void onLogin2LogOutClickListener();
    }
}
