package com.example.kevin.baidumusic.musiclibrary.radio;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.example.kevin.baidumusic.app.MyApp;
import com.example.kevin.baidumusic.base.BaseFragment;
import com.example.kevin.baidumusic.R;
import com.example.kevin.baidumusic.musiclibrary.radio.radioplay.RadioPlayActivity;

/**
 * Created by kevin on 16/5/19.
 */
public class RadioFragment extends BaseFragment{
    private TextView tvMore;
    @Override
    public int setlayout() {
        return R.layout.fragment_le_radio;
    }

    @Override
    protected void initView(View view) {
        tvMore= (TextView) view.findViewById(R.id.tv_le_radio_more);
    }

    @Override
    protected void initData() {
        tvMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyApp.context,RadioPlayActivity.class));
            }
        });
    }
}
