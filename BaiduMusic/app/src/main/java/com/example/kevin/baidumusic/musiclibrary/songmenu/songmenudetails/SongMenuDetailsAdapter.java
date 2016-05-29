package com.example.kevin.baidumusic.musiclibrary.songmenu.songmenudetails;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.kevin.baidumusic.R;

import java.util.List;

/**
 * Created by kevin on 16/5/28.
 */
public class SongMenuDetailsAdapter extends BaseAdapter{
    private List<SongMenuDetailsBean.ContentBean> contentBeanList;
    private Context context;

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
    public View getView(int position, View convertView, ViewGroup parent) {
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

        return convertView;
    }
    class MyHolder{
        TextView tvTitle,tvAuthor;

        public MyHolder(View itemView){
            tvAuthor= (TextView) itemView.findViewById(R.id.tv_songmenudetails_author);
            tvTitle= (TextView) itemView.findViewById(R.id.tv_songmenudetails_title);
        }
    }
}
