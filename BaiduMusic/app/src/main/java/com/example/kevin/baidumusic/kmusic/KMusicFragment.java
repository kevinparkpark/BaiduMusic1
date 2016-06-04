package com.example.kevin.baidumusic.kmusic;

import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;
import com.example.kevin.baidumusic.base.BaseFragment;
import com.example.kevin.baidumusic.R;
import com.example.kevin.baidumusic.netutil.NetListener;
import com.example.kevin.baidumusic.netutil.NetTool;
import com.example.kevin.baidumusic.netutil.URLValues;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static com.example.kevin.baidumusic.R.id.k_viewpager;

/**
 * Created by kevin on 16/5/19.
 */
public class KMusicFragment extends BaseFragment implements View.OnClickListener {
    private ViewPager viewPager;
    private List<KMusicBean.ArtistBean> artistBeanList;
    private kMusicAdapter adapter;
    private int sleepTick;
    private ViewGroup group;
    private ImageView tips[];
    private int tipsCount;
    private boolean userTouch = false;
    private boolean threadAlive = true;
    private Handler handler;
    private Thread thread;
    private RelativeLayout rlCHMan, rlChWo, rlCHGroup, rlEuMan, rlEuWo, rlEuGroup, rlKrMan, rlKrWo,
            rlKrGroup, rlJpMan, rlJpWo, rlJpGroup, rlOther;

    @Override
    public int setlayout() {
        return R.layout.fragment_k;
    }

    @Override
    protected void initView(View view) {
        viewPager = (ViewPager) view.findViewById(k_viewpager);
        group = (ViewGroup) view.findViewById(R.id.kmusic_viewgroup);
        rlCHMan = (RelativeLayout) view.findViewById(R.id.k_manauthor);
        rlChWo = (RelativeLayout) view.findViewById(R.id.k_womenauthor);
        rlCHGroup = (RelativeLayout) view.findViewById(R.id.k_groupauthor);

        rlEuMan = (RelativeLayout) view.findViewById(R.id.k_euro_man_author);
        rlEuWo = (RelativeLayout) view.findViewById(R.id.k_euro_women_author);
        rlEuGroup = (RelativeLayout) view.findViewById(R.id.k_euro_group_author);

        rlKrMan = (RelativeLayout) view.findViewById(R.id.k_korea_man_author);
        rlKrWo = (RelativeLayout) view.findViewById(R.id.k_korea_woman_author);
        rlKrGroup = (RelativeLayout) view.findViewById(R.id.k_korea_group_author);

        rlJpMan = (RelativeLayout) view.findViewById(R.id.k_jp_man_author);
        rlJpWo = (RelativeLayout) view.findViewById(R.id.k_jp_woman_author);
        rlJpGroup = (RelativeLayout) view.findViewById(R.id.k_jp_group_author);

        rlOther = (RelativeLayout) view.findViewById(R.id.k_other_author);
        rlCHMan.setOnClickListener(this);
        rlChWo.setOnClickListener(this);
        rlCHGroup.setOnClickListener(this);
        rlEuMan.setOnClickListener(this);
        rlEuWo.setOnClickListener(this);
        rlEuGroup.setOnClickListener(this);
        rlKrMan.setOnClickListener(this);
        rlKrWo.setOnClickListener(this);
        rlKrGroup.setOnClickListener(this);
        rlJpMan.setOnClickListener(this);
        rlJpWo.setOnClickListener(this);
        rlJpGroup.setOnClickListener(this);
        rlOther.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        adapter = new kMusicAdapter();
        artistBeanList = new ArrayList<>();
        viewPager.setOffscreenPageLimit(3);
        viewPager.setPageMargin(10);
        NetTool netTool = new NetTool();
        netTool.getUrl(new NetListener() {
            @Override
            public void onSuccessed(String result) {
                Gson gson = new Gson();
                KMusicBean kMusicBean = gson.fromJson(result, KMusicBean.class);
                artistBeanList = kMusicBean.getArtist();
                adapter.setArtistBeanList(artistBeanList);
                viewPager.setAdapter(adapter);

                tips = new ImageView[artistBeanList.size()];
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
        }, URLValues.KMUSIC_BANNERS);

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
                    while (threadAlive) {
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

    public void setImageBackground(int position) {
        int index = position % tips.length;
        for (int i = 0; i < tips.length; i++) {
            if (i == index) {
                tips[i].setBackgroundResource(R.mipmap.page_indicator_focused);
            } else {
                tips[i].setBackgroundResource(R.mipmap.page_indicator_unfocused);
            }
        }
    }

    @Override
    public void onDestroy() {
        threadAlive = false;
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.k_manauthor:
                ((kMusicToDetailsOnClickListener) getActivity()).onKMusicToDetailsClickListener(
                        URLValues.KMUSIC_CH_MAN_AUTHOR_LIST1, URLValues.KMUSIC_CH_MAN_AUTHOR_LIST2, "华语男歌手");
                break;
            case R.id.k_womenauthor:
                ((kMusicToDetailsOnClickListener) getActivity()).onKMusicToDetailsClickListener(
                        URLValues.KMUSIC_CH_WOMAN_AUTHOR_LIST1, URLValues.KMUSIC_CH_WOMAN_AUTHOR_LIST2, "华语女歌手");
                break;
            case R.id.k_groupauthor:
                ((kMusicToDetailsOnClickListener) getActivity()).onKMusicToDetailsClickListener(
                        URLValues.KMUSIC_CH_GROUP_AUTHOR_LIST1, URLValues.KMUSIC_CH_GROUP_AUTHOR_LIST2, "华语组合");
                break;
            case R.id.k_euro_man_author:
                ((kMusicToDetailsOnClickListener) getActivity()).onKMusicToDetailsClickListener(
                        URLValues.KMUSIC_EURO_MAN_AUTHOR_LIST1, URLValues.KMUSIC_EURO_MAN_AUTHOR_LIST2, "欧美男歌手");
                break;
            case R.id.k_euro_women_author:
                ((kMusicToDetailsOnClickListener) getActivity()).onKMusicToDetailsClickListener(
                        URLValues.KMUSIC_EURO_WOMAN_AUTHOR_LIST1, URLValues.KMUSIC_EURO_WOMAN_AUTHOR_LIST2, "欧美女歌手");
                break;
            case R.id.k_euro_group_author:
                ((kMusicToDetailsOnClickListener) getActivity()).onKMusicToDetailsClickListener(
                        URLValues.KMUSIC_EURO_GROUP_AUTHOR_LIST1, URLValues.KMUSIC_EURO_GROUP_AUTHOR_LIST2, "欧美组合");
                break;
            case R.id.k_korea_man_author:
                ((kMusicToDetailsOnClickListener) getActivity()).onKMusicToDetailsClickListener(
                        URLValues.KMUSIC_KR_MAN_AUTHOR_LIST1, URLValues.KMUSIC_KR_MAN_AUTHOR_LIST2, "韩国男歌手");
                break;
            case R.id.k_korea_woman_author:
                ((kMusicToDetailsOnClickListener) getActivity()).onKMusicToDetailsClickListener(
                        URLValues.KMUSIC_KR_WOMAN_AUTHOR_LIST1, URLValues.KMUSIC_KR_WOMAN_AUTHOR_LIST2, "韩国女歌手");
                break;
            case R.id.k_korea_group_author:
                ((kMusicToDetailsOnClickListener) getActivity()).onKMusicToDetailsClickListener(
                        URLValues.KMUSIC_KR_GROUP_AUTHOR_LIST1, URLValues.KMUSIC_KR_GROUP_AUTHOR_LIST2, "韩国组合");
                break;
            case R.id.k_jp_man_author:
                ((kMusicToDetailsOnClickListener) getActivity()).onKMusicToDetailsClickListener(
                        URLValues.KMUSIC_JP_MAN_AUTHOR_LIST1, URLValues.KMUSIC_JP_MAN_AUTHOR_LIST2, "日本男歌手");
                break;
            case R.id.k_jp_woman_author:
                ((kMusicToDetailsOnClickListener) getActivity()).onKMusicToDetailsClickListener(
                        URLValues.KMUSIC_JP_WOMAN_AUTHOR_LIST1, URLValues.KMUSIC_JP_WOMAN_AUTHOR_LIST2, "日本女歌手");
                break;
            case R.id.k_jp_group_author:
                ((kMusicToDetailsOnClickListener) getActivity()).onKMusicToDetailsClickListener(
                        URLValues.KMUSIC_JP_GROUP_AUTHOR_LIST1, URLValues.KMUSIC_JP_GROUP_AUTHOR_LIST2, "日本组合");
                break;
            case R.id.k_other_author:
                ((kMusicToDetailsOnClickListener) getActivity()).onKMusicToDetailsClickListener(
                        URLValues.KMUSIC_OTHER_AUTHOR_LIST1, URLValues.KMUSIC_OTHER_AUTHOR_LIST2, "其他歌手");
                break;
        }
    }

    public interface kMusicToDetailsOnClickListener {
        void onKMusicToDetailsClickListener(String url1, String url2, String authorName);
    }
}
