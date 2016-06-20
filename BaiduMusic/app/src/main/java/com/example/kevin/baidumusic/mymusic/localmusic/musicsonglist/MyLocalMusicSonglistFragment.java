package com.example.kevin.baidumusic.mymusic.localmusic.musicsonglist;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kevin.baidumusic.MainActivity;
import com.example.kevin.baidumusic.R;
import com.example.kevin.baidumusic.base.BaseFragment;
import com.example.kevin.baidumusic.db.DBHeart;
import com.example.kevin.baidumusic.db.DBSongListCacheBean;
import com.example.kevin.baidumusic.db.DBUtilsHelper;
import com.example.kevin.baidumusic.db.LiteOrmSington;
import com.example.kevin.baidumusic.eventbean.EventPosition;
import com.example.kevin.baidumusic.musiclibrary.rank.RankDetailsOnClickListener;
import com.example.kevin.baidumusic.netutil.ShowShare;
import com.example.kevin.baidumusic.util.LocalMusic;
import com.example.kevin.baidumusic.util.MusicUtils;
import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.assit.QueryBuilder;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 16/5/22.
 */
public class MyLocalMusicSonglistFragment extends BaseFragment {
    private MyLocalMusicSongListAdapter adapter;
    private ListView listView;
    private List<LocalMusic> localMusics;
    private PopupWindow popupWindow;
    private RelativeLayout relativeLayout;
    private DBUtilsHelper dbUtilsHelper=new DBUtilsHelper();

    @Override
    public int setlayout() {
        return R.layout.fragment_mylocalmusicsong;
    }

    @Override
    protected void initView(View view) {
        listView = (ListView) view.findViewById(R.id.my_localmusic_songlist_listview);
        relativeLayout= (RelativeLayout) view.findViewById(R.id.back_to_mylocalmusic);
    }

    @Override
    protected void initData() {
        localMusics = new ArrayList<>();
        adapter = new MyLocalMusicSongListAdapter(context);
        MusicUtils.scanMusic(context, localMusics);
        adapter.setMusicList(localMusics);


        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EventBus.getDefault().post(new EventPosition(position));
                EventBus.getDefault().post(localMusics.get(position));
                dbUtilsHelper.deleteAll(DBSongListCacheBean.class);

                SharedPreferences sp = context.getSharedPreferences(context.getString(R.string.songposition), context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("position", position);
                editor.commit();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < localMusics.size(); i++) {
                            dbUtilsHelper.insertDB(new DBSongListCacheBean(localMusics.get(i).getTitle(), localMusics.get(i).getArtist(),
                                    null, null, localMusics.get(i).getFileName()));
                        }
                    }
                }).start();

            }
        });
        //长点击事件
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                showAlertDialog(localMusics.get(position).getUri(),position);

                return true;
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
                tvTitle.setText(localMusics.get(position).getTitle());
                contentView.findViewById(R.id.relativelayout_customer_dialog_other).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });

                final List<DBHeart> dbHeart=dbUtilsHelper.showQuery(DBHeart.class
                        ,DBHeart.TITLE,localMusics.get(position).getTitle());
                Log.d("RankDetailsFragment", "dbHeart:" + dbHeart.size());
                if (dbHeart.size()>0){
                    ivHart.setImageResource(R.mipmap.cust_heart_press);
                }
                ivHart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dbHeart.size()==0){
                            ivHart.setImageResource(R.mipmap.cust_heart_press);
                            DBHeart dbHeartBeen=new DBHeart(localMusics.get(position).getTitle(),
                                    localMusics.get(position).getAlbum(), null
                                    , null, localMusics.get(position).getUri());
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
                        Toast.makeText(context, context.getString(R.string.download_complete), Toast.LENGTH_SHORT).show();
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
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
    }

    @Override
    public void onDestroy() {
        if (!getActivity().isDestroyed()) {
            ((MainActivity) getActivity()).showTitleFragment();
        }
        super.onDestroy();
    }

    //删除事件
    public void showAlertDialog(final String path, final int position) {

        AlertDialog.Builder alert1 = new AlertDialog.Builder(context);

        //设置图标
        alert1.setIcon(R.mipmap.yuan);
        //设置标题
        alert1.setTitle(context.getString(R.string.del_song));
        //设置提示内容
        alert1.setMessage(context.getString(R.string.del_conofirm));

        alert1.setPositiveButton(context.getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                File file = new File(path);
                if (file.exists()) {
                    file.delete();
                    MusicUtils.delMusic(context,localMusics.get(position).getTitle());

                } else {
                    if (localMusics.get(position)!=null){
                        MusicUtils.delMusic(context,localMusics.get(position).getTitle());
                    }
                }
                MusicUtils.scanMusic(context, localMusics);
                adapter.setMusicList(localMusics);

                Toast.makeText(context, R.string.del_success, Toast.LENGTH_SHORT).show();
            }
        });

        alert1.setNegativeButton(context.getString(R.string.del_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(context, context.getString(R.string.del_cancel), Toast.LENGTH_SHORT).show();
            }
        });


        alert1.show();

    }

}
