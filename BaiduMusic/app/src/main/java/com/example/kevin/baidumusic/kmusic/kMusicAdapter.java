package com.example.kevin.baidumusic.kmusic;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.example.kevin.baidumusic.R;
import com.example.kevin.baidumusic.app.MyApp;
import com.example.kevin.baidumusic.netutil.VolleySingleton;

import java.util.List;

/**
 * Created by kevin on 16/6/1.
 */
public class kMusicAdapter extends PagerAdapter{
    private List<KMusicBean.ArtistBean>artistBeanList;

    public void setArtistBeanList(List<KMusicBean.ArtistBean> artistBeanList) {
        this.artistBeanList = artistBeanList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return artistBeanList==null?0:Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView=new ImageView(MyApp.context);
        ImageLoader loader = VolleySingleton.getInstance().getImageLoader();
        loader.get(artistBeanList.get(position%artistBeanList.size()).getAvatar_big(),ImageLoader.getImageListener(
                imageView, R.mipmap.ic_launcher,R.mipmap.ic_launcher));
        try {
            container.addView(imageView);
        } catch (IllegalStateException e) {
            //先从container中移除ImageView
            container.removeView(imageView);
            //再次添加
            container.addView(imageView);
        }

        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (container.getChildAt(position%artistBeanList.size())==object){
            container.removeViewAt(position%artistBeanList.size());
        }
    }
}
