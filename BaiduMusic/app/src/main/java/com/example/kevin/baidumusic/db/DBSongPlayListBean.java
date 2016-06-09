package com.example.kevin.baidumusic.db;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.enums.AssignType;

/**
 * Created by kevin on 16/5/27.
 */
public class DBSongPlayListBean {

    public static final String TITLE= "title";
    String songId;
    String author;
    String picUrl;
    String picBigUrl;
    @Column(TITLE)
    String title;
    @PrimaryKey(AssignType.AUTO_INCREMENT)
    int id;

    public DBSongPlayListBean(String songId, String author, String title, String picUrl, String picBigUrl) {
        this.songId = songId;
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

    public String getSongId() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }
}
