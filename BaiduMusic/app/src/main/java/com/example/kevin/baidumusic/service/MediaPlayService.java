package com.example.kevin.baidumusic.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.VolleyError;
import com.example.kevin.baidumusic.db.DBSongListCacheBean;
import com.example.kevin.baidumusic.db.DBSongPlayListBean;
import com.example.kevin.baidumusic.eventbean.EventProgressBean;
import com.example.kevin.baidumusic.eventbean.EventRankDetailsPositionBen;
import com.example.kevin.baidumusic.eventbean.EventSeekToBean;
import com.example.kevin.baidumusic.eventbean.EventServiceToPauseBean;
import com.example.kevin.baidumusic.eventbean.EventServiceToPlayBtnBean;
import com.example.kevin.baidumusic.eventbean.EventSongLastPlayListBean;
import com.example.kevin.baidumusic.eventbean.EventUpDateSongUI;
import com.example.kevin.baidumusic.musiclibrary.rank.RankDetailsBean;
import com.example.kevin.baidumusic.musiclibrary.rank.songplay.SongPlayBean;
import com.example.kevin.baidumusic.netutil.NetListener;
import com.example.kevin.baidumusic.netutil.NetTool;
import com.example.kevin.baidumusic.util.BroadcastValues;
import com.example.kevin.baidumusic.db.LiteOrmSington;
import com.google.gson.Gson;
import com.litesuits.orm.LiteOrm;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 16/5/21.
 * 音乐播放service
 */
public class MediaPlayService extends Service {
    private MediaPlayer mp;
    private String songUrl;
    private ReceivePauseMode pauseMode;
    private ReceivePlayMode playMode;
    private ReceiveNextMode nextMode;
    private int detailsSongPosition = 0;
    private ArrayList<String> detailsSongUrl;
    private int current;
    private int maxCurrent;
    private LiteOrm liteOrm;
    private List<DBSongPlayListBean> songUrlBeen;
    private List<DBSongListCacheBean> dbSongListBeen;
    private EventServiceToPauseBean eventServiceToPlayBean;
    private List<RankDetailsBean.SongListBean> data;
    private SongPlayBean songPlayBean;

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);

        mp = new MediaPlayer();
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

            }
        });

        pauseMode = new ReceivePauseMode();
        IntentFilter pauseFilter = new IntentFilter();
        pauseFilter.addAction(BroadcastValues.PAUSE);
        registerReceiver(pauseMode, pauseFilter);

        playMode = new ReceivePlayMode();
        IntentFilter playFilter = new IntentFilter();
        playFilter.addAction(BroadcastValues.PLAY);
        registerReceiver(playMode, playFilter);

        nextMode = new ReceiveNextMode();
        IntentFilter nextFilter = new IntentFilter();
        nextFilter.addAction(BroadcastValues.NEXT);
        registerReceiver(nextMode, nextFilter);

        liteOrm = LiteOrmSington.getInstance().getLiteOrm();
    }

    //接收排行点击position
    @Subscribe
    public void Detailsposition(EventRankDetailsPositionBen pos) {

        detailsSongPosition = pos.getPostion();
        Log.d("MediaPlayService", "detailsSongPosition:" + detailsSongPosition);
    }

    //接收排行详情数据
    @Subscribe
    public void getEventDetails(final RankDetailsBean rankDetailsBean) {
        Log.d("MediaPlayService", "service-eventdetails");
        NetTool netTool = new NetTool();
        netTool.getDetailsSongUrl(new NetListener() {
            @Override
            public void onSuccessed(String result) {

                Gson gson = new Gson();
                result = result.replace("(", "");
                result = result.replace(")", "");
                result = result.replace(";", "");
                songPlayBean = new SongPlayBean();
                songPlayBean = gson.fromJson(result, SongPlayBean.class);
                songUrl = songPlayBean.getBitrate().getFile_link();
                String author = songPlayBean.getSonginfo().getAuthor();
                String songTitle = songPlayBean.getSonginfo().getTitle();
                String songImageUrl = songPlayBean.getSonginfo().getPic_small();
                String songImageBigUrl = songPlayBean.getSonginfo().getPic_premium();
                EventBus.getDefault().post(new EventSongLastPlayListBean(songTitle, author, songImageUrl, songImageBigUrl));

                Log.d("MediaPlayService", "service" + songUrl);

                detailsSongUrl = new ArrayList<>();
                data = rankDetailsBean.getSong_list();
                for (int i = 0; i < data.size(); i++) {
                    detailsSongUrl.add(rankDetailsBean.getSong_list().get(i).getSong_id());
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        for (int i = 0; i < data.size(); i++) {
                            liteOrm.insert(new DBSongListCacheBean(data.get(i).getTitle(), data.get(i).getAuthor(),
                                    data.get(i).getPic_small(), data.get(i).getPic_big(), data.get(i).getSong_id()));
                        }
                    }
                }).start();

                startPlay(songUrl);

                if (songUrl != null) {
                    liteOrm.save(new DBSongPlayListBean(songUrl, author, songTitle, songImageUrl, songImageBigUrl));
                    List<DBSongListCacheBean> song = liteOrm.query(DBSongListCacheBean.class);
                    for (DBSongListCacheBean songUrlBean : song) {
                        Log.d("MediaPlayService", "songUrlBean:" + songUrlBean.getTitle());
                    }
                }
            }

            @Override
            public void onFailed(VolleyError error) {

            }
            //传入position并接收SongId
        }, rankDetailsBean.getSong_list().get(detailsSongPosition).getSong_id());

    }

    //接收seekbar传过来的数据 刷新seekbar
    @Subscribe
    public void getSeekTo(EventSeekToBean seekToBean) {

        if (mp != null) {
            mp.seekTo(seekToBean.getCurrent());
        }
    }

    //实时发送seekto数据
    public void seeTo() {
        maxCurrent = mp.getDuration();
        dbSongListBeen = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {

                while (mp != null && current < maxCurrent) {
                    try {
                        Thread.sleep(1000);
                        if (mp != null) {
                            current = mp.getCurrentPosition();
                            EventBus.getDefault().post(new EventProgressBean(current, maxCurrent));
                            EventBus.getDefault().post(new EventUpDateSongUI(songPlayBean.getSonginfo().getTitle(), songPlayBean.getSonginfo().getAuthor(),
                                    songPlayBean.getSonginfo().getPic_small(), songPlayBean.getSonginfo().getPic_premium()));
                            if (mp.isPlaying()) {
                                EventBus.getDefault().post(new EventServiceToPlayBtnBean(true));
                            } else {
                                EventBus.getDefault().post(new EventServiceToPauseBean(songPlayBean.getSonginfo().getTitle(), songPlayBean.getSonginfo().getAuthor(),
                                        songPlayBean.getSonginfo().getPic_small(), songPlayBean.getSonginfo().getPic_premium()));
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    //开始播放
    public void startPlay(String url) {

        mp.reset();
        try {
            mp.setDataSource(this, Uri.parse(url));
            mp.prepare();
            mp.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        maxCurrent = mp.getDuration();
        seeTo();
    }

    //播放音乐的时候发送eventbus到 播放页面
    public void play() {


//        if (!mp.isPlaying()) {
//            songUrlBeen = new ArrayList<>();
//            songUrlBeen = liteOrm.query(DBSongPlayListBean.class);
//            for (DBSongPlayListBean bean : songUrlBeen) {
//                Log.d("MediaPlayService", "------" + bean.getSongUrl());
//            }
//            if (songUrlBeen.size() > 0) {
//                startPlay(songUrlBeen.get(songUrlBeen.size() - 1).getSongUrl());
//                EventBus.getDefault().post(new EventServiceToPauseBean(songUrlBeen.get(songUrlBeen.size() - 1).getTitle(),
//                        songUrlBeen.get(songUrlBeen.size() - 1).getAuthor(), songUrlBeen.get(songUrlBeen.size() - 1).getPicUrl(),
//                        songUrlBeen.get(songUrlBeen.size() - 1).getPicBigUrl()));
//                Toast.makeText(this, "fasong", Toast.LENGTH_SHORT).show();
//            } else {
//                startPlay(songUrlBeen.get(0).getSongUrl());
//                EventBus.getDefault().post(new EventServiceToPauseBean(songUrlBeen.get(0).getTitle(),
//                        songUrlBeen.get(0).getAuthor(), songUrlBeen.get(0).getPicUrl(),
//                        songUrlBeen.get(0).getPicBigUrl()));
//            }
//        } else {
        mp.start();
//        }
    }

    //暂停
    public void pause() {

        if (mp != null && mp.isPlaying()) {
            mp.pause();
            EventBus.getDefault().post(new EventServiceToPlayBtnBean(true));
        }
    }

    //下一首
    public void next(int position) {

        NetTool netTool = new NetTool();
        netTool.getDetailsSongUrl(new NetListener() {
            @Override
            public void onSuccessed(String result) {
                Gson gson = new Gson();
                result = result.replace("(", "");
                result = result.replace(")", "");
                result = result.replace(";", "");
                songPlayBean = gson.fromJson(result, SongPlayBean.class);
                songUrl = songPlayBean.getBitrate().getFile_link();
                String author = songPlayBean.getSonginfo().getAuthor();
                String songTitle = songPlayBean.getSonginfo().getTitle();
                String songImageUrl = songPlayBean.getSonginfo().getPic_small();
                String songImageBigUrl = songPlayBean.getSonginfo().getPic_radio();

                startPlay(songUrl);

                dbSongListBeen = new ArrayList<>();
                dbSongListBeen = liteOrm.query(DBSongListCacheBean.class);
                for (DBSongListCacheBean bean : dbSongListBeen) {
                    Log.d("MediaPlayService", "------" + bean.getTitle());
                }


                if (songUrl != null) {
                    liteOrm.insert(new DBSongPlayListBean(songUrl, author, songTitle, songImageUrl, songImageBigUrl));
                }
            }

            @Override
            public void onFailed(VolleyError error) {

            }
        }, detailsSongUrl.get(position));

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        unregisterReceiver(pauseMode);
        unregisterReceiver(playMode);
    }

    //接收音乐播放按键广播
    class ReceivePlayMode extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            play();
        }
    }

    //接收音乐暂停广播
    class ReceivePauseMode extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            pause();
        }
    }

    //接收音乐下一首广播
    class ReceiveNextMode extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            next(++detailsSongPosition);
        }
    }

}
