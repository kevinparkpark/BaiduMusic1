package com.example.kevin.baidumusic.netutil;

import com.example.kevin.baidumusic.R;
import com.example.kevin.baidumusic.MyApp;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by kevin on 16/6/12.
 */
public class ShowShare {

    public void showShare() {
        ShareSDK.initSDK(MyApp.context);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(MyApp.context.getString(R.string.show_share));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(MyApp.context.getString(R.string.sharesdk_cn));

        oks.setImageUrl(MyApp.context.getString(R.string.showshare_imageurl));

        // text是分享文本，所有平台都需要这个字段
        oks.setText(MyApp.context.getString(R.string.showshare_share_text));
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(MyApp.context.getString(R.string.sharesdk_cn));
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment(MyApp.context.getString(R.string.showshare_text));
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(MyApp.context.getString(R.string.shwoshare_qq));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(MyApp.context.getString(R.string.sharesdk_cn));

// 启动分享GUI
        oks.show(MyApp.context);
    }

}
