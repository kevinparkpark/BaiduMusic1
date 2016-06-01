package com.example.kevin.baidumusic.musiclibrary.mv;

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
public class MvFragment extends BaseFragment{
    private RecyclerView recyclerView;
    private MvAdapter adapter;
    private List<MvBean.ResultBean.MvListBean> mvListBeen;


    @Override
    public int setlayout() {
        return R.layout.fragment_le_mv;
    }

    @Override
    protected void initView(View view) {
        recyclerView= (RecyclerView) view.findViewById(R.id.mv_recyclerview);
    }

    @Override
    protected void initData() {
        NetTool netTool=new NetTool();
        netTool.getUrl(new NetListener() {
            @Override
            public void onSuccessed(String result) {
                Gson gson=new Gson();
                MvBean mvBean=gson.fromJson(result,MvBean.class);
                mvListBeen =new ArrayList<>();
                mvListBeen =mvBean.getResult().getMv_list();
                adapter=new MvAdapter(context);
                adapter.setMvListBeen(mvListBeen);
                GridLayoutManager manager = new GridLayoutManager(context, 2);
                manager.setOrientation(GridLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(manager);
                recyclerView.setAdapter(adapter);

//                adapter.setClickListener(new MvRecyclerViewOnClickListener() {
//                    @Override
//                    public void onMvClick(int position) {
//
//                    }
//                });
            }

            @Override
            public void onFailed(VolleyError error) {

            }
        }, URLValues.MVLIST);
    }
}
