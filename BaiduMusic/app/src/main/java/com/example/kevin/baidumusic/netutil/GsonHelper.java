package com.example.kevin.baidumusic.netutil;

import com.android.volley.VolleyError;
import com.example.kevin.baidumusic.service.songplay.SongPlayBean;
import com.google.gson.Gson;

import java.util.List;
import java.util.Objects;

/**
 * Created by kevin on 16/6/19.
 */
public class GsonHelper {
    SongPlayBean songPlayBean = new SongPlayBean();

    public <T> SongPlayBean gsonUtils(String url) {
        NetTool netTool = new NetTool();
        netTool.getUrl(new NetListener() {
            @Override
            public void onSuccessed(String result) {
                Gson gson = new Gson();
                result = result.replace("(", "");
                result = result.replace(")", "");
                result = result.replace(";", "");
                songPlayBean=new SongPlayBean();
                songPlayBean = gson.fromJson(result, SongPlayBean.class);
            }

            @Override
            public void onFailed(VolleyError error) {

            }
        }, url);

        return songPlayBean;
    }

}
