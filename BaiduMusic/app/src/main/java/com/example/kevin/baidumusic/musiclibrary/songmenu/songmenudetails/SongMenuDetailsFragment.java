package com.example.kevin.baidumusic.musiclibrary.songmenu.songmenudetails;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.example.kevin.baidumusic.MainActivity;
import com.example.kevin.baidumusic.R;
import com.example.kevin.baidumusic.base.BaseFragment;
import com.example.kevin.baidumusic.db.DBSongListCacheBean;
import com.example.kevin.baidumusic.db.LiteOrmSington;
import com.example.kevin.baidumusic.eventbean.EventGenericBean;
import com.example.kevin.baidumusic.eventbean.EventPosition;
import com.example.kevin.baidumusic.musiclibrary.radio.radioplay.songplaylist.RadioPlayListBean;
import com.example.kevin.baidumusic.netutil.NetListener;
import com.example.kevin.baidumusic.netutil.NetTool;
import com.example.kevin.baidumusic.netutil.URLValues;
import com.google.gson.Gson;
import com.litesuits.orm.LiteOrm;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 16/5/28.
 */
public class SongMenuDetailsFragment extends BaseFragment{
    private ListView listView;
    private SongMenuDetailsAdapter adapter;
    private List<SongMenuDetailsBean.ContentBean> contentBeanList;
    private View view;
    private ImageView ivSongMenuDetailsHeadImg;
    private TextView tvTitle,tvListNum,tvTag,tvCollectNum;
    private SongMenuDetailsBean bean;

    @Override
    public int setlayout() {
        return R.layout.fragment_songmenudetails;
    }

    @Override
    protected void initView(View view) {
        listView= (ListView) view.findViewById(R.id.songmenudetails_listview);
    }

    public void headView(){
        view= LayoutInflater.from(context).inflate(R.layout.head_songmenudetails,null);
        ivSongMenuDetailsHeadImg= (ImageView) view.findViewById(R.id.iv_songmenudetails_headbac);
        tvCollectNum= (TextView) view.findViewById(R.id.tv_songmenudetails_head_collectnum);
        tvListNum= (TextView) view.findViewById(R.id.tv_songmenudetails_head_listnum);
        tvTag= (TextView) view.findViewById(R.id.tv_songmenudetails_head_tag);
        tvTitle= (TextView) view.findViewById(R.id.tv_songmenudetails_title);
    }

    @Override
    protected void initData() {
        String listId=getArguments().getString("listid");
        Log.d("SongMenuDetailsFragment","-----"+ listId);
        NetTool netTool=new NetTool();
        netTool.getUrlId(new NetListener() {
            @Override
            public void onSuccessed(String result) {
                Gson gson=new Gson();
                bean=gson.fromJson(result,SongMenuDetailsBean.class);
                contentBeanList=bean.getContent();
                adapter=new SongMenuDetailsAdapter(context);
                adapter.setContentBeanList(contentBeanList);
                headView();
                Picasso.with(context).load(bean.getPic_w700()).resize(1000,500).
                        centerCrop().into(ivSongMenuDetailsHeadImg);
                tvTag.setText(bean.getTag());
                tvTitle.setText(bean.getTitle());
                tvListNum.setText(bean.getListenum());
                tvCollectNum.setText(bean.getCollectnum());
                listView.addHeaderView(view,null,false);
                listView.setAdapter(adapter);
            }

            @Override
            public void onFailed(VolleyError error) {

            }
        }, URLValues.LE_SONGMENUDETAILS_LIST1,listId,URLValues.LE_SONGMENUDETAILS_LIST2);

        //点击歌曲时播放并删除cache所有歌曲
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                EventBus.getDefault().post(new EventPosition(position-1));
//                EventBus.getDefault().post(bean);
//                LiteOrm liteOrm= LiteOrmSington.getInstance().getLiteOrm();
//                liteOrm.deleteAll(DBSongListCacheBean.class);

                List<EventGenericBean> eventGenericBeen=new ArrayList<EventGenericBean>();

                final LiteOrm liteOrm= LiteOrmSington.getInstance().getLiteOrm();
                liteOrm.deleteAll(DBSongListCacheBean.class);
                for (SongMenuDetailsBean.ContentBean contentBean : bean.getContent()) {

                    EventGenericBean bean1=new EventGenericBean(contentBean.getTitle(),contentBean.getAuthor(),
                            bean.getPic_300(),bean.getPic_500(),contentBean.getSong_id());
                    eventGenericBeen.add(bean1);
                }

                EventBus.getDefault().post(new EventPosition(position-1));
                EventBus.getDefault().post(eventGenericBeen);
            }
        });
    }

    @Override
    public void onDestroy() {
        if (!getActivity().isDestroyed()) {
            ((MainActivity) getActivity()).showTitleFragment();
        }
        super.onDestroy();

    }
}
