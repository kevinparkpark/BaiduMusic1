package com.example.kevin.baidumusic.kmusic.authordetails.songlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.kevin.baidumusic.R;

import java.util.List;

/**
 * Created by kevin on 16/6/1.
 */
public class AuthorDetailsSonglistAdapter extends BaseAdapter {
    private List<AuthorDetailsSonglistBean.SonglistBean> songlistBeanList;
    private Context context;
    private PopupWindow popupWindow;
    private View popView;
    private AuthorDetailsSonglistOnClickListener onClickListener;

    public void setOnClickListener(AuthorDetailsSonglistOnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public AuthorDetailsSonglistAdapter(Context context) {
        this.context = context;
    }

    public void setSonglistBeanList(List<AuthorDetailsSonglistBean.SonglistBean> songlistBeanList) {
        this.songlistBeanList = songlistBeanList;
    }

    @Override
    public int getCount() {
        return songlistBeanList == null ? 0 : songlistBeanList.size();
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
        holder.tvtitle.setText(songlistBeanList.get(position).getTitle());
        holder.tvAlbumTitle.setText(songlistBeanList.get(position).getAlbum_title());
        final MyHolder finalHolder = holder;
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
