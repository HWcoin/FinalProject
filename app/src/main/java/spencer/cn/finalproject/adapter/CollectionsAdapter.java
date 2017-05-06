package spencer.cn.finalproject.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import spencer.cn.finalproject.R;
import spencer.cn.finalproject.acview.NewsDetailsActivity;
import spencer.cn.finalproject.dojo.CollectionListResp;
import spencer.cn.finalproject.dojo.NewsInfo;
import spencer.cn.finalproject.dojo.resp.CollectListBean;
import spencer.cn.finalproject.iexport.NewsCallBack;
import spencer.cn.finalproject.listener.OnViewHolderClickListener;
import spencer.cn.finalproject.manager.LocalDataManager;
import spencer.cn.finalproject.manager.NetWorkManager;

/**
 *  created at 2016/7/2 15:09
 *  @author : 吴培健
 *  @todo   : 装载新闻条目的RecycleViewAdapter
 */
public class CollectionsAdapter extends RecyclerView.Adapter<CollectionsAdapter.ItemViewHolder>{

    private Context context;
    private ArrayList<CollectionListResp> items;
    private Gson parser = new GsonBuilder().serializeNulls().create();
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x259){

                String newDetail = (String) msg.obj;
                Log.e("ada", newDetail);
                CollectListBean result = parser.fromJson(newDetail, CollectListBean.class);
                if (result.getCode() == 200){
                    setItems(result.getData());
                }else {
                    Toast.makeText(context, result.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }
    };
    public CollectionsAdapter(Context context, ArrayList<CollectionListResp> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_collections, parent, false);
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
        holder.tvDate.setText(items.get(position).getNewDate());

        //加载网络图片、加载中的图片和加载失败的图片之后再补、太晚了，要睡觉
        String url = context.getResources().getString(R.string.url_download_img)+items.get(position).getPictureUrl();
        Picasso.with(context).load(url).into(holder.ivPic);

        //设置点击事件
        holder.ivPic.setOnClickListener(holder);
        holder.tvTitle.setOnClickListener(holder);
        holder.tvSource.setOnClickListener(holder);
        holder.tvDate.setOnClickListener(holder);
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = context.getResources().getString(R.string.url_post_cancel_collect);
                String accessToken = LocalDataManager.getAccessToken(context);
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("collectionId", items.get(position).getCollectionId()+"");
                NetWorkManager.doPost(url + accessToken, params, new NewsCallBack() {
                    @Override
                    public void onNewsReturn(String gstring) {
                        Message msg = new Message();
                        msg.what = 0x259;
                        msg.obj = gstring;
                        handler.sendMessage(msg);
                    }
                });
            }
        });
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
            ivPic = (ImageView) itemView.findViewById(R.id.iv_collect_img);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_collect_title);
            tvSource = (TextView) itemView.findViewById(R.id.tv_collect_hits);
            tvDate = (TextView) itemView.findViewById(R.id.tv_collect_date);
            btnDelete = (Button) itemView.findViewById(R.id.btn_collect_delete);
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
    public void setItems(ArrayList<CollectionListResp> items){
        this.items = items;
        this.notifyDataSetChanged();
    }
}
