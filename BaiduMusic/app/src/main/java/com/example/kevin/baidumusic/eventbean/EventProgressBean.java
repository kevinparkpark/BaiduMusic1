package com.example.kevin.baidumusic.eventbean;

/**
 * Created by kevin on 16/5/26.
 */
public class EventProgressBean {
    int current,maxCurrent;
    String lrc;

    public EventProgressBean(int current, int maxCurrent, String lrc) {
        this.current = current;
        this.maxCurrent = maxCurrent;
        this.lrc = lrc;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getMaxCurrent() {
        return maxCurrent;
    }

    public void setMaxCurrent(int maxCurrent) {
        this.maxCurrent = maxCurrent;
    }

    public String getLrc() {
        return lrc;
    }

    public void setLrc(String lrc) {
        this.lrc = lrc;
    }
}
