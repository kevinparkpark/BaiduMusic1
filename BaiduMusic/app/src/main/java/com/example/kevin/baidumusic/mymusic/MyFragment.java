package com.example.kevin.baidumusic.mymusic;

import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.kevin.baidumusic.base.BaseFragment;
import com.example.kevin.baidumusic.R;

/**
 * Created by kevin on 16/5/19.
 */
public class MyFragment extends BaseFragment {
    private RelativeLayout relativeLayout;

    @Override
    public int setlayout() {
        return R.layout.fragment_my;
    }

    @Override
    protected void initView(View view) {
        relativeLayout= (RelativeLayout) view.findViewById(R.id.rl_my_local_music);
    }

    @Override
    protected void initData() {
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               ( (MyToLocalFragmentOnClick) getActivity()).onMyToLocalFragmentClick();
            }
        });

    }
    public interface MyToLocalFragmentOnClick{
        void onMyToLocalFragmentClick();
    }
}
