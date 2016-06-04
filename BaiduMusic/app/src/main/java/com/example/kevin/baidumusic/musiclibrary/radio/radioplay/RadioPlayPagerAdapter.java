package com.example.kevin.baidumusic.musiclibrary.radio.radioplay;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.kevin.baidumusic.base.BaseFragment;

import java.util.ArrayList;

/**
 * Created by kevin on 16/6/2.
 */
public class RadioPlayPagerAdapter extends FragmentPagerAdapter{
    private ArrayList<BaseFragment> fragments;
    private String[] titles={"活动","心情","主题","语言","年代","曲风"};

    public void setFragments(ArrayList<BaseFragment> fragments) {
        this.fragments = fragments;
        notifyDataSetChanged();
    }

    public RadioPlayPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments==null?0:fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
