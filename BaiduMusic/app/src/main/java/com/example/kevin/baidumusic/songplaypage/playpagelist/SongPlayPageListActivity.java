package com.example.kevin.baidumusic.songplaypage.playpagelist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kevin.baidumusic.R;
import com.example.kevin.baidumusic.db.DBSongListCacheBean;
import com.example.kevin.baidumusic.db.LiteOrmSington;
import com.example.kevin.baidumusic.eventbean.EventPosition;
import com.example.kevin.baidumusic.totalfragment.popsonglist.PopSonglistAdapter;
import com.example.kevin.baidumusic.util.BroadcastValues;
import com.litesuits.orm.LiteOrm;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by kevin on 16/6/9.
 */
public class SongPlayPageListActivity extends AppCompatActivity implements View.OnClickListener {
    private PopSonglistAdapter adapter;
    private ListView listView;
    private List<DBSongListCacheBean> cacheBeen;
    private final int MODE_RANDOM = 1;//随机播放
    private final int MODE_ONE = 2;//单曲循环
    private final int MODE_LOOP = 0;//列表循环
    private int mode = 0;//播放方式
    private ImageView ivMode;
    private TextView tvClear,tvShutDown;
    private LiteOrm liteOrm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songplaypagelist);
        listView= (ListView) findViewById(R.id.songplaypagelist_listview);
        ivMode= (ImageView) findViewById(R.id.iv_songplaypagelist_playmode);
        tvClear= (TextView) findViewById(R.id.tv_songplaypagelist_clear);
        tvShutDown= (TextView) findViewById(R.id.tv_songplaypagelist_shutdown);

        ivMode.setOnClickListener(this);
        tvClear.setOnClickListener(this);
        tvShutDown.setOnClickListener(this);

        adapter=new PopSonglistAdapter(this);
        liteOrm= LiteOrmSington.getInstance().getLiteOrm();
        cacheBeen=liteOrm.query(DBSongListCacheBean.class);
        adapter.setCacheBeen(cacheBeen);
        listView.setAdapter(adapter);

        //读取播放模式
        SharedPreferences getsp = getSharedPreferences(getString(R.string.mode), MODE_PRIVATE);
        mode = getsp.getInt(getString(R.string.mode), 0);
        ivMode.setImageLevel(mode);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EventBus.getDefault().post(new EventPosition(position));
                sendBroadcast(new Intent(BroadcastValues.NEXT));
                adapter.setClick(position);
            }
        });
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
                SharedPreferences sp = getSharedPreferences(getString(R.string.mode), MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt(getString(R.string.mode), mode);
                editor.commit();
                break;
            case R.id.tv_songplaypagelist_clear:
                liteOrm.deleteAll(DBSongListCacheBean.class);
                cacheBeen=liteOrm.query(DBSongListCacheBean.class);
                adapter.setCacheBeen(cacheBeen);
                break;
            case R.id.tv_songplaypagelist_shutdown:
                finish();
                break;
        }
    }

    private void swithPlayMode(int mode) {
        switch (mode) {
            case MODE_LOOP:
                Toast.makeText(this, R.string.loop_play, Toast.LENGTH_SHORT).show();
                break;
            case MODE_RANDOM:
                Toast.makeText(this, R.string.random_play, Toast.LENGTH_SHORT).show();
                break;
            case MODE_ONE:
                Toast.makeText(this, R.string.one_play, Toast.LENGTH_SHORT).show();
                break;
        }
        Intent intent=new Intent(BroadcastValues.PLAY_MODE);
        intent.putExtra(getString(R.string.mode),mode);
        sendBroadcast(intent);
        ivMode.setImageLevel(mode);
    }
}
