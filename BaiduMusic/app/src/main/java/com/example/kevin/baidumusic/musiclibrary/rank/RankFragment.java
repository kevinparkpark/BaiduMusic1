package com.example.kevin.baidumusic.musiclibrary.rank;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
    private ImageView ivError;
    private ProgressBar progressBar;

    @Override
    public int setlayout() {
        return R.layout.fragment_le_rank;
    }

    @Override
    protected void initView(View view) {
        listView = (ListView) view.findViewById(R.id.rank_listview);
        ivError= (ImageView) view.findViewById(R.id.iv_le_rank_load_error);
        ivError.setVisibility(View.GONE);
        progressBar= (ProgressBar) view.findViewById(R.id.progressbar_le_rank);
        progressBar.setVisibility(View.GONE);
        ivError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivError.setVisibility(View.GONE);
                initData();
            }
        });
    }

    @Override
    protected void initData() {
        progressBar.setVisibility(View.VISIBLE);
        NetTool netTool = new NetTool();
        netTool.getLeRank(new NetListener() {
            @Override
            public void onSuccessed(String result) {
                Gson gson = new Gson();
                RankBean rankBean = gson.fromJson(result, RankBean.class);

                contentBeans = new ArrayList<>();
                contentBeans = rankBean.getContent();

                adapter = new RankAdapter(context);
                adapter.setContentBeans(contentBeans);
                progressBar.setVisibility(View.GONE);
                listView.setAdapter(adapter);
            }

            @Override
            public void onFailed(VolleyError error) {
                progressBar.setVisibility(View.GONE);
            ivError.setVisibility(View.VISIBLE);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ((RankFragment.rankToOnItemListener)getActivity()).onRankToItemListener(
                        contentBeans.get(position).getType(),contentBeans.get(position).getPic_s192());

            }
        });
    }

    public interface rankToOnItemListener {
        void onRankToItemListener(int count, String url);
    }
}
