package com.example.kevin.baidumusic.totalfragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.example.kevin.baidumusic.base.BaseFragment;

import java.util.ArrayList;

/**
 * Created by kevin on 16/5/19.
 */
public class TotalFragmentPagerAdapter extends android.support.v4.app.FragmentPagerAdapter{
    private ArrayList<BaseFragment> fragments;
    private String[] titles={"我的","乐库","歌手","直播"};


    public void setFragments(ArrayList<BaseFragment> fragments) {
        this.fragments = fragments;
        notifyDataSetChanged();
    }

    public TotalFragmentPagerAdapter(FragmentManager fm) {
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
