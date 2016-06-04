package com.example.kevin.baidumusic.musiclibrary.radio.radioplay;

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
 * Created by kevin on 16/6/2.
 */
public class RadioPlayRecyclerAdapter extends RecyclerView.Adapter<RadioPlayRecyclerAdapter.MyHolder>{
    private Context context;
    private List<RadioPlayBean.ResultBean> resultBeanList;
    private RadioPlayRecyclerViewOnClickListener clickListener;

    public void setClickListener(RadioPlayRecyclerViewOnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public RadioPlayRecyclerAdapter(Context context) {
        this.context = context;
    }

    public void setResultBeanList(List<RadioPlayBean.ResultBean> resultBeanList) {
        this.resultBeanList = resultBeanList;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(context).inflate(R.layout.item_radioplay_recyclerview,parent,false);
        MyHolder holder=new MyHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, int position) {
        holder.tvName.setText(resultBeanList.get(position).getScene_name());
        ImageLoader loader= VolleySingleton.getInstance().getImageLoader();
        loader.get(resultBeanList.get(position).getIcon_android(),ImageLoader.getImageListener(
                holder.ivIcon,R.mipmap.yuan,R.mipmap.yuan));
        if (clickListener!=null){
            holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=holder.getLayoutPosition();
                    clickListener.onRadioPlayRecyclerViewCliclListener(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return resultBeanList==null?0:resultBeanList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        ImageView ivIcon;
        TextView tvName;
        RelativeLayout relativeLayout;
        public MyHolder(View itemView) {
            super(itemView);
            ivIcon= (ImageView) itemView.findViewById(R.id.iv_item_radioplay_recyclerview);
            tvName= (TextView) itemView.findViewById(R.id.tv_item_radioplay_recyclerview);
            relativeLayout= (RelativeLayout) itemView.findViewById(R.id.relativelayout_item_radioplay);
        }
    }
}
