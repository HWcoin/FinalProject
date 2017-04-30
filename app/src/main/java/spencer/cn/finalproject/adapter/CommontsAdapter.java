package spencer.cn.finalproject.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import spencer.cn.finalproject.R;
import spencer.cn.finalproject.dojo.CommentInfoResp;

/**
 *  created at 2016/7/2 15:09
 *  @author : 吴培健
 *  @todo   : 装载新闻条目的RecycleViewAdapter
 */
public class CommontsAdapter extends RecyclerView.Adapter<CommontsAdapter.CommentViewHolder>{

    private Context context;
    private ArrayList<CommentInfoResp> items;
    private Long userId;
    SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss E");

    public CommontsAdapter(Context context, ArrayList<CommentInfoResp> items, Long userId) {
        this.context = context;
        this.items = items;
        this.userId = userId;
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_comments, parent, false);
        return new CommentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, final int position) {
        /**
         * 设置点击事件，获得被点击的位置position
         */
        holder.tvUserName.setText(items.get(position).getUsername());
        holder.tvPostTime.setText(sdf.format(items.get(position).getCreateDate()));
        holder.tvPostContent.setText(items.get(position).getContent());

        //加载网络图片、加载中的图片和加载失败的图片之后再补、太晚了，要睡觉
        String url = context.getResources().getString(R.string.url_download_img)+items.get(position).getAvatar();
        Picasso.with(context).load(url).into(holder.ivUserIcon);

        holder.btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "left", Toast.LENGTH_LONG).show();
            }
        });
        holder.btnCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "center", Toast.LENGTH_LONG).show();
            }
        });
        if (userId != items.get(position).getUserId()){
            holder.btnRight.setVisibility(View.GONE);
        }else {
            holder.btnRight.setVisibility(View.VISIBLE);
            holder.btnRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Right", Toast.LENGTH_LONG).show();
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    //控件装载器
    public class CommentViewHolder extends RecyclerView.ViewHolder{
        ImageView ivUserIcon;
        TextView tvUserName;
        TextView tvPostTime;
        TextView tvPostContent;
        Button btnLeft;
        Button btnCenter;
        Button btnRight;

        public CommentViewHolder(View itemView) {
            super(itemView);
            ivUserIcon = (ImageView) itemView.findViewById(R.id.iv_user_icon);
            tvUserName = (TextView) itemView.findViewById(R.id.tv_user_name);
            tvPostTime = (TextView) itemView.findViewById(R.id.tv_post_time);
            tvPostContent = (TextView) itemView.findViewById(R.id.tv_post_content);
            btnLeft = (Button) itemView.findViewById(R.id.btn_left);
            btnCenter = (Button) itemView.findViewById(R.id.btn_center);
            btnRight = (Button) itemView.findViewById(R.id.btn_right);
        }

    }
    //设置数据源，刷新界面
    public void setItems(ArrayList<CommentInfoResp> items){
        this.items = items;
        this.notifyDataSetChanged();
    }
}