package com.example.kevin.baidumusic.musiclibrary.radio.radioplay.songplaylist;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.example.kevin.baidumusic.R;
import com.example.kevin.baidumusic.eventbean.EventProgressBean;
import com.example.kevin.baidumusic.eventbean.EventServiceToPauseBean;
import com.example.kevin.baidumusic.eventbean.EventServiceToPlayBtnBean;
import com.example.kevin.baidumusic.eventbean.EventUpDateSongUI;
import com.example.kevin.baidumusic.musiclibrary.radio.radioplay.songplaylist.RadioPlayListBean;
import com.example.kevin.baidumusic.netutil.VolleySingleton;
import com.example.kevin.baidumusic.util.BroadcastValues;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 16/6/2.
 */
public class RadioPlayListPagerAdapter extends PagerAdapter{
    private List<RadioPlayListBean.ResultBean.SonglistBean> songlistBeanList;
    private List<View> viewGroup;
    private Context context;
    private View group;
    private ImageView ivPlay;
    private TextView tvTime;
    private DateFormat dateFormat;
    private TextView tvTitle,tvAuthor;
    private ImageView imageView;
    private int pos;


    public RadioPlayListPagerAdapter(Context context) {
        EventBus.getDefault().register(this);
        this.context = context;
    }

    public void setSonglistBeanList(List<RadioPlayListBean.ResultBean.SonglistBean> songlistBeanList) {
        viewGroup=new ArrayList<>();
        for (RadioPlayListBean.ResultBean.SonglistBean bean : songlistBeanList) {
            group=new View(context);
            group= LayoutInflater.from(context).inflate(R.layout.item_radioplaylist_view,null);
            imageView= (ImageView) group.findViewById(R.id.iv_view_playlist_img);
            tvTitle= (TextView) group.findViewById(R.id.tv_view_playlist_title);
            tvAuthor= (TextView) group.findViewById(R.id.tv_view_playlist_author);
            ivPlay= (ImageView) group.findViewById(R.id.iv_view_playlist_play);
            tvTime= (TextView) group.findViewById(R.id.tv_view_playlist_time);

            tvTitle.setText(bean.getTitle());
            tvAuthor.setText(bean.getAuthor());

            ImageLoader loader= VolleySingleton.getInstance().getImageLoader();
            loader.get(bean.getPic_big(),ImageLoader.getImageListener(imageView,
                    R.mipmap.yuan,R.mipmap.yuan));

            viewGroup.add(group);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return viewGroup==null?0:viewGroup.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        group=viewGroup.get(position);

        ivPlay = (ImageView) group.findViewById(R.id.iv_view_playlist_play);
        ivPlay.setOnClickListener(new View.OnClickListener() {
            boolean flag=false;
            @Override
            public void onClick(View v) {
                if (flag=!flag){
                context.sendBroadcast(new Intent(BroadcastValues.PAUSE));
                ivPlay.setImageResource(R.mipmap.scenario_drive_pause_button_normal);
                }else {
                    context.sendBroadcast(new Intent(BroadcastValues.PLAY));
                    ivPlay.setImageResource(R.mipmap.scenario_drive_play_button_normal);
                }
            }
        });

//        tvTitle.setText(songlistBeanList.get(position).getTitle());
//        tvAuthor.setText(songlistBeanList.get(position).getAuthor());
////        Picasso.with(context).load(songlistBeanList.get(position).getPic_big()).into(imageView);
//
//        ImageLoader loader= VolleySingleton.getInstance().getImageLoader();
//        loader.get(songlistBeanList.get(position).getPic_big(),ImageLoader.getImageListener(imageView,
//                R.mipmap.yuan,R.mipmap.yuan));
        container.addView(group);

        return group;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void serviceToplaySong(EventServiceToPauseBean serviceToPlayBean){
        ImageView imageView= (ImageView) viewGroup.get(pos).findViewById(R.id.iv_view_playlist_play);
        imageView.setImageResource(R.mipmap.scenario_drive_play_button_normal);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void serviceToPlayBtn(EventServiceToPlayBtnBean btnBean){
        ImageView imageView= (ImageView) viewGroup.get(pos).findViewById(R.id.iv_view_playlist_play);
        imageView.setImageResource(R.mipmap.scenario_drive_pause_button_normal);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void serviceToplaySong(EventProgressBean bean){
       TextView textView = (TextView) viewGroup.get(pos).findViewById(R.id.tv_view_playlist_time);
        textView.setText('-'+timeFormat(bean.getMaxCurrent()-bean.getCurrent()));
    }
    //格式化时间
    public String timeFormat(long time) {
        if (dateFormat == null) {
            dateFormat = new SimpleDateFormat("mm:ss");
        }
        return dateFormat.format(time);
    }

    public void setPos(int pos) {
        this.pos = pos;
    }
}
