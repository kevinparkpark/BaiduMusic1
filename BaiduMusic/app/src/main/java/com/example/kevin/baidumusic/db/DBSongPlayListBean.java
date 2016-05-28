package com.example.kevin.baidumusic.db;

import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.enums.AssignType;

/**
 * Created by kevin on 16/5/27.
 */
public class DBSongPlayListBean {
    String songUrl;
    String author;
    String title;
    String picUrl;
    String picBigUrl;

    int id;

    public DBSongPlayListBean(String songUrl, String author, String title, String picUrl, String picBigUrl) {
        this.songUrl = songUrl;
        this.author = author;
        this.title = title;
        this.picUrl = picUrl;
        this.picBigUrl = picBigUrl;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getPicBigUrl() {
        return picBigUrl;
    }

    public void setPicBigUrl(String picBigUrl) {
        this.picBigUrl = picBigUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSongUrl() {
        return songUrl;
    }

    public void setSongUrl(String songUrl) {
        this.songUrl = songUrl;
    }
}
