package com.example.kevin.baidumusic.musiclibrary.songmenu;

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
public class SongMenuAdapter extends RecyclerView.Adapter<SongMenuAdapter.MyHolder> {
    private Context context;
    private ArrayList<SongMenuBean> datas;

    public SongMenuAdapter(Context context) {
        this.context = context;
    }

    public void setDatas(ArrayList<SongMenuBean> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView= LayoutInflater.from(context).inflate(R.layout.item_songmenu_recycler,parent,false);
        MyHolder holder=new MyHolder(itemView);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {

        holder.tvSongmenuCount.setText(datas.get(position).getCount());
//        holder.tvSongmenuTitle.setText(datas.get(position).getTitle());
//        holder.tvSongmenuWhere.setText(datas.get(position).getWhere());
//        holder.ivSongmenu.setImageBitmap(datas.get(position).getImageSongmenu());
    }

    @Override
    public int getItemCount() {
        return datas==null?0:datas.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        TextView tvSongmenuCount,tvSongmenuTitle,tvSongmenuWhere;
        ImageView ivSongmenu;

        public MyHolder(View itemView) {
            super(itemView);
            tvSongmenuCount= (TextView) itemView.findViewById(R.id.tv_songmenu_count);
            tvSongmenuTitle= (TextView) itemView.findViewById(R.id.tv_songmenu_list);
            tvSongmenuWhere= (TextView) itemView.findViewById(R.id.tv_songmenu_where);
            ivSongmenu= (ImageView) itemView.findViewById(R.id.iv_songmenu_image);

        }
    }
}
