package com.example.kevin.baidumusic.musiclibrary.mv;

import android.graphics.Bitmap;

/**
 * Created by kevin on 16/5/21.
 */
public class MvBean {

    private String name,author;
    private Bitmap mvImage;

    public MvBean() {
    }

    public MvBean(String name, String author, Bitmap mvImage) {
        this.name = name;
        this.author = author;
        this.mvImage = mvImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Bitmap getMvImage() {
        return mvImage;
    }

    public void setMvImage(Bitmap mvImage) {
        this.mvImage = mvImage;
    }
}
