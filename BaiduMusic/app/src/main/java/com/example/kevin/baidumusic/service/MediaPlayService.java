package com.example.kevin.baidumusic.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RemoteViews;

import com.android.volley.VolleyError;
import com.example.kevin.baidumusic.R;
import com.example.kevin.baidumusic.db.DBSongListCacheBean;
import com.example.kevin.baidumusic.db.DBSongPlayListBean;
import com.example.kevin.baidumusic.eventbean.EventProgressBean;
import com.example.kevin.baidumusic.eventbean.EventRankDetailsPositionBen;
import com.example.kevin.baidumusic.eventbean.EventRankDetailsSongIdBean;
import com.example.kevin.baidumusic.eventbean.EventSeekToBean;
import com.example.kevin.baidumusic.eventbean.EventServiceToPauseBean;
import com.example.kevin.baidumusic.eventbean.EventServiceToPlayBtnBean;
import com.example.kevin.baidumusic.eventbean.EventSongLastPlayListBean;
import com.example.kevin.baidumusic.eventbean.EventUpDateSongUI;
import com.example.kevin.baidumusic.musiclibrary.rank.RankDetailsBean;
import com.example.kevin.baidumusic.musiclibrary.rank.songplay.SongPlayBean;
import com.example.kevin.baidumusic.musiclibrary.songmenu.songmenudetails.SongMenuDetailsBean;
import com.example.kevin.baidumusic.netutil.GsonUtil;
import com.example.kevin.baidumusic.netutil.NetListener;
import com.example.kevin.baidumusic.netutil.NetTool;
import com.example.kevin.baidumusic.search.SearchBean;
import com.example.kevin.baidumusic.util.BroadcastValues;
import com.example.kevin.baidumusic.db.LiteOrmSington;
import com.google.gson.Gson;
import com.litesuits.orm.LiteOrm;
import com.squareup.picasso.Picasso;

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
    private ReceivePrevious previousMode;
    private ReceiveDestroy destroyMode;
    private ReceiveNotiPlay notiPlay;
    private int detailsSongPosition;
    private ArrayList<String> detailsSongUrl;
    private int current;
    private int maxCurrent;
    private LiteOrm liteOrm;
    private List<DBSongPlayListBean> songUrlBeen;
    private List<DBSongListCacheBean> dbSongListCacheBeen;
    private EventServiceToPauseBean eventServiceToPlayBean;
    private List<RankDetailsBean.SongListBean> data;
    private List<SongMenuDetailsBean.ContentBean> contentBeanList;
    private SongPlayBean songPlayBean;
    private boolean flag = false;
    private RemoteViews remoteViews;
    private Notification.Builder builder;
    private String searchSongId;
    private NotificationManager manager;
    private String lrc;

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
        //notificationplay
        notiPlay = new ReceiveNotiPlay();
        registerReceiver(notiPlay, new IntentFilter(BroadcastValues.NOTI_PLAY));
        //previous注册
        previousMode = new ReceivePrevious();
        registerReceiver(previousMode, new IntentFilter(BroadcastValues.PREVIOUS));
        //destroy注册
        destroyMode = new ReceiveDestroy();
        registerReceiver(destroyMode, new IntentFilter(BroadcastValues.DESTORY));

        liteOrm = LiteOrmSington.getInstance().getLiteOrm();
        List<DBSongListCacheBean> dbSongListCacheBeen = liteOrm.query(DBSongListCacheBean.class);
        SharedPreferences getsp = getSharedPreferences("songposition", MODE_PRIVATE);
        detailsSongPosition = getsp.getInt("position", 0);
        if (dbSongListCacheBeen.size() != 0) {
            EventBus.getDefault().post(new EventUpDateSongUI(dbSongListCacheBeen.get(detailsSongPosition).getTitle(),
                    dbSongListCacheBeen.get(detailsSongPosition).getAuthor(), dbSongListCacheBeen.get(detailsSongPosition).getImageUrl(),
                    dbSongListCacheBeen.get(detailsSongPosition).getImageBigUrl()));
        }
    }

    //接收排行点击position
    @Subscribe
    public void Detailsposition(EventRankDetailsPositionBen pos) {
        detailsSongPosition = pos.getPostion();
    }

    //搜索页面传值
    @Subscribe
    public void getSearchSongId(final SearchBean searchBean) {
        searchSongId = searchBean.getResult().getSong_info().getSong_list().get(detailsSongPosition).getSong_id();
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
                lrc=songPlayBean.getSonginfo().getLrclink();
                String author = songPlayBean.getSonginfo().getAuthor();
                String songTitle = songPlayBean.getSonginfo().getTitle();
                String songImageUrl = songPlayBean.getSonginfo().getPic_small();
                String songImageBigUrl = songPlayBean.getSonginfo().getPic_premium();

                detailsSongUrl = new ArrayList<>();
                final List<SearchBean.ResultBean.SongInfoBean.SongListBean> songInfoBeen = searchBean.
                        getResult().getSong_info().getSong_list();
                for (int i = 0; i < songInfoBeen.size(); i++) {
                    detailsSongUrl.add(songInfoBeen.get(i).getSong_id());
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < songInfoBeen.size(); i++) {
                            liteOrm.insert(new DBSongListCacheBean(songInfoBeen.get(i).getTitle(), songInfoBeen.get(i).getAuthor(),
                                    null, null, songInfoBeen.get(i).getSong_id()));
                        }
                    }
                }).start();

                startPlay(songUrl);
                liteOrm.insert(new DBSongPlayListBean(songUrl, author, songTitle, songImageUrl, songImageBigUrl));

            }

            @Override
            public void onFailed(VolleyError error) {

            }
        }, searchSongId);
    }

    //接收排行详情数据
    @Subscribe
    public void getEventDetails(final RankDetailsBean rankDetailsBean) {
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
                lrc=songPlayBean.getSonginfo().getLrclink();
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
//                    for (DBSongListCacheBean songUrlBean : song) {
//                        Log.d("MediaPlayService", "songUrlBean:" + songUrlBean.getTitle());
//                    }
                }
                SharedPreferences sp = getSharedPreferences("songposition", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("position", detailsSongPosition);
                editor.commit();
            }

            @Override
            public void onFailed(VolleyError error) {

            }
            //传入position并接收SongId
        }, rankDetailsBean.getSong_list().get(detailsSongPosition).getSong_id());

    }

    //接收歌单数据
    @Subscribe
    public void getEventSongMenuDetails(final SongMenuDetailsBean songMenuDetailsBean) {
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
                lrc=songPlayBean.getSonginfo().getLrclink();
                String author = songPlayBean.getSonginfo().getAuthor();
                String songTitle = songPlayBean.getSonginfo().getTitle();
                String songImageUrl = songPlayBean.getSonginfo().getPic_small();
                final String songImageBigUrl = songPlayBean.getSonginfo().getPic_premium();
                EventBus.getDefault().post(new EventSongLastPlayListBean(songTitle, author, songImageUrl, songImageBigUrl));

                Log.d("MediaPlayService", "service" + songUrl);

                detailsSongUrl = new ArrayList<>();
                contentBeanList = songMenuDetailsBean.getContent();
                for (int i = 0; i < contentBeanList.size(); i++) {
                    detailsSongUrl.add(contentBeanList.get(i).getSong_id());
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < contentBeanList.size(); i++) {
                            liteOrm.insert(new DBSongListCacheBean(songMenuDetailsBean.getTitle(), contentBeanList.get(i).getAuthor(),
                                    null, null, contentBeanList.get(i).getSong_id()));
                        }
                    }
                }).start();

                startPlay(songUrl);

                if (songUrl != null) {
                    liteOrm.save(new DBSongPlayListBean(songUrl, author, songTitle, songImageUrl, songImageBigUrl));
                    List<DBSongListCacheBean> song = liteOrm.query(DBSongListCacheBean.class);
//                    for (DBSongListCacheBean songUrlBean : song) {
//                        Log.d("MediaPlayService", "songUrlBean:" + songUrlBean.getTitle());
//                    }
                }
                SharedPreferences sp = getSharedPreferences("songposition", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("position", detailsSongPosition);
                editor.commit();
            }

            @Override
            public void onFailed(VolleyError error) {

            }
            //传入position并接收SongId
        }, songMenuDetailsBean.getContent().get(detailsSongPosition).getSong_id());

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
        dbSongListCacheBeen = liteOrm.query(DBSongListCacheBean.class);
        new Thread(new Runnable() {
            @Override
            public void run() {

                while (mp != null && current < maxCurrent) {
                    try {
                        Thread.sleep(1000);
                        if (mp != null) {
                            current = mp.getCurrentPosition();
                            EventBus.getDefault().post(new EventProgressBean(current, maxCurrent,lrc));
                            EventBus.getDefault().post(new EventUpDateSongUI(songPlayBean.getSonginfo().getTitle(), songPlayBean.getSonginfo().getAuthor(),
                                    songPlayBean.getSonginfo().getPic_small(), songPlayBean.getSonginfo().getPic_big()));
                            if (mp.isPlaying()) {
                                EventBus.getDefault().post(new EventServiceToPlayBtnBean(true));
                            } else {
                                if (detailsSongPosition < dbSongListCacheBeen.size()) {
                                    EventBus.getDefault().post(new EventServiceToPauseBean(dbSongListCacheBeen.get(detailsSongPosition + 1).getTitle(),
                                            dbSongListCacheBeen.get(detailsSongPosition + 1).getAuthor(), dbSongListCacheBeen.get(detailsSongPosition + 1).getImageUrl(),
                                            dbSongListCacheBeen.get(detailsSongPosition + 1).getImageBigUrl()));
                                }
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
        showNotification(songPlayBean.getSonginfo().getPic_big(), songPlayBean.getSonginfo().getTitle()
                , songPlayBean.getSonginfo().getAuthor(), R.mipmap.bt_widget_pause_press);
        Log.d("MediaPlayService", "-----" + songPlayBean.getSonginfo().getPic_small());
    }

    //播放音乐的时候发送eventbus到 播放页面
    public void play() {

        if (flag) {
            mp.start();
            showNotification(songPlayBean.getSonginfo().getPic_big(), songPlayBean.getSonginfo().getTitle()
                    , songPlayBean.getSonginfo().getAuthor(), R.mipmap.bt_widget_pause_press);
        } else {

            next(detailsSongPosition);
            flag = true;
        }
    }

    //暂停
    public void pause() {

        if (mp != null && mp.isPlaying()) {
            mp.pause();
            EventBus.getDefault().post(new EventServiceToPlayBtnBean(true));
            showNotification(songPlayBean.getSonginfo().getPic_big(), songPlayBean.getSonginfo().getTitle()
                    , songPlayBean.getSonginfo().getAuthor(), R.mipmap.bt_widget_play_press);
        }
    }

    //下一首
    public void next(int position) {

        List<DBSongListCacheBean> been = liteOrm.query(DBSongListCacheBean.class);

//        GsonUtil gsonUtil=new GsonUtil(been.get(position).getSongId());
//        songUrl = gsonUtil.getSongUrl();
//        lrc=gsonUtil.getLrc();
//        String author = gsonUtil.getAuthor();
//        String songTitle = gsonUtil.getSongTitle();
//        String songImageUrl = gsonUtil.getSongImageUrl();
//        String songImageBigUrl = gsonUtil.getSongImageBigUrl();
//
//        startPlay(songUrl);
//        liteOrm.insert(new DBSongPlayListBean(songUrl, author, songTitle, songImageUrl, songImageBigUrl));
//        SharedPreferences sp = getSharedPreferences("songposition", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sp.edit();
//        editor.putInt("position", detailsSongPosition);
//        editor.commit();

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
                lrc=songPlayBean.getSonginfo().getLrclink();
                String author = songPlayBean.getSonginfo().getAuthor();
                String songTitle = songPlayBean.getSonginfo().getTitle();
                String songImageUrl = songPlayBean.getSonginfo().getPic_small();
                String songImageBigUrl = songPlayBean.getSonginfo().getPic_premium();

                startPlay(songUrl);
                liteOrm.insert(new DBSongPlayListBean(songUrl, author, songTitle, songImageUrl, songImageBigUrl));
                SharedPreferences sp = getSharedPreferences("songposition", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("position", detailsSongPosition);
                editor.commit();
            }

            @Override
            public void onFailed(VolleyError error) {

            }
        }, been.get(position).getSongId());
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
        unregisterReceiver(nextMode);
        unregisterReceiver(previousMode);
        unregisterReceiver(destroyMode);
        unregisterReceiver(notiPlay);
    }

    //notification
    public void showNotification(String imgUrl, String title, String author, int id) {
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        builder = new Notification.Builder(this);
        builder.setSmallIcon(R.mipmap.yuan).setAutoCancel(true);
        remoteViews = new RemoteViews(getPackageName(), R.layout.remote_view);
//        remoteViews.setImageViewResource(R.id.iv_remote_img,R.mipmap.yuan);
        remoteViews.setTextViewText(R.id.tv_remote_title, title);
        remoteViews.setTextViewText(R.id.tv_remote_author, author);
        remoteViews.setImageViewResource(R.id.iv_remote_play, id);
        Intent intent = new Intent(BroadcastValues.NOTI_PLAY);
        Intent pauseIntent = new Intent(BroadcastValues.NEXT);
        Intent destroyIntent = new Intent(BroadcastValues.DESTORY);
        PendingIntent destroyPendingIntent = PendingIntent.getBroadcast(this, 0, destroyIntent, 0);
        PendingIntent pausePendingIntent = PendingIntent.getBroadcast(this, 0, pauseIntent, 0);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        remoteViews.setOnClickPendingIntent(R.id.iv_remote_play, pendingIntent);
        remoteViews.setOnClickPendingIntent(R.id.iv_remote_next, pausePendingIntent);
        remoteViews.setOnClickPendingIntent(R.id.iv_remote_diestroy, destroyPendingIntent);
        builder.setContent(remoteViews);
        manager.notify(2016, builder.build());
        Picasso.with(this).load(imgUrl).into(remoteViews,
                R.id.iv_remote_img, 2016, builder.build());
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

    //上一首
    class ReceivePrevious extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (detailsSongPosition>0){
            next(--detailsSongPosition);
            }
        }
    }

    //退出程序
    class ReceiveDestroy extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //退出系统及notification
            manager.cancel(2016);
            System.exit(0);
        }
    }
    //notification播放按键监听
    class ReceiveNotiPlay extends BroadcastReceiver {
        boolean play = true;

        @Override
        public void onReceive(Context context, Intent intent) {
            if (play = !play) {
                play();
            } else {
                pause();
            }
        }
    }

}
