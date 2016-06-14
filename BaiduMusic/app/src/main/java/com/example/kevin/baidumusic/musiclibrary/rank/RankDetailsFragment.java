package com.example.kevin.baidumusic.musiclibrary.rank;

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
 * Created by kevin on 16/5/23.
 */
public class RankDetailsFragment extends BaseFragment implements OnRefreshListener {
    private RefreshListView refreshListView;
    private List<RankDetailsBean.SongListBean> songListBeen;
    private RankDetailsBean rankDetailsBean;
    private RankDetailsAdapter adapter;
    private int num;
    private ImageView ivRankdetailsBack;
    private View view;
    private ImageView ivLeRankDetailsHead;
    private String url;
    private PopupWindow popupWindow;
    private LiteOrm liteOrm;

    @Override
    public int setlayout() {
        return R.layout.fragment_rankdetails;
    }

    @Override
    protected void initView(View view) {
        //自定义listview
        refreshListView = (RefreshListView) view.findViewById(R.id.rankdetails_listview);
        ivRankdetailsBack = (ImageView) view.findViewById(R.id.iv_rankdetails_back);

        ivRankdetailsBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
    }
    //添加头部
    public void headView() {

        view = LayoutInflater.from(context).inflate(R.layout.head_rankdetails, null);
        ivLeRankDetailsHead = (ImageView) view.findViewById(R.id.iv_le_rankdetails_head);
    }


    @Override
    protected void initData() {

        url = getArguments().getString("url");
        adapter = new RankDetailsAdapter(context);
        liteOrm = LiteOrmSington.getInstance().getLiteOrm();

        initResolve(url);

        headView();
        Picasso.with(context).load(url).into(ivLeRankDetailsHead);
        //设置head
        refreshListView.addHeaderView(view, null, false);

        refreshListView.setAdapter(adapter);
        refreshListView.setOnRefreshListener(this);

        refreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final List<EventGenericBean> eventGenericBeen = new ArrayList<EventGenericBean>();

                liteOrm.deleteAll(DBSongListCacheBean.class);

                for (RankDetailsBean.SongListBean songListBean : rankDetailsBean.getSong_list()) {
                    EventGenericBean bean = new EventGenericBean(songListBean.getTitle(), songListBean.getAuthor(),
                            songListBean.getPic_small(), songListBean.getPic_big(), songListBean.getSong_id());
                    eventGenericBeen.add(bean);
                }
                EventBus.getDefault().post(new EventPosition(position - 2));
                EventBus.getDefault().post(eventGenericBeen);
                ((MainActivity) getActivity()).send().setSelected(true);

            }
        });
        //popupwindow
        adapter.setOnClickListener(new RankDetailsOnClickListener() {
            @Override
            public void onRankDetailsClickListener(final int position) {
                View contentView = LayoutInflater.from(context).inflate(R.layout.customer_dialog, null);
                popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT
                        , ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setFocusable(true);
                popupWindow.setBackgroundDrawable(new BitmapDrawable());
                popupWindow.setAnimationStyle(R.style.contextMenuAnim);
                popupWindow.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);

                final ImageView ivHart = (ImageView) contentView.findViewById(R.id.iv_customer_hart);
                ImageView ivDownload= (ImageView) contentView.findViewById(R.id.iv_customer_download);
                TextView tvTitle= (TextView) contentView.findViewById(R.id.tv_customer_dialog_title);
                tvTitle.setText(songListBeen.get(position).getTitle());
                contentView.findViewById(R.id.relativelayout_customer_dialog_other).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });

                final QueryBuilder<DBHeart> list = new QueryBuilder<DBHeart>(DBHeart.class).whereEquals
                        (DBHeart.TITLE, songListBeen.get(position).getTitle());

                if (liteOrm.query(list).size() > 0) {
                    ivHart.setImageResource(R.mipmap.cust_heart_press);
                }
                //红心点击事件
                ivHart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (liteOrm.query(list).size() == 0) {
                            ivHart.setImageResource(R.mipmap.cust_heart_press);
                            liteOrm.insert(new DBHeart(songListBeen.get(position).getTitle(),
                                    songListBeen.get(position).getAuthor(), songListBeen.get(position).getPic_small()
                                    , songListBeen.get(position).getPic_big(), songListBeen.get(position).getSong_id()));
                            popupWindow.dismiss();
                            Toast.makeText(context, "已添加到我喜欢的音乐", Toast.LENGTH_SHORT).show();
                        } else {
                            ivHart.setImageResource(R.mipmap.cust_dialog_hart);

                            QueryBuilder<DBHeart> list = new QueryBuilder<DBHeart>(DBHeart.class).whereEquals
                                    (DBHeart.TITLE, songListBeen.get(position).getTitle());

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
                        },songListBeen.get(position).getSong_id());
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
        //返回上一层 刷新页面
        if (!getActivity().isDestroyed()) {
            ((MainActivity) getActivity()).showTitleFragment();
        }
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    //下拉刷新
    public void onDownPullRefresh() {
        initResolve(url);
    }

    //上拉加载
    @Override
    public void onLoadingMore() {
        initResolve(url);
    }
    //加载数据
    public void initResolve(final String url) {
        NetTool netTool = new NetTool();
        netTool.getLeRankDetails(new NetListener() {
            @Override
            public void onSuccessed(String result) {
                Gson gson = new Gson();
                rankDetailsBean = gson.fromJson(result, RankDetailsBean.class);
                songListBeen = new ArrayList<>();
                songListBeen = rankDetailsBean.getSong_list();

                adapter.setSongListBeen(songListBeen);
                adapter.notifyDataSetChanged();
                refreshListView.hideFooterView();
                refreshListView.hideHeaderView();


            }

            @Override
            public void onFailed(VolleyError error) {

            }
        }, getArguments().getInt("count"));
    }

}
