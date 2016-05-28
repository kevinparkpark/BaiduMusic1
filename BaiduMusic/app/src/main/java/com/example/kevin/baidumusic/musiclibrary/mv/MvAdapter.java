package com.example.kevin.baidumusic.musiclibrary.mv;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kevin.baidumusic.R;

import java.util.ArrayList;

/**
 * Created by kevin on 16/5/21.
 */
public class MvAdapter extends RecyclerView.Adapter<MvAdapter.MyHolder> {

    private ArrayList<MvBean> datas;
    private Context context;

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_mv, parent, false);
        MyHolder holder = new MyHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvAuthor;
        ImageView ivMv;

        public MyHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_mv_name);
            tvAuthor = (TextView) itemView.findViewById(R.id.tv_mv_author);
            ivMv = (ImageView) itemView.findViewById(R.id.iv_mv);
        }
    }
}
