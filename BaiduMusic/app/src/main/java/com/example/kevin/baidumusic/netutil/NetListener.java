package com.example.kevin.baidumusic.netutil;

import com.android.volley.VolleyError;

/**
 * Created by kevin on 16/5/23.
 */
public interface NetListener {

    void onSuccessed(String result);
    void onFailed(VolleyError error);

}
