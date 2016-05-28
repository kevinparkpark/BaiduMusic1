package com.example.kevin.baidumusic.musiclibrary;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.kevin.baidumusic.base.BaseFragment;

import java.util.ArrayList;

/**
 * Created by kevin on 16/5/19.
 */
public class LeFragmentPagerAdapter extends FragmentPagerAdapter{
    private ArrayList<BaseFragment> fragments;
    private String titles[]={"推荐","排行","歌单","电台","MV"};

    public void setFragments(ArrayList<BaseFragment> fragments) {
        this.fragments = fragments;
        notifyDataSetChanged();
    }

    public LeFragmentPagerAdapter(FragmentManager fm) {
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
