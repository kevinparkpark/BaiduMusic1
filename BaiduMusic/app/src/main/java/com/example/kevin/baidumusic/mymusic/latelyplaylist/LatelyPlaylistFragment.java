package com.example.kevin.baidumusic.mymusic.latelyplaylist;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.kevin.baidumusic.R;
import com.example.kevin.baidumusic.base.SecBaseFragment;
import com.example.kevin.baidumusic.db.DBSongListCacheBean;
import com.example.kevin.baidumusic.db.DBSongPlayListBean;
import com.example.kevin.baidumusic.db.LiteOrmSington;
import com.example.kevin.baidumusic.eventbean.EventPosition;
import com.example.kevin.baidumusic.util.BroadcastValues;
import com.litesuits.orm.LiteOrm;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by kevin on 16/6/5.
 */
public class LatelyPlaylistFragment extends SecBaseFragment{
    private ListView listView;
    private LatelyPlaylistAdapter adapter;
    private List<DBSongPlayListBean> dbSongPlayListBean;
    private ImageView ivBack;

    @Override
    public int setlayout() {
        return R.layout.fragment_latelyplaylist;
    }

    @Override
    protected void initView(View view) {
        listView= (ListView) view.findViewById(R.id.my_latelyplaylist_listview);
        ivBack= (ImageView) view.findViewById(R.id.iv_my_latelyplaylist_back);
    }

    @Override
    protected void initData() {
        final LiteOrm liteOrm=LiteOrmSington.getInstance().getLiteOrm();
        dbSongPlayListBean=liteOrm.query(DBSongPlayListBean.class);
        adapter=new LatelyPlaylistAdapter(context);
        adapter.setBeen(dbSongPlayListBean);
        listView.setAdapter(adapter);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                liteOrm.deleteAll(DBSongListCacheBean.class);
                for (int i = 0; i < dbSongPlayListBean.size(); i++) {

                liteOrm.save(new DBSongListCacheBean(dbSongPlayListBean.get(i).getTitle(),
                        dbSongPlayListBean.get(i).getAuthor(),dbSongPlayListBean.get(i).getPicUrl(),
                        dbSongPlayListBean.get(i).getPicBigUrl(),dbSongPlayListBean.get(i).getSongId()));
                }

                EventBus.getDefault().post(new EventPosition(position));
                context.sendBroadcast(new Intent(BroadcastValues.PREVIOUS));
            }
        });
    }

}
