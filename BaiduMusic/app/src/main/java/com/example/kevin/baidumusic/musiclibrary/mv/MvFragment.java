package com.example.kevin.baidumusic.musiclibrary.mv;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
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
public class MvFragment extends BaseFragment {
    private RecyclerView recyclerView;
    private MvAdapter adapter;
    private List<MvBean.ResultBean.MvListBean> mvListBeen;
    private PtrClassicFrameLayout ptrClassicFrameLayout;
    private RecyclerAdapterWithHF recyclerAdapterWithHF;
    private int page = 1;//第一页


    @Override
    public int setlayout() {
        return R.layout.fragment_le_mv;
    }

    @Override
    protected void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.mv_recyclerview);
        ptrClassicFrameLayout = (PtrClassicFrameLayout) view.findViewById(R.id.mv_framelayout);
    }

    @Override
    protected void initData() {
        adapter = new MvAdapter(context);
        mvListBeen = new ArrayList<>();
        GridLayoutManager manager = new GridLayoutManager(context, 2);
        manager.setOrientation(GridLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(manager);
        recyclerAdapterWithHF = new RecyclerAdapterWithHF(adapter);
        recyclerView.setAdapter(recyclerAdapterWithHF);
        ptrClassicFrameLayout.setPtrHandler(ptrDefaultHandler);
        ptrClassicFrameLayout.setOnLoadMoreListener(onLoadMoreListener);
        ptrClassicFrameLayout.setLoadMoreEnable(true);
        reFresh();
//        adapter.setMvListBeen(mvListBeen);
    }

    public void reFresh() {
        NetTool netTool = new NetTool();
        netTool.getUrl(new NetListener() {
            @Override
            public void onSuccessed(String result) {
                Log.d("MvFragment", "------------>" + result);
                Gson gson = new Gson();
                MvBean mvBean = gson.fromJson(result, MvBean.class);
                if (mvBean.getResult() == null) {
                    Toast.makeText(context, "网络有问题", Toast.LENGTH_SHORT).show();
                } else {
                    mvListBeen.addAll(mvBean.getResult().getMv_list());
                    adapter.setMvListBeen(mvListBeen);

                    recyclerAdapterWithHF.notifyDataSetChanged();
                    ptrClassicFrameLayout.refreshComplete();
                    ptrClassicFrameLayout.loadMoreComplete(true);
                    if (page > 1) {
                        Toast.makeText(context, "加载完毕", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailed(VolleyError error) {

            }
        }, URLValues.MVLIST1 + String.valueOf(page) + URLValues.MVLIST2);
    }

    private PtrDefaultHandler ptrDefaultHandler = new PtrDefaultHandler() {
        @Override
        public void onRefreshBegin(PtrFrameLayout frame) {
            mvListBeen = new ArrayList<>();
            reFresh();
        }
    };
    private OnLoadMoreListener onLoadMoreListener = new OnLoadMoreListener() {
        @Override
        public void loadMore() {
            ++page;
            reFresh();
        }
    };
}
