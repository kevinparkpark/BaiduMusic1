package com.example.kevin.baidumusic.musiclibrary.songmenu;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
public class SongMenuFragment extends BaseFragment {
    private SongMenuAdapter adapter;
    private List<SongMenuBean.ContentBean> contentBeanList;
    private SongMenuBean bean;
    private RecyclerView recyclerView;
    private PtrClassicFrameLayout ptrClassicFrameLayout;//recyclerviewhead类
    private RecyclerAdapterWithHF adapterWithHF;//recyclerview帮助类
    private int page=1;//网络加载首页

    @Override
    public int setlayout() {
        return R.layout.fragment_le_songmenu;
    }

    @Override
    protected void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.songmenu_recyclerview);
        ptrClassicFrameLayout= (PtrClassicFrameLayout) view.findViewById(R.id.ptrFramlayout);

    }

    @Override
    protected void initData() {
        adapter = new SongMenuAdapter(context);
        contentBeanList = new ArrayList<>();

        GridLayoutManager manager = new GridLayoutManager(context, 2);
        manager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        adapterWithHF=new RecyclerAdapterWithHF(adapter);
        recyclerView.setAdapter(adapterWithHF);
        ptrClassicFrameLayout.setPtrHandler(ptrDefaultHandler);
        ptrClassicFrameLayout.setOnLoadMoreListener(onLoadMoreListener);
        ptrClassicFrameLayout.setLoadMoreEnable(true);
        reFleshData();
        adapter.setDatas(contentBeanList);


        adapter.setClickListener(new SongMenuRecyclerViewOnClickListener() {
            @Override
            public void onSongMenuClick(int position) {
                ((songMenuToDetailsOnClickListener) getActivity())
                        .onSongMenuToDetailsClickListener(contentBeanList.get(position).getListid());
            }
        });

    }
    //第一次加载
    private PtrDefaultHandler ptrDefaultHandler=new PtrDefaultHandler() {
        @Override
        public void onRefreshBegin(PtrFrameLayout frame) {
            contentBeanList=new ArrayList<>();
            reFleshData();
        }
    };
    //loadmore
    private OnLoadMoreListener onLoadMoreListener=new OnLoadMoreListener() {
        @Override
        public void loadMore() {
            ++page;
            reFleshData();
        }
    };

    public interface songMenuToDetailsOnClickListener {
        void onSongMenuToDetailsClickListener(String position);
    }

    //刷新加载数据
    public void reFleshData(){
        NetTool netTool = new NetTool();
        netTool.getUrl(new NetListener() {
            @Override
            public void onSuccessed(String result) {
                Gson gson = new Gson();
                bean = gson.fromJson(result, SongMenuBean.class);

                contentBeanList.addAll(bean.getContent());
                adapter.setDatas(contentBeanList);
                adapterWithHF.notifyDataSetChanged();
                ptrClassicFrameLayout.refreshComplete();
                ptrClassicFrameLayout.loadMoreComplete(true);

                if (page>1){
                Toast.makeText(context, "加载完毕", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailed(VolleyError error) {

            }
        }, URLValues.LE_SONGMENU1+String.valueOf(page)+URLValues.LE_SONGMENU2);
    }

}
