package com.example.kevin.baidumusic.musiclibrary.rank;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.example.kevin.baidumusic.base.BaseFragment;
import com.example.kevin.baidumusic.R;
import com.example.kevin.baidumusic.netutil.NetListener;
import com.example.kevin.baidumusic.netutil.NetTool;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 16/5/19.
 */
public class RankFragment extends BaseFragment {
    private ListView listView;
    private RankAdapter adapter;
    private List<RankBean.ContentBean> contentBeans;
    private RankToOnItemListener rankToOnItemListener;

    public void setRankToOnItemListener(RankToOnItemListener rankToOnItemListener) {
        this.rankToOnItemListener = rankToOnItemListener;
    }

    @Override
    public int setlayout() {
        return R.layout.fragment_le_rank;
    }

    @Override
    protected void initView(View view) {
        listView = (ListView) view.findViewById(R.id.rank_listview);
    }

    @Override
    protected void initData() {

        NetTool netTool = new NetTool();
        netTool.getLeRank(new NetListener() {
            @Override
            public void onSuccessed(String result) {
                Gson gson = new Gson();
                RankBean rankBean = gson.fromJson(result, RankBean.class);

                contentBeans = new ArrayList<RankBean.ContentBean>();
                contentBeans = rankBean.getContent();

                adapter = new RankAdapter(context);
                adapter.setContentBeans(contentBeans);
                listView.setAdapter(adapter);
            }

            @Override
            public void onFailed(VolleyError error) {

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ((RankToOnItemListener)getActivity()).onRankToItemListener(contentBeans.get(position).getType()
                ,contentBeans.get(position).getPic_s192());

            }
        });
    }

    public interface RankToOnItemListener{
        void onRankToItemListener(int count, String url);
    }
}
