package com.example.kevin.baidumusic.musiclibrary.radio.radioplay;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.volley.VolleyError;
import com.example.kevin.baidumusic.R;
import com.example.kevin.baidumusic.base.BaseFragment;
import com.example.kevin.baidumusic.musiclibrary.radio.radioplay.songplaylist.RadioPlayListActivity;
import com.example.kevin.baidumusic.netutil.NetListener;
import com.example.kevin.baidumusic.netutil.NetTool;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 16/6/2.
 */
public class RadioPlayFragment extends BaseFragment{
    private RecyclerView recyclerView;
    private RadioPlayRecyclerAdapter adapter;
    private List<RadioPlayBean.ResultBean> resultBeanList;

    @Override
    public int setlayout() {
        return R.layout.fragment_radioplay;
    }

    @Override
    protected void initView(View view) {
        recyclerView= (RecyclerView) view.findViewById(R.id.radioplay_recyclerview);
    }

    @Override
    protected void initData() {
        String url=getArguments().getString(context.getString(R.string.radioplay_url));
        adapter=new RadioPlayRecyclerAdapter(context);
        GridLayoutManager manager=new GridLayoutManager(context,3);
        manager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);

        NetTool netTool=new NetTool();
        netTool.getUrl(new NetListener() {
            @Override
            public void onSuccessed(String result) {
                Gson gson=new Gson();
                RadioPlayBean bean=gson.fromJson(result,RadioPlayBean.class);
                resultBeanList=new ArrayList<>();
                resultBeanList=bean.getResult();
                adapter.setResultBeanList(resultBeanList);
                recyclerView.setAdapter(adapter);
                adapter.setClickListener(new RadioPlayRecyclerViewOnClickListener() {
                    @Override
                    public void onRadioPlayRecyclerViewCliclListener(int position) {
                        Intent intent=new Intent(getActivity(),RadioPlayListActivity.class);
                        intent.putExtra(context.getString(R.string.sceneid),resultBeanList.get(position).getScene_id());
                        intent.putExtra(context.getString(R.string.scenename),resultBeanList.get(position).getScene_name());
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onFailed(VolleyError error) {

            }
        },url);
    }
}
