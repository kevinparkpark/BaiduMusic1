package com.example.kevin.baidumusic.songlist;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.kevin.baidumusic.R;
import com.example.kevin.baidumusic.base.BaseFragment;
import com.example.kevin.baidumusic.db.DBSongListCacheBean;
import com.example.kevin.baidumusic.db.LiteOrmSington;
import com.example.kevin.baidumusic.eventbean.EventRankDetailsPositionBen;
import com.example.kevin.baidumusic.util.BroadcastValues;
import com.litesuits.orm.LiteOrm;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by kevin on 16/5/30.
 */
public class SongListCacheFragment extends BaseFragment{
    private List<DBSongListCacheBean>cacheBeen;
    private SongListCacheAdapter adapter;
    private ListView listView;
    @Override
    public int setlayout() {
        return R.layout.fragment_songlistcache;
    }

    @Override
    protected void initView(View view) {
        listView= (ListView) view.findViewById(R.id.songlistcache_listview);
    }

    @Override
    protected void initData() {
        LiteOrm liteOrm= LiteOrmSington.getInstance().getLiteOrm();
        cacheBeen=liteOrm.query(DBSongListCacheBean.class);
        adapter=new SongListCacheAdapter(context);
        adapter.setCacheBeen(cacheBeen);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                context.sendBroadcast(new Intent(BroadcastValues.NEXT));
                EventBus.getDefault().post(new EventRankDetailsPositionBen(position-1));
            }
        });
    }
}