package com.example.kevin.baidumusic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kevin.baidumusic.eventbean.EventServiceToPauseBean;
import com.example.kevin.baidumusic.eventbean.EventServiceToPlayBtnBean;
import com.example.kevin.baidumusic.eventbean.EventUpDateSongUI;
import com.example.kevin.baidumusic.musiclibrary.rank.RankDetailsFragment;
import com.example.kevin.baidumusic.musiclibrary.rank.RankFragment;
import com.example.kevin.baidumusic.mymusic.MyFragment;
import com.example.kevin.baidumusic.mymusic.localmusic.MyLocalMusicFragment;
import com.example.kevin.baidumusic.search.SearchFragment;
import com.example.kevin.baidumusic.service.MediaPlayService;
import com.example.kevin.baidumusic.songplaypage.SongPlayPageActivity;
import com.example.kevin.baidumusic.totalfragment.TotalFragment;
import com.example.kevin.baidumusic.util.BroadcastValues;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity implements MyFragment.MyToLocalFragmentOnClick, RankFragment.RankToOnItemListener {

    private TotalFragment totalFragment;
    private MyFragment myFragment;
    private ImageView ivPlay,ivSongImage,ivMainNext;
    private TextView tvSongTitle,tvSongAuthor;
    private boolean flag = false;
    private LinearLayout linearLayoutMainPlaylist;
    private EventUpDateSongUI eventUpDateSongUI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        startService(new Intent(this, MediaPlayService.class));

        ivPlay = (ImageView) findViewById(R.id.iv_main_play);
        ivSongImage= (ImageView) findViewById(R.id.iv_main_song_image);
        ivMainNext= (ImageView) findViewById(R.id.iv_main_next);
        tvSongTitle= (TextView) findViewById(R.id.tv_main_song_title);
        tvSongAuthor= (TextView) findViewById(R.id.tv_main_song_author);
        linearLayoutMainPlaylist= (LinearLayout) findViewById(R.id.linearLayout_main_playlist);

        linearLayoutMainPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(new Intent(MainActivity.this, SongPlayPageActivity.class));
//                if (eventUpDateSongUI !=null) {
//                    intent.putExtra("title", eventUpDateSongUI.getTitle());
//                    intent.putExtra("imageurl", eventUpDateSongUI.getImageBigUrl());
//                    intent.putExtra("author", eventUpDateSongUI.getAuthor());
//                    Log.d("MainActivity", "getimageurl------" + eventUpDateSongUI.getImageBigUrl());
//                }
                startActivity(intent);
            }
        });

        ivPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag = !flag) {
//                    ivPlay.setSelected(true);
                    Log.d("MainActivity", "bo-A");
                    sendBroadcast(new Intent(BroadcastValues.PLAY));
                } else {
//                    ivPlay.setSelected(false);
                    sendBroadcast(new Intent(BroadcastValues.PAUSE));
                }

            }
        });
        ivMainNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendBroadcast(new Intent(BroadcastValues.NEXT));
            }
        });

        totalFragment = new TotalFragment();
        //跳转到mainfragment
        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_main, totalFragment).commit();

        //实现接口跳转fragment
        totalFragment.setTitleOnClick(new TotalFragment.titleOnClick() {
            @Override
            public void onTitleClick() {
                // 隐藏当前fragment         进场动画
                getSupportFragmentManager().beginTransaction().hide(totalFragment).setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out)
                        .add(R.id.framelayout_main, new SearchFragment()).addToBackStack(null).commit();

            }
        });
    }

    public ImageView send() {
        return ivPlay;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setMainSongInfo(EventUpDateSongUI bean){
        tvSongTitle.setText(bean.getTitle());
        tvSongAuthor.setText(bean.getAuthor());
        Picasso.with(this).load(bean.getImageUrl()).fit().into(ivSongImage);
//        eventUpDateSongUI =new EventUpDateSongUI(bean.getTitle(),bean.getAuthor(),bean.getImageUrl(),bean.getImageBigUrl());
    }


    @Override
    public void onMyToLocalFragmentClick() {
        getSupportFragmentManager().beginTransaction().hide(totalFragment).setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out)
                .add(R.id.framelayout_main, new MyLocalMusicFragment()).addToBackStack(null).commit();
    }

    RankDetailsFragment rankDetailsFragment = new RankDetailsFragment();

    @Override
    public void onRankToItemListener(int count, String url) {
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out)
                .add(R.id.framelayout_main, rankDetailsFragment).addToBackStack(null).commit();
        getSupportFragmentManager().beginTransaction().hide(totalFragment).commit();

        Bundle bundle = new Bundle();
        bundle.putInt("count", count);
        bundle.putString("url", url);
        rankDetailsFragment.setArguments(bundle);
    }

    //重新显示titlefragment
    public void showTitleFragment() {
        getSupportFragmentManager().beginTransaction().show(totalFragment).commitAllowingStateLoss();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void serviceToplaySong(EventServiceToPauseBean serviceToPlayBean){
        ivPlay.setImageResource(R.mipmap.bt_minibar_pause_normal);
        Picasso.with(this).load(serviceToPlayBean.getImageUrl()).into(ivSongImage);
        tvSongAuthor.setText(serviceToPlayBean.getAuthor());
        tvSongTitle.setText(serviceToPlayBean.getTitle());
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void serviceToPlayBtn(EventServiceToPlayBtnBean btnBean){
        ivPlay.setImageResource(R.mipmap.bt_minibar_play_normal);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
