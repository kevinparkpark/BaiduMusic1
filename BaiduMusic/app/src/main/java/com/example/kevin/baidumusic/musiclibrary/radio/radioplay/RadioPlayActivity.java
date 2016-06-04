package com.example.kevin.baidumusic.musiclibrary.radio.radioplay;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.example.kevin.baidumusic.R;
import com.example.kevin.baidumusic.base.BaseFragment;
import com.example.kevin.baidumusic.netutil.URLValues;
import com.example.kevin.baidumusic.util.BroadcastValues;

import java.util.ArrayList;

/**
 * Created by kevin on 16/6/2.
 */
public class RadioPlayActivity extends AppCompatActivity {
    private ArrayList<BaseFragment> fragments;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private RadioPlayPagerAdapter adapter;
    private ImageView ivBack;
    private ReceviceRaidoPlaylist receviceRaidoPlaylist;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radioplay);
        tabLayout= (TabLayout) findViewById(R.id.radioplay_tablayout);
        viewPager= (ViewPager) findViewById(R.id.radioplay_viewpager);
        ivBack= (ImageView) findViewById(R.id.iv_activity_radioplay_back);

        receviceRaidoPlaylist=new ReceviceRaidoPlaylist();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(BroadcastValues.FINISH_RADIOPLAY);
        registerReceiver(receviceRaidoPlaylist,intentFilter);

        adapter=new RadioPlayPagerAdapter(getSupportFragmentManager());
        fragments=new ArrayList<>();



        RadioPlayFragment radioPlayFragment1=new RadioPlayFragment();
        Bundle bundle1=new Bundle();
        bundle1.putString("url", URLValues.RADIOPLAY_EXERCISE);
        radioPlayFragment1.setArguments(bundle1);
        fragments.add(radioPlayFragment1);

        RadioPlayFragment radioPlayFragment2=new RadioPlayFragment();
        Bundle bundle2=new Bundle();
        bundle2.putString("url", URLValues.RADIOPLAY_MOOD);
        radioPlayFragment2.setArguments(bundle2);
        fragments.add(radioPlayFragment2);

        RadioPlayFragment radioPlayFragment3=new RadioPlayFragment();
        Bundle bundle3=new Bundle();
        bundle3.putString("url", URLValues.RADIOPLAY_GENRE);
        radioPlayFragment3.setArguments(bundle3);
        fragments.add(radioPlayFragment3);

        RadioPlayFragment radioPlayFragment4=new RadioPlayFragment();
        Bundle bundle4=new Bundle();
        bundle4.putString("url", URLValues.RADIOPLAY_LANGUAGE);
        radioPlayFragment4.setArguments(bundle4);
        fragments.add(radioPlayFragment4);

        RadioPlayFragment radioPlayFragment5=new RadioPlayFragment();
        Bundle bundle5=new Bundle();
        bundle5.putString("url", URLValues.RADIOPLAY_YEARS);
        radioPlayFragment5.setArguments(bundle5);
        fragments.add(radioPlayFragment5);

        RadioPlayFragment radioPlayFragment6=new RadioPlayFragment();
        Bundle bundle6=new Bundle();
        bundle6.putString("url", URLValues.RADIOPLAY_THEME);
        radioPlayFragment6.setArguments(bundle6);
        fragments.add(radioPlayFragment6);

        adapter.setFragments(fragments);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
//        tabLayout.setTabTextColors(R.color.radionormal,R.color.white);
//        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    class ReceviceRaidoPlaylist extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receviceRaidoPlaylist);
    }
}
