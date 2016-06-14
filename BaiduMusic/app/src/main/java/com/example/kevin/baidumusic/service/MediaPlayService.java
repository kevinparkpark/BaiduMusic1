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
import com.example.kevin.baidumusic.MainActivity;
import com.example.kevin.baidumusic.R;
import com.example.kevin.baidumusic.db.DBSongListCacheBean;
import com.example.kevin.baidumusic.db.DBSongPlayListBean;
import com.example.kevin.baidumusic.eventbean.EventGenericBean;
import com.example.kevin.baidumusic.eventbean.EventProgressBean;
import com.example.kevin.baidumusic.eventbean.EventPosition;
import com.example.kevin.baidumusic.eventbean.EventSeekToBean;
import com.example.kevin.baidumusic.eventbean.EventServiceToPauseBean;
import com.example.kevin.baidumusic.eventbean.EventServiceToPlayBtnBean;
import com.example.kevin.baidumusic.eventbean.EventSongLastPlayListBean;
import com.example.kevin.baidumusic.eventbean.EventUpDateSongUI;
import com.example.kevin.baidumusic.netutil.HttpDownloader;
import com.example.kevin.baidumusic.netutil.NetListener;
import com.example.kevin.baidumusic.netutil.NetTool;
import com.example.kevin.baidumusic.service.songplay.SongPlayBean;
import com.example.kevin.baidumusic.util.BroadcastValues;
import com.example.kevin.baidumusic.db.LiteOrmSington;
import com.example.kevin.baidumusic.util.LocalMusic;
import com.google.gson.Gson;
import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
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
        playListMode = new ReceivePlayListMode();
        registerReceiver(playListMode, new IntentFilter(BroadcastValues.PLAY_MODE));
        receiveDownload = new ReceiveDownload();
        registerReceiver(receiveDownload, new IntentFilter(BroadcastValues.DOWNLOAD));

        liteOrm = LiteOrmSington.getInstance().getLiteOrm();
        List<DBSongListCacheBean> dbSongListCacheBeen = liteOrm.query(DBSongListCacheBean.class);
        SharedPreferences getsp = getSharedPreferences("songposition", MODE_PRIVATE);
        detailsPosition = getsp.getInt("position", 0);
        if (dbSongListCacheBeen.size() != 0) {
            EventBus.getDefault().post(new EventUpDateSongUI(dbSongListCacheBeen.get(detailsPosition).getTitle(),
                    dbSongListCacheBeen.get(detailsPosition).getAuthor(), dbSongListCacheBeen.get(detailsPosition).getImageUrl(),
                    dbSongListCacheBeen.get(detailsPosition).getImageBigUrl(), dbSongListCacheBeen.get(detailsPosition).getSongId()));
        }
    }

    //接收点击position
    @Subscribe
    public void Detailsposition(EventPosition pos) {
        detailsPosition = pos.getPostion();
    }

    //接收数据
    @Subscribe
    public void DetailsResolution(final List<EventGenericBean> eventGenericBean) {

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
                EventBus.getDefault().post(new EventSongLastPlayListBean(songTitle, songAuthor, songImageUrl, songImageBigUrl));

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < eventGenericBean.size(); i++) {
                            liteOrm.insert(new DBSongListCacheBean(eventGenericBean.get(i).getTitle(), eventGenericBean.get(i).getAuthor(),
                                    eventGenericBean.get(i).getImageUrl(), eventGenericBean.get(i).getImageBigUrl(), eventGenericBean.get(i).getSongId()));
                        }
                    }
                }).start();

                startPlay(songUrl);

                //判断数据库是否存在这条信息,如果存在-替换,不存在插入
                QueryBuilder<DBSongPlayListBean> list = new QueryBuilder<DBSongPlayListBean>
                        (DBSongPlayListBean.class).whereEquals(DBSongPlayListBean.TITLE, songTitle);

                List<DBSongPlayListBean> dbSongPlayListBeen = liteOrm.query(list);
                if (dbSongPlayListBeen.size() == 0) {
                    liteOrm.insert(new DBSongPlayListBean(songPlayBean.getSonginfo().getSong_id(),
                            songAuthor, songTitle, songImageUrl, songImageBigUrl));
                } else {

                    liteOrm.delete(dbSongPlayListBeen);
                    liteOrm.insert(new DBSongPlayListBean(songPlayBean.getSonginfo().getSong_id(),
                            songAuthor, songTitle, songImageUrl, songImageBigUrl));
                }

                SharedPreferences sp = getSharedPreferences("songposition", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("position", detailsPosition);
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
                            EventBus.getDefault().post(new EventUpDateSongUI(songTitle, songAuthor,
                                    songImageUrl, songImageBigUrl, songId));
                            if (mp.isPlaying()) {
                                EventBus.getDefault().post(new EventServiceToPlayBtnBean(true));
                            } else {
                                if (detailsPosition < dbSongListCacheBeen.size()) {
                                    EventBus.getDefault().post(new EventServiceToPauseBean(dbSongListCacheBeen.get(detailsPosition).getTitle(),
                                            dbSongListCacheBeen.get(detailsPosition).getAuthor(), dbSongListCacheBeen.get(detailsPosition).getImageUrl(),
                                            dbSongListCacheBeen.get(detailsPosition).getImageBigUrl()));
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

        SharedPreferences sp = getSharedPreferences("songposition", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("position", detailsPosition);
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
        seeTo();
        showNotification(songImageBigUrl, songTitle, songAuthor, R.mipmap.bt_widget_pause_press);

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
                    QueryBuilder<DBSongPlayListBean> list = new QueryBuilder<DBSongPlayListBean>
                            (DBSongPlayListBean.class).whereEquals(DBSongPlayListBean.TITLE, songTitle);

                    List<DBSongPlayListBean> dbSongPlayListBeen = liteOrm.query(list);
                    if (dbSongPlayListBeen.size() == 0) {
                        liteOrm.insert(new DBSongPlayListBean(songPlayBean.getSonginfo().getSong_id(),
                                songAuthor, songTitle, songImageUrl, songImageBigUrl));
                    } else {

                        liteOrm.delete(dbSongPlayListBeen);
                        liteOrm.insert(new DBSongPlayListBean(songPlayBean.getSonginfo().getSong_id(),
                                songAuthor, songTitle, songImageUrl, songImageBigUrl));
                    }

                    SharedPreferences sp = getSharedPreferences("songposition", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt("position", detailsPosition);
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
                QueryBuilder<DBSongPlayListBean> list = new QueryBuilder<DBSongPlayListBean>
                        (DBSongPlayListBean.class).whereEquals(DBSongPlayListBean.TITLE, songTitle);

                List<DBSongPlayListBean> dbSongPlayListBeen = liteOrm.query(list);
                if (dbSongPlayListBeen.size() == 0) {
                    liteOrm.insert(new DBSongPlayListBean(songPlayBean.getSonginfo().getSong_id(),
                            songAuthor, songTitle, songImageUrl, songImageBigUrl));
                } else {

                    liteOrm.delete(dbSongPlayListBeen);
                    liteOrm.insert(new DBSongPlayListBean(songPlayBean.getSonginfo().getSong_id(),
                            songAuthor, songTitle, songImageUrl, songImageBigUrl));
                }

                SharedPreferences sp = getSharedPreferences("songposition", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("position", detailsPosition);
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
        builder.setSmallIcon(R.mipmap.yuan).setAutoCancel(true);
        remoteViews = new RemoteViews(getPackageName(), R.layout.remote_view);
//        remoteViews.setImageViewResource(R.id.iv_remote_img,R.mipmap.yuan);
        remoteViews.setTextViewText(R.id.tv_remote_title, title);
        remoteViews.setTextViewText(R.id.tv_remote_author, author);
        remoteViews.setImageViewResource(R.id.iv_remote_play, id);
        Intent intent = new Intent(BroadcastValues.NOTI_PLAY);
        Intent pauseIntent = new Intent(BroadcastValues.NEXT);
        Intent destroyIntent = new Intent(BroadcastValues.DESTORY);
        Intent remote2ActIntent = new Intent(this, MainActivity.class);
        PendingIntent remote2ActPendingIntent = PendingIntent.getActivity(this, 0, remote2ActIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent destroyPendingIntent = PendingIntent.getBroadcast(this, 0, destroyIntent, 0);
        PendingIntent pausePendingIntent = PendingIntent.getBroadcast(this, 0, pauseIntent, 0);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        remoteViews.setOnClickPendingIntent(R.id.iv_remote_play, pendingIntent);
        remoteViews.setOnClickPendingIntent(R.id.iv_remote_next, pausePendingIntent);
        remoteViews.setOnClickPendingIntent(R.id.iv_remote_diestroy, destroyPendingIntent);
        remoteViews.setOnClickPendingIntent(R.id.remote_view_linearlayout, remote2ActPendingIntent);
        builder.setContent(remoteViews);
        manager.notify(2016, builder.build());
        Log.d("MediaPlayService", "---------" + imgUrl);
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
                Log.d("ReceivePrevious", "previous" + (detailsPosition));
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
            mode = intent.getIntExtra("mode", 0);
            Log.d("ReceivePlayListMode", "mode:" + mode);
        }
    }

    //接收下载广播
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
                    } else {
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
                    int result = httpDownloader.download(songUrl, "/music/mp3/", songTitle + ".mp3");

                    handler.sendEmptyMessage(0);
                    Log.d("ReceiveDownload", "开始下载");

                    Message msg = new Message();
                    msg.what = 100;
                    msg.obj = result;
                    handler.sendMessage(msg);

                }
            }).start();
            //下载歌词
            new Thread(new Runnable() {
                @Override
                public void run() {
                    HttpDownloader httpDownloader1 = new HttpDownloader();
                    int reuslt1 = httpDownloader1.download(lrc, "/music/lrc/", songTitle + ".lrc");
                }
            }).start();
        }
    }

}
