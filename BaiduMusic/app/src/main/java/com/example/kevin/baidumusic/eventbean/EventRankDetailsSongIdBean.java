package com.example.kevin.baidumusic.eventbean;

/**
 * Created by kevin on 16/5/25.
 */
public class EventRankDetailsSongIdBean {
    String songId;

    public EventRankDetailsSongIdBean(String songId) {
        this.songId = songId;
    }

    public String getSongId() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }
}
