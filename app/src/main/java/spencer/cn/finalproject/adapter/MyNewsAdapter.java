package spencer.cn.finalproject.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import spencer.cn.finalproject.R;
import spencer.cn.finalproject.application.BaseApplication;
import spencer.cn.finalproject.dojo.XiaozhongNewResp;
import spencer.cn.finalproject.listener.OnViewHolderClickListener;

/**
 *  created at 2016/7/2 15:09
 *  @author : 吴培健
 *  @todo   : 装载新闻条目的RecycleViewAdapter
 */
public class MyNewsAdapter extends RecyclerView.Adapter<MyNewsAdapter.ItemViewHolder>{

    private Context context;
    private ArrayList<XiaozhongNewResp> items;

    public MyNewsAdapter(Context context, ArrayList<XiaozhongNewResp> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_xiaozhong_artical, parent, false);
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
        holder.tvSource.setText(items.get(position).getHits()+" hit(s)");
        holder.tvDate.setText(items.get(position).getCreateDate());

        //加载网络图片、加载中的图片和加载失败的图片之后再补、太晚了，要睡觉
        String url = context.getResources().getString(R.string.url_download_img)+items.get(position).getPictureUrl();
        Picasso.with(context).load(url).into(holder.ivPic);

        //设置点击事件
        holder.ivPic.setOnClickListener(holder);
        holder.tvTitle.setOnClickListener(holder);
        holder.tvSource.setOnClickListener(holder);
        holder.tvDate.setOnClickListener(holder);
        Long userId = BaseApplication.getLoginBean().getData().getUser().getUid();
        if (userId != items.get(position).getUserId()){
            holder.btnDelete.setVisibility(View.GONE);
        }else {
            holder.btnDelete.setVisibility(View.VISIBLE);
            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("xxx", "caonima");
                }
            });
        }
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
        private Button btnDelete;
        private OnViewHolderClickListener mViewHolderClickListener;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ivPic = (ImageView) itemView.findViewById(R.id.iv_xiaohzong_img);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_xiaozhong_title);
            tvSource = (TextView) itemView.findViewById(R.id.tv_xiaozhong_hits);
            tvDate = (TextView) itemView.findViewById(R.id.tv_xiaozhong_date);
            btnDelete = (Button) itemView.findViewById(R.id.btn_xiaozhong_delete);
        }

        @Override
        public void onClick(View v) {
            if (mViewHolderClickListener!=null){
                Log.e("xxx", "zhengmingwobeidianjile");
//                Intent intent = new Intent(context, NewsDetailsActivity.class);
//                intent.putExtra(NewsInfo.URL, items.get(mViewHolderClickListener.getPosition()).getUrl());
//                intent.putExtra(NewsInfo.TITLE, items.get(mViewHolderClickListener.getPosition()).getTitle());
//                intent.putExtra(NewsInfo.PICTUREURL, items.get(mViewHolderClickListener.getPosition()).getThumbnail_pic_s());
//                intent.putExtra(NewsInfo.UNIQUEKEY, items.get(mViewHolderClickListener.getPosition()).getUniquekey());
//                intent.putExtra(NewsInfo.NEWDATE, items.get(mViewHolderClickListener.getPosition()).getDate());
//                context.startActivity(intent);
                /////////////////////////////////////////////点击新闻，加入历史记录
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
    public void setItems(ArrayList<XiaozhongNewResp> items){
        this.items = items;
        this.notifyDataSetChanged();
    }
}
