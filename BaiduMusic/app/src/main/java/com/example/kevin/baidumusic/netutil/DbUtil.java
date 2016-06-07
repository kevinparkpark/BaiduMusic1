package com.example.kevin.baidumusic.netutil;

import com.example.kevin.baidumusic.db.DBSongListCacheBean;
import com.example.kevin.baidumusic.db.LiteOrmSington;
import com.example.kevin.baidumusic.search.SearchBean;
import com.litesuits.orm.LiteOrm;

import java.util.List;

/**
 * Created by kevin on 16/6/6.
 */
public class DbUtil {
    private static LiteOrm liteOrm = LiteOrmSington.getInstance().getLiteOrm();
    public static void insertInfo(final List<SearchBean.ResultBean.SongInfoBean.SongListBean> songInfoBeen){
        new Thread(new Runnable() {
            @Override
            public void run() {

                for (int i = 0; i < songInfoBeen.size(); i++) {
                    liteOrm.insert(new DBSongListCacheBean(songInfoBeen.get(i).getTitle(), songInfoBeen.get(i).getAuthor(),
                            null, null, songInfoBeen.get(i).getSong_id()));
                }
            }
        }).start();
    }


}
