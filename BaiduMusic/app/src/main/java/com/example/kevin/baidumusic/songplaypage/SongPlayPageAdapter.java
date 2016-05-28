package com.example.kevin.baidumusic.songplaypage;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.kevin.baidumusic.base.BaseFragment;

import java.util.ArrayList;

/**
 * Created by kevin on 16/5/26.
 */
public class SongPlayPageAdapter extends FragmentPagerAdapter{
    private ArrayList<BaseFragment> fragments;

    public void setFragments(ArrayList<BaseFragment> fragments) {
        this.fragments = fragments;
        notifyDataSetChanged();
    }

    public SongPlayPageAdapter(FragmentManager fm) {
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
}
