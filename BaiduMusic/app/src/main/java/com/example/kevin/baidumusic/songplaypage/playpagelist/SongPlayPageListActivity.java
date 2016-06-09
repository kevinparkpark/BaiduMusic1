package com.example.kevin.baidumusic.songplaypage.playpagelist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kevin.baidumusic.R;
import com.example.kevin.baidumusic.db.DBSongListCacheBean;
import com.example.kevin.baidumusic.db.LiteOrmSington;
import com.example.kevin.baidumusic.songlist.SongListCacheAdapter;
import com.example.kevin.baidumusic.util.BroadcastValues;
import com.litesuits.orm.LiteOrm;

import java.util.List;

/**
 * Created by kevin on 16/6/9.
 */
public class SongPlayPageListActivity extends AppCompatActivity implements View.OnClickListener {
    private SongListCacheAdapter adapter;
    private ListView listView;
    private List<DBSongListCacheBean> cacheBeen;
    private final int MODE_RANDOM = 1;//随机播放
    private final int MODE_ONE = 2;//单曲循环
    private final int MODE_LOOP = 0;//列表循环
    private int mode = 0;//播放方式
    private ImageView ivMode;
    private TextView tvClear;
    private LiteOrm liteOrm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songplaypagelist);
        listView= (ListView) findViewById(R.id.songplaypagelist_listview);
        ivMode= (ImageView) findViewById(R.id.iv_songplaypagelist_playmode);
        tvClear= (TextView) findViewById(R.id.tv_songplaypagelist_clear);

        ivMode.setOnClickListener(this);
        tvClear.setOnClickListener(this);

        adapter=new SongListCacheAdapter(this);
        liteOrm= LiteOrmSington.getInstance().getLiteOrm();
        cacheBeen=liteOrm.query(DBSongListCacheBean.class);
        adapter.setCacheBeen(cacheBeen);
        listView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_songplaypagelist_playmode:
                if (mode < 2) {
                    mode++;
                } else {
                    mode = 0;
                }
                swithPlayMode(mode);
                break;
            case R.id.tv_songplaypagelist_clear:
                liteOrm.deleteAll(DBSongListCacheBean.class);
                cacheBeen=liteOrm.query(DBSongListCacheBean.class);
                adapter.setCacheBeen(cacheBeen);
                break;
        }
    }

    private void swithPlayMode(int mode) {
        switch (mode) {
            case MODE_LOOP:
                Toast.makeText(this, "循环播放", Toast.LENGTH_SHORT).show();
                break;
            case MODE_RANDOM:
                Toast.makeText(this, "随机播放", Toast.LENGTH_SHORT).show();
                break;
            case MODE_ONE:
                Toast.makeText(this, "单曲循环", Toast.LENGTH_SHORT).show();
                break;
        }
        Intent intent=new Intent(BroadcastValues.PLAY_MODE);
        intent.putExtra("mode",mode);
        sendBroadcast(intent);
        ivMode.setImageLevel(mode);
    }
}
