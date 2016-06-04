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
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.kevin.baidumusic.R;
import com.example.kevin.baidumusic.db.DBSongListCacheBean;
import com.example.kevin.baidumusic.db.DBSongPlayListBean;
import com.example.kevin.baidumusic.eventbean.EventProgressBean;
import com.example.kevin.baidumusic.eventbean.EventRankDetailsPositionBen;
import com.example.kevin.baidumusic.eventbean.EventSeekToBean;
import com.example.kevin.baidumusic.eventbean.EventServiceToPauseBean;
import com.example.kevin.baidumusic.eventbean.EventServiceToPlayBtnBean;
import com.example.kevin.baidumusic.eventbean.EventSongLastPlayListBean;
import com.example.kevin.baidumusic.eventbean.EventUpDateSongUI;
import com.example.kevin.baidumusic.kmusic.authordetails.songlist.AuthorDetailsSonglistBean;
import com.example.kevin.baidumusic.musiclibrary.radio.radioplay.songplaylist.RadioPlayListBean;
import com.example.kevin.baidumusic.musiclibrary.rank.RankDetailsBean;
import com.example.kevin.baidumusic.musiclibrary.rank.songplay.SongPlayBean;
import com.example.kevin.baidumusic.musiclibrary.songmenu.songmenudetails.SongMenuDetailsBean;
import com.example.kevin.baidumusic.netutil.HttpDownloader;
import com.example.kevin.baidumusic.netutil.NetListener;
import com.example.kevin.baidumusic.netutil.NetTool;
import com.example.kevin.baidumusic.netutil.URLValues;
import com.example.kevin.baidumusic.search.SearchBean;
import com.example.kevin.baidumusic.util.BroadcastValues;
import com.example.kevin.baidumusic.db.LiteOrmSington;
import com.example.kevin.baidumusic.util.LocalMusic;
import com.google.gson.Gson;
import com.litesuits.orm.LiteOrm;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    private ReceivePlayListMode playListMode;
    private ReceiveDownload receiveDownload;
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
    private List<AuthorDetailsSonglistBean.SonglistBean> songlistBeanList;
    private SongPlayBean songPlayBean;
    private boolean flag = false;
    private RemoteViews remoteViews;
    private Notification.Builder builder;
    private String searchSongId;
    private NotificationManager manager;
    private String lrc;
    private String author, songTitle, songImageUrl, songImageBigUrl;
    private final int MODE_RANDOM = 1;//随机播放
    private final int MODE_ONE = 2;//单曲循环
    private final int MODE_LOOP = 0;//列表循环
    private int mode;//播放方式

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);

        mp = new MediaPlayer();
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                next(detailsSongPosition);
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
        playListMode = new ReceivePlayListMode();
        registerReceiver(playListMode, new IntentFilter(BroadcastValues.PLAY_MODE));
        receiveDownload = new ReceiveDownload();
        registerReceiver(receiveDownload, new IntentFilter(BroadcastValues.DOWNLOAD));

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

    //接收点击position
    @Subscribe
    public void Detailsposition(EventRankDetailsPositionBen pos) {
        detailsSongPosition = pos.getPostion();
    }

    @Subscribe
    public void getRadioPlayList(final RadioPlayListBean radioPlayListBean) {
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
                lrc = songPlayBean.getSonginfo().getLrclink();
                author = songPlayBean.getSonginfo().getAuthor();
                songTitle = songPlayBean.getSonginfo().getTitle();
                songImageUrl = songPlayBean.getSonginfo().getPic_small();
                songImageBigUrl = songPlayBean.getSonginfo().getPic_premium();
                EventBus.getDefault().post(new EventSongLastPlayListBean(songTitle, author, songImageUrl, songImageBigUrl));

                Log.d("MediaPlayService", "service" + songUrl);

//                detailsSongUrl = new ArrayList<>();
                final List<RadioPlayListBean.ResultBean.SonglistBean> playListBean = radioPlayListBean.getResult().getSonglist();
//                for (int i = 0; i < playListBean.size(); i++) {
//                    detailsSongUrl.add(playListBean.get(i).getSong_id());
//                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < playListBean.size(); i++) {
                            liteOrm.insert(new DBSongListCacheBean(playListBean.get(i).getTitle(), playListBean.get(i).getAuthor(),
                                    playListBean.get(i).getPic_small(), playListBean.get(i).getPic_big(), playListBean.get(i).getSong_id()));
                        }
                    }
                }).start();

                startPlay(songUrl);

                SharedPreferences sp = getSharedPreferences("songposition", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("position", detailsSongPosition);
                editor.commit();
            }

            @Override
            public void onFailed(VolleyError error) {

            }
            //传入position并接收SongId
        }, radioPlayListBean.getResult().getSonglist().get(detailsSongPosition).getSong_id());
    }

    //歌手详情
    @Subscribe
    public void getAuthorDetailsSonglist(final AuthorDetailsSonglistBean authorDetailsSonglistBean) {
        String ID = authorDetailsSonglistBean.getSonglist().get(detailsSongPosition).getSong_id();

        NetTool netTool = new NetTool();
        netTool.getUrlId(new NetListener() {
            @Override
            public void onSuccessed(String result) {

                Gson gson = new Gson();
                result = result.replace("(", "");
                result = result.replace(")", "");
                result = result.replace(";", "");
                songPlayBean = new SongPlayBean();
                songPlayBean = gson.fromJson(result, SongPlayBean.class);
                songUrl = songPlayBean.getBitrate().getFile_link();
                lrc = songPlayBean.getSonginfo().getLrclink();
                author = songPlayBean.getSonginfo().getAuthor();
                songTitle = songPlayBean.getSonginfo().getTitle();
                songImageUrl = songPlayBean.getSonginfo().getPic_small();
                songImageBigUrl = songPlayBean.getSonginfo().getPic_premium();
                EventBus.getDefault().post(new EventSongLastPlayListBean(songTitle, author, songImageUrl, songImageBigUrl));

                Log.d("MediaPlayService", "service" + songUrl);

                detailsSongUrl = new ArrayList<>();
                songlistBeanList = authorDetailsSonglistBean.getSonglist();
                for (int i = 0; i < songlistBeanList.size(); i++) {
                    detailsSongUrl.add(songlistBeanList.get(i).getSong_id());
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < songlistBeanList.size(); i++) {
                            liteOrm.insert(new DBSongListCacheBean(songlistBeanList.get(i).getTitle(), songlistBeanList.get(i).getAuthor(),
                                    songlistBeanList.get(i).getPic_small(), songlistBeanList.get(i).getPic_big(), songlistBeanList.get(i).getSong_id()));
                        }
                    }
                }).start();

                startPlay(songUrl);

                if (songUrl != null) {
                    liteOrm.save(new DBSongPlayListBean(songUrl, author, songTitle, songImageUrl, songImageBigUrl));
                    List<DBSongListCacheBean> song = liteOrm.query(DBSongListCacheBean.class);
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
        }, URLValues.LE_RANKDETAILS_SONGURL1, ID, URLValues.LE_RANKDETAILS_SONGURL2);
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
                lrc = songPlayBean.getSonginfo().getLrclink();
                author = songPlayBean.getSonginfo().getAuthor();
                songTitle = songPlayBean.getSonginfo().getTitle();
                songImageUrl = songPlayBean.getSonginfo().getPic_small();
                songImageBigUrl = songPlayBean.getSonginfo().getPic_premium();

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
                lrc = songPlayBean.getSonginfo().getLrclink();
                author = songPlayBean.getSonginfo().getAuthor();
                songTitle = songPlayBean.getSonginfo().getTitle();
                songImageUrl = songPlayBean.getSonginfo().getPic_small();
                songImageBigUrl = songPlayBean.getSonginfo().getPic_premium();
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
                lrc = songPlayBean.getSonginfo().getLrclink();
                author = songPlayBean.getSonginfo().getAuthor();
                songTitle = songPlayBean.getSonginfo().getTitle();
                songImageUrl = songPlayBean.getSonginfo().getPic_small();
                songImageBigUrl = songPlayBean.getSonginfo().getPic_premium();
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
                            EventBus.getDefault().post(new EventProgressBean(current, maxCurrent, lrc));
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

    //接收本地音乐数据
    @Subscribe
    public void getLocalMusicSonglist(final ArrayList<LocalMusic> localMusic){

        startPlay(localMusic.get(detailsSongPosition));

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < localMusic.size(); i++) {
                    liteOrm.insert(new DBSongListCacheBean(localMusic.get(i).getTitle(), localMusic.get(i).getArtist(),
                            "", "", localMusic.get(i).getFileName()));
                }
            }
        }).start();

    }
    //播放本地音乐
    public void startPlay(LocalMusic localMusic){
        mp.reset();
        try {
            mp.setDataSource(localMusic.getUri());
            mp.prepare();
            mp.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        maxCurrent=mp.getDuration();
//        seeTo();
//        showNotification(songPlayBean.getSonginfo().getPic_big(), songPlayBean.getSonginfo().getTitle()
//                , songPlayBean.getSonginfo().getAuthor(), R.mipmap.bt_widget_pause_press);

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
        Log.d("MediaPlayService", "position:" + position);
        List<DBSongListCacheBean> been = liteOrm.query(DBSongListCacheBean.class);
        //播放模式
        switch (mode) {
            case MODE_RANDOM:
                Random random = new Random(System.currentTimeMillis());
                detailsSongPosition = random.nextInt(been.size());
                break;
            case MODE_ONE:

                break;
            case MODE_LOOP:
                if (detailsSongPosition == been.size() - 1) {
                    detailsSongPosition = 0;
                } else {
                    detailsSongPosition++;
                }
                break;
        }
        Log.d("MediaPlayService", "detailsSongPosition:" + detailsSongPosition);
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
                lrc = songPlayBean.getSonginfo().getLrclink();
                author = songPlayBean.getSonginfo().getAuthor();
                songTitle = songPlayBean.getSonginfo().getTitle();
                songImageUrl = songPlayBean.getSonginfo().getPic_small();
                songImageBigUrl = songPlayBean.getSonginfo().getPic_premium();

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
        unregisterReceiver(playListMode);
        unregisterReceiver(receiveDownload);
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
        if (imgUrl != null) {
            Picasso.with(this).load(imgUrl).into(remoteViews,
                    R.id.iv_remote_img, 2016, builder.build());
        }
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
            next(detailsSongPosition);
        }
    }

    //上一首
    class ReceivePrevious extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (detailsSongPosition > 0) {
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

    class ReceivePlayListMode extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            mode = intent.getIntExtra("mode", 0);
            Log.d("ReceivePlayListMode", "mode:" + mode);
        }
    }


    class ReceiveDownload extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            final Handler handler = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {

                    if (msg.what == 100) {
                        int result = (int) msg.obj;
                        if (result == 0) {
                            Toast.makeText(MediaPlayService.this, "下载完成", Toast.LENGTH_SHORT).show();
                        } else if (result == 1) {
                            Toast.makeText(MediaPlayService.this, "重复下载", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(MediaPlayService.this, "开始下载", Toast.LENGTH_SHORT).show();

                    }
                    return false;
                }
            });

            //下载歌曲
            new Thread(new Runnable() {
                @Override
                public void run() {

                    HttpDownloader httpDownloader = new HttpDownloader();
                    int result = httpDownloader.download(songUrl, "music/mp3/", songTitle + ".mp3");

                    handler.sendEmptyMessage(0);
                    Log.d("ReceiveDownload", "开始下载");

                    Message msg = new Message();
                    msg.what = 100;
                    msg.obj = result;
                    handler.sendMessage(msg);
//                    if (result == 0) {
//                        Log.d("ReceiveDownload", "下载完成");
//                    } else if (result == 1) {
//                    }

                }
            }).start();
            //下载歌词
            new Thread(new Runnable() {
                @Override
                public void run() {
                    HttpDownloader httpDownloader1 = new HttpDownloader();
                    int reuslt1 = httpDownloader1.download(lrc, "music/lrc/", songTitle + ".lrc");
                }
            }).start();
        }
    }

}
