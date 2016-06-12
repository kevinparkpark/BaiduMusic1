package com.example.kevin.baidumusic.musiclibrary.songmenu.songmenudetails;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kevin.baidumusic.R;

import java.util.List;

/**
 * Created by kevin on 16/5/28.
 */
public class SongMenuDetailsAdapter extends BaseAdapter{
    private List<SongMenuDetailsBean.ContentBean> contentBeanList;
    private Context context;
    private SongMenuDetailsOnClickListener clickListener;

    public void setClickListener(SongMenuDetailsOnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public SongMenuDetailsAdapter(Context context) {
        this.context = context;
    }

    public void setContentBeanList(List<SongMenuDetailsBean.ContentBean> contentBeanList) {
        this.contentBeanList = contentBeanList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return contentBeanList==null?0:contentBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MyHolder holder=null;
        if (convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.item_songmenudetails,parent,false);
            holder=new MyHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder= (MyHolder) convertView.getTag();
        }

        holder.tvTitle.setText(contentBeanList.get(position).getTitle());
        holder.tvAuthor.setText(contentBeanList.get(position).getAuthor());
        holder.ivMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onSongMenuDetailsClickListener(position);
            }
        });

        return convertView;
    }
    class MyHolder{
        TextView tvTitle,tvAuthor;
        ImageView ivMore;

        public MyHolder(View itemView){
            tvAuthor= (TextView) itemView.findViewById(R.id.tv_songmenudetails_author);
            tvTitle= (TextView) itemView.findViewById(R.id.tv_songmenudetails_title);
            ivMore= (ImageView) itemView.findViewById(R.id.iv_songmenudetails_more);
        }
    }
}
