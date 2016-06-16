package com.example.kevin.baidumusic.musiclibrary.recommend;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.example.kevin.baidumusic.base.BaseFragment;
import com.example.kevin.baidumusic.R;
import com.example.kevin.baidumusic.netutil.NetListener;
import com.example.kevin.baidumusic.netutil.NetTool;
import com.example.kevin.baidumusic.netutil.URLValues;
import com.example.kevin.baidumusic.util.BroadcastValues;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 16/5/19.
 */
public class RecommendFragment extends BaseFragment implements View.OnClickListener {
    private ViewPager viewPager;
    private RecommendPagerAdapter adapter;
    private Handler handler;
    private boolean thredAlive = true;
    private boolean userTouch = false;
    private int sleepTick;
    private List<RecommendBean.PicBean> picBeanList;
    private Thread thread;
    private ViewGroup group;
    private ImageView tips[];
    private List<RecommendSongBean.ContentBean.ListBean> listBeen;
    private RecommendRecyclerAdapter recyclerAdapter;
    private RecyclerView recyclerView;
    private ImageView ivAllAuthor;
    private TextView tvSonglistMore;

    @Override
    public int setlayout() {
        return R.layout.fragment_le_recommend;
    }

    @Override
    protected void initView(View view) {
        viewPager = (ViewPager) view.findViewById(R.id.recommend_viewpager);
        group = (ViewGroup) view.findViewById(R.id.recommend_viewgroup);
        recyclerView = (RecyclerView) view.findViewById(R.id.le_recommend_recyclerview);
        ivAllAuthor = (ImageView) view.findViewById(R.id.iv_le_recommend_allauthor);
        tvSonglistMore = (TextView) view.findViewById(R.id.tv_recommend_songlist_more);
    }

    @Override
    protected void initData() {
        ivAllAuthor.setOnClickListener(this);
        tvSonglistMore.setOnClickListener(this);

        adapter = new RecommendPagerAdapter();
        picBeanList = new ArrayList<>();
        //设置缓存图片数量及间距
        viewPager.setOffscreenPageLimit(5);
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
                //画点点
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
                        for (sleepTick = 0; sleepTick < 3; sleepTick++)
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

        recyclerAdapter = new RecommendRecyclerAdapter(context);
        GridLayoutManager manager = new GridLayoutManager(context, 3);
        manager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        //取消recyclerview焦点,加载后先显示viewpager
        recyclerView.setFocusable(false);

        NetTool netTool1 = new NetTool();
        netTool1.getUrl(new NetListener() {
            @Override
            public void onSuccessed(String result) {
                Gson gson = new Gson();
                RecommendSongBean bean = gson.fromJson(result, RecommendSongBean.class);
                listBeen = new ArrayList<>();
                listBeen.addAll(bean.getContent().getList());
                recyclerAdapter.setListBeen(listBeen);
                recyclerView.setAdapter(recyclerAdapter);

                recyclerAdapter.setClickListener(new RecommendToSonglistOnClickListener() {
                    @Override
                    public void onRecommendToSonglistClickListener(int position) {
                        ((RecommendToSongMenuDetailsOnClickListener) getActivity())
                                .onRecommendToSongMenuDetailsClickListener(listBeen.get(position).getListid());
                    }
                });
            }

            @Override
            public void onFailed(VolleyError error) {

            }
        }, URLValues.RECOMMEND_SONGLIST);

    }

    public interface RecommendToSongMenuDetailsOnClickListener {
        void onRecommendToSongMenuDetailsClickListener(String listId);
    }

    public interface RecommendToKmusicOnClickListener {
        void onRecommendToKmusicClickListener();
    }

    @Override
    public void onDestroy() {
        thredAlive = false;
        super.onDestroy();
    }

    //设置轮播图点点
    public void setImageBackground(int items) {
//        int index = items % tips.length;
        if (tips != null && tips.length > 0) {
            for (int i = 0; i < tips.length; i++) {
                if (i == items % tips.length) {
                    tips[i].setBackgroundResource(R.mipmap.page_indicator_focused);
                } else {
                    tips[i].setBackgroundResource(R.mipmap.page_indicator_unfocused);
                }
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //全部歌手
            case R.id.iv_le_recommend_allauthor:
                ((RecommendToKmusicOnClickListener) getActivity()).onRecommendToKmusicClickListener();
                break;
            //歌曲推荐-更多
            case R.id.tv_recommend_songlist_more:
                context.sendBroadcast(new Intent(BroadcastValues.RECO_TO_SONGLIST));
                break;
        }
    }
}
