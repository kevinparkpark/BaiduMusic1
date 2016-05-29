package com.example.kevin.baidumusic.netutil;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;

/**
 * Created by kevin on 16/5/23.
 */
public class NetTool {

    private RequestQueue requestQueue;
    private ImageLoader imageLoader;

    public NetTool() {
        requestQueue=VolleySingleton.getInstance().getRequestQueue();
        imageLoader=VolleySingleton.getInstance().getImageLoader();
    }

    //排行榜数据解析
    public void getLeRank(final NetListener netListener){

        StringRequest stringRequest=new StringRequest(URLValues.LE_NEWRANK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                netListener.onSuccessed(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                netListener.onFailed(error);
            }
        });
        requestQueue.add(stringRequest);
    }

    //排行榜item数据解析
    public void getLeRankDetails(final NetListener netListener, int count){

        StringRequest stringRequest=new StringRequest(URLValues.LE_RANKDETAILS1+String.valueOf(count)+URLValues
                .LE_RANKDETAILS2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                netListener.onSuccessed(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                netListener.onFailed(error);
            }
        });
        requestQueue.add(stringRequest);
    }

    public void getDetailsSongUrl(final NetListener netListener, String songId){
        StringRequest request=new StringRequest(URLValues.LE_RANKDETAILS_SONGURL1 + songId + URLValues.LE_RANKDETAILS_SONGURL2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                netListener.onSuccessed(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                netListener.onFailed(error);
            }
        });
        requestQueue.add(request);
    }
    //输入URl 解析
    public void getUrl(final NetListener netListener,String url){

        StringRequest stringRequest=new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                netListener.onSuccessed(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                netListener.onFailed(error);
            }
        });
        requestQueue.add(stringRequest);
    }
    public void getUrlId(final NetListener netListener,String url1, String songId,String url2){
        StringRequest stringRequest=new StringRequest(url1+songId+url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                netListener.onSuccessed(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                netListener.onFailed(error);
            }
        });
        requestQueue.add(stringRequest);
    }

}
