package com.example.kevin.baidumusic.mymusic.localmusic.musicsonglist;

import android.content.SharedPreferences;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.kevin.baidumusic.R;
import com.example.kevin.baidumusic.base.BaseFragment;
import com.example.kevin.baidumusic.db.DBSongListCacheBean;
import com.example.kevin.baidumusic.db.LiteOrmSington;
import com.example.kevin.baidumusic.eventbean.EventPosition;
import com.example.kevin.baidumusic.util.LocalMusic;
import com.example.kevin.baidumusic.util.MusicUtils;
import com.litesuits.orm.LiteOrm;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 16/5/22.
 */
public class MyLocalMusicSonglistFragment extends BaseFragment{
    private MyLocalMusicSongListAdapter adapter;
    private ListView listView;
    private List<LocalMusic> localMusics;
    @Override
    public int setlayout() {
        return R.layout.fragment_mylocalmusicsong;
    }

    @Override
    protected void initView(View view) {
    listView= (ListView) view.findViewById(R.id.my_localmusic_songlist_listview);
    }

    @Override
    protected void initData() {
        localMusics =new ArrayList<>();
        MusicUtils.scanMusic(context, localMusics);
        adapter=new MyLocalMusicSongListAdapter(context);
        adapter.setMusicList(localMusics);


        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EventBus.getDefault().post(new EventPosition(position));
                EventBus.getDefault().post(localMusics.get(position));
                final LiteOrm liteOrm= LiteOrmSington.getInstance().getLiteOrm();
                liteOrm.deleteAll(DBSongListCacheBean.class);

        SharedPreferences sp = context.getSharedPreferences("songposition", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("position", position);
        editor.commit();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < localMusics.size(); i++) {
                            liteOrm.insert(new DBSongListCacheBean(localMusics.get(i).getTitle(), localMusics.get(i).getArtist(),
                                    null, null, localMusics.get(i).getFileName()));
                        }
                    }
                }).start();

            }
        });
    }
}
