package com.example.kevin.baidumusic.search;

import android.view.View;
import android.widget.ImageView;

import com.example.kevin.baidumusic.R;
import com.example.kevin.baidumusic.base.SecBaseFragment;

/**
 * Created by kevin on 16/5/21.
 */
public class SearchFragment extends SecBaseFragment{
    private SearchOnClick searchOnClick;
    private ImageView ivSearchBack;


    public void setSearchOnClick(SearchOnClick searchOnClick) {
        this.searchOnClick = searchOnClick;
    }

    @Override
    public int setlayout() {
        return R.layout.fragment_search;
    }

    @Override
    protected void initView(View view) {
        ivSearchBack= (ImageView) view.findViewById(R.id.iv_search_back);
    }

    @Override
    protected void initData() {
        ivSearchBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                searchOnClick.onSearchClick();
                //返回上一个fragment
                getFragmentManager().popBackStack();
            }
        });
    }
    public interface SearchOnClick{
        void onSearchClick();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().overridePendingTransition(R.anim.fragment_in,R.anim.fragment_out);
    }

}
