package com.example.kevin.baidumusic.kmusic;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.example.kevin.baidumusic.R;
import com.example.kevin.baidumusic.app.MyApp;
import com.example.kevin.baidumusic.netutil.VolleySingleton;

import java.util.List;

/**
 * Created by kevin on 16/6/1.
 */
public class kMusicAdapter extends PagerAdapter {
    private List<KMusicBean.ArtistBean> artistBeanList;

    public void setArtistBeanList(List<KMusicBean.ArtistBean> artistBeanList) {
        this.artistBeanList = artistBeanList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return artistBeanList == null ? 0 : Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final ImageView imageView = new ImageView(MyApp.context);
        imageView.setMaxWidth(300);
        imageView.setMaxHeight(250);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ImageLoader loader = VolleySingleton.getInstance().getImageLoader();
        loader.get(artistBeanList.get(position%artistBeanList.size()).getAvatar_big(),ImageLoader.getImageListener(
                imageView,R.mipmap.yuan, R.mipmap.yuan));
//        loader.get(artistBeanList.get(position % artistBeanList.size()).getAvatar_big(), new ImageLoader.ImageListener() {
//            @Override
//            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
//
//                Bitmap bitmap = response.getBitmap();
//                if (bitmap != null) {
//                    Bitmap out = Bitmap.createBitmap(200, 150, Bitmap.Config.ARGB_4444);
//                    Canvas canvas = new Canvas(out);
//                    canvas.drawBitmap(bitmap, new Rect(0, 0, 200, 150), new Rect(0, 0, 200, 150), new Paint());
//                    imageView.setImageBitmap(out);
//                    bitmap.recycle();
//                }
//            }
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
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
        if (container.getChildAt(position % artistBeanList.size()) == object) {
            container.removeViewAt(position % artistBeanList.size());
        }
    }
}
