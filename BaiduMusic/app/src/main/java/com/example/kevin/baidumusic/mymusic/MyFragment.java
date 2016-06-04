package com.example.kevin.baidumusic.mymusic;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.kevin.baidumusic.base.BaseFragment;
import com.example.kevin.baidumusic.R;
import com.example.kevin.baidumusic.util.LocalMusic;
import com.example.kevin.baidumusic.util.MusicUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 16/5/19.
 */
public class MyFragment extends BaseFragment {
    private RelativeLayout relativeLayout;
    private TextView tvDownloadCount;
    private List<LocalMusic> musicList;

    @Override
    public int setlayout() {
        return R.layout.fragment_my;
    }

    @Override
    protected void initView(View view) {
        relativeLayout= (RelativeLayout) view.findViewById(R.id.rl_my_local_music);
        tvDownloadCount= (TextView) view.findViewById(R.id.tv_localmusic_count);
    }

    @Override
    protected void initData() {
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               ( (MyToLocalFragmentOnClick) getActivity()).onMyToLocalFragmentClick();
            }
        });

        musicList=new ArrayList<>();
        MusicUtils.scanMusic(context,musicList);
        tvDownloadCount.setText("共"+musicList.size()+"首");
    }
    public interface MyToLocalFragmentOnClick{
        void onMyToLocalFragmentClick();
    }
}
