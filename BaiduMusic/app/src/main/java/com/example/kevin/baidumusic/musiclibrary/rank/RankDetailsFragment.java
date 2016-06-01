package com.example.kevin.baidumusic.musiclibrary.rank;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.example.kevin.baidumusic.MainActivity;
import com.example.kevin.baidumusic.R;
import com.example.kevin.baidumusic.base.BaseFragment;
import com.example.kevin.baidumusic.db.DBSongListCacheBean;
import com.example.kevin.baidumusic.db.LiteOrmSington;
import com.example.kevin.baidumusic.eventbean.EventRankDetailsPositionBen;
import com.example.kevin.baidumusic.netutil.NetListener;
import com.example.kevin.baidumusic.netutil.NetTool;
import com.example.kevin.baidumusic.util.RefreshListView;
import com.example.kevin.baidumusic.util.myinterface.OnRefreshListener;
import com.google.gson.Gson;
import com.litesuits.orm.LiteOrm;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 16/5/23.
 */
public class RankDetailsFragment extends BaseFragment implements OnRefreshListener{
    private RefreshListView refreshListView;
    private List<RankDetailsBean.SongListBean> songListBeen;
    private RankDetailsBean rankDetailsBean;
    private RankDetailsAdapter adapter;
    private int num;
    private ImageView ivRankdetailsBack;
    private View view;
    private ImageView ivLeRankDetailsHead;
    private String url;

    @Override
    public int setlayout() {
        return R.layout.fragment_rankdetails;
    }

    @Override
    protected void initView(View view) {
        refreshListView = (RefreshListView) view.findViewById(R.id.rankdetails_listview);
        ivRankdetailsBack = (ImageView) view.findViewById(R.id.iv_rankdetails_back);

        ivRankdetailsBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
    }

    public void headView() {

        view = LayoutInflater.from(context).inflate(R.layout.head_rankdetails, null);
        ivLeRankDetailsHead = (ImageView) view.findViewById(R.id.iv_le_rankdetails_head);
    }


    @Override
    protected void initData() {

         url = getArguments().getString("url");
        adapter = new RankDetailsAdapter(context);

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

                EventBus.getDefault().post(new EventRankDetailsPositionBen(position-2));
                EventBus.getDefault().post(rankDetailsBean);
                LiteOrm liteOrm= LiteOrmSington.getInstance().getLiteOrm();
                liteOrm.deleteAll(DBSongListCacheBean.class);
                ((MainActivity)getActivity()).send().setSelected(true);
            }
        });

    }

    @Override
    public void onDestroy() {
        //返回上一层 刷新页面
        ((MainActivity) getActivity()).showTitleFragment();
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    //下拉刷新
    public void onDownPullRefresh(){
        initResolve(url);
    }
    //上拉加载
    @Override
    public void onLoadingMore() {
        initResolve(url);
    }
    public void initResolve(final String url){
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
