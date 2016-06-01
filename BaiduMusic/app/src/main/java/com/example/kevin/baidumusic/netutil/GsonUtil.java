package com.example.kevin.baidumusic.netutil;


import com.android.volley.VolleyError;
import com.example.kevin.baidumusic.musiclibrary.rank.songplay.SongPlayBean;
import com.google.gson.Gson;

/**
 * Created by kevin on 16/5/30.
 */
public class GsonUtil {
    String songUrl,lrc,author,songTitle,songImageUrl,songImageBigUrl;



    public GsonUtil(String url) {
        NetTool netTool = new NetTool();
        netTool.getUrl(new NetListener() {
            @Override
            public void onSuccessed(String result) {
                Gson gson = new Gson();
                result = result.replace("(", "");
                result = result.replace(")", "");
                result = result.replace(";", "");
                SongPlayBean songPlayBean = gson.fromJson(result, SongPlayBean.class);
                songUrl = songPlayBean.getBitrate().getFile_link();
                 lrc=songPlayBean.getSonginfo().getLrclink();
                 author = songPlayBean.getSonginfo().getAuthor();
                 songTitle = songPlayBean.getSonginfo().getTitle();
                 songImageUrl = songPlayBean.getSonginfo().getPic_small();
                 songImageBigUrl = songPlayBean.getSonginfo().getPic_premium();
            }

            @Override
            public void onFailed(VolleyError error) {

            }
        }, url);

    }
    public String getSongUrl() {
        return songUrl;
    }

    public String getLrc() {
        return lrc;
    }

    public String getAuthor() {
        return author;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public String getSongImageUrl() {
        return songImageUrl;
    }

    public String getSongImageBigUrl() {
        return songImageBigUrl;
    }

}
