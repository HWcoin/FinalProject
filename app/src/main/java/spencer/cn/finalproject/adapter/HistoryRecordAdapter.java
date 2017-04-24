package spencer.cn.finalproject.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import spencer.cn.finalproject.R;
import spencer.cn.finalproject.acview.NewsDetailsActivity;
import spencer.cn.finalproject.dojo.News;
import spencer.cn.finalproject.dojo.NewsInfo;

/**
 * Created by Administrator on 2017/4/9.
 */

public class HistoryRecordAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<News> dataSource;

    public HistoryRecordAdapter(Context mContext, ArrayList<News> dataSource) {
        this.mContext = mContext;
        this.dataSource = dataSource;
    }

    @Override
    public int getCount() {
        return dataSource.size();
    }

    @Override
    public Object getItem(int position) {
        return dataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        HistoryRecordHolder holder;
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_history_record, parent, false);
            holder = new HistoryRecordHolder();
            holder.title = (TextView) convertView.findViewById(R.id.tv_record_title);
            holder.icon = (ImageView) convertView.findViewById(R.id.iv_history_icon);
            convertView.setTag(holder);
        }else{
            holder = (HistoryRecordHolder) convertView.getTag();
        }
        holder.title.setText(dataSource.get(position).getTitle());
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, NewsDetailsActivity.class);
                intent.putExtra(NewsInfo.URL, dataSource.get(position).getUrl());
                mContext.startActivity(intent);
            }
        });
        Picasso.with(mContext).load(dataSource.get(position).getThumbnail_pic_s()).into(holder.icon);
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, NewsDetailsActivity.class);
                intent.putExtra(NewsInfo.URL, dataSource.get(position).getUrl());
                mContext.startActivity(intent);
            }
        });
        return  convertView;
    }

    static class HistoryRecordHolder{
        TextView title;
        ImageView icon;
    }
}
