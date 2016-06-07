package com.example.kevin.baidumusic.mymusic.latelyplaylist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kevin.baidumusic.R;
import com.example.kevin.baidumusic.db.DBSongPlayListBean;

import java.util.List;

/**
 * Created by kevin on 16/6/5.
 */
public class LatelyPlaylistAdapter extends BaseAdapter{
    private Context context;
    private List<DBSongPlayListBean> been;

    public LatelyPlaylistAdapter(Context context) {
        this.context = context;
    }

    public void setBeen(List<DBSongPlayListBean> been) {
        this.been = been;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return been==null?0:been.size();
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
        MyViewHolder holder=null;
        if (convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.item_le_rankdetails,parent,false);
            holder=new MyViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder= (MyViewHolder) convertView.getTag();
        }

//        holder.tv_count.setText(songListBeen.get(position).getRank());
        holder.tv_song.setText(been.get(position).getTitle());
        holder.tv_author.setText(been.get(position).getAuthor());

        holder.imageView.setImageResource(R.mipmap.yuan);

//        ImageLoader loader = VolleySingleton.getInstance().getImageLoader();
//        loader.get(songListBeen.get(position).getPic_big(),ImageLoader.getImageListener(
//                holder.imageView,R.mipmap.ic_launcher,R.mipmap.ic_launcher));

        return convertView;
    }
    class MyViewHolder{
        TextView tv_song,tv_author,tv_count;
        ImageView imageView;
        public MyViewHolder(View itemView){
            tv_author= (TextView) itemView.findViewById(R.id.tv_le_rankdetails_author);
            tv_count= (TextView) itemView.findViewById(R.id.tv_le_rankdetails_count);
            tv_song= (TextView) itemView.findViewById(R.id.tv_le_rankdetails_song);
            imageView= (ImageView) itemView.findViewById(R.id.iv_le_rankdetails);
        }
    }
}
