package com.example.kevin.baidumusic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.example.kevin.baidumusic.eventbean.EventServiceToPauseBean;
import com.example.kevin.baidumusic.eventbean.EventServiceToPlayBtnBean;
import com.example.kevin.baidumusic.eventbean.EventUpDateSongUI;
import com.example.kevin.baidumusic.kmusic.KMusicFragment;
import com.example.kevin.baidumusic.kmusic.authordetails.AuthorDetailsFragment;
import com.example.kevin.baidumusic.kmusic.authordetails.songlist.AuthorDetailsSonglistFragment;
import com.example.kevin.baidumusic.musiclibrary.rank.RankDetailsFragment;
import com.example.kevin.baidumusic.musiclibrary.rank.RankFragment;
import com.example.kevin.baidumusic.musiclibrary.recommend.RecommendFragment;
import com.example.kevin.baidumusic.musiclibrary.songmenu.SongMenuFragment;
import com.example.kevin.baidumusic.musiclibrary.songmenu.songmenudetails.SongMenuDetailsFragment;
import com.example.kevin.baidumusic.mymusic.MyFragment;
import com.example.kevin.baidumusic.mymusic.heartsonglist.HeartSongListFragment;
import com.example.kevin.baidumusic.mymusic.latelyplaylist.LatelyPlaylistFragment;
import com.example.kevin.baidumusic.mymusic.localmusic.MyLocalMusicFragment;
import com.example.kevin.baidumusic.netutil.VolleySingleton;
import com.example.kevin.baidumusic.search.SearchFragment;
import com.example.kevin.baidumusic.service.MediaPlayService;
import com.example.kevin.baidumusic.songlist.SongListCacheFragment;
import com.example.kevin.baidumusic.songplaypage.SongPlayPageActivity;
import com.example.kevin.baidumusic.totalfragment.TotalFragment;
import com.example.kevin.baidumusic.util.BroadcastValues;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

public class MainActivity extends AppCompatActivity implements MyFragment.MyToLocalFragmentOnClick, RankFragment.rankToOnItemListener
        , SongMenuFragment.songMenuToDetailsOnClickListener, KMusicFragment.kMusicToDetailsOnClickListener
        , AuthorDetailsFragment.authorDetailsToSonglistOnClickListener,MyFragment.LatelyPlaylistOnClick
,RecommendFragment.RecommendToSongMenuDetailsOnClickListener,RecommendFragment.RecommendToKmusicOnClickListener
,MyFragment.HeartSongListOnClick{

    private TotalFragment totalFragment;
    private MyFragment myFragment;
    private ImageView ivPlay, ivSongImage, ivMainNext, ivSongListCache;
    private TextView tvSongTitle, tvSongAuthor;
    private boolean flag = false;
    private RelativeLayout linearLayoutMainPlaylist;
    private EventUpDateSongUI eventUpDateSongUI;
    private Intent startIntent;
    private boolean songlistCacheIsCreated=false;

    public void setSonglistCacheIsCreated(boolean songlistCacheIsCreated) {
        this.songlistCacheIsCreated = songlistCacheIsCreated;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        startIntent = new Intent(this, MediaPlayService.class);
        startService(startIntent);

        ivPlay = (ImageView) findViewById(R.id.iv_main_play);
        ivSongImage = (ImageView) findViewById(R.id.iv_main_song_image);
        ivMainNext = (ImageView) findViewById(R.id.iv_main_next);
        ivSongListCache = (ImageView) findViewById(R.id.iv_main_songlistcache);
        tvSongTitle = (TextView) findViewById(R.id.tv_main_song_title);
        tvSongAuthor = (TextView) findViewById(R.id.tv_main_song_author);
        linearLayoutMainPlaylist = (RelativeLayout) findViewById(R.id.linearLayout_main_playlist);

        linearLayoutMainPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(new Intent(MainActivity.this, SongPlayPageActivity.class));
                startActivity(intent);
            }
        });

        ivPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!flag) {
//                    ivPlay.setSelected(true);
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
        //歌曲缓存列表
      //  final SongListCacheFragment songListCacheFragment=new SongListCacheFragment();
        ivSongListCache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!songlistCacheIsCreated)
                getSupportFragmentManager().beginTransaction().add(R.id.framelayout_main, new SongListCacheFragment())
                        .addToBackStack(null).commit();
                songlistCacheIsCreated=true;
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
    public void setMainSongInfo(EventUpDateSongUI bean) {
        tvSongTitle.setText(bean.getTitle());
        tvSongAuthor.setText(bean.getAuthor());
        if (bean.getImageUrl()!=null){
            ImageLoader imageLoader= VolleySingleton.getInstance().getImageLoader();
            imageLoader.get(bean.getImageUrl(),ImageLoader.getImageListener(ivSongImage,R.mipmap.yuan
                    ,R.mipmap.yuan));
//        Picasso.with(this).load(bean.getImageUrl()).fit().into(ivSongImage);
        }
    }

    //跳转到本地音乐
    @Override
    public void onMyToLocalFragmentClick() {
        getSupportFragmentManager().beginTransaction().hide(totalFragment).setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out)
                .add(R.id.framelayout_main, new MyLocalMusicFragment()).addToBackStack(null).commit();
    }
    //跳转到最近播放
    @Override
    public void onLatelyPlaylistClick() {
        getSupportFragmentManager().beginTransaction().hide(totalFragment).setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out)
                .add(R.id.framelayout_main, new LatelyPlaylistFragment()).addToBackStack(null).commit();
    }
    //跳转到我喜欢的音乐
    @Override
    public void onHeartSongListClick() {
        getSupportFragmentManager().beginTransaction().hide(totalFragment).setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out)
                .add(R.id.framelayout_main, new HeartSongListFragment()).addToBackStack(null).commit();
    }

    //跳转到排行榜
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
    //跳转到歌单
    SongMenuDetailsFragment songMenuDetailsFragment = new SongMenuDetailsFragment();
    @Override
    public void onSongMenuToDetailsClickListener(String position) {
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out)
                .add(R.id.framelayout_main, songMenuDetailsFragment).hide(totalFragment).addToBackStack(null).commit();
        Bundle bundle = new Bundle();
        bundle.putString("listid", position);
        songMenuDetailsFragment.setArguments(bundle);
    }
    //跳转歌单详情页面
    @Override
    public void onRecommendToSongMenuDetailsClickListener(String listId) {
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out)
                .add(R.id.framelayout_main, songMenuDetailsFragment).hide(totalFragment).addToBackStack(null).commit();
        Bundle bundle = new Bundle();
        bundle.putString("listid", listId);
        songMenuDetailsFragment.setArguments(bundle);
    }
    //跳转到k歌界面
    @Override
    public void onRecommendToKmusicClickListener() {

        totalFragment.flipPage();
    }

    //歌手详情页面
    AuthorDetailsFragment authorDetailsFragment = new AuthorDetailsFragment();

    @Override
    public void onKMusicToDetailsClickListener(String url1, String url2, String authorName) {
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out)
                .add(R.id.framelayout_main, authorDetailsFragment).hide(totalFragment).addToBackStack(null).commit();
        Bundle bundle = new Bundle();
        bundle.putString("authorurl1", url1);
        bundle.putString("authorurl2", url2);
        bundle.putString("authorname", authorName);
        authorDetailsFragment.setArguments(bundle);
    }

    //歌手歌曲页面
    AuthorDetailsSonglistFragment authorDetailsSonglistFragment = new AuthorDetailsSonglistFragment();

    @Override
    public void onAuthorDetailsToSonglistClickListener(String tingUid, String author, String country, String imgUrl) {
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out)
                .add(R.id.framelayout_main, authorDetailsSonglistFragment).hide(authorDetailsFragment).addToBackStack(null).commit();
        Bundle bundle = new Bundle();
        bundle.putString("tinguid", tingUid);
        bundle.putString("author", author);
        bundle.putString("country", country);
        bundle.putString("imgurl", imgUrl);
        authorDetailsSonglistFragment.setArguments(bundle);
    }

    //重新显示titlefragment
    public void showTitleFragment() {
        getSupportFragmentManager().beginTransaction().show(totalFragment).commitAllowingStateLoss();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void serviceToplaySong(EventServiceToPauseBean serviceToPlayBean) {
        flag = false;
        ivPlay.setImageResource(R.mipmap.bt_minibar_play_normal);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void serviceToPlayBtn(EventServiceToPlayBtnBean btnBean) {
        flag = true;
        ivPlay.setImageResource(R.mipmap.bt_minibar_pause_normal);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
       // stopService(startIntent);
    }




    //主页状态
//    private void setWindowsState(){
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        getWindow().setBackgroundDrawableResource(R.color.maincolor);
//    }

    //点击2此退出
//    private long exitTime = 0;
//
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
//            if((System.currentTimeMillis()-exitTime) > 2000){
//                Toast.makeText(getApplicationContext(), "再按一次后退键退出程序", Toast.LENGTH_SHORT).show();
//                exitTime = System.currentTimeMillis();
//            } else {
//                //退出代码
//                finish();
//            }
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }


}
