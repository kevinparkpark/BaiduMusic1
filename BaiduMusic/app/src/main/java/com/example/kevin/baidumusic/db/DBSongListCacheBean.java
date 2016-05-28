package com.example.kevin.baidumusic.db;

/**
 * Created by kevin on 16/5/27.
 */
public class DBSongListCacheBean {
    int id;
    String title;
    String author;
    String imageUrl;
    String imageBigUrl;
    String songId;

    public DBSongListCacheBean(String title, String author, String imageUrl, String imageBigUrl, String songId) {
        this.title = title;
        this.author = author;
        this.imageUrl = imageUrl;
        this.imageBigUrl = imageBigUrl;
        this.songId = songId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageBigUrl() {
        return imageBigUrl;
    }

    public void setImageBigUrl(String imageBigUrl) {
        this.imageBigUrl = imageBigUrl;
    }

    public String getSongId() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }
}
