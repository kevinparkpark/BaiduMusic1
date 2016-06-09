package com.example.kevin.baidumusic.musiclibrary.recommend;

import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.example.kevin.baidumusic.R;
import com.example.kevin.baidumusic.app.MyApp;
import com.example.kevin.baidumusic.netutil.VolleySingleton;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by kevin on 16/5/31.
 */
public class RecommendPagerAdapter extends PagerAdapter{
    private List<RecommendBean.PicBean> picBeanList;

    public void setPicBeanList(List<RecommendBean.PicBean> picBeanList) {
        this.picBeanList = picBeanList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return picBeanList==null?0:Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView=new ImageView(MyApp.context);
//        Picasso.with(MyApp.context).load(picBeanList.get(position%picBeanList.size())
//                .getRandpic()).into(imageView);
        ImageLoader loader = VolleySingleton.getInstance().getImageLoader();
        loader.get(picBeanList.get(position%picBeanList.size()).getRandpic(),ImageLoader.getImageListener(
                imageView, R.mipmap.yuan,R.mipmap.yuan));
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
        if (container.getChildAt(position%picBeanList.size())==object){
            container.removeViewAt(position%picBeanList.size());
        }
    }
}
