package com.example.kevin.baidumusic.eventbean;

/**
 * Created by kevin on 16/5/26.
 */
public class EventSongLastPlayListBean {
    String title;
    String author;
    String imageUrl;
    String imageBigUrl;

    public String getImageBigUrl() {
        return imageBigUrl;
    }

    public void setImageBigUrl(String imageBigUrl) {
        this.imageBigUrl = imageBigUrl;
    }


    public EventSongLastPlayListBean(String title, String author, String imageUrl, String imageBigUrl) {
        this.title = title;
        this.author = author;
        this.imageUrl = imageUrl;
        this.imageBigUrl = imageBigUrl;
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
}
