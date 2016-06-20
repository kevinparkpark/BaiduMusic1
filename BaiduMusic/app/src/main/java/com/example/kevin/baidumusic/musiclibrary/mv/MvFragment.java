package com.example.kevin.baidumusic.musiclibrary.mv;

import android.content.Intent;
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
import com.example.kevin.baidumusic.MyApp;
import com.example.kevin.baidumusic.base.BaseFragment;
import com.example.kevin.baidumusic.R;
import com.example.kevin.baidumusic.netutil.NetListener;
import com.example.kevin.baidumusic.netutil.NetTool;
import com.example.kevin.baidumusic.netutil.URLValues;
import com.example.kevin.baidumusic.util.BroadcastValues;
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


    }
    //刷新方法
    public void reFresh() {
        final NetTool netTool = new NetTool();
        netTool.getUrl(new NetListener() {
            @Override
            public void onSuccessed(final String result) {
                final Gson gson = new Gson();
                final MvBean mvBean = gson.fromJson(result, MvBean.class);
                if (mvBean.getResult() == null) {
                    Toast.makeText(context, R.string.net_error, Toast.LENGTH_SHORT).show();
                } else {
                    mvListBeen.addAll(mvBean.getResult().getMv_list());
                    adapter.setMvListBeen(mvListBeen);

                    recyclerAdapterWithHF.notifyDataSetChanged();
                    ptrClassicFrameLayout.refreshComplete();
                    ptrClassicFrameLayout.loadMoreComplete(true);
                    if (page > 1) {
                        Toast.makeText(context, R.string.load_complete, Toast.LENGTH_SHORT).show();
                    }

                    recyclerAdapterWithHF.setOnItemClickListener(new RecyclerAdapterWithHF.OnItemClickListener() {
                        @Override
                        public void onItemClick(RecyclerAdapterWithHF adapter, RecyclerView.ViewHolder vh, final int position) {

                            //暂停音乐播放
                            context.sendBroadcast(new Intent(BroadcastValues.PAUSE));
                            NetTool netTool1 = new NetTool();
                            netTool1.getUrl(new NetListener() {
                                @Override
                                public void onSuccessed(String result) {
                                    Gson gson1 = new Gson();
                                    MvPlayBean mvPlayBean = gson1.fromJson(result, MvPlayBean.class);
                                    String str = mvPlayBean.getResult().getVideo_info().getSourcepath();
                                    int indexOf = str.indexOf(context.getString(R.string.www_yinyuetai));
                                    String substring=str.substring(31,str.length()-1);
//                                    String substring = str.substring(indexOf+1);
//                                    Uri uri = Uri.parse("http://www.yinyuetai.com/mv/video-url/"+substring);
//                                    Intent intent = new Intent(Intent.ACTION_VIEW);
//                                    intent.setDataAndType(uri,"video/3gp");
//                                    startActivity(intent);
                                    Intent intent=new Intent(MyApp.context,MvPlayActivity.class);
                                    intent.putExtra(context.getString(R.string.url),context.getString(R.string.www_yinyuetai_url)+substring);
                                    startActivity(intent);

                                }

                                @Override
                                public void onFailed(VolleyError error) {

                                }
                            }, URLValues.MV_PLAY1 + mvListBeen.get(position).getMv_id() + URLValues.MV_PLAY2);
                        }
                    });
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
    //上拉
    private OnLoadMoreListener onLoadMoreListener = new OnLoadMoreListener() {
        @Override
        public void loadMore() {
            ++page;
            reFresh();
        }
    };
}
