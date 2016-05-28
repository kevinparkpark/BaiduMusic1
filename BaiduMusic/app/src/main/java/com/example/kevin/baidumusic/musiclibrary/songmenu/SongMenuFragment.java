package com.example.kevin.baidumusic.musiclibrary.songmenu;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.kevin.baidumusic.base.BaseFragment;
import com.example.kevin.baidumusic.R;

import java.util.ArrayList;

/**
 * Created by kevin on 16/5/19.
 */
public class SongMenuFragment extends BaseFragment {
    private SongMenuAdapter adapter;
    private ArrayList<SongMenuBean> datas;
    private RecyclerView recyclerView;

    @Override
    public int setlayout() {
        return R.layout.fragment_le_songmenu;
    }

    @Override
    protected void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.songmenu_recyclerview);

    }

    @Override
    protected void initData() {

        adapter = new SongMenuAdapter(context);
        datas = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            SongMenuBean bean = new SongMenuBean();
            bean.setCount(i + "");
            datas.add(bean);
        }
        adapter.setDatas(datas);
        GridLayoutManager manager = new GridLayoutManager(context, 2);
        manager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);

        recyclerView.setAdapter(adapter);


//        gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//            @Override
//            public int getSpanSize(int position) {
//                return getItemViewType(position) == TYPE_HEADER
//                        ? gridManager.getSpanCount() : 1;
//            }
//        });

    }
}
