package com.example.kevin.baidumusic.totalfragment;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.example.kevin.baidumusic.R;
import com.example.kevin.baidumusic.base.BaseFragment;
import com.example.kevin.baidumusic.kmusic.KMusicFragment;
import com.example.kevin.baidumusic.loginandregister.LoginActivity;
import com.example.kevin.baidumusic.musiclibrary.LeFragment;
import com.example.kevin.baidumusic.liveFragment.LiveFragment;
import com.example.kevin.baidumusic.mymusic.MyFragment;
import java.util.ArrayList;

/**
 * Created by kevin on 16/5/19.
 * 主fragment
 */
public class TotalFragment extends BaseFragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ArrayList<BaseFragment> fragments;
    private TotalFragmentPagerAdapter adapter;
    private ImageView ivTitleSearch,ivTitle2Login;
    private titleOnClick titleOnClick;

    public void setTitleOnClick(TotalFragment.titleOnClick titleOnClick) {
        this.titleOnClick = titleOnClick;
    }

    @Override
    public int setlayout() {
        return R.layout.fragment_title;
    }

    @Override
    protected void initView(View view) {
        tabLayout = (TabLayout) view.findViewById(R.id.title_tablayout);
        viewPager = (ViewPager) view.findViewById(R.id.title_viewpager);
        ivTitleSearch= (ImageView) view.findViewById(R.id.iv_title_search);
        ivTitle2Login= (ImageView) view.findViewById(R.id.iv_title2login);
    }

    @Override
    protected void initData() {

        adapter = new TotalFragmentPagerAdapter(getActivity().getSupportFragmentManager());
        fragments = new ArrayList<>();
        fragments.add(new MyFragment());
        fragments.add(new LeFragment());
        fragments.add(new KMusicFragment());
        fragments.add(new LiveFragment());

        adapter.setFragments(fragments);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        //跳转另一个fragment,使用接口
        ivTitleSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                titleOnClick.onTitleClick();

            }
        });
        ivTitle2Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, LoginActivity.class));
            }
        });
    }

    public interface titleOnClick{
        void onTitleClick();
    }
    public void flipPage(){
        viewPager.setCurrentItem(2);
    }
}
