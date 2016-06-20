package com.example.kevin.baidumusic.db;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.enums.AssignType;

/**
 * Created by kevin on 16/6/11.
 */
public class DBHeart {
    public static final String TITLE= "title";

    @PrimaryKey(AssignType.AUTO_INCREMENT)
    int id;
    @Column(TITLE)
    String title;
    String author;
    String imageUrl;
    String imageBigUrl;
    String songId;

    public DBHeart(String title, String author, String imageUrl, String imageBigUrl, String songId) {
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
