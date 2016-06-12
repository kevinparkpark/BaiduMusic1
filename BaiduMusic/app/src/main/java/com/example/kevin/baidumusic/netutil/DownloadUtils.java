package com.example.kevin.baidumusic.netutil;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.kevin.baidumusic.app.MyApp;

/**
 * Created by kevin on 16/6/12.
 */
public class DownloadUtils {
    private String songUrl, songTitle,lrc;

    public DownloadUtils(String songUrl, String songTitle, String lrc) {
        this.songUrl = songUrl;
        this.songTitle = songTitle;
        this.lrc = lrc;

        //下载歌曲
        new Thread(new Runnable() {
            @Override
            public void run() {

                HttpDownloader httpDownloader = new HttpDownloader();
                int result = httpDownloader.download(DownloadUtils.this.songUrl, "/music/mp3/",
                        DownloadUtils.this.songTitle + ".mp3");

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
                int reuslt1 = httpDownloader1.download(DownloadUtils.this.lrc, "/music/lrc/"
                        , DownloadUtils.this.songTitle + ".lrc");
            }
        }).start();
    }

    final Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            if (msg.what == 100) {
                int result = (int) msg.obj;
                if (result == 0) {
                    Toast.makeText(MyApp.context, "下载完成", Toast.LENGTH_SHORT).show();
                } else if (result == 1) {
                    Toast.makeText(MyApp.context, "重复下载", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MyApp.context, "开始下载", Toast.LENGTH_SHORT).show();

            }
            return false;
        }
    });



}
