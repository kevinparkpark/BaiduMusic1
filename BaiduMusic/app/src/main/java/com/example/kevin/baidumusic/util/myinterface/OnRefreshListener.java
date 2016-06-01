package com.example.kevin.baidumusic.util.myinterface;

/**
 * Created by dllo on 16/5/28.
 */
public interface OnRefreshListener {
    /**
     * 下拉刷新
     */
    void onDownPullRefresh();
    /**
     * 上拉加载更多
     */
    void onLoadingMore();
}
