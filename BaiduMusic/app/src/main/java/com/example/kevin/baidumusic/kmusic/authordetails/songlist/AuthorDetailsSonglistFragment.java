package com.example.kevin.baidumusic.kmusic.authordetails.songlist;

import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.kevin.baidumusic.R;
import com.example.kevin.baidumusic.base.SecBaseFragment;
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
import com.example.kevin.baidumusic.util.RefreshListView;
import com.example.kevin.baidumusic.util.myinterface.OnRefreshListener;
import com.google.gson.Gson;
import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 16/6/1.
 */
public class AuthorDetailsSonglistFragment extends SecBaseFragment implements OnRefreshListener {
    private String tingUid;//歌手id
    private String author, country, imgUrl;
    private RefreshListView refreshListView;
    private AuthorDetailsSonglistAdapter adapter;
    private List<AuthorDetailsSonglistBean.SonglistBean> songlistBeanList;
    private View view;
    private ImageView ivAuthorImg;
    private TextView tvAuthor, tvCountry;
    private PopupWindow popupWindow;

    @Override
    public int setlayout() {
        return R.layout.fragment_authordetails_songlist;
    }

    @Override
    protected void initView(View view) {
        refreshListView = (RefreshListView) view.findViewById(R.id.authordetails_songlist_refreshlistview);

    }

    public void headView() {

        view = LayoutInflater.from(context).inflate(R.layout.head_k_authordetailssonglist, null);
        ivAuthorImg = (ImageView) view.findViewById(R.id.iv_head_k_authordetails_songlist_img);
        tvAuthor = (TextView) view.findViewById(R.id.tv_head_k_authordetails_songlist_author);
        tvCountry = (TextView) view.findViewById(R.id.tv_head_k_authordetails_songlist_country);
    }

    @Override
    protected void initData() {
        tingUid = getArguments().getString("tinguid");
        author = getArguments().getString("author");
        country = getArguments().getString("country");
        imgUrl = getArguments().getString("imgurl");

        final LiteOrm liteOrm = LiteOrmSington.getInstance().getLiteOrm();
        headView();
        adapter = new AuthorDetailsSonglistAdapter(context);
        songlistBeanList = new ArrayList<>();

        Picasso.with(context).load(imgUrl).resize(250, 150).centerCrop().into(ivAuthorImg);
        tvAuthor.setText(author);
        tvCountry.setText(country);
        refreshListView.addHeaderView(view, null, false);

        refreshListView.setOnRefreshListener(this);

        NetTool netTool = new NetTool();
        netTool.getUrl(new NetListener() {
            @Override
            public void onSuccessed(String result) {
                Gson gson = new Gson();
                final AuthorDetailsSonglistBean bean = gson.fromJson(result, AuthorDetailsSonglistBean.class);
                songlistBeanList.addAll(bean.getSonglist());
                adapter.setSonglistBeanList(songlistBeanList);

                refreshListView.setAdapter(adapter);
                refreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        List<EventGenericBean> eventGenericBeen = new ArrayList<EventGenericBean>();
                        liteOrm.deleteAll(DBSongListCacheBean.class);

                        for (AuthorDetailsSonglistBean.SonglistBean songlistBean : songlistBeanList) {
                            EventGenericBean bean1 = new EventGenericBean(songlistBean.getTitle(), songlistBean.getAuthor(),
                                    songlistBean.getPic_small(), songlistBean.getPic_big(), songlistBean.getSong_id());
                            eventGenericBeen.add(bean1);

                        }
                        EventBus.getDefault().post(new EventPosition(position - 2));
                        EventBus.getDefault().post(eventGenericBeen);
                    }
                });
            }

            @Override
            public void onFailed(VolleyError error) {

            }
        }, URLValues.AUTHORDETAILS_SONGLIST_TINGUID1 + tingUid + URLValues.AUTHORDETAILS_SONGLIST_TINGUID2);
        //popupwindow 点击事件
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
//                backgroundAlpha(0.4f);
//                popupWindow.setOnDismissListener(new poponDismissListener());

                final ImageView ivHart = (ImageView) contentView.findViewById(R.id.iv_customer_hart);
                ImageView ivDownload= (ImageView) contentView.findViewById(R.id.iv_customer_download);
                //设置title
                TextView tvTi= (TextView) contentView.findViewById(R.id.tv_customer_dialog_title);
                tvTi.setText(songlistBeanList.get(position).getTitle());
                contentView.findViewById(R.id.relativelayout_customer_dialog_other).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });

                final QueryBuilder<DBHeart> list = new QueryBuilder<DBHeart>(DBHeart.class).whereEquals
                        (DBHeart.TITLE, songlistBeanList.get(position).getTitle());

                if (list != null && liteOrm.query(list).size() > 0) {
                    ivHart.setImageResource(R.mipmap.cust_heart_press);
                }
                //红心点击事件
                ivHart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (liteOrm.query(list).size() == 0) {
                            ivHart.setImageResource(R.mipmap.cust_heart_press);
                            liteOrm.insert(new DBHeart(songlistBeanList.get(position).getTitle(),
                                    songlistBeanList.get(position).getAuthor(), songlistBeanList.get(position).getPic_small()
                                    , songlistBeanList.get(position).getPic_big(), songlistBeanList.get(position).getSong_id()));
                            popupWindow.dismiss();
                            Toast.makeText(context, "已添加到我喜欢的音乐", Toast.LENGTH_SHORT).show();
                        } else {
                            ivHart.setImageResource(R.mipmap.cust_dialog_hart);

                            QueryBuilder<DBHeart> list = new QueryBuilder<DBHeart>(DBHeart.class).whereEquals
                                    (DBHeart.TITLE, songlistBeanList.get(position).getTitle());

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
                        },songlistBeanList.get(position).getSong_id());
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
//        ((MainActivity)getActivity()).showTitleFragment();
        super.onDestroy();
    }

    public void reFresh() {

    }

    @Override
    public void onDownPullRefresh() {
        refreshListView.hideHeaderView();
    }

    @Override
    public void onLoadingMore() {
        refreshListView.hideFooterView();
    }

    //设置背景透明度
//    public void backgroundAlpha(float bgAlpha) {
//        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
//        lp.alpha = bgAlpha; //0.0-1.0
//        getActivity().getWindow().setAttributes(lp);
//    }
//
//    class poponDismissListener implements PopupWindow.OnDismissListener {
//        @Override
//        public void onDismiss() {
//            backgroundAlpha(1.0f);
//        }
//    }
}
