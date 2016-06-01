package com.example.kevin.baidumusic.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.kevin.baidumusic.R;

import java.util.List;

/**
 * Created by kevin on 16/5/29.
 */
public class SearchAdapter extends BaseAdapter{
    private List<SearchBean.ResultBean.SongInfoBean.SongListBean> songListBeen;
    private Context context;

    public SearchAdapter(Context context) {
        this.context = context;
    }

    public void setSongListBeen(List<SearchBean.ResultBean.SongInfoBean.SongListBean> songListBeen) {
        this.songListBeen = songListBeen;
    }

    @Override
    public int getCount() {
        return songListBeen ==null?0: songListBeen.size();
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
            convertView= LayoutInflater.from(context).inflate(R.layout.item_search,parent,false);
            holder=new MyHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder= (MyHolder) convertView.getTag();
        }
        holder.tvTitle.setText(songListBeen.get(position).getTitle());
        holder.tvAuthor.setText(songListBeen.get(position).getAuthor());
        if (songListBeen.get(position).getInfo()!=null) {
            holder.tvWhere.setText(songListBeen.get(position).getInfo());
        }else {
            //如没有info属性,直接隐藏
            holder.tvWhere.setVisibility(View.GONE);
        }
        return convertView;
    }
    class MyHolder{
        private TextView tvAuthor,tvTitle,tvWhere;

        public MyHolder(View itemView){
            tvAuthor= (TextView) itemView.findViewById(R.id.tv_search_author);
            tvTitle= (TextView) itemView.findViewById(R.id.tv_search_songname);
            tvWhere= (TextView) itemView.findViewById(R.id.tv_search_where);
        }
    }
}
