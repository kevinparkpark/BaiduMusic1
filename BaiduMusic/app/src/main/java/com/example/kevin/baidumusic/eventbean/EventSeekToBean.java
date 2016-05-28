package com.example.kevin.baidumusic.eventbean;

/**
 * Created by kevin on 16/5/27.
 */
public class EventSeekToBean {
    int current;

    public EventSeekToBean(int current) {
        this.current = current;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }
}
