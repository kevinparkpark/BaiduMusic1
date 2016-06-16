package com.example.kevin.baidumusic.totalfragment.popsonglist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kevin.baidumusic.MainActivity;
import com.example.kevin.baidumusic.R;
import com.example.kevin.baidumusic.base.BaseFragment;
import com.example.kevin.baidumusic.db.DBSongListCacheBean;
import com.example.kevin.baidumusic.db.LiteOrmSington;
import com.example.kevin.baidumusic.eventbean.EventPosition;
import com.example.kevin.baidumusic.util.BroadcastValues;
import com.litesuits.orm.LiteOrm;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by kevin on 16/6/15.
 */
public class PopSongListFragment extends BaseFragment {

    private List<DBSongListCacheBean> cacheBeen;
    private PopSonglistAdapter adapter;
    private ListView listView;
    private TextView tvList, tvTop,tvClear;
    private ImageView ivMode;
    private LiteOrm liteOrm;
    private final int MODE_RANDOM = 1;//随机播放
    private final int MODE_ONE = 2;//单曲循环
    private final int MODE_LOOP = 0;//列表循环
    private int mode = 0;//播放方式

    @Override
    public int setlayout() {
        return R.layout.fragment_popsonglist;
    }

    @Override
    protected void initView(View view) {
        listView = (ListView) view.findViewById(R.id.popupwindow_main_listview);
        tvList = (TextView) view.findViewById(R.id.tv_main_popupwindow_text);
        tvTop = (TextView) view.findViewById(R.id.tv_popsonglist_top);
        tvClear= (TextView) view.findViewById(R.id.tv_main_popupwindow_clear);
        ivMode= (ImageView) view.findViewById(R.id.iv_main_popupwindow_playorder);
    }


    @Override
    protected void initData() {

        //读取播放模式
        SharedPreferences getsp = context.getSharedPreferences(getString(R.string.mode), context.MODE_PRIVATE);
        mode = getsp.getInt(getString(R.string.mode), 0);
        ivMode.setImageLevel(mode);

        liteOrm = LiteOrmSington.getInstance().getLiteOrm();
        cacheBeen = liteOrm.query(DBSongListCacheBean.class);
        adapter = new PopSonglistAdapter(context);
        adapter.setCacheBeen(cacheBeen);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EventBus.getDefault().post(new EventPosition(position+1));
                context.sendBroadcast(new Intent(BroadcastValues.PREVIOUS));
                adapter.setClick(position);
            }
        });
        tvList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).removePopup();
            }
        });
        tvTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).removePopup();
            }
        });
        tvClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                liteOrm.deleteAll(DBSongListCacheBean.class);
                cacheBeen = liteOrm.query(DBSongListCacheBean.class);
                adapter.setCacheBeen(cacheBeen);
            }
        });
        ivMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode < 2) {
                    mode++;
                } else {
                    mode = 0;
                }
                swithPlayMode(mode);
                SharedPreferences sp = context.getSharedPreferences(getString(R.string.mode), context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt(getString(R.string.mode), mode);
                editor.commit();
                ivMode.setImageLevel(mode);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    //playmode
    private void swithPlayMode(int mode) {
        switch (mode) {
            case MODE_LOOP:
                Toast.makeText(context, getString(R.string.loop_play), Toast.LENGTH_SHORT).show();
                break;
            case MODE_RANDOM:
                Toast.makeText(context, getString(R.string.random_play), Toast.LENGTH_SHORT).show();
                break;
            case MODE_ONE:
                Toast.makeText(context, getString(R.string.one_play), Toast.LENGTH_SHORT).show();
                break;
        }
        Intent intent = new Intent(BroadcastValues.PLAY_MODE);
        intent.putExtra(getString(R.string.mode), mode);
        context.sendBroadcast(intent);
    }
}
