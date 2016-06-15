package com.example.kevin.baidumusic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.example.kevin.baidumusic.db.DBSongListCacheBean;
import com.example.kevin.baidumusic.db.LiteOrmSington;
import com.example.kevin.baidumusic.eventbean.EventPosition;
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
import com.example.kevin.baidumusic.mymusic.localmusic.musicsonglist.MyLocalMusicSonglistFragment;
import com.example.kevin.baidumusic.netutil.VolleySingleton;
import com.example.kevin.baidumusic.search.SearchFragment;
import com.example.kevin.baidumusic.service.MediaPlayService;
import com.example.kevin.baidumusic.songplaypage.SongPlayPageActivity;
import com.example.kevin.baidumusic.totalfragment.MainPopAdapter;
import com.example.kevin.baidumusic.totalfragment.TotalFragment;
import com.example.kevin.baidumusic.util.BroadcastValues;
import com.litesuits.orm.LiteOrm;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MyFragment.MyToLocalFragmentOnClick, RankFragment.rankToOnItemListener
        , SongMenuFragment.songMenuToDetailsOnClickListener, KMusicFragment.kMusicToDetailsOnClickListener
        , AuthorDetailsFragment.authorDetailsToSonglistOnClickListener, MyFragment.LatelyPlaylistOnClick
        , RecommendFragment.RecommendToSongMenuDetailsOnClickListener, RecommendFragment.RecommendToKmusicOnClickListener
        , MyFragment.HeartSongListOnClick {

    private TotalFragment totalFragment;
    private ImageView ivPlay, ivSongImage, ivMainNext, ivSongListCache;
    private TextView tvSongTitle, tvSongAuthor;
    private boolean flag = false;
    private RelativeLayout linearLayoutMainPlaylist;
    private Intent startIntent;
    private boolean songlistCacheIsCreated = false;
    private PopupWindow popupWindow;
    private LiteOrm liteOrm;
    private List<DBSongListCacheBean> cacheBeen;
    private final int MODE_RANDOM = 1;//随机播放
    private final int MODE_ONE = 2;//单曲循环
    private final int MODE_LOOP = 0;//列表循环
    private int mode = 0;//播放方式
    private ImageView ivMode;
    private FrameLayout frameLayout;

    public void setSonglistCacheIsCreated(boolean songlistCacheIsCreated) {
        this.songlistCacheIsCreated = songlistCacheIsCreated;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        liteOrm= LiteOrmSington.getInstance().getLiteOrm();
        //启动服务
        startIntent = new Intent(this, MediaPlayService.class);
        startService(startIntent);

        frameLayout= (FrameLayout) findViewById(R.id.framelayout_main);

        ivPlay = (ImageView) findViewById(R.id.iv_main_play);
        ivSongImage = (ImageView) findViewById(R.id.iv_main_song_image);
        ivMainNext = (ImageView) findViewById(R.id.iv_main_next);
        ivSongListCache = (ImageView) findViewById(R.id.iv_main_songlistcache);
        tvSongTitle = (TextView) findViewById(R.id.tv_main_song_title);
        tvSongAuthor = (TextView) findViewById(R.id.tv_main_song_author);
        linearLayoutMainPlaylist = (RelativeLayout) findViewById(R.id.linearLayout_main_playlist);
        //跳转播放界面
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
                showSongListCache();
            }
        });

        totalFragment = new TotalFragment();
        //跳转到mainfragment
        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_main, totalFragment).commit();

        //实现接口跳转fragment
        totalFragment.setTitleOnClick(new TotalFragment.titleOnClick() {
            @Override
            public void onTitleClick() {
                // 隐藏当前fragment     进场动画
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
        if (bean.getImageUrl() != null) {
            ImageLoader imageLoader = VolleySingleton.getInstance().getImageLoader();
            imageLoader.get(bean.getImageUrl(), ImageLoader.getImageListener(ivSongImage, R.mipmap.yuan
                    , R.mipmap.yuan));
//        Picasso.with(this).load(bean.getImageUrl()).fit().into(ivSongImage);
        }
    }

    //跳转到本地音乐
    @Override
    public void onMyToLocalFragmentClick() {
        getSupportFragmentManager().beginTransaction().hide(totalFragment).setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out)
                .add(R.id.framelayout_main, new MyLocalMusicSonglistFragment()).addToBackStack(null).commit();
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
    private long exitTime = 0;

    //
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (!totalFragment.isVisible()) {
            return super.onKeyDown(keyCode, event);
        }
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次后退键退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //popupwindow songlistcache
    private void showSongListCache() {
        //获取屏幕高度
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        //获取通知栏高度
        Rect outRect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);

        View contentView=LayoutInflater.from(this).inflate(R.layout.popupwindow_main,null);
        popupWindow=new PopupWindow(contentView,ViewGroup.LayoutParams.MATCH_PARENT
        ,displayMetrics.heightPixels-linearLayoutMainPlaylist.getLayoutParams().height-outRect.top);
        popupWindow.setFocusable(true);

        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setAnimationStyle(R.style.contextMenuAnim);
        popupWindow.showAtLocation(frameLayout,Gravity.TOP,0,0);

        ListView popListView= (ListView) contentView.findViewById(R.id.popupwindow_main_listview);
        TextView tvClear= (TextView) contentView.findViewById(R.id.tv_main_popupwindow_clear);
        TextView tvList= (TextView) contentView.findViewById(R.id.tv_main_popupwindow_text);
        ivMode= (ImageView) contentView.findViewById(R.id.iv_main_popupwindow_playorder);

        //读取播放模式
        SharedPreferences getsp = getSharedPreferences("mode", MODE_PRIVATE);
        mode = getsp.getInt("mode", 0);
        ivMode.setImageLevel(mode);

        contentView.findViewById(R.id.popupwindow_main_relativelayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        final MainPopAdapter adapter=new MainPopAdapter(this);
        cacheBeen=liteOrm.query(DBSongListCacheBean.class);
        adapter.setCacheBeen(cacheBeen);
        popListView.setAdapter(adapter);
        popListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EventBus.getDefault().post(new EventPosition(position+1));
                sendBroadcast(new Intent(BroadcastValues.PREVIOUS));
            }
        });
        tvClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                liteOrm.deleteAll(DBSongListCacheBean.class);
                cacheBeen=liteOrm.query(DBSongListCacheBean.class);
                adapter.setCacheBeen(cacheBeen);
            }
        });
        tvList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
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
                SharedPreferences sp = getSharedPreferences("mode", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("mode", mode);
                editor.commit();
                ivMode.setImageLevel(mode);
            }
        });

    }
    //playmode
    private void swithPlayMode(int mode) {
        switch (mode) {
            case MODE_LOOP:
                Toast.makeText(MainActivity.this, "循环播放", Toast.LENGTH_SHORT).show();
                break;
            case MODE_RANDOM:
                Toast.makeText(MainActivity.this, "随机播放", Toast.LENGTH_SHORT).show();
                break;
            case MODE_ONE:
                Toast.makeText(MainActivity.this, "单曲循环", Toast.LENGTH_SHORT).show();
                break;
        }
        Intent intent=new Intent(BroadcastValues.PLAY_MODE);
        intent.putExtra("mode",mode);
        sendBroadcast(intent);
    }
}
