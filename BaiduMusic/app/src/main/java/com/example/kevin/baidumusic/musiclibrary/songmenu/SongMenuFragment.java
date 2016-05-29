package com.example.kevin.baidumusic.musiclibrary.songmenu;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.volley.VolleyError;
import com.example.kevin.baidumusic.base.BaseFragment;
import com.example.kevin.baidumusic.R;
import com.example.kevin.baidumusic.netutil.NetListener;
import com.example.kevin.baidumusic.netutil.NetTool;
import com.example.kevin.baidumusic.netutil.URLValues;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 16/5/19.
 */
public class SongMenuFragment extends BaseFragment {
    private SongMenuAdapter adapter;
    private List<SongMenuBean.ContentBean> contentBeanList;
    private RecyclerView recyclerView;

    @Override
    public int setlayout() {
        return R.layout.fragment_le_songmenu;
    }

    @Override
    protected void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.songmenu_recyclerview);

    }

    @Override
    protected void initData() {
        NetTool netTool = new NetTool();
        netTool.getUrl(new NetListener() {
            @Override
            public void onSuccessed(String result) {
                Gson gson = new Gson();
                SongMenuBean bean = gson.fromJson(result, SongMenuBean.class);
                contentBeanList = new ArrayList<SongMenuBean.ContentBean>();
                contentBeanList = bean.getContent();
                adapter = new SongMenuAdapter(context);

                adapter.setDatas(contentBeanList);
                GridLayoutManager manager = new GridLayoutManager(context, 2);
                manager.setOrientation(GridLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(manager);

                recyclerView.setAdapter(adapter);

                adapter.setClickListener(new SongMenuRecyclerViewOnClickListener() {
                    @Override
                    public void onSongMenuClick(int position) {
                        ((SongMenuToDetailsOnClickListener) getActivity()).onSongMenuToDetailsClickListener(contentBeanList.get(position).getListid());
                    }
                });
            }

            @Override
            public void onFailed(VolleyError error) {

            }
        }, URLValues.LE_SONGMENU);

    }

    public interface SongMenuToDetailsOnClickListener {
        void onSongMenuToDetailsClickListener(String position);
    }
}
