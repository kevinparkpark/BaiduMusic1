package com.example.kevin.baidumusic.mymusic.localmusic.musicsonglist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kevin.baidumusic.R;
import com.example.kevin.baidumusic.util.LocalMusic;

import java.util.List;

/**
 * Created by kevin on 16/6/4.
 */
public class MyLocalMusicSongListAdapter extends BaseAdapter{
    private List<LocalMusic> musicList;
    private Context context;

    public MyLocalMusicSongListAdapter(Context context) {
        this.context = context;
    }

    public void setMusicList(List<LocalMusic> musicList) {
        this.musicList = musicList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return musicList==null?0:musicList.size();
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
        ViewHolder holder=null;
        if (convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.item_le_rankdetails,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }

//        holder.tv_count.setText(songListBeen.get(position).getRank());
        holder.tv_song.setText(musicList.get(position).getTitle());
        holder.tv_author.setText(musicList.get(position).getArtist());

        holder.imageView.setImageResource(R.mipmap.yuan);

//        ImageLoader loader = VolleySingleton.getInstance().getImageLoader();
//        loader.get(songListBeen.get(position).getPic_big(),ImageLoader.getImageListener(
//                holder.imageView,R.mipmap.ic_launcher,R.mipmap.ic_launcher));

        return convertView;
    }
    class ViewHolder{
        TextView tv_song,tv_author,tv_count;
        ImageView imageView;

        public ViewHolder(View itemView){

            tv_author= (TextView) itemView.findViewById(R.id.tv_le_rankdetails_author);
            tv_count= (TextView) itemView.findViewById(R.id.tv_le_rankdetails_count);
            tv_song= (TextView) itemView.findViewById(R.id.tv_le_rankdetails_song);
            imageView= (ImageView) itemView.findViewById(R.id.iv_le_rankdetails);
        }
    }
}
