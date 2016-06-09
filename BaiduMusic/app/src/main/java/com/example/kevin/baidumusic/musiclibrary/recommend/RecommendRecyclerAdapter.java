package com.example.kevin.baidumusic.musiclibrary.recommend;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.example.kevin.baidumusic.R;
import com.example.kevin.baidumusic.netutil.VolleySingleton;

import java.util.List;

/**
 * Created by kevin on 16/6/8.
 */
public class RecommendRecyclerAdapter extends RecyclerView.Adapter<RecommendRecyclerAdapter.MyHolder> {
    private List<RecommendSongBean.ContentBean.ListBean> listBeen;
    private Context context;
    private RecommendToSonglistOnClickListener clickListener;

    public void setClickListener(RecommendToSonglistOnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public RecommendRecyclerAdapter(Context context) {
        this.context = context;
    }

    public void setListBeen(List<RecommendSongBean.ContentBean.ListBean> listBeen) {
        this.listBeen = listBeen;
        notifyDataSetChanged();
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(context).inflate(R.layout.item_le_recommend,parent,false);
        MyHolder holder=new MyHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, int position) {
        holder.tvTitle.setText(listBeen.get(position).getTitle());
        holder.tvCount.setText(listBeen.get(position).getListenum());
        ImageLoader imageLoader= VolleySingleton.getInstance().getImageLoader();
        imageLoader.get(listBeen.get(position).getPic(),ImageLoader.getImageListener(holder.ivImg,
                R.mipmap.yuan,R.mipmap.yuan));
        if (clickListener!=null){
            holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onRecommendToSonglistClickListener(holder.getLayoutPosition());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listBeen==null?0:listBeen.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        ImageView ivImg;
        TextView tvCount, tvTitle;
        RelativeLayout relativeLayout;

        public MyHolder(View itemView) {
            super(itemView);
            ivImg = (ImageView) itemView.findViewById(R.id.iv_item_recommend_img);
            tvCount = (TextView) itemView.findViewById(R.id.tv_item_recommend_count);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_item_recommend_title);
            relativeLayout= (RelativeLayout) itemView.findViewById(R.id.relativelayout_item_recommend);
        }
    }
}
