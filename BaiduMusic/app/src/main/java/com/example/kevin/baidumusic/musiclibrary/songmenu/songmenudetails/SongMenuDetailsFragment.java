package com.example.kevin.baidumusic.musiclibrary.songmenu.songmenudetails;

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
import com.example.kevin.baidumusic.MainActivity;
import com.example.kevin.baidumusic.R;
import com.example.kevin.baidumusic.base.BaseFragment;
import com.example.kevin.baidumusic.db.DBHeart;
import com.example.kevin.baidumusic.db.DBSongListCacheBean;
import com.example.kevin.baidumusic.db.LiteOrmSington;
import com.example.kevin.baidumusic.eventbean.EventGenericBean;
import com.example.kevin.baidumusic.eventbean.EventPosition;
import com.example.kevin.baidumusic.service.songplay.SongPlayBean;
import com.example.kevin.baidumusic.netutil.DownloadUtils;
import com.example.kevin.baidumusic.netutil.NetListener;
import com.example.kevin.baidumusic.netutil.NetTool;
import com.example.kevin.baidumusic.netutil.ShowShare;
import com.example.kevin.baidumusic.netutil.URLValues;
import com.google.gson.Gson;
import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 16/5/28.
 */
public class SongMenuDetailsFragment extends BaseFragment {
    private ListView listView;
    private SongMenuDetailsAdapter adapter;
    private List<SongMenuDetailsBean.ContentBean> contentBeanList;
    private View view;
    private ImageView ivSongMenuDetailsHeadImg;
    private TextView tvTitle, tvListNum, tvTag, tvCollectNum;
    private SongMenuDetailsBean bean;
    private PopupWindow popupWindow;
    private LiteOrm liteOrm;

    @Override
    public int setlayout() {
        return R.layout.fragment_songmenudetails;
    }

    @Override
    protected void initView(View view) {
        listView = (ListView) view.findViewById(R.id.songmenudetails_listview);
    }

    public void headView() {
        view = LayoutInflater.from(context).inflate(R.layout.head_songmenudetails, null);
        ivSongMenuDetailsHeadImg = (ImageView) view.findViewById(R.id.iv_songmenudetails_headbac);
        tvCollectNum = (TextView) view.findViewById(R.id.tv_songmenudetails_head_collectnum);
        tvListNum = (TextView) view.findViewById(R.id.tv_songmenudetails_head_listnum);
        tvTag = (TextView) view.findViewById(R.id.tv_songmenudetails_head_tag);
        tvTitle = (TextView) view.findViewById(R.id.tv_songmenudetails_title);
    }

    @Override
    protected void initData() {
        liteOrm = LiteOrmSington.getInstance().getLiteOrm();
        adapter = new SongMenuDetailsAdapter(context);
        String listId = getArguments().getString("listid");
        NetTool netTool = new NetTool();
        netTool.getUrlId(new NetListener() {
            @Override
            public void onSuccessed(String result) {
                Gson gson = new Gson();
                bean = gson.fromJson(result, SongMenuDetailsBean.class);
                contentBeanList = bean.getContent();
                adapter.setContentBeanList(contentBeanList);
                headView();
                Picasso.with(context).load(bean.getPic_500()).resize(1000, 700).
                        centerCrop().into(ivSongMenuDetailsHeadImg);
                tvTag.setText(bean.getTag());
                tvTitle.setText(bean.getTitle());
                tvListNum.setText(bean.getListenum());
                tvCollectNum.setText(bean.getCollectnum());
                listView.addHeaderView(view, null, false);
                listView.setAdapter(adapter);
            }

            @Override
            public void onFailed(VolleyError error) {

            }
        }, URLValues.LE_SONGMENUDETAILS_LIST1, listId, URLValues.LE_SONGMENUDETAILS_LIST2);

        //点击歌曲时播放并删除cache所有歌曲
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                List<EventGenericBean> eventGenericBeen = new ArrayList<EventGenericBean>();

                liteOrm.deleteAll(DBSongListCacheBean.class);
                for (SongMenuDetailsBean.ContentBean contentBean : bean.getContent()) {

                    EventGenericBean bean1 = new EventGenericBean(contentBean.getTitle(), contentBean.getAuthor(),
                            bean.getPic_300(), bean.getPic_500(), contentBean.getSong_id());
                    eventGenericBeen.add(bean1);
                }

                EventBus.getDefault().post(new EventPosition(position - 1));
                EventBus.getDefault().post(eventGenericBeen);
            }
        });
        //popupwindow
        adapter.setClickListener(new SongMenuDetailsOnClickListener() {
            @Override
            public void onSongMenuDetailsClickListener(final int position) {
                View contentView = LayoutInflater.from(context).inflate(R.layout.customer_dialog, null);
                popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT
                        , ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setFocusable(true);
                popupWindow.setBackgroundDrawable(new BitmapDrawable());
                popupWindow.setAnimationStyle(R.style.contextMenuAnim);
                popupWindow.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);

                final ImageView ivHart = (ImageView) contentView.findViewById(R.id.iv_customer_hart);
                ImageView ivDownload= (ImageView) contentView.findViewById(R.id.iv_customer_download);

                //设置title
                TextView tvTi= (TextView) contentView.findViewById(R.id.tv_customer_dialog_title);
                tvTi.setText(contentBeanList.get(position).getTitle());
                //点击其他地方隐藏
                contentView.findViewById(R.id.relativelayout_customer_dialog_other).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
                final QueryBuilder<DBHeart> list = new QueryBuilder<DBHeart>(DBHeart.class).whereEquals
                        (DBHeart.TITLE, contentBeanList.get(position).getTitle());

                if (list != null && liteOrm.query(list).size() > 0) {
                    ivHart.setImageResource(R.mipmap.cust_heart_press);
                }
                //红心收藏
                ivHart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (liteOrm.query(list).size() == 0) {
                            ivHart.setImageResource(R.mipmap.cust_heart_press);
                            liteOrm.insert(new DBHeart(contentBeanList.get(position).getTitle(),
                                    contentBeanList.get(position).getAuthor(), bean.getPic_300()
                                    , bean.getPic_500(), contentBeanList.get(position).getSong_id()));
                            popupWindow.dismiss();
                            Toast.makeText(context, "已添加到我喜欢的音乐", Toast.LENGTH_SHORT).show();
                        } else {
                            ivHart.setImageResource(R.mipmap.cust_dialog_hart);

                            QueryBuilder<DBHeart> list = new QueryBuilder<DBHeart>(DBHeart.class).whereEquals
                                    (DBHeart.TITLE, contentBeanList.get(position).getTitle());

                            List<DBHeart> dbHearts = liteOrm.query(list);
                            if (dbHearts.size() > 0) {
                                liteOrm.delete(dbHearts);
                            }
                            popupWindow.dismiss();
                            Toast.makeText(context, "已取消喜欢的音乐", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                //歌曲下载
                ivDownload.setOnClickListener(new View.OnClickListener() {
                    List<DBHeart> dbHeartList=liteOrm.query(DBHeart.class);
                    @Override
                    public void onClick(View v) {
                        NetTool netTool=new NetTool();
                        netTool.getDetailsSongUrl(new NetListener() {
                            @Override
                            public void onSuccessed(String result) {
                                Gson gson=new Gson();
                                result = result.replace("(", "");
                                result = result.replace(")", "");
                                result = result.replace(";", "");
                                SongPlayBean songPlayBean=new SongPlayBean();
                                songPlayBean=gson.fromJson(result,SongPlayBean.class);
                                String songUrl=songPlayBean.getBitrate().getFile_link();
                                String lrc=songPlayBean.getSonginfo().getLrclink();
                                String songTitle = songPlayBean.getSonginfo().getTitle();
                                Log.d("HeartSongListFragment","-------"+ songUrl);
                                //下载歌曲
                                DownloadUtils downloadUtils=new DownloadUtils(songUrl,songTitle,lrc);
                                popupWindow.dismiss();
                            }

                            @Override
                            public void onFailed(VolleyError error) {

                            }
                        },contentBeanList.get(position).getSong_id());
                    }
                });
                //分享
                ImageView ivShare= (ImageView) contentView.findViewById(R.id.iv_customer_Share);
                ivShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ShowShare showShare=new ShowShare();
                        showShare.showShare();
                        popupWindow.dismiss();
                    }
                });
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
}
