package com.example.kevin.baidumusic.kmusic.authordetails.songlist;

import android.view.View;

import com.android.volley.VolleyError;
import com.example.kevin.baidumusic.R;
import com.example.kevin.baidumusic.base.SecBaseFragment;
import com.example.kevin.baidumusic.netutil.NetListener;
import com.example.kevin.baidumusic.netutil.NetTool;
import com.example.kevin.baidumusic.netutil.URLValues;
import com.example.kevin.baidumusic.util.RefreshListView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 16/6/1.
 */
public class AuthorDetailsSonglistFragment extends SecBaseFragment{
    private String tingUid;//歌手id
    private RefreshListView refreshListView;
    private AuthorDetailsSonglistAdapter adapter;
    private List<AuthorDetailsSonglistBean.SonglistBean> songlistBeanList;

    @Override
    public int setlayout() {
        return R.layout.fragment_authordetails_songlist;
    }

    @Override
    protected void initView(View view) {
        refreshListView= (RefreshListView) view.findViewById(R.id.authordetails_songlist_refreshlistview);

    }

    @Override
    protected void initData() {
        adapter=new AuthorDetailsSonglistAdapter(context);
        songlistBeanList=new ArrayList<>();

        tingUid =getArguments().getString("tinguid");
        NetTool netTool=new NetTool();
        netTool.getUrl(new NetListener() {
            @Override
            public void onSuccessed(String result) {
                Gson gson=new Gson();
                AuthorDetailsSonglistBean bean=gson.fromJson(result,AuthorDetailsSonglistBean.class);
                songlistBeanList.addAll(bean.getSonglist());
                adapter.setSonglistBeanList(songlistBeanList);
                refreshListView.setAdapter(adapter);
            }

            @Override
            public void onFailed(VolleyError error) {

            }
        }, URLValues.AUTHORDETAILS_SONGLIST_TINGUID1+ tingUid +URLValues.AUTHORDETAILS_SONGLIST_TINGUID2);
    }

    @Override
    public void onDestroy() {
//        ((MainActivity)getActivity()).showTitleFragment();
        super.onDestroy();
    }
}
