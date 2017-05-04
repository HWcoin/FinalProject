package spencer.cn.finalproject.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import spencer.cn.finalproject.R;
import spencer.cn.finalproject.acview.NewsDetailsActivity;
import spencer.cn.finalproject.dojo.HotNews;
import spencer.cn.finalproject.dojo.NewsInfo;
import spencer.cn.finalproject.listener.OnViewHolderClickListener;

/**
 *  created at 2016/7/2 15:09
 *  @author : 吴培健
 *  @todo   : 装载新闻条目的RecycleViewAdapter
 */
public class HotNewsAdapter extends RecyclerView.Adapter<HotNewsAdapter.ItemViewHolder>{

    private Context context;
    private ArrayList<HotNews> items;

    public HotNewsAdapter(Context context, ArrayList<HotNews> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.news_sub_item_layout, parent, false);
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        /**
         * 设置点击事件，获得被点击的位置position
         */
        holder.setmViewHolderClickListener(new OnViewHolderClickListener() {
            @Override
            public int getPosition() {
                return position;
            }
        });
        holder.tvTitle.setText(items.get(position).getTitle());
        holder.tvSource.setText(items.get(position).getHits()+"点击");
        holder.tvDate.setText(items.get(position).getNewDate());

        //加载网络图片、加载中的图片和加载失败的图片之后再补、太晚了，要睡觉
        Picasso.with(context).load(items.get(position).getPictureUrl()).into(holder.ivPic);

        //设置点击事件
        holder.ivPic.setOnClickListener(holder);
        holder.tvTitle.setOnClickListener(holder);
        holder.tvSource.setOnClickListener(holder);
        holder.tvDate.setOnClickListener(holder);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    //控件装载器
    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView ivPic;
        private TextView tvTitle;
        private TextView tvSource;
        private TextView tvDate;
        private OnViewHolderClickListener mViewHolderClickListener;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ivPic = (ImageView) itemView.findViewById(R.id.iv_news_pic);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_news_title);
            tvSource = (TextView) itemView.findViewById(R.id.tv_news_source);
            tvDate = (TextView) itemView.findViewById(R.id.tv_news_date);
        }

        @Override
        public void onClick(View v) {
            if (mViewHolderClickListener!=null){
                Intent intent = new Intent(context, NewsDetailsActivity.class);
                intent.putExtra(NewsInfo.URL, items.get(mViewHolderClickListener.getPosition()).getUrl());
                intent.putExtra(NewsInfo.TITLE, items.get(mViewHolderClickListener.getPosition()).getTitle());
                intent.putExtra(NewsInfo.PICTUREURL, items.get(mViewHolderClickListener.getPosition()).getPictureUrl());
                intent.putExtra(NewsInfo.UNIQUEKEY, items.get(mViewHolderClickListener.getPosition()).getUniquekey());
                intent.putExtra(NewsInfo.NEWDATE, items.get(mViewHolderClickListener.getPosition()).getNewDate());
                context.startActivity(intent);
                /////////////////////////////////////////////点击新闻，加入历史记录
//                LocalDataManager.keepNewsRecord(items.get(mViewHolderClickListener.getPosition()));
            }else {
                throw new RuntimeException("mViewHolderClickListener is null");
            }
        }

        public OnViewHolderClickListener getmViewHolderClickListener() {
            return mViewHolderClickListener;
        }

        public void setmViewHolderClickListener(OnViewHolderClickListener mViewHolderClickListener) {
            this.mViewHolderClickListener = mViewHolderClickListener;
        }
    }
    //设置数据源，刷新界面
    public void setItems(ArrayList<HotNews> items){
        this.items = items;
        this.notifyDataSetChanged();
    }
}
