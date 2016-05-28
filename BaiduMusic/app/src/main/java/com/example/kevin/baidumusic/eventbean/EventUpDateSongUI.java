package com.example.kevin.baidumusic.eventbean;

/**
 * Created by kevin on 16/5/27.
 */
public class EventUpDateSongUI {
    String title,author,imageUrl,ImageBigUrl;

    public EventUpDateSongUI(String title, String author, String imageUrl, String imageBigUrl) {
        this.title = title;
        this.author = author;
        this.imageUrl = imageUrl;
        ImageBigUrl = imageBigUrl;
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
        return ImageBigUrl;
    }

    public void setImageBigUrl(String imageBigUrl) {
        ImageBigUrl = imageBigUrl;
    }
}
