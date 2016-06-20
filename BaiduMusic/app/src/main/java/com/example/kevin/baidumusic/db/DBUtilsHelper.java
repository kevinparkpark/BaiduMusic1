package com.example.kevin.baidumusic.db;

import android.widget.Toast;

import com.example.kevin.baidumusic.R;
import com.example.kevin.baidumusic.service.songplay.SongPlayBean;
import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.assit.QueryBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by kevin on 16/6/17.
 * liteOrm helper
 */
public class DBUtilsHelper {
    private LiteOrm liteOrm = LiteOrmSington.getInstance().getLiteOrm();

    public List<DBSongListCacheBean> showQuery() {

        List<DBSongListCacheBean> dbSongListCacheBeen = liteOrm.query(DBSongListCacheBean.class);
        return dbSongListCacheBeen;
    }

    //判断数据库是否存在这条信息,如果存在-替换,不存在插入
    public void showQueryBuilder(SongPlayBean songPlayBean) {

        QueryBuilder<DBSongPlayListBean> list = new QueryBuilder<>(DBSongPlayListBean.class)
                .whereEquals(DBSongPlayListBean.TITLE, songPlayBean.getSonginfo().getTitle());
        List<DBSongPlayListBean> dbSongPlayListBeen = liteOrm.query(list);
        if (dbSongPlayListBeen.size() == 0) {
            liteOrm.insert(new DBSongPlayListBean(songPlayBean.getSonginfo().getSong_id(),
                    songPlayBean.getSonginfo().getAuthor(), songPlayBean.getSonginfo().getTitle(),
                    songPlayBean.getSonginfo().getPic_small(), songPlayBean.getSonginfo().getPic_premium()));
        }else {
            liteOrm.delete(dbSongPlayListBeen);
            liteOrm.insert(new DBSongPlayListBean(songPlayBean.getSonginfo().getSong_id(),
                    songPlayBean.getSonginfo().getAuthor(), songPlayBean.getSonginfo().getTitle(),
                    songPlayBean.getSonginfo().getPic_small(), songPlayBean.getSonginfo().getPic_premium()));
        }
    }
    public int showQueryDBHeart(String title,String author,String img, String bigimg,String songId){
        int size=0;
        QueryBuilder<DBHeart> list=new QueryBuilder<>(DBHeart.class).whereEquals(DBHeart.TITLE,title);
        if (liteOrm.query(list).size()==size){
            liteOrm.insert(new DBHeart(title,author,img,bigimg,songId));
        }else {
            liteOrm.delete(liteOrm.query(list));
            size=1;
        }
        return size;
    }

    public <T>ArrayList<T> showQuery(Class<T> clazz, String where, String value){

        QueryBuilder<T> list=new QueryBuilder<>(clazz).whereEquals(where,value);

        return liteOrm.query(list);
    }

    public <T>List<T> queryAll(Class<T> clazz){

        List<T> list=liteOrm.query(clazz);

        return list;
    }

    public void insertDB(Object o) {
        liteOrm.insert(o);
    }

    public <T>void delete(List<T> o) {
        liteOrm.delete(o);
    }
    public <T>void deleteAll(Class<T> clazz){
        liteOrm.delete(clazz);
    }

}
