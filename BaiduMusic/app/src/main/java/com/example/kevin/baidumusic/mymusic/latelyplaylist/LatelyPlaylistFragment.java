package com.example.kevin.baidumusic.mymusic.latelyplaylist;

import android.content.Intent;
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

import com.example.kevin.baidumusic.R;
import com.example.kevin.baidumusic.base.SecBaseFragment;
import com.example.kevin.baidumusic.db.DBHeart;
import com.example.kevin.baidumusic.db.DBSongListCacheBean;
import com.example.kevin.baidumusic.db.DBSongPlayListBean;
import com.example.kevin.baidumusic.db.DBUtilsHelper;
import com.example.kevin.baidumusic.db.LiteOrmSington;
import com.example.kevin.baidumusic.eventbean.EventPosition;
import com.example.kevin.baidumusic.musiclibrary.rank.RankDetailsOnClickListener;
import com.example.kevin.baidumusic.netutil.ShowShare;
import com.example.kevin.baidumusic.util.BroadcastValues;
import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.assit.QueryBuilder;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by kevin on 16/6/5.
 */
public class LatelyPlaylistFragment extends SecBaseFragment{
    private ListView listView;
    private LatelyPlaylistAdapter adapter;
    private List<DBSongPlayListBean> dbSongPlayListBean;
    private ImageView ivBack;
    private PopupWindow popupWindow;
    private DBUtilsHelper dbUtilsHelper=new DBUtilsHelper();

    @Override
    public int setlayout() {
        return R.layout.fragment_latelyplaylist;
    }

    @Override
    protected void initView(View view) {
        listView= (ListView) view.findViewById(R.id.my_latelyplaylist_listview);
        ivBack= (ImageView) view.findViewById(R.id.iv_my_latelyplaylist_back);
    }

    @Override
    protected void initData() {
        dbSongPlayListBean=dbUtilsHelper.queryAll(DBSongPlayListBean.class);
        adapter=new LatelyPlaylistAdapter(context);
        adapter.setBeen(dbSongPlayListBean);
        listView.setAdapter(adapter);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                dbUtilsHelper.deleteAll(DBSongListCacheBean.class);
                for (int i = 0; i < dbSongPlayListBean.size(); i++) {

                dbUtilsHelper.insertDB(new DBSongListCacheBean(dbSongPlayListBean.get(i).getTitle(),
                        dbSongPlayListBean.get(i).getAuthor(),dbSongPlayListBean.get(i).getPicUrl(),
                        dbSongPlayListBean.get(i).getPicBigUrl(),dbSongPlayListBean.get(i).getSongId()));
                }
                //previous方法-- 所以position+1
                EventBus.getDefault().post(new EventPosition(position+1));
                context.sendBroadcast(new Intent(BroadcastValues.PREVIOUS));
            }
        });
        adapter.setOnClickListener(new RankDetailsOnClickListener() {
            @Override
            public void onRankDetailsClickListener(final int position) {
                View contentView = LayoutInflater.from(context).inflate(R.layout.customer_dialog, null);
                popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT
                        , ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setFocusable(true);
                popupWindow.setAnimationStyle(R.style.contextMenuAnim);
                popupWindow.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);

                final ImageView ivHart = (ImageView) contentView.findViewById(R.id.iv_customer_hart);
                ImageView ivDownload = (ImageView) contentView.findViewById(R.id.iv_customer_download);
                TextView tvTitle = (TextView) contentView.findViewById(R.id.tv_customer_dialog_title);
                tvTitle.setText(dbSongPlayListBean.get(position).getTitle());
                contentView.findViewById(R.id.relativelayout_customer_dialog_other).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });

                final List<DBHeart> dbHeart=dbUtilsHelper.showQuery(DBHeart.class
                        ,DBHeart.TITLE,dbSongPlayListBean.get(position).getTitle());
                if (dbHeart.size()>0){
                    ivHart.setImageResource(R.mipmap.cust_heart_press);
                }
                ivHart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dbHeart.size()==0){
                            ivHart.setImageResource(R.mipmap.cust_heart_press);
                            DBHeart dbHeartBeen=new DBHeart(dbSongPlayListBean.get(position).getTitle(),
                                    dbSongPlayListBean.get(position).getAuthor(), dbSongPlayListBean.get(position).getPicUrl()
                                    , dbSongPlayListBean.get(position).getPicBigUrl(), dbSongPlayListBean.get(position).getSongId());
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
                        Toast.makeText(context, R.string.download_now, Toast.LENGTH_SHORT).show();
                    }
                });
                //红心
                ImageView ivShare = (ImageView) contentView.findViewById(R.id.iv_customer_Share);
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

}
