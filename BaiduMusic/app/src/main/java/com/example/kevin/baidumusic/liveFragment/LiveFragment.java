package com.example.kevin.baidumusic.liveFragment;


import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.kevin.baidumusic.base.BaseFragment;
import com.example.kevin.baidumusic.R;

/**
 * Created by kevin on 16/5/19.
 */
public class LiveFragment extends BaseFragment{

    private WebView webView;
    @Override
    public int setlayout() {
        return R.layout.fragment_live;
    }

    @Override
    protected void initView(View view) {
        webView= (WebView) view.findViewById(R.id.live_webview);

    }

    //直接webView打开网站
    @Override
    protected void initData() {
        webView.loadUrl("http://m.jd.com");
        //http://www.9xiu.com/rank
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }
}
