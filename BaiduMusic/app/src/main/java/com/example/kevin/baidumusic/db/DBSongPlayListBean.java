package com.example.kevin.baidumusic.db;

/**
 * Created by kevin on 16/5/27.
 */
public class DBSongPlayListBean {
    String songId;
    String author;
    String title;
    String picUrl;
    String picBigUrl;

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
