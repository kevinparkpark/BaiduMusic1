package com.example.kevin.baidumusic.mymusic.localmusic;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.kevin.baidumusic.base.BaseFragment;

import java.util.ArrayList;

/**
 * Created by kevin on 16/5/22.
 */
public class LocalMusicAdapter extends FragmentPagerAdapter{
    private ArrayList<BaseFragment> fragments;
    private String titles[]={"歌曲","文件夹","歌手","专辑"};

    public void setFragments(ArrayList<BaseFragment> fragments) {
        this.fragments = fragments;
        notifyDataSetChanged();
    }

    public LocalMusicAdapter(FragmentManager fm) {
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
