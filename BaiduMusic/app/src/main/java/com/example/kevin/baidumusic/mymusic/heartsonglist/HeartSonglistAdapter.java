package com.example.kevin.baidumusic.mymusic.heartsonglist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.kevin.baidumusic.R;
import com.example.kevin.baidumusic.db.DBHeart;
import com.example.kevin.baidumusic.kmusic.authordetails.songlist.AuthorDetailsSonglistBean;
import com.example.kevin.baidumusic.kmusic.authordetails.songlist.AuthorDetailsSonglistOnClickListener;

import java.util.List;

/**
 * Created by kevin on 16/6/12.
 */
public class HeartSonglistAdapter extends BaseAdapter{
    private List<DBHeart> dbHearts;
    private Context context;
    private PopupWindow popupWindow;
    private View popView;
    private AuthorDetailsSonglistOnClickListener onClickListener;

    public HeartSonglistAdapter(Context context) {
        this.context = context;
    }

    public void setDbHearts(List<DBHeart> dbHearts) {
        this.dbHearts = dbHearts;
        notifyDataSetChanged();
    }

    public void setOnClickListener(AuthorDetailsSonglistOnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public int getCount() {
        return dbHearts==null?0:dbHearts.size();
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        MyHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_k_authordetails_songlist, parent, false);
            holder = new MyHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (MyHolder) convertView.getTag();
        }
        holder.tvtitle.setText(dbHearts.get(position).getTitle());
        holder.tvAlbumTitle.setText(dbHearts.get(position).getAuthor());
        holder.ivMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onAuthorDetailsSonglistClickListener(position);
            }
        });

        return convertView;
    }



    class MyHolder {
        TextView tvtitle, tvAlbumTitle;
        ImageView ivMore;

        public MyHolder(View itemView) {
            tvtitle = (TextView) itemView.findViewById(R.id.tv_item_authordetailssonglist_title);
            tvAlbumTitle = (TextView) itemView.findViewById(R.id.tv_item_authordetailssonglist_albumtitle);
            ivMore = (ImageView) itemView.findViewById(R.id.iv_item_k_authordetails_songlist_more);
        }
    }
}
