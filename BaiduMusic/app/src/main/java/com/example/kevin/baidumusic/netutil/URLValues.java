package com.example.kevin.baidumusic.netutil;

/**
 * Created by kevin on 16/5/23.
 */
public final class URLValues {

    public static final String RECOMMEND_BANNERS="http://tingapi.ting.baidu.com/v1/restserver/ting?" +
            "method=baidu.ting.plaza.getFocusPic&format=json&from=ios&version=5.2.3&from=ios&channel=appstore";
    public static final String RECOMMEND_SONGLIST="http://tingapi.ting.baidu.com/v1/restserver/ting?method=" +
            "baidu.ting.diy.getHotGeDanAndOfficial&num=6&version=5.2.3&from=ios&channel=appstore";
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
    public static final String MVLIST1="http://tingapi.ting.baidu.com/v1/restserver/ting?" +
            "from=android&version=5.7.3.0&channel=xiaomi&operator=3&provider=11%2C12&method=" +
            "baidu.ting.mv.searchMV&format=json&order=1&page_num=";
    public static final String MVLIST2="&page_size=20&query=全部";

    public static final String MV_PLAY1="http://tingapi.ting.baidu.com/v1/restserver/ting?from=android&version=5.7.3.0&channel=xiaomi&operator=3&provider=11%2C12&method=baidu.ting.mv.playMV&format=json&mv_id=";
    public static final String MV_PLAY2="&song_id=&definition=0";


    public static final String KMUSIC_BANNERS="http://tingapi.ting.baidu.com/v1/restserver/" +
            "ting?method=baidu.ting.artist.getList&format=json&order=1&limit=6&offset=0&area=0&" +
            "sex=0&abc=&from=ios&version=5.2.1&from=ios&channel=appstore";

    public static final String KMUSIC_CH_MAN_AUTHOR_LIST1="http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.artist.getList&format=json&order=";
    public static final String KMUSIC_CH_MAN_AUTHOR_LIST2="&limit=50&offset=0&area=6&sex=1&abc=热门&from=ios&version=5.2.5&from=ios&channel=appstore";
    public static final String KMUSIC_CH_WOMAN_AUTHOR_LIST1="http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.artist.getList&format=json&order=";
    public static final String KMUSIC_CH_WOMAN_AUTHOR_LIST2="&limit=50&offset=0&area=6&sex=2&abc=热门&from=ios&version=5.2.5&from=ios&channel=appstore";
    public static final String KMUSIC_CH_GROUP_AUTHOR_LIST1="http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.artist.getList&format=json&order=";
    public static final String KMUSIC_CH_GROUP_AUTHOR_LIST2="&limit=50&offset=0&area=6&sex=3&abc=热门&from=ios&version=5.2.5&from=ios&channel=appstore";

    public static final String KMUSIC_EURO_MAN_AUTHOR_LIST1="http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.artist.getList&format=json&order=";
    public static final String KMUSIC_EURO_MAN_AUTHOR_LIST2="&limit=50&offset=0&area=3&sex=1&abc=热门&from=ios&version=5.2.5&from=ios&channel=appstore";
    public static final String KMUSIC_EURO_WOMAN_AUTHOR_LIST1="http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.artist.getList&format=json&order=";
    public static final String KMUSIC_EURO_WOMAN_AUTHOR_LIST2="&limit=50&offset=0&area=3&sex=2&abc=热门&from=ios&version=5.2.5&from=ios&channel=appstore";
    public static final String KMUSIC_EURO_GROUP_AUTHOR_LIST1="http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.artist.getList&format=json&order=";
    public static final String KMUSIC_EURO_GROUP_AUTHOR_LIST2="&limit=50&offset=0&area=3&sex=3&abc=热门&from=ios&version=5.2.5&from=ios&channel=appstore";

    public static final String KMUSIC_KR_MAN_AUTHOR_LIST1="http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.artist.getList&format=json&order=";
    public static final String KMUSIC_KR_MAN_AUTHOR_LIST2="&limit=50&offset=0&area=7&sex=1&abc=热门&from=ios&version=5.2.5&from=ios&channel=appstore";
    public static final String KMUSIC_KR_WOMAN_AUTHOR_LIST1="http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.artist.getList&format=json&order=";
    public static final String KMUSIC_KR_WOMAN_AUTHOR_LIST2="&limit=50&offset=0&area=7&sex=2&abc=热门&from=ios&version=5.2.5&from=ios&channel=appstore";
    public static final String KMUSIC_KR_GROUP_AUTHOR_LIST1="http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.artist.getList&format=json&order=";
    public static final String KMUSIC_KR_GROUP_AUTHOR_LIST2="&limit=50&offset=0&area=7&sex=3&abc=热门&from=ios&version=5.2.5&from=ios&channel=appstore";

    public static final String KMUSIC_JP_MAN_AUTHOR_LIST1="http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.artist.getList&format=json&order=";
    public static final String KMUSIC_JP_MAN_AUTHOR_LIST2="&limit=50&offset=0&area=60&sex=1&abc=热门&from=ios&version=5.2.5&from=ios&channel=appstore";
    public static final String KMUSIC_JP_WOMAN_AUTHOR_LIST1="http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.artist.getList&format=json&order=";
    public static final String KMUSIC_JP_WOMAN_AUTHOR_LIST2="&limit=50&offset=0&area=60&sex=2&abc=热门&from=ios&version=5.2.5&from=ios&channel=appstore";
    public static final String KMUSIC_JP_GROUP_AUTHOR_LIST1="http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.artist.getList&format=json&order=";
    public static final String KMUSIC_JP_GROUP_AUTHOR_LIST2="&limit=50&offset=0&area=60&sex=3&abc=热门&from=ios&version=5.2.5&from=ios&channel=appstore";

    public static final String KMUSIC_OTHER_AUTHOR_LIST1="http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.artist.getList&format=json&order=";
    public static final String KMUSIC_OTHER_AUTHOR_LIST2="&limit=50&offset=0&area=5&sex=4&abc=热门&from=ios&version=5.2.5&from=ios&channel=appstore";

    public static final String AUTHORDETAILS_SONGLIST_TINGUID1="http://tingapi.ting.baidu.com/v1/restserver/ting?from=android&version=5.7.3.0&channel=xiaomi&operator=3&method=baidu.ting.artist.getSongList&format=json&order=2&tinguid=";
    public static final String AUTHORDETAILS_SONGLIST_TINGUID2="&artistid=null&offset=0&limits=50";

    public static final String RADIOPLAY_EXERCISE="http://tingapi.ting.baidu.com/v1/restserver/ting/?method=baidu.ting.scene.getCategoryScene&category_id=0&version=5.2.5&from=ios&channel=appstore";
    public static final String RADIOPLAY_MOOD="http://tingapi.ting.baidu.com/v1/restserver/ting/?method=baidu.ting.scene.getCategoryScene&category_id=3&version=5.2.5&from=ios&channel=appstore";
    public static final String RADIOPLAY_THEME="http://tingapi.ting.baidu.com/v1/restserver/ting/?method=baidu.ting.scene.getCategoryScene&category_id=1&version=5.2.5&from=ios&channel=appstore";
    public static final String RADIOPLAY_LANGUAGE="http://tingapi.ting.baidu.com/v1/restserver/ting/?method=baidu.ting.scene.getCategoryScene&category_id=4&version=5.2.5&from=ios&channel=appstore";
    public static final String RADIOPLAY_YEARS="http://tingapi.ting.baidu.com/v1/restserver/ting/?method=baidu.ting.scene.getCategoryScene&category_id=5&version=5.2.5&from=ios&channel=appstore";
    public static final String RADIOPLAY_GENRE="http://tingapi.ting.baidu.com/v1/restserver/ting/?method=baidu.ting.scene.getCategoryScene&category_id=6&version=5.2.5&from=ios&channel=appstore";

    public static final String RADIOPLAYLIST_SCENE1="http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.song.getSmartSongList&page_no=1&page_size=50&scene_id=";
    public static final String RADIOPLAYLIST_SCENE2="&item_id=0&version=5.2.5&from=ios&channel=appstore";

}
