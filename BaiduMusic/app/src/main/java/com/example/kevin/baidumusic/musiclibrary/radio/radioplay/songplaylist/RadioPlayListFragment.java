package com.example.kevin.baidumusic.musiclibrary.radio.radioplay.songplaylist;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.example.kevin.baidumusic.R;
import com.example.kevin.baidumusic.base.BaseFragment;
import com.example.kevin.baidumusic.netutil.NetListener;
import com.example.kevin.baidumusic.netutil.NetTool;
import com.example.kevin.baidumusic.netutil.URLValues;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by kevin on 16/6/2.
 */
//public class RadioPlayListFragment extends BaseFragment{
//    private ViewPager viewPager;
//    private List<RadioPlayListBean.ResultBean.SonglistBean> songlistBeanList;
//    private RadioPlayListPagerAdapter adapter;
//    private TextView tvScene;
//    private ImageView ivBack;
//
//    @Override
//    public int setlayout() {
//        return R.layout.activity_radioplaylist;
//    }
//
//    @Override
//    protected void initView(View view) {
//        viewPager= (ViewPager) view.findViewById(R.id.radioplaylist_viewpager);
//        tvScene= (TextView) view.findViewById(R.id.tv_radioplaylist_scene);
//        ivBack= (ImageView) view.findViewById(R.id.iv_radioplaylist_back_to_activity);
//    }
//
//    @Override
//    protected void initData() {
//
//        String sceneId=getArguments().getString("scene");
//        tvScene.setText(getArguments().getString("scenename"));
//
//        adapter=new RadioPlayListPagerAdapter(getActivity());
//        viewPager.setOffscreenPageLimit(3);
//        viewPager.setPageMargin(10);
//
//        NetTool netTool=new NetTool();
//        netTool.getUrl(new NetListener() {
//            @Override
//            public void onSuccessed(String result) {
//                Gson gson=new Gson();
//                RadioPlayListBean bean=gson.fromJson(result,RadioPlayListBean.class);
//                adapter.setSonglistBeanList(bean.getResult().getSonglist());
//                viewPager.setAdapter(adapter);
//                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//                    @Override
//                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//                    }
//
//                    @Override
//                    public void onPageSelected(int position) {
//                        Log.d("RadioPlayListFragment", "selected:" + position);
//                    }
//
//                    @Override
//                    public void onPageScrollStateChanged(int state) {
//
//                    }
//                });
//            }
//
//            @Override
//            public void onFailed(VolleyError error) {
//
//            }
//        }, URLValues.RADIOPLAYLIST_SCENE1+sceneId+URLValues.RADIOPLAYLIST_SCENE2);
//
//        //返回上一层
//        tvScene.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getFragmentManager().popBackStack();
//            }
//        });
//        //返回主页面
//        ivBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getActivity().finish();
//            }
//        });
//    }
//}
