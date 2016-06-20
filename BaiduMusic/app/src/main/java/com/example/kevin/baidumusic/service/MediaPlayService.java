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
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.kevin.baidumusic.MainActivity;
import com.example.kevin.baidumusic.R;
import com.example.kevin.baidumusic.db.DBSongListCacheBean;
import com.example.kevin.baidumusic.db.DBSongPlayListBean;
import com.example.kevin.baidumusic.db.DBUtilsHelper;
import com.example.kevin.baidumusic.eventbean.EventGenericBean;
import com.example.kevin.baidumusic.eventbean.EventProgressBean;
import com.example.kevin.baidumusic.eventbean.EventPosition;
import com.example.kevin.baidumusic.eventbean.EventSeekToBean;
import com.example.kevin.baidumusic.eventbean.EventServiceToPauseBean;
import com.example.kevin.baidumusic.eventbean.EventServiceToPlayBtnBean;
import com.example.kevin.baidumusic.eventbean.EventSongLastPlayListBean;
import com.example.kevin.baidumusic.eventbean.EventUpDateSongUI;
import com.example.kevin.baidumusic.netutil.DownloadUtils;
import com.example.kevin.baidumusic.netutil.GsonRequestGet;
import com.example.kevin.baidumusic.netutil.NetListener;
import com.example.kevin.baidumusic.netutil.NetTool;
import com.example.kevin.baidumusic.service.songplay.SongPlayBean;
import com.example.kevin.baidumusic.util.BroadcastValues;
import com.example.kevin.baidumusic.util.LocalMusic;
import com.google.gson.Gson;
import com.litesuits.orm.LiteOrm;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Random;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.TransformerException;

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
    private int detailsPosition;
    private int current;
    private int maxCurrent;
    private LiteOrm liteOrm;
    private List<DBSongPlayListBean> songUrlBeen;
    private List<DBSongListCacheBean> dbSongListCacheBeen;
    private EventServiceToPauseBean eventServiceToPlayBean;
    private SongPlayBean songPlayBean;
    private boolean flag = false;
    private RemoteViews remoteViews;
    private Notification.Builder builder;
    private NotificationManager manager;
    private String lrc;
    private String songAuthor, songTitle, songImageUrl, songImageBigUrl, songId;
    private final int MODE_RANDOM = 1;//随机播放
    private final int MODE_ONE = 2;//单曲循环
    private final int MODE_LOOP = 0;//列表循环
    private int mode = 0;//播放方式
    private DBUtilsHelper dbUtilsHelper=new DBUtilsHelper();

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        mp = new MediaPlayer();
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                next(detailsPosition);
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
        //播放模式
        playListMode = new ReceivePlayListMode();
        registerReceiver(playListMode, new IntentFilter(BroadcastValues.PLAY_MODE));
        //下载
        receiveDownload = new ReceiveDownload();
        registerReceiver(receiveDownload, new IntentFilter(BroadcastValues.DOWNLOAD));
        //先获取cache的歌单

        List<DBSongListCacheBean> dbSongListCacheBeen=dbUtilsHelper.queryAll(DBSongListCacheBean.class);

//        List<DBSongListCacheBean> dbSongListCacheBeen = liteOrm.query(DBSongListCacheBean.class);
        SharedPreferences getsp = getSharedPreferences(getString(R.string.songposition), MODE_PRIVATE);
        detailsPosition = getsp.getInt(getString(R.string.position), 0);
        if (dbSongListCacheBeen.size() >= detailsPosition) {
            EventBus.getDefault().post(new EventUpDateSongUI(dbSongListCacheBeen.get(detailsPosition).getTitle(),
                    dbSongListCacheBeen.get(detailsPosition).getAuthor(), dbSongListCacheBeen.get(detailsPosition).getImageUrl(),
                    dbSongListCacheBeen.get(detailsPosition).getImageBigUrl(), dbSongListCacheBeen.get(detailsPosition).getSongId()));
        }
    }

    //接收点击position
    @Subscribe
    public void detailsPosition(EventPosition pos) {
        detailsPosition = pos.getPostion();
    }

    //接收数据
    @Subscribe
    public void detailsResolution(final List<EventGenericBean> eventGenericBean) {

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
                songAuthor = songPlayBean.getSonginfo().getAuthor();
                songTitle = songPlayBean.getSonginfo().getTitle();
                songImageUrl = songPlayBean.getSonginfo().getPic_small();
                songImageBigUrl = songPlayBean.getSonginfo().getPic_premium();
                songId = songPlayBean.getSonginfo().getSong_id();
                EventBus.getDefault().post(new EventSongLastPlayListBean(songTitle,
                        songAuthor, songImageUrl, songImageBigUrl));

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < eventGenericBean.size(); i++) {
                            DBSongListCacheBean dbSongListCacheBean=new DBSongListCacheBean(eventGenericBean.get(i).getTitle()
                                    , eventGenericBean.get(i).getAuthor(), eventGenericBean.get(i).getImageUrl()
                                    , eventGenericBean.get(i).getImageBigUrl(), eventGenericBean.get(i).getSongId());
                            dbUtilsHelper.insertDB(dbSongListCacheBean);
                        }
                    }
                }).start();

                startPlay(songUrl);

                //判断数据库是否存在这条信息,如果存在-替换,不存在插入
                DBUtilsHelper dbUtilsHelper = new DBUtilsHelper();
                dbUtilsHelper.showQueryBuilder(songPlayBean);

                SharedPreferences sp = getSharedPreferences(getString(R.string.songposition), MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt(getString(R.string.position), detailsPosition);
                editor.commit();
            }

            @Override
            public void onFailed(VolleyError error) {

            }
            //传入position并接收SongId
        }, eventGenericBean.get(detailsPosition).getSongId());
    }

    //接收seekbar传过来的数据 刷新seekbar
    @Subscribe
    public void getSeekTo(EventSeekToBean seekToBean) {

        if (mp != null) {
            mp.seekTo(seekToBean.getCurrent());
        }
    }

    //实时发送seekto数据
    public void seeTo(final String title, final String author, final String imageUrl, final String imageBigUrl) {
        maxCurrent = mp.getDuration();
        final List<DBSongListCacheBean> dbSongListCacheBeen=dbUtilsHelper.queryAll(DBSongListCacheBean.class);
        new Thread(new Runnable() {
            @Override
            public void run() {

                while (mp != null && current < maxCurrent) {
                    try {
                        Thread.sleep(1000);
                        if (mp != null) {
                            current = mp.getCurrentPosition();
                            EventBus.getDefault().post(new EventProgressBean(current, maxCurrent, lrc));
                            EventBus.getDefault().post(new EventUpDateSongUI(songTitle, songAuthor,
                                    songImageUrl, songImageBigUrl, songId));
                            if (mp.isPlaying()) {
                                EventBus.getDefault().post(new EventServiceToPlayBtnBean(true));
                            } else {
                                if (detailsPosition < dbSongListCacheBeen.size()) {
                                    EventBus.getDefault().post(new EventServiceToPauseBean(title,author,
                                            imageUrl,imageBigUrl));
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
    public void getLocalMusicSonglist(LocalMusic localMusic) {

        startPlay(localMusic);

        SharedPreferences sp = getSharedPreferences(getString(R.string.songposition), MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(getString(R.string.position), detailsPosition);
        editor.commit();
    }

    //播放本地音乐
    public void startPlay(LocalMusic localMusic) {
        mp.reset();
        try {
            mp.setDataSource(localMusic.getUri());
            mp.prepare();
            mp.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        songUrl = localMusic.getFileName();
        lrc = localMusic.getTitle();
        songAuthor = localMusic.getArtist();
        songTitle = localMusic.getTitle();
        songImageUrl = null;
        songImageBigUrl = null;

        maxCurrent = mp.getDuration();
        seeTo(songTitle,songAuthor,songImageUrl,songImageBigUrl);
        showNotification(songImageBigUrl, songTitle, songAuthor, R.mipmap.bt_widget_pause_press);

    }

    //开始播放
    public void startPlay(String url) {

        mp.reset();
        try {
            mp.setDataSource(this, Uri.parse(url));
            mp.prepare();
            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        maxCurrent = mp.getDuration();
        seeTo(songTitle,songAuthor,songImageUrl,songImageBigUrl);
        showNotification(songImageBigUrl, songTitle, songAuthor, R.mipmap.bt_widget_pause_press);
    }

    //播放音乐的时候发送eventbus到 播放页面
    public void play() {
        if (!flag) {
            previous(detailsPosition);
        } else {
            mp.start();
            showNotification(songImageBigUrl, songTitle, songAuthor, R.mipmap.bt_widget_pause_press);
        }
    }

    //暂停
    public void pause() {
        if (mp != null && mp.isPlaying()) {
            mp.pause();
            EventBus.getDefault().post(new EventServiceToPlayBtnBean(true));
            showNotification(songImageBigUrl, songTitle, songAuthor, R.mipmap.bt_widget_play_press);
            flag = true;
        }
    }

    //下一首
    public void next(int position) {
        final List<DBSongListCacheBean> been = liteOrm.query(DBSongListCacheBean.class);
        //播放模式
        switch (mode) {
            case MODE_RANDOM:
                Random random = new Random(System.currentTimeMillis());
                detailsPosition = random.nextInt(been.size());
                break;
            case MODE_ONE:

                break;
            case MODE_LOOP:
                if (detailsPosition == been.size() - 1) {
                    detailsPosition = 0;
                } else {
                    detailsPosition++;
                }
                break;
        }
        if (been.size() > detailsPosition) {
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
                    songAuthor = songPlayBean.getSonginfo().getAuthor();
                    songTitle = songPlayBean.getSonginfo().getTitle();
                    songImageUrl = songPlayBean.getSonginfo().getPic_small();
                    songImageBigUrl = songPlayBean.getSonginfo().getPic_premium();

                    startPlay(songUrl);

                    //判断数据库是否存在这条信息,如果存在-替换,不存在插入
                    dbUtilsHelper.showQueryBuilder(songPlayBean);

                    SharedPreferences sp = getSharedPreferences(getString(R.string.songposition), MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt(getString(R.string.position), detailsPosition);
                    editor.commit();
                }

                @Override
                public void onFailed(VolleyError error) {

                }
            }, been.get(detailsPosition).getSongId());
        }
    }

    //上一曲
    public void previous(int position) {
        final List<DBSongListCacheBean> been = liteOrm.query(DBSongListCacheBean.class);

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
                songAuthor = songPlayBean.getSonginfo().getAuthor();
                songTitle = songPlayBean.getSonginfo().getTitle();
                songImageUrl = songPlayBean.getSonginfo().getPic_small();
                songImageBigUrl = songPlayBean.getSonginfo().getPic_premium();

                startPlay(songUrl);

                //判断数据库是否存在这条信息,如果存在-替换,不存在插入
                dbUtilsHelper.showQueryBuilder(songPlayBean);

                SharedPreferences sp = getSharedPreferences(getString(R.string.songposition), MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt(getString(R.string.position), detailsPosition);
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
        EventBus.getDefault().unregister(this);
        //服务停止时释放mediaplayer
        mp.stop();
        mp.release();
        unregisterReceiver(pauseMode);
        unregisterReceiver(playMode);
        unregisterReceiver(nextMode);
        unregisterReceiver(previousMode);
        unregisterReceiver(destroyMode);
        unregisterReceiver(notiPlay);
        unregisterReceiver(playListMode);
        unregisterReceiver(receiveDownload);
        super.onDestroy();
    }

    //notification
    public void showNotification(String imgUrl, String title, String author, int id) {
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        builder = new Notification.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_aboutus_logo).setAutoCancel(true);
        remoteViews = new RemoteViews(getPackageName(), R.layout.remote_view);
//        remoteViews.setImageViewResource(R.id.iv_remote_img,R.mipmap.yuan);
        remoteViews.setTextViewText(R.id.tv_remote_title, title);
        remoteViews.setTextViewText(R.id.tv_remote_author, author);
        remoteViews.setImageViewResource(R.id.iv_remote_play, id);
        Intent intent = new Intent(BroadcastValues.NOTI_PLAY);
        Intent pauseIntent = new Intent(BroadcastValues.NEXT);
        Intent destroyIntent = new Intent(BroadcastValues.DESTORY);
        Intent remote2ActIntent = new Intent(this, MainActivity.class);
        PendingIntent remote2ActPendingIntent = PendingIntent.getActivity(this, 0, remote2ActIntent
                , PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent destroyPendingIntent = PendingIntent.getBroadcast(this, 0, destroyIntent, 0);
        PendingIntent pausePendingIntent = PendingIntent.getBroadcast(this, 0, pauseIntent, 0);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        remoteViews.setOnClickPendingIntent(R.id.iv_remote_play, pendingIntent);
        remoteViews.setOnClickPendingIntent(R.id.iv_remote_next, pausePendingIntent);
        remoteViews.setOnClickPendingIntent(R.id.iv_remote_diestroy, destroyPendingIntent);
        remoteViews.setOnClickPendingIntent(R.id.remote_view_linearlayout, remote2ActPendingIntent);
        builder.setContent(remoteViews);
        manager.notify(2016, builder.build());
        if (imgUrl != null && imgUrl.length() > 0) {
            Picasso.with(this).load(imgUrl).into(remoteViews,
                    R.id.iv_remote_img, 2016, builder.build());
        } else {
            remoteViews.setImageViewResource(R.id.iv_remote_img, R.mipmap.yuan);
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
            next(detailsPosition);
        }
    }

    //上一首
    class ReceivePrevious extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (detailsPosition > 0) {
                previous(--detailsPosition);
            }
        }
    }

    //退出程序
    class ReceiveDestroy extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //退出系统及notification
            if (manager != null) {
                manager.cancel(2016);
            }
            System.exit(0);
        }
    }

    //notification播放按键监听
    class ReceiveNotiPlay extends BroadcastReceiver {
        boolean isPlay = true;

        @Override
        public void onReceive(Context context, Intent intent) {
            if (isPlay = !isPlay) {
                play();
            } else {
                pause();
            }
        }
    }

    //接收播放模式广播
    class ReceivePlayListMode extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            mode = intent.getIntExtra(getString(R.string.mode), 0);
        }
    }

    //接收下载广播
    class ReceiveDownload extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            DownloadUtils downloadUtils = new DownloadUtils(songUrl, songTitle, lrc);

        }
    }

}
