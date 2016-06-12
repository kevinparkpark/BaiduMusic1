package com.example.kevin.baidumusic.musiclibrary.rank;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kevin.baidumusic.R;
import com.example.kevin.baidumusic.musiclibrary.rank.songplay.RankDetailsOnClickListener;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by kevin on 16/5/23.
 */
public class RankDetailsAdapter extends BaseAdapter{

    private List<RankDetailsBean.SongListBean> songListBeen;
    private Context context;
    private RankDetailsOnClickListener onClickListener;

    public void setOnClickListener(RankDetailsOnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public RankDetailsAdapter(Context context) {
        this.context = context;
    }

    public void setSongListBeen(List<RankDetailsBean.SongListBean> songListBeen) {
        this.songListBeen = songListBeen;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return songListBeen==null?0:songListBeen.size();
    }

    @Override
    public Object getItem(int position) {
        return songListBeen.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.item_le_rankdetails,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }

//        holder.tv_count.setText(songListBeen.get(position).getRank());
        holder.tv_song.setText(songListBeen.get(position).getTitle());
        holder.tv_author.setText(songListBeen.get(position).getAuthor());
        if (songListBeen.get(position).getPic_small().trim().length() == 0){

            Log.d("RankDetailsAdapter", "getCount():" + getCount());
        }else {

            Picasso.with(context).load(songListBeen.get(position).getPic_small()).resize(150,150).into(holder.imageView);
        }

        holder.ivMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onRankDetailsClickListener(position);
            }
        });

//        ImageLoader loader = VolleySingleton.getInstance().getImageLoader();
//        loader.get(songListBeen.get(position).getPic_big(),ImageLoader.getImageListener(
//                holder.imageView,R.mipmap.ic_launcher,R.mipmap.ic_launcher));

        return convertView;
    }
    class ViewHolder{
        TextView tv_song,tv_author,tv_count;
        ImageView imageView,ivMore;

        public ViewHolder(View itemView){

            tv_author= (TextView) itemView.findViewById(R.id.tv_le_rankdetails_author);
            tv_count= (TextView) itemView.findViewById(R.id.tv_le_rankdetails_count);
            tv_song= (TextView) itemView.findViewById(R.id.tv_le_rankdetails_song);
            imageView= (ImageView) itemView.findViewById(R.id.iv_le_rankdetails);
            ivMore= (ImageView) itemView.findViewById(R.id.iv_le_rankdetails_more);
        }
    }
}
