package com.example.kevin.baidumusic.kmusic.authordetails;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.example.kevin.baidumusic.R;
import com.example.kevin.baidumusic.netutil.VolleySingleton;

import java.util.List;

/**
 * Created by kevin on 16/6/1.
 */
public class AuthorDeailsAdapter extends BaseAdapter{
    private List<AuthorDetailsBean.ArtistBean> artistBeanList;
    private Context context;

    public AuthorDeailsAdapter(Context context) {
        this.context = context;
    }

    public void setArtistBeanList(List<AuthorDetailsBean.ArtistBean> artistBeanList) {
        this.artistBeanList = artistBeanList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return artistBeanList==null?0:artistBeanList.size();
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
            convertView= LayoutInflater.from(context).inflate(R.layout.item_k_authordetails,parent,false);
            holder=new MyViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder= (MyViewHolder) convertView.getTag();
        }
        holder.tvAuthor.setText(artistBeanList.get(position).getName());
        ImageLoader loader= VolleySingleton.getInstance().getImageLoader();
        loader.get(artistBeanList.get(position).getAvatar_middle(),ImageLoader.getImageListener(
                holder.ivAuthor, R.mipmap.yuan,R.mipmap.yuan));

        return convertView;
    }
    class MyViewHolder{
        ImageView ivAuthor;
        TextView tvAuthor;
        public MyViewHolder(View itemView){
            ivAuthor= (ImageView) itemView.findViewById(R.id.iv_k_authordetails_iv);
            tvAuthor= (TextView) itemView.findViewById(R.id.tv_k_authordetails_text);
        }
    }
}
