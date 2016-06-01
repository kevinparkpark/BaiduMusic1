package com.example.kevin.baidumusic.musiclibrary.mv;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.kevin.baidumusic.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 16/5/21.
 */
public class MvAdapter extends RecyclerView.Adapter<MvAdapter.MyHolder> {

    private List<MvBean.ResultBean.MvListBean> mvListBeen;
    private Context context;
    private MvRecyclerViewOnClickListener clickListener;

    public void setClickListener(MvRecyclerViewOnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public MvAdapter(Context context) {
        this.context = context;
    }

    public void setMvListBeen(List<MvBean.ResultBean.MvListBean> mvListBeen) {
        this.mvListBeen = mvListBeen;
        notifyDataSetChanged();
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_mv, parent, false);
        MyHolder holder = new MyHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, int position) {
        holder.tvAuthor.setText(mvListBeen.get(position).getArtist());
        holder.tvName.setText(mvListBeen.get(position).getTitle());
        Picasso.with(context).load(mvListBeen.get(position).getThumbnail()).into(holder.ivMv);
//        if (clickListener!=null){
//            holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int position=holder.getLayoutPosition();
//                    clickListener.onMvClick(position);
//                }
//            });
//        }
    }

    @Override
    public int getItemCount() {
        return mvListBeen==null?0:mvListBeen.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvAuthor;
        ImageView ivMv;
        RelativeLayout relativeLayout;

        public MyHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_mv_name);
            tvAuthor = (TextView) itemView.findViewById(R.id.tv_mv_author);
            ivMv = (ImageView) itemView.findViewById(R.id.iv_mv);
            relativeLayout= (RelativeLayout) itemView.findViewById(R.id.mv_recyclerview);
        }
    }
}
