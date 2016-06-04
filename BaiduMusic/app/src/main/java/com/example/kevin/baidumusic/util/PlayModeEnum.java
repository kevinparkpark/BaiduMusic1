package com.example.kevin.baidumusic.util;

/**
 * Created by kevin on 16/6/4.
 */
public enum PlayModeEnum {
    MODE_RANDOM(1),MODE_LOOP(0),MODE_ONE(2);
    private int value;

    PlayModeEnum(int value) {
        this.value = value;
    }
    public int value(){
        return value;
    }
    public static PlayModeEnum valueOf(int value){
        switch (value){
            case 0:
                return MODE_LOOP;
            case 1:
                return MODE_RANDOM;
            case 2:
                return MODE_ONE;
            default:
                return MODE_LOOP;
        }
    }

}
