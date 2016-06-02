package com.example.kevin.baidumusic.kmusic.authordetails;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.example.kevin.baidumusic.MainActivity;
import com.example.kevin.baidumusic.R;
import com.example.kevin.baidumusic.base.SecBaseFragment;
import com.example.kevin.baidumusic.netutil.NetListener;
import com.example.kevin.baidumusic.netutil.NetTool;
import com.example.kevin.baidumusic.util.RefreshListView;
import com.example.kevin.baidumusic.util.myinterface.OnRefreshListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 16/6/1.
 */
public class AuthorDetailsFragment extends SecBaseFragment implements OnRefreshListener{
    private TextView tvTitle;
    private ImageView ivBack;
    private List<AuthorDetailsBean.ArtistBean> artistBeanList;
    private AuthorDeailsAdapter adapter;
    private String url1,url2;
    private RefreshListView refreshListView;
    private int page=1;

    @Override
    public int setlayout() {
        return R.layout.fragment_authordetails;
    }

    @Override
    protected void initView(View view) {
        tvTitle= (TextView) view.findViewById(R.id.tv_k_authordetails_text_title);
        ivBack= (ImageView) view.findViewById(R.id.iv_k_authordetails_back);
        refreshListView= (RefreshListView) view.findViewById(R.id.k_authordetails_refreshlistview);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
    }

    @Override
    protected void initData() {
        String title=getArguments().getString("authorname");
        tvTitle.setText(title);
        url1=getArguments().getString("authorurl1");
        url2=getArguments().getString("authorurl2");
        adapter=new AuthorDeailsAdapter(context);
        artistBeanList=new ArrayList<>();
        refesh(page);
        refreshListView.setAdapter(adapter);
        refreshListView.setOnRefreshListener(this);
        refreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((authorDetailsToSonglistOnClickListener)getActivity())
                        .onAuthorDetailsToSonglistClickListener(artistBeanList.get(position).getTing_uid());
            }
        });
    }
    public void refesh(int page){
        NetTool netTool=new NetTool();
        netTool.getUrl(new NetListener() {
            @Override
            public void onSuccessed(String result) {
                Gson gson=new Gson();
                AuthorDetailsBean bean=gson.fromJson(result,AuthorDetailsBean.class);
                artistBeanList.addAll(bean.getArtist());
                adapter.setArtistBeanList(artistBeanList);
                adapter.notifyDataSetChanged();
                refreshListView.hideHeaderView();
                refreshListView.hideFooterView();
            }

            @Override
            public void onFailed(VolleyError error) {

            }
        },url1+String.valueOf(page)+url2);
    }

    @Override
    public void onDownPullRefresh() {
        artistBeanList=new ArrayList<>();
        page=1;
        refesh(page);
    }

    @Override
    public void onLoadingMore() {
        refesh(++page);
    }
    public interface authorDetailsToSonglistOnClickListener{
        void onAuthorDetailsToSonglistClickListener(String tingUid);
    }

    @Override
    public void onDestroy() {
        ((MainActivity)getActivity()).showTitleFragment();
        super.onDestroy();
    }
}
