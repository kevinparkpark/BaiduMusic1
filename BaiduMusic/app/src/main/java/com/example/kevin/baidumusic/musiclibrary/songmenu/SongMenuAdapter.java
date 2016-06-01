package com.example.kevin.baidumusic.musiclibrary.songmenu;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.kevin.baidumusic.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 16/5/21.
 */
public class SongMenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<SongMenuBean.ContentBean> datas;
    private SongMenuRecyclerViewOnClickListener clickListener;

    public void setClickListener(SongMenuRecyclerViewOnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public SongMenuAdapter(Context context) {
        this.context = context;
    }

    public void setDatas(List<SongMenuBean.ContentBean> datas) {
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
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        MyHolder holder1= (MyHolder) holder;
        holder1.tvSongmenuCount.setText(datas.get(position).getListenum());
        holder1.tvSongmenuTitle.setText(datas.get(position).getTitle());
        holder1.tvSongmenuWhere.setText(datas.get(position).getTag());
        Picasso.with(context).load(datas.get(position).getPic_300()).into(holder1.ivSongmenu);

        if (clickListener!=null){
            holder1.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=holder.getLayoutPosition();
                    clickListener.onSongMenuClick(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return datas==null?0:datas.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{

        TextView tvSongmenuCount,tvSongmenuTitle,tvSongmenuWhere;
        ImageView ivSongmenu;
        RelativeLayout relativeLayout;

        public MyHolder(View itemView) {
            super(itemView);
            tvSongmenuCount= (TextView) itemView.findViewById(R.id.tv_songmenu_count);
            tvSongmenuTitle= (TextView) itemView.findViewById(R.id.tv_songmenu_title);
            tvSongmenuWhere= (TextView) itemView.findViewById(R.id.tv_songmenu_where);
            ivSongmenu= (ImageView) itemView.findViewById(R.id.iv_songmenu_image);
            relativeLayout= (RelativeLayout) itemView.findViewById(R.id.relativelayout_songmenu);
        }
    }
}
