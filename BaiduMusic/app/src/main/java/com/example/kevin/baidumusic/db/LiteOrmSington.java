package com.example.kevin.baidumusic.db;

import com.example.kevin.baidumusic.R;
import com.example.kevin.baidumusic.MyApp;
import com.litesuits.orm.LiteOrm;

/**
 * Created by kevin on 16/5/27.
 */
public class LiteOrmSington {
    private static LiteOrmSington ourInstance = new LiteOrmSington();
    private LiteOrm liteOrm;

    public static LiteOrmSington getInstance() {

        return ourInstance;
    }

    private LiteOrmSington() {
                liteOrm=LiteOrm.newCascadeInstance(MyApp.context,MyApp.context.getString(R.string.liteorm_songurl));
    }

    public LiteOrm getLiteOrm() {
        return liteOrm;
    }
}
