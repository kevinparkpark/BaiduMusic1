package com.example.kevin.baidumusic.mymusic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.kevin.baidumusic.base.BaseFragment;
import com.example.kevin.baidumusic.R;
import com.example.kevin.baidumusic.db.DBHeart;
import com.example.kevin.baidumusic.db.DBSongPlayListBean;
import com.example.kevin.baidumusic.db.LiteOrmSington;
import com.example.kevin.baidumusic.util.BroadcastValues;
import com.example.kevin.baidumusic.util.LocalMusic;
import com.example.kevin.baidumusic.util.MusicUtils;
import com.litesuits.orm.LiteOrm;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 16/5/19.
 */
public class MyFragment extends BaseFragment implements View.OnClickListener {
    private RelativeLayout rllMyLocalMusic,rllLatelyPlaylist,rllHeartMore;
    private TextView tvDownloadCount,tvLatelyPlaylistCount,tvHeartCount;
    private List<LocalMusic> musicList;
    private LiteOrm liteOrm;

    @Override
    public int setlayout() {
        return R.layout.fragment_my;
    }

    @Override
    protected void initView(View view) {
        rllMyLocalMusic = (RelativeLayout) view.findViewById(R.id.rl_my_local_music);
        tvDownloadCount= (TextView) view.findViewById(R.id.tv_localmusic_count);
        rllLatelyPlaylist= (RelativeLayout) view.findViewById(R.id.relativelayout_latelyplaylist);
        tvLatelyPlaylistCount= (TextView) view.findViewById(R.id.tv_latelyPlaylist_count);
        tvHeartCount= (TextView) view.findViewById(R.id.tv_my_heart_count);
        rllHeartMore= (RelativeLayout) view.findViewById(R.id.my_heart_more);
    }

    @Override
    protected void initData() {

        rllMyLocalMusic.setOnClickListener(this);
        rllLatelyPlaylist.setOnClickListener(this);
        rllHeartMore.setOnClickListener(this);
        //本地歌曲
        musicList=new ArrayList<>();
        MusicUtils.scanMusic(context,musicList);
        tvDownloadCount.setText(context.getString(R.string.total)+musicList.size()+context.getString(R.string.song));
        //最近播放列表
        liteOrm= LiteOrmSington.getInstance().getLiteOrm();
        List<DBSongPlayListBean> dbSongPlayListBeen=liteOrm.query(DBSongPlayListBean.class);
        tvLatelyPlaylistCount.setText(context.getString(R.string.total)+dbSongPlayListBeen.size()+context.getString(R.string.song));
        //红心列表
        List<DBHeart> dbHearts=liteOrm.query(DBHeart.class);
        tvHeartCount.setText(dbHearts.size()+context.getString(R.string.song));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //本地音乐
            case R.id.rl_my_local_music:
                ( (MyToLocalFragmentOnClick) getActivity()).onMyToLocalFragmentClick();
                break;
            //最近播放
            case R.id.relativelayout_latelyplaylist:
                ((LatelyPlaylistOnClick)getActivity()).onLatelyPlaylistClick();
                break;
            //红心列表
            case R.id.my_heart_more:
                ((HeartSongListOnClick)getActivity()).onHeartSongListClick();
                break;
        }
    }

    public interface MyToLocalFragmentOnClick{
        void onMyToLocalFragmentClick();
    }
    public interface LatelyPlaylistOnClick{
        void onLatelyPlaylistClick();
    }
    public interface HeartSongListOnClick{
        void onHeartSongListClick();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
