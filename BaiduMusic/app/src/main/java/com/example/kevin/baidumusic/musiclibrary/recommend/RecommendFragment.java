package com.example.kevin.baidumusic.musiclibrary.recommend;

import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.VolleyError;
import com.example.kevin.baidumusic.base.BaseFragment;
import com.example.kevin.baidumusic.R;
import com.example.kevin.baidumusic.netutil.NetListener;
import com.example.kevin.baidumusic.netutil.NetTool;
import com.example.kevin.baidumusic.netutil.URLValues;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

/**
 * Created by kevin on 16/5/19.
 */
public class RecommendFragment extends BaseFragment {
    private ViewPager viewPager;
    private RecommendAdapter adapter;
    private Handler handler;
    private boolean thredAlive = true;
    private boolean userTouch = false;
    private int sleepTick;
    private List<RecommendBean.PicBean> picBeanList;
    private Thread thread;
    private ViewGroup group;
    private ImageView tips[];
    private int tipsCount;

    @Override
    public int setlayout() {
        return R.layout.fragment_le_recommend;
    }

    @Override
    protected void initView(View view) {
        viewPager = (ViewPager) view.findViewById(R.id.recommend_viewpager);
        group = (ViewGroup) view.findViewById(R.id.recommend_viewgroup);
    }

    @Override
    protected void initData() {
        adapter = new RecommendAdapter();
        picBeanList = new ArrayList<>();
        //设置缓存图片数量及艰巨
        viewPager.setOffscreenPageLimit(3);
        viewPager.setPageMargin(10);

        NetTool netTool = new NetTool();
        netTool.getUrl(new NetListener() {
            @Override
            public void onSuccessed(String result) {
                Gson gson = new Gson();
                RecommendBean recommendBean = gson.fromJson(result, RecommendBean.class);
                picBeanList = recommendBean.getPic();
                adapter.setPicBeanList(picBeanList);
                viewPager.setAdapter(adapter);

                //获得tips的数量
                tips = new ImageView[picBeanList.size()];
                //花点点
                for (int i = 0; i < tips.length; i++) {
                    ImageView imageView = new ImageView(context);
                    if (i == 0) {
                        imageView.setBackgroundResource(R.mipmap.page_indicator_focused);
                    } else {
                        imageView.setBackgroundResource(R.mipmap.page_indicator_unfocused);
                    }
                    tips[i] = imageView;
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup
                            .LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    layoutParams.leftMargin = 10;// 设置点点点view的左边距
                    layoutParams.rightMargin = 10;// 设置点点点view的右边距
                    group.addView(imageView, layoutParams);
                }
            }

            @Override
            public void onFailed(VolleyError error) {

            }
        }, URLValues.RECOMMEND_BANNERS);


        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                int current = viewPager.getCurrentItem();
                viewPager.setCurrentItem(current + 1);
                return false;
            }
        });
        if (thread == null || !thread.isAlive()) {
            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (thredAlive) {
                        for (sleepTick = 0; sleepTick < 2; sleepTick++)
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        if ((!userTouch)) {
                            handler.sendEmptyMessage(0);
                        }
                    }
                }
            });
            thread.start();
        }
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //当用户触摸了轮播图的时候
                        userTouch = true;
                        break;
                    case MotionEvent.ACTION_UP:
                        //当用户手指离开轮播图的时候
                        userTouch = false;
                        sleepTick = 0;//每次当用户抬起手指,都会重新开始计时
                        break;
                }
                return false;
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setImageBackground(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



    }

    @Override
    public void onDestroy() {
        thredAlive = false;
        super.onDestroy();
    }
    //设置轮播图点点
    public void setImageBackground(int items) {
        int index = items % tips.length;
        for (int i = 0; i < tips.length; i++) {
            if (i == index) {
                tips[i].setBackgroundResource(R.mipmap.page_indicator_focused);
            } else {
                tips[i].setBackgroundResource(R.mipmap.page_indicator_unfocused);
            }
        }

    }

}
