package com.example.kevin.baidumusic.netutil;

/**
 * Created by kevin on 16/5/23.
 */
public final class URLValues {

    public static final String RECOMMEND_BANNERS="http://tingapi.ting.baidu.com/v1/restserver/ting?" +
            "method=baidu.ting.plaza.getFocusPic&format=json&from=ios&version=5.2.3&from=ios&channel=appstore";
    public static final String LE_NEWRANK="http://tingapi.ting.baidu.com/v1/restserver/ting?" +
            "method=baidu.ting.billboard.billCategory&format=json&from=ios&version=5.2.1&" +
            "from=ios&channel=appstore";
    public static final String LE_RANKDETAILS1="http://tingapi.ting.baidu.com/v1/restserver/ting" +
            "?method=baidu.ting.billboard.billList&type=";
    public static final String LE_RANKDETAILS2="&format=json&offset=0&size=50&from=ios&fields=" +
            "title,song_id,author,resource_type,havehigh,is_new,has_mv_mobile,album_title," +
            "ting_uid,album_id,charge,all_rate&version=5.2.1&from=ios&channel=appstore";

    public static final String LE_RANKDETAILS_SONGURL1="http://tingapi.ting.baidu.com/v1/" +
            "restserver/ting?from=webapp_music&method=baidu.ting.song.play&format=json&callback=&songid=";
    public static final String LE_RANKDETAILS_SONGURL2="&_=1413017198449";

    public static final String LE_SONGMENU1="http://tingapi.ting.baidu.com/v1/restserver/ting?method" +
            "=baidu.ting.diy.gedan&page_no=";
    public static final String LE_SONGMENU2="&page_size=30&from=ios&version=5.2.3&from=ios&channel=" +
            "appstore";

    public static final String LE_SONGMENUDETAILS_LIST1="http://tingapi.ting.baidu.com/v1/restserver" +
            "/ting?method=baidu.ting.diy.gedanInfo&from=ios&listid=";
    public static final String LE_SONGMENUDETAILS_LIST2="&version=5.2.3&from=ios&channel=appstore";
    public static final String SEARCH1="http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu" +
            ".ting.search.merge&query=";
    public static final String SEARCH2="&page_size=50&page_no=1&type=-1&format=json&from=ios&" +
            "version=5.2.5&from=ios&channel=appstore";
    public static final String MVLIST="http://tingapi.ting.baidu.com/v1/restserver/ting?from=" +
            "android&version=5.7.3.0&channel=xiaomi&operator=3&provider=11%2C12&method=baidu." +
            "ting.mv.searchMV&format=json&order=1&page_num=1&page_size=20&query=全部";
    public static final String MV_DEMO="http://music.baidu.com/playmv/264730630";
}
