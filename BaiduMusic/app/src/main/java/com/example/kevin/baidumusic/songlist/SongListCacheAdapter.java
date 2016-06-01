package com.example.kevin.baidumusic.songlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.kevin.baidumusic.R;
import com.example.kevin.baidumusic.db.DBSongListCacheBean;

import java.util.List;

/**
 * Created by kevin on 16/5/30.
 */
public class SongListCacheAdapter extends BaseAdapter{
    private List<DBSongListCacheBean> cacheBeen;
    private Context context;

    public SongListCacheAdapter(Context context) {
        this.context = context;
    }

    public void setCacheBeen(List<DBSongListCacheBean> cacheBeen) {
        this.cacheBeen = cacheBeen;
    }

    @Override
    public int getCount() {
        return cacheBeen==null?0:cacheBeen.size();
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
            convertView= LayoutInflater.from(context).inflate(R.layout.item_songlistcache,parent,false);
            holder=new MyHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder= (MyHolder) convertView.getTag();
        }
        holder.tvTitle.setText(cacheBeen.get(position).getTitle());
        holder.tvAuthor.setText(cacheBeen.get(position).getAuthor());


        return convertView;
    }
    class MyHolder {
        TextView tvTitle,tvAuthor;
        public MyHolder(View itemView){
            tvAuthor= (TextView) itemView.findViewById(R.id.tv_item_songlistcache_author);
            tvTitle= (TextView) itemView.findViewById(R.id.tv_item_songlistcache_title);
        }
    }
}
