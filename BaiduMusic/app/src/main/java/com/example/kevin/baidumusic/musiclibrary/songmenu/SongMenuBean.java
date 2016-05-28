package com.example.kevin.baidumusic.musiclibrary.songmenu;

import android.graphics.Bitmap;

/**
 * Created by kevin on 16/5/21.
 */
public class SongMenuBean {
    private String count,title,where;
    private Bitmap imageSongmenu;

    public SongMenuBean(Bitmap imageSongmenu) {
        this.imageSongmenu = imageSongmenu;
    }

    public Bitmap getImageSongmenu() {
        return imageSongmenu;
    }

    public void setImageSongmenu(Bitmap imageSongmenu) {
        this.imageSongmenu = imageSongmenu;
    }

    public SongMenuBean() {
    }

    public SongMenuBean(String count, String title, String where) {
        this.count = count;
        this.title = title;
        this.where = where;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }
}
