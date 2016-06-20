package com.example.kevin.baidumusic.mymusic.heartsonglist;

import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.example.kevin.baidumusic.R;
import com.example.kevin.baidumusic.base.SecBaseFragment;
import com.example.kevin.baidumusic.db.DBHeart;
import com.example.kevin.baidumusic.db.DBSongListCacheBean;
import com.example.kevin.baidumusic.db.DBUtilsHelper;
import com.example.kevin.baidumusic.db.LiteOrmSington;
import com.example.kevin.baidumusic.eventbean.EventGenericBean;
import com.example.kevin.baidumusic.eventbean.EventPosition;
import com.example.kevin.baidumusic.kmusic.authordetails.songlist.AuthorDetailsSonglistOnClickListener;
import com.example.kevin.baidumusic.service.songplay.SongPlayBean;
import com.example.kevin.baidumusic.netutil.DownloadUtils;
import com.example.kevin.baidumusic.netutil.NetListener;
import com.example.kevin.baidumusic.netutil.NetTool;
import com.example.kevin.baidumusic.netutil.ShowShare;
import com.example.kevin.baidumusic.netutil.VolleySingleton;
import com.google.gson.Gson;
import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.assit.QueryBuilder;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 16/6/12.
 */
public class HeartSongListFragment extends SecBaseFragment {
    private HeartSonglistAdapter adapter;
    private List<DBHeart> dbHearts;
    private ListView listView;
    private TextView tvCount;
    private ImageView ivImg1, ivImg2, ivImg3;
    private LiteOrm liteOrm;
    private PopupWindow popupWindow;
    private SongPlayBean songPlayBean;
    private DBUtilsHelper dbUtilsHelper=new DBUtilsHelper();

    @Override
    public int setlayout() {
        return R.layout.fragment_heartsonglist;
    }

    @Override
    protected void initView(View view) {
        listView = (ListView) view.findViewById(R.id.heartsonglist_listview);
        tvCount = (TextView) view.findViewById(R.id.tv_heartsonglist_totalcount);
        ivImg1 = (ImageView) view.findViewById(R.id.iv_heartsonglist_img1);
        ivImg2 = (ImageView) view.findViewById(R.id.iv_heartsonglist_img2);
        ivImg3 = (ImageView) view.findViewById(R.id.iv_heartsonglist_img3);
    }

    @Override
    protected void initData() {
        adapter = new HeartSonglistAdapter(context);

        dbHearts = dbUtilsHelper.queryAll(DBHeart.class);
        loadImg(dbHearts);

        tvCount.setText(dbHearts.size() + context.getString(R.string.song));

        adapter.setDbHearts(dbHearts);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                List<EventGenericBean> eventGenericBeen = new ArrayList<EventGenericBean>();

                dbUtilsHelper.deleteAll(DBSongListCacheBean.class);

                for (DBHeart dbHeart : dbHearts) {
                    EventGenericBean been = new EventGenericBean(dbHeart.getTitle(), dbHeart.getAuthor()
                            , dbHeart.getImageUrl(), dbHeart.getImageBigUrl(), dbHeart.getSongId());
                    eventGenericBeen.add(been);
                }
                EventBus.getDefault().post(new EventPosition(position));
                EventBus.getDefault().post(eventGenericBeen);
            }
        });
        //popupwindow监听
        adapter.setOnClickListener(new AuthorDetailsSonglistOnClickListener() {
            @Override
            public void onAuthorDetailsSonglistClickListener(final int position) {
                View contentView = LayoutInflater.from(context).inflate(R.layout.customer_dialog, null);
                popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT
                        , ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setFocusable(true);
                popupWindow.setBackgroundDrawable(new BitmapDrawable());
                popupWindow.setAnimationStyle(R.style.contextMenuAnim);
                popupWindow.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);

                final ImageView ivHart = (ImageView) contentView.findViewById(R.id.iv_customer_hart);
                ImageView ivDownload = (ImageView) contentView.findViewById(R.id.iv_customer_download);
                TextView tvTitle = (TextView) contentView.findViewById(R.id.tv_customer_dialog_title);
                ImageView ivShare = (ImageView) contentView.findViewById(R.id.iv_customer_Share);
                tvTitle.setText(dbHearts.get(position).getTitle());

                contentView.findViewById(R.id.relativelayout_customer_dialog_other).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });

                final List<DBHeart> dbHeart=dbUtilsHelper.showQuery(DBHeart.class
                        ,DBHeart.TITLE,dbHearts.get(position).getTitle());
                Log.d("RankDetailsFragment", "dbHeart:" + dbHeart.size());
                if (dbHeart.size()>0){
                    ivHart.setImageResource(R.mipmap.cust_heart_press);
                }
                ivHart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dbHeart.size()==0){
                            ivHart.setImageResource(R.mipmap.cust_heart_press);
                            DBHeart dbHeartBeen=new DBHeart(dbHearts.get(position).getTitle(),
                                    dbHearts.get(position).getAuthor(), dbHearts.get(position).getImageUrl()
                                    , dbHearts.get(position).getImageBigUrl(), dbHearts.get(position).getSongId());
                            dbUtilsHelper.insertDB(dbHeartBeen);
                            popupWindow.dismiss();
                            Toast.makeText(context, context.getString(R.string.add_to_heart), Toast.LENGTH_SHORT).show();
                        }else {
                            ivHart.setImageResource(R.mipmap.cust_dialog_hart);
                            dbUtilsHelper.delete(dbHeart);
                            popupWindow.dismiss();
                            Toast.makeText(context, context.getString(R.string.del_heart), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                //歌曲下载
                ivDownload.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        NetTool netTool = new NetTool();
                        netTool.getDetailsSongUrl(new NetListener() {
                            @Override
                            public void onSuccessed(String result) {
                                Gson gson = new Gson();
                                result = result.replace("(", "");
                                result = result.replace(")", "");
                                result = result.replace(";", "");
                                songPlayBean = new SongPlayBean();
                                songPlayBean = gson.fromJson(result, SongPlayBean.class);
                                String songUrl = songPlayBean.getBitrate().getFile_link();
                                String lrc = songPlayBean.getSonginfo().getLrclink();
                                String songTitle = songPlayBean.getSonginfo().getTitle();
                                //下载歌曲
                                DownloadUtils downloadUtils = new DownloadUtils(songUrl, songTitle, lrc);
                                popupWindow.dismiss();
                            }

                            @Override
                            public void onFailed(VolleyError error) {

                            }
                        }, dbHearts.get(position).getSongId());
                    }
                });
                //分享
                ivShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ShowShare showShare = new ShowShare();
                        showShare.showShare();
                        popupWindow.dismiss();
                    }
                });
            }
        });
    }

    //title图片
    public void loadImg(List<DBHeart> dbHearts) {
        ImageLoader imageLoader = VolleySingleton.getInstance().getImageLoader();
        if (dbHearts.size() > 2) {
            if (dbHearts.get(dbHearts.size() - 1).getImageBigUrl() != null) {
                imageLoader.get(dbHearts.get(dbHearts.size() - 1).getImageBigUrl(), ImageLoader.getImageListener(
                        ivImg1, R.mipmap.default_live_ic, R.mipmap.default_live_ic));
            }
            if (dbHearts.get(dbHearts.size() - 2).getImageBigUrl() != null) {
                imageLoader.get(dbHearts.get(dbHearts.size() - 2).getImageBigUrl(), ImageLoader.getImageListener(
                        ivImg2, R.mipmap.default_live_ic, R.mipmap.default_live_ic));
            }
            if (dbHearts.get(dbHearts.size() - 3).getImageBigUrl() != null) {
                imageLoader.get(dbHearts.get(dbHearts.size() - 3).getImageBigUrl(), ImageLoader.getImageListener(
                        ivImg3, R.mipmap.default_live_ic, R.mipmap.default_live_ic));
            }
        } else if (dbHearts.size() == 2) {
            if (dbHearts.get(dbHearts.size() - 1).getImageBigUrl() != null) {
                imageLoader.get(dbHearts.get(dbHearts.size() - 1).getImageBigUrl(), ImageLoader.getImageListener(
                        ivImg1, R.mipmap.default_live_ic, R.mipmap.default_live_ic));
            }
            if (dbHearts.get(dbHearts.size() - 2).getImageBigUrl() != null) {
                imageLoader.get(dbHearts.get(dbHearts.size() - 2).getImageBigUrl(), ImageLoader.getImageListener(
                        ivImg2, R.mipmap.default_live_ic, R.mipmap.default_live_ic));
            }
            ivImg3.setImageResource(R.mipmap.default_live_ic);
        } else if (dbHearts.size() == 1) {
            if (dbHearts.get(dbHearts.size() - 1).getImageBigUrl() != null) {
                imageLoader.get(dbHearts.get(dbHearts.size() - 1).getImageBigUrl(), ImageLoader.getImageListener(
                        ivImg1, R.mipmap.default_live_ic, R.mipmap.default_live_ic));
            }
            ivImg2.setImageResource(R.mipmap.default_live_ic);
            ivImg3.setImageResource(R.mipmap.default_live_ic);
        } else if (dbHearts.size() == 0) {
            ivImg1.setImageResource(R.mipmap.default_live_ic);
            ivImg2.setImageResource(R.mipmap.default_live_ic);
            ivImg3.setImageResource(R.mipmap.default_live_ic);
        }
    }

}
