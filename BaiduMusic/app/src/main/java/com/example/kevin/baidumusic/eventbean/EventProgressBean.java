package com.example.kevin.baidumusic.eventbean;

/**
 * Created by kevin on 16/5/26.
 */
public class EventProgressBean {
    int current,maxCurrent;

    public EventProgressBean(int current, int maxCurrent) {
        this.current = current;
        this.maxCurrent = maxCurrent;
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
}
