package com.example.kevin.baidumusic.songplaypage;

import android.app.DownloadManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kevin.baidumusic.R;
import com.example.kevin.baidumusic.base.BaseFragment;
import com.example.kevin.baidumusic.eventbean.EventProgressBean;
import com.example.kevin.baidumusic.eventbean.EventSeekToBean;
import com.example.kevin.baidumusic.eventbean.EventServiceToPauseBean;
import com.example.kevin.baidumusic.eventbean.EventServiceToPlayBtnBean;
import com.example.kevin.baidumusic.eventbean.EventUpDateSongUI;
import com.example.kevin.baidumusic.netutil.NetTool;
import com.example.kevin.baidumusic.songplaypage.playpagelist.SongPlayPageListActivity;
import com.example.kevin.baidumusic.util.BroadcastValues;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by kevin on 16/5/26.
 */
public class SongPlayPageActivity extends AppCompatActivity implements View.OnClickListener {
    private boolean flag = true;
    private ViewPager viewPager;
    private ArrayList<BaseFragment> fragments;
    private SongPlayPageAdapter adapter;
    private ImageView ivSongPlay, ivNext, ivPrevious, ivMode,ivDownload,ivBack,iv2More;
    private TextView tvSongPlayTitle, tvSongPlayAuthor, tvSongPlayTime, tvSongPlayMaxTime;
    private SeekBar seekBar;
    private int maxCurrent;
    private DateFormat dateFormat;
    private boolean seekbarTouch = true;
    private final int MODE_RANDOM = 1;//随机播放
    private final int MODE_ONE = 2;//单曲循环
    private final int MODE_LOOP = 0;//列表循环
    private int mode = 0;//播放方式

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songplaypage);
        viewPager = (ViewPager) findViewById(R.id.songplaypage_viewpager);
        ivSongPlay = (ImageView) findViewById(R.id.iv_songplayactivity_play);
        tvSongPlayAuthor = (TextView) findViewById(R.id.songplayactivity_author);
        tvSongPlayTitle = (TextView) findViewById(R.id.songplayactivity_title);
        tvSongPlayTime = (TextView) findViewById(R.id.tv_songplaypage_time);
        tvSongPlayMaxTime = (TextView) findViewById(R.id.tv_songplaypage_maxtime);
        ivNext = (ImageView) findViewById(R.id.iv_songplayactivity_next);
        ivPrevious = (ImageView) findViewById(R.id.iv_songplayactivity_previous);
        ivMode = (ImageView) findViewById(R.id.iv_songplaypage_playmode);
        ivDownload= (ImageView) findViewById(R.id.iv_songplaypage_download);
        ivBack= (ImageView) findViewById(R.id.iv_songplaypage_back);
        iv2More= (ImageView) findViewById(R.id.iv_songplaypage_more);

        seekBar = (SeekBar) findViewById(R.id.seekbar_songplaypage);

        EventBus.getDefault().register(this);

        //接收赋值显示
//        Intent intent = getIntent();
//        tvSongPlayTitle.setText(intent.getStringExtra("title"));
//        tvSongPlayAuthor.setText(intent.getStringExtra("author"));
//        String imageUrl = intent.getStringExtra("imageurl");

        fragments = new ArrayList<>();
        AuthorImgFragment authorImgFragment = new AuthorImgFragment();
        fragments.add(new AuthorInfFragment());
        fragments.add(authorImgFragment);
        fragments.add(new AuthorLrcFragment());
        adapter = new SongPlayPageAdapter(getSupportFragmentManager());
        adapter.setFragments(fragments);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);

//        Bundle bundle = new Bundle();
//        bundle.putString("imagebigurl", imageUrl);
//        authorImgFragment.setArguments(bundle);

        //播放按键监听
        ivSongPlay.setOnClickListener(this);
        ivNext.setOnClickListener(this);
        ivPrevious.setOnClickListener(this);
        ivMode.setOnClickListener(this);
        ivDownload.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        iv2More.setOnClickListener(this);
    }

    //接收服务中seekbar相关数据
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void SeeBarControl(final EventProgressBean bean) {

        if (bean != null && maxCurrent != bean.getMaxCurrent()) {
            maxCurrent = bean.getMaxCurrent();
            seekBar.setMax(maxCurrent);
            tvSongPlayMaxTime.setText(timeFormat(bean.getMaxCurrent()));
        }
        tvSongPlayTime.setText(timeFormat(bean.getCurrent()));
        if (bean.getCurrent() != 0 && seekbarTouch) {
            seekBar.setProgress(bean.getCurrent());
        }
        //seekbar监听
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                seekbarTouch = false;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekbarTouch = true;
                EventBus.getDefault().post(new EventSeekToBean(seekBar.getProgress()));
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void serviceToplaySong(EventServiceToPauseBean serviceToPlayBean) {
        flag = false;
        ivSongPlay.setImageResource(R.mipmap.bt_widget_play_press);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void serviceToPlayBtn(EventServiceToPlayBtnBean btnBean) {
        flag=true;
        ivSongPlay.setImageResource(R.mipmap.bt_widget_pause_press);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void serviceToplaySong(EventUpDateSongUI songUI) {
        tvSongPlayAuthor.setText(songUI.getAuthor());
        tvSongPlayTitle.setText(songUI.getTitle());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    //格式化时间
    public String timeFormat(long time) {
        if (dateFormat == null) {
            dateFormat = new SimpleDateFormat("mm:ss");
        }
        return dateFormat.format(time);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_songplayactivity_play:
                if (!flag) {
//                    ivSongPlay.setImageResource(R.mipmap.bt_widget_pause_press);
                    sendBroadcast(new Intent(BroadcastValues.PLAY));
                } else {
//                    ivSongPlay.setImageResource(R.mipmap.bt_widget_play_press);
                    sendBroadcast(new Intent(BroadcastValues.PAUSE));
                }
                break;
            case R.id.iv_songplayactivity_next:
                sendBroadcast(new Intent(BroadcastValues.NEXT));
                break;
            case R.id.iv_songplayactivity_previous:
                sendBroadcast(new Intent(BroadcastValues.PREVIOUS));
                break;
            case R.id.iv_songplaypage_playmode:
                if (mode < 2) {
                    mode++;
                } else {
                    mode = 0;
                }
                swithPlayMode(mode);
                Log.d("SongPlayPageActivity", "mode:" + mode);
                break;
            case R.id.iv_songplaypage_download:
                sendBroadcast(new Intent(BroadcastValues.DOWNLOAD));
                break;
            case R.id.iv_songplaypage_back:
                finish();
                break;
            case R.id.iv_songplaypage_more:
                startActivity(new Intent(this, SongPlayPageListActivity.class));
                break;
        }
    }

    public void swithPlayMode(int mode) {
        switch (mode) {
            case MODE_LOOP:
                Toast.makeText(this, "循环播放", Toast.LENGTH_SHORT).show();
                break;
            case MODE_RANDOM:
                Toast.makeText(this, "随机播放", Toast.LENGTH_SHORT).show();
                break;
            case MODE_ONE:
                Toast.makeText(this, "单曲循环", Toast.LENGTH_SHORT).show();
                break;
        }
        Intent intent=new Intent(BroadcastValues.PLAY_MODE);
        intent.putExtra("mode",mode);
        sendBroadcast(intent);
        ivMode.setImageLevel(mode);
    }


}
