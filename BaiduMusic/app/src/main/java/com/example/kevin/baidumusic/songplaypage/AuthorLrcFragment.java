package com.example.kevin.baidumusic.songplaypage;

import android.provider.SyncStateContract;
import android.view.View;

import com.example.kevin.baidumusic.R;
import com.example.kevin.baidumusic.base.BaseFragment;
import com.example.kevin.baidumusic.eventbean.EventProgressBean;
import com.example.kevin.baidumusic.util.LocalMusic;
import com.example.kevin.baidumusic.util.LrcView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

/**
 * Created by kevin on 16/5/26.
 */
public class AuthorLrcFragment extends BaseFragment{
    private LrcView lrcViewSingle;
    private LrcView lrcViewFull;
    @Override
    public int setlayout() {
        return R.layout.fragment_authorlrc;
    }

    @Override
    protected void initView(View view) {
    lrcViewSingle= (LrcView) view.findViewById(R.id.lrcview);
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);

    }

    public void loadLrc(String lrc){
        lrcViewSingle.loadLrc(lrc);
    }
    public void onPublish(int progress){
        if (lrcViewSingle.hasLrc()){
            lrcViewSingle.updateTime(progress);
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void SeeBarControl(EventProgressBean bean){

        loadLrc(bean.getLrc());
        onPublish(bean.getCurrent());
    }

//    public static String getLrcFilePath(LocalMusic music) {
//        String lrcFilePath = "music/" + music.getFileName().replace(".mp3", ".lrc");
//        if (!new File(lrcFilePath).exists()) {
//            lrcFilePath = music.getUri().replace(".mp3", ".lrc");
//        }
//        return lrcFilePath;
//    }
}
