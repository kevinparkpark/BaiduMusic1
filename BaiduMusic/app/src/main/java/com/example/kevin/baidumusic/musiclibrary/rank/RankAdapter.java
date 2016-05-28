package com.example.kevin.baidumusic.musiclibrary.rank;

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
 * Created by kevin on 16/5/20.
 */
public class RankAdapter extends BaseAdapter {
    private Context context;
    private List<RankBean.ContentBean> contentBeans;

    public void setContentBeans(List<RankBean.ContentBean> contentBeans) {
        this.contentBeans = contentBeans;
//        Log.d("RankAdapter","-------"+ contentBeans.get(0).getName());
        notifyDataSetChanged();
    }

    public RankAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return contentBeans == null ? 0 : contentBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return contentBeans == null ? null : contentBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.rank_le_listview, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvRank.setText(contentBeans.get(position).getName());
        holder.tvNo1.setText(contentBeans.get(position).getContent().get(0).getTitle()
                + "-" + contentBeans.get(position).getContent().get(0).getAuthor());
        holder.tvNo2.setText(contentBeans.get(position).getContent().get(1).getTitle()
                + "-" + contentBeans.get(position).getContent().get(1).getAuthor());
        holder.tvNo3.setText(contentBeans.get(position).getContent().get(2).getTitle()
                + "-" + contentBeans.get(position).getContent().get(2).getAuthor());

        ImageLoader loader = VolleySingleton.getInstance().getImageLoader();
        loader.get(contentBeans.get(position).getPic_s260(),ImageLoader.getImageListener(
                holder.ivRankNew,R.mipmap.ic_launcher,R.mipmap.ic_launcher));

        return convertView;
    }

    class ViewHolder {
        TextView tvRank, tvNo1, tvNo3, tvNo2;
        ImageView ivRankNew;

        public ViewHolder(View itemView) {
            tvRank = (TextView) itemView.findViewById(R.id.tv_le_rank_new);
            tvNo1 = (TextView) itemView.findViewById(R.id.tv_le_rank_no1);
            tvNo2 = (TextView) itemView.findViewById(R.id.tv_le_rank_no2);
            tvNo3 = (TextView) itemView.findViewById(R.id.tv_le_rank_no3);
            ivRankNew = (ImageView) itemView.findViewById(R.id.iv_le_rank_new);

        }
    }
}
