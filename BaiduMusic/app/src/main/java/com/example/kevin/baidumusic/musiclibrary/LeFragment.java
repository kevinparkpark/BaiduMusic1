package com.example.kevin.baidumusic.musiclibrary;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.example.kevin.baidumusic.base.BaseFragment;
import com.example.kevin.baidumusic.R;
import com.example.kevin.baidumusic.musiclibrary.mv.MvFragment;
import com.example.kevin.baidumusic.musiclibrary.radio.RadioFragment;
import com.example.kevin.baidumusic.musiclibrary.rank.RankFragment;
import com.example.kevin.baidumusic.musiclibrary.recommend.RecommendFragment;
import com.example.kevin.baidumusic.musiclibrary.songmenu.SongMenuFragment;

import java.util.ArrayList;

/**
 * Created by kevin on 16/5/19.
 */
public class LeFragment extends BaseFragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ArrayList<BaseFragment> fragments;
    private LeFragmentPagerAdapter adapter;

    @Override
    public int setlayout() {
        return R.layout.fragment_le;
    }

    @Override
    protected void initView(View view) {
        tabLayout = (TabLayout) view.findViewById(R.id.tablayout_le);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager_le);

    }

    @Override
    protected void initData() {

        adapter = new LeFragmentPagerAdapter(getActivity().getSupportFragmentManager());
        fragments = new ArrayList<>();

        fragments.add(new RecommendFragment());
        fragments.add(new RankFragment());
        fragments.add(new SongMenuFragment());
        fragments.add(new RadioFragment());
        fragments.add(new MvFragment());

        adapter.setFragments(fragments);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }
}
