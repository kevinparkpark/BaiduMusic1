package com.example.kevin.baidumusic.musiclibrary.radio.radioplay.songplaylist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.example.kevin.baidumusic.R;
import com.example.kevin.baidumusic.db.DBSongListCacheBean;
import com.example.kevin.baidumusic.db.LiteOrmSington;
import com.example.kevin.baidumusic.eventbean.EventGenericBean;
import com.example.kevin.baidumusic.eventbean.EventPosition;
import com.example.kevin.baidumusic.musiclibrary.rank.RankDetailsBean;
import com.example.kevin.baidumusic.netutil.NetListener;
import com.example.kevin.baidumusic.netutil.NetTool;
import com.example.kevin.baidumusic.netutil.URLValues;
import com.example.kevin.baidumusic.util.BroadcastValues;
import com.google.gson.Gson;
import com.litesuits.orm.LiteOrm;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 16/6/3.
 */
public class RadioPlayListActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private List<RadioPlayListBean.ResultBean.SonglistBean> songlistBeanList;
    private RadioPlayListPagerAdapter adapter;
    private TextView tvScene;
    private ImageView ivBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radioplaylist);
        viewPager = (ViewPager) findViewById(R.id.radioplaylist_viewpager);
        tvScene = (TextView) findViewById(R.id.tv_radioplaylist_scene);
        ivBack = (ImageView) findViewById(R.id.iv_radioplaylist_back_to_activity);

        initData();

        //返回上一层
        tvScene.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //返回主页面
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendBroadcast(new Intent(BroadcastValues.FINISH_RADIOPLAY));
            }
        });
    }

    public void initData() {
        String sceneId = getIntent().getStringExtra("sceneid");
        Log.d("RadioPlayListActivity", "----------" + sceneId);
        String sceneName = getIntent().getStringExtra("scenename");

        adapter = new RadioPlayListPagerAdapter(this);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setPageMargin(30);

        NetTool netTool = new NetTool();
        netTool.getUrl(new NetListener() {
            @Override
            public void onSuccessed(String result) {
                Gson gson = new Gson();
                final RadioPlayListBean bean = gson.fromJson(result, RadioPlayListBean.class);
                adapter.setSonglistBeanList(bean.getResult().getSonglist());
                viewPager.setAdapter(adapter);

                //播放第一首歌

                List<EventGenericBean> eventGenericBeen = new ArrayList<EventGenericBean>();

                final LiteOrm liteOrm = LiteOrmSington.getInstance().getLiteOrm();
                liteOrm.deleteAll(DBSongListCacheBean.class);

                for (RadioPlayListBean.ResultBean.SonglistBean songlistBean : bean.getResult().getSonglist()) {
                    EventGenericBean bean1 = new EventGenericBean(songlistBean.getTitle(), songlistBean.getAuthor(),
                            songlistBean.getPic_small(), songlistBean.getPic_big(), songlistBean.getSong_id());
                    eventGenericBeen.add(bean1);
                }
                EventBus.getDefault().post(new EventPosition(0));
                EventBus.getDefault().post(eventGenericBeen);


//                for (RankDetailsBean.SongListBean songListBean : rankDetailsBean.getSong_list()) {
//                    EventGenericBean bean=new EventGenericBean(songListBean.getTitle(),songListBean.getAuthor(),
//                            songListBean.getPic_small(),songListBean.getPic_big(),songListBean.getSong_id());
//                    eventGenericBeen.add(bean);
//                }
//                EventBus.getDefault().post(new EventPosition(position-2));
//                EventBus.getDefault().post(eventGenericBeen);

                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        adapter.setPos(position);
//                        EventBus.getDefault().post(new EventPosition(position));
//                        EventBus.getDefault().post(bean);
//                        LiteOrm liteOrm = LiteOrmSington.getInstance().getLiteOrm();
//                        liteOrm.deleteAll(DBSongListCacheBean.class);

                        List<EventGenericBean> eventGenericBeen = new ArrayList<EventGenericBean>();

                        final LiteOrm liteOrm = LiteOrmSington.getInstance().getLiteOrm();
                        liteOrm.deleteAll(DBSongListCacheBean.class);

                        for (RadioPlayListBean.ResultBean.SonglistBean songlistBean : bean.getResult().getSonglist()) {
                            EventGenericBean bean1 = new EventGenericBean(songlistBean.getTitle(), songlistBean.getAuthor(),
                                    songlistBean.getPic_small(), songlistBean.getPic_big(), songlistBean.getSong_id());
                            eventGenericBeen.add(bean1);
                        }
                        EventBus.getDefault().post(new EventPosition(position));
                        EventBus.getDefault().post(eventGenericBeen);

                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
            }

            @Override
            public void onFailed(VolleyError error) {

            }
        }, URLValues.RADIOPLAYLIST_SCENE1 + sceneId + URLValues.RADIOPLAYLIST_SCENE2);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
