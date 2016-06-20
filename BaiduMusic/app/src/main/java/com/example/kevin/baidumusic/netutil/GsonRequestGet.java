package com.example.kevin.baidumusic.netutil;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;

import javax.xml.transform.ErrorListener;

/**
 * Created by kevin on 16/6/19.
 */
public class GsonRequestGet<T> extends Request<T> {
    private final Response.Listener<T> mListener;

    private Gson mGson;

    private Class<T> mClass;

    public GsonRequestGet(int method, String url, Class<T> clazz, Response.Listener<T> listener,
                          ErrorListener errorListener) {
        super(method, url, (Response.ErrorListener) errorListener);
        mGson = new Gson();
        mClass = clazz;
        mListener = listener;
    }

    public GsonRequestGet(String url, Class<T> clazz, Response.Listener<T> listener,
                          ErrorListener errorListener) {
        this(Method.GET, url, clazz, listener, errorListener);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(mGson.fromJson(jsonString, mClass),
                    HttpHeaderParser.parseCacheHeaders(response));//用Gson解析返回Java对象
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);//回调T对象
    }
}
