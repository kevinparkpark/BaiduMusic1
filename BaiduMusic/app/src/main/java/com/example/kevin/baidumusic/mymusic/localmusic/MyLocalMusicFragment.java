package com.example.kevin.baidumusic.mymusic.localmusic;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.kevin.baidumusic.MainActivity;
import com.example.kevin.baidumusic.R;
import com.example.kevin.baidumusic.base.BaseFragment;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by kevin on 16/5/22.
 */
public class MyLocalMusicFragment extends BaseFragment{

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ArrayList<BaseFragment> fragments;
    private LocalMusicAdapter adapter;
    private RelativeLayout relativeLayout;

    @Override
    public int setlayout() {
        return R.layout.my_localmusic;
    }

    @Override
    protected void initView(View view) {
        tabLayout= (TabLayout) view.findViewById(R.id.localmusic_tablayout);
        viewPager= (ViewPager) view.findViewById(R.id.localmusic_viewpager);
        relativeLayout= (RelativeLayout) view.findViewById(R.id.back_to_mylocalmusic);
    }

    @Override
    protected void initData() {
        adapter=new LocalMusicAdapter(getActivity().getSupportFragmentManager());

        fragments=new ArrayList<>();
        fragments.add(new MyLocalMusicSongFragment());
        fragments.add(new MyLocalMusicFolderFragment());
        fragments.add(new MyLocalMusicAuthorFragment());
        fragments.add(new MyLocalMusicAlbumFragment());

        adapter.setFragments(fragments);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
    }

    @Override
    public void onDestroy() {
        ((MainActivity)getActivity()).showTitleFragment();
        super.onDestroy();
    }
}
