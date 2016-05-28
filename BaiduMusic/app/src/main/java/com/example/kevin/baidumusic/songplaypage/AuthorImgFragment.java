package com.example.kevin.baidumusic.songplaypage;

import android.view.View;
import android.widget.ImageView;

import com.example.kevin.baidumusic.R;
import com.example.kevin.baidumusic.base.BaseFragment;
import com.example.kevin.baidumusic.eventbean.EventUpDateSongUI;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by kevin on 16/5/26.
 */
public class AuthorImgFragment extends BaseFragment{
    private ImageView ivAuthorImg;
    @Override
    public int setlayout() {
        return R.layout.fragment_authorimg;
    }

    @Override
    protected void initView(View view) {
    ivAuthorImg= (ImageView) view.findViewById(R.id.iv_songplay_authorimage);
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);

//        Bundle bundle=getArguments();
//        String imageUrl=bundle.getString("imagebigurl");
//        Log.d("AuthorImgFragment","-----"+ imageUrl);
//        if (imageUrl!=null) {
//            Picasso.with(context).load(imageUrl).into(ivAuthorImg);
//        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void serviceToplaySong(EventUpDateSongUI songUI){
        if (songUI.getImageBigUrl()!=null) {
            Picasso.with(context).load(songUI.getImageBigUrl()).into(ivAuthorImg);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
