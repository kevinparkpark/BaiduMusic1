package com.example.kevin.baidumusic.kmusic.authordetails.songlist;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.example.kevin.baidumusic.R;
import com.example.kevin.baidumusic.base.SecBaseFragment;
import com.example.kevin.baidumusic.db.DBSongListCacheBean;
import com.example.kevin.baidumusic.db.LiteOrmSington;
import com.example.kevin.baidumusic.eventbean.EventGenericBean;
import com.example.kevin.baidumusic.eventbean.EventPosition;
import com.example.kevin.baidumusic.musiclibrary.radio.radioplay.songplaylist.RadioPlayListBean;
import com.example.kevin.baidumusic.netutil.NetListener;
import com.example.kevin.baidumusic.netutil.NetTool;
import com.example.kevin.baidumusic.netutil.URLValues;
import com.example.kevin.baidumusic.util.RefreshListView;
import com.example.kevin.baidumusic.util.myinterface.OnRefreshListener;
import com.google.gson.Gson;
import com.litesuits.orm.LiteOrm;
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
//                        EventBus.getDefault().post(new EventPosition(position-2));
//                        EventBus.getDefault().post(bean);
//                        LiteOrm liteOrm= LiteOrmSington.getInstance().getLiteOrm();
//                        liteOrm.deleteAll(DBSongListCacheBean.class);
                        List<EventGenericBean> eventGenericBeen = new ArrayList<EventGenericBean>();

                        final LiteOrm liteOrm = LiteOrmSington.getInstance().getLiteOrm();
                        liteOrm.deleteAll(DBSongListCacheBean.class);

                        for (AuthorDetailsSonglistBean.SonglistBean songlistBean : songlistBeanList) {
                            EventGenericBean bean1 = new EventGenericBean(songlistBean.getTitle(), songlistBean.getAuthor(),
                                    songlistBean.getPic_small(), songlistBean.getPic_big(), songlistBean.getSong_id());
                            eventGenericBeen.add(bean1);

                        }

                        EventBus.getDefault().post(new EventPosition(position-2));
                        EventBus.getDefault().post(eventGenericBeen);
                    }
                });
            }

            @Override
            public void onFailed(VolleyError error) {

            }
        }, URLValues.AUTHORDETAILS_SONGLIST_TINGUID1 + tingUid + URLValues.AUTHORDETAILS_SONGLIST_TINGUID2);
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
}
