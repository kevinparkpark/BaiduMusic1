package com.example.kevin.baidumusic.search;

import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.example.kevin.baidumusic.R;
import com.example.kevin.baidumusic.base.SecBaseFragment;
import com.example.kevin.baidumusic.db.DBSongListCacheBean;
import com.example.kevin.baidumusic.db.LiteOrmSington;
import com.example.kevin.baidumusic.eventbean.EventGenericBean;
import com.example.kevin.baidumusic.eventbean.EventPosition;
import com.example.kevin.baidumusic.musiclibrary.rank.RankDetailsBean;
import com.example.kevin.baidumusic.netutil.NetListener;
import com.example.kevin.baidumusic.netutil.NetTool;
import com.example.kevin.baidumusic.netutil.URLValues;
import com.google.gson.Gson;
import com.litesuits.orm.LiteOrm;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 16/5/21.
 */
public class SearchFragment extends SecBaseFragment {
    private SearchOnClick searchOnClick;
    private ImageView ivSearchBack;
    private List<SearchBean.ResultBean.SongInfoBean.SongListBean> songListBeen;
    private SearchBean searchBean;
    private SearchAdapter adapter;
    private ListView listView;
    private EditText etSearch;
    private ImageView ivSearchIcon;


    public void setSearchOnClick(SearchOnClick searchOnClick) {
        this.searchOnClick = searchOnClick;
    }

    @Override
    public int setlayout() {
        return R.layout.fragment_search;
    }

    @Override
    protected void initView(View view) {
        ivSearchBack = (ImageView) view.findViewById(R.id.iv_search_back);
        listView = (ListView) view.findViewById(R.id.search_listview);
        etSearch = (EditText) view.findViewById(R.id.et_search);
        ivSearchIcon = (ImageView) view.findViewById(R.id.iv_search_icon);
    }

    @Override
    protected void initData() {
        //返回键
        ivSearchBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                searchOnClick.onSearchClick();
                //返回上一个fragment
                getFragmentManager().popBackStack();
            }
        });
        //搜索键
        ivSearchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String et = etSearch.getText().toString();
                if (et.length() != 0) {
                    NetTool netTool = new NetTool();
                    netTool.getUrlId(new NetListener() {
                        @Override
                        public void onSuccessed(String result) {
                            Gson gson = new Gson();
                            searchBean = gson.fromJson(result, SearchBean.class);
                            songListBeen = new ArrayList<>();
                            if (searchBean.getResult().getSong_info() != null) {
                                songListBeen = searchBean.getResult().getSong_info().getSong_list();
                                adapter = new SearchAdapter(context);
                                adapter.setSongListBeen(songListBeen);
                                listView.setAdapter(adapter);
                            }
                        }

                        @Override
                        public void onFailed(VolleyError error) {

                        }
                    }, URLValues.SEARCH1, et, URLValues.SEARCH2);
                    etSearch.setText("");
                    //将输入法隐藏，mPasswordEditText 代表密码输入框
//                    InputMethodManager imm =(InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(ivSearchIcon.getWindowToken(), 0);
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                EventBus.getDefault().post(new EventPosition(position));
//                EventBus.getDefault().post(searchBean);
//                LiteOrm liteOrm= LiteOrmSington.getInstance().getLiteOrm();
//                liteOrm.deleteAll(DBSongListCacheBean.class);

                List<EventGenericBean> eventGenericBeen=new ArrayList<EventGenericBean>();

                final LiteOrm liteOrm= LiteOrmSington.getInstance().getLiteOrm();
                liteOrm.deleteAll(DBSongListCacheBean.class);

                for (SearchBean.ResultBean.SongInfoBean.SongListBean songListBean : songListBeen) {

                    EventGenericBean bean=new EventGenericBean(songListBean.getTitle(),songListBean.getAuthor(),
                            "","",songListBean.getSong_id());
                    eventGenericBeen.add(bean);
                }

                EventBus.getDefault().post(new EventPosition(position));
                EventBus.getDefault().post(eventGenericBeen);
            }
        });
    }

    public interface SearchOnClick {
        void onSearchClick();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().overridePendingTransition(R.anim.fragment_in, R.anim.fragment_out);
    }

}
