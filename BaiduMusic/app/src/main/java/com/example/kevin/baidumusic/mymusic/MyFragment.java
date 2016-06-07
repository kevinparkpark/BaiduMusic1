package com.example.kevin.baidumusic.mymusic;

import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.kevin.baidumusic.base.BaseFragment;
import com.example.kevin.baidumusic.R;
import com.example.kevin.baidumusic.db.DBSongPlayListBean;
import com.example.kevin.baidumusic.db.LiteOrmSington;
import com.example.kevin.baidumusic.util.LocalMusic;
import com.example.kevin.baidumusic.util.MusicUtils;
import com.litesuits.orm.LiteOrm;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 16/5/19.
 */
public class MyFragment extends BaseFragment implements View.OnClickListener {
    private RelativeLayout rllMyLocalMusic,rllLatelyPlaylist;
    private TextView tvDownloadCount,tvLatelyPlaylistCount;
    private List<LocalMusic> musicList;

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
    }

    @Override
    protected void initData() {
        rllMyLocalMusic.setOnClickListener(this);
        rllLatelyPlaylist.setOnClickListener(this);

        musicList=new ArrayList<>();
        MusicUtils.scanMusic(context,musicList);
        tvDownloadCount.setText("共"+musicList.size()+"首");

        LiteOrm liteOrm= LiteOrmSington.getInstance().getLiteOrm();
        List<DBSongPlayListBean> dbSongPlayListBeen=liteOrm.query(DBSongPlayListBean.class);
        tvLatelyPlaylistCount.setText("共"+dbSongPlayListBeen.size()+"首");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_my_local_music:
                ( (MyToLocalFragmentOnClick) getActivity()).onMyToLocalFragmentClick();
                break;
            case R.id.relativelayout_latelyplaylist:
                Log.d("MyFragment", "最近播放");
                ((LatelyPlaylistOnClick)getActivity()).onLatelyPlaylistClick();
                break;
        }
    }

    public interface MyToLocalFragmentOnClick{
        void onMyToLocalFragmentClick();
    }
    public interface LatelyPlaylistOnClick{
        void onLatelyPlaylistClick();
    }
}
