package spencer.cn.finalproject.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import spencer.cn.finalproject.R;
import spencer.cn.finalproject.dojo.IntegralInfoResp;

/**
 *  created at 2016/7/2 15:09
 *  @author : 吴培健
 *  @todo   : 装载新闻条目的RecycleViewAdapter
 */
public class PointsDescAdapter extends RecyclerView.Adapter<PointsDescAdapter.CommentViewHolder>{

    private Context context;
    private ArrayList<IntegralInfoResp> items;
    SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public PointsDescAdapter(Context context, ArrayList<IntegralInfoResp> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_points_details, parent, false);
        return new CommentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, final int position) {
        /**
         * 设置点击事件，获得被点击的位置position
         */
        holder.tvDate.setText(sdf.format(items.get(position).getCreateDate()));
        holder.tvPoints.setText(items.get(position).getChangeIntegral()+"");
        holder.tvDesc.setText(items.get(position).getRemarks());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    //控件装载器
    public class CommentViewHolder extends RecyclerView.ViewHolder{
        TextView tvDate;
        TextView tvPoints;
        TextView tvDesc;


        public CommentViewHolder(View itemView) {
            super(itemView);

            tvDate = (TextView) itemView.findViewById(R.id.tv_points_change_date);
            tvPoints = (TextView) itemView.findViewById(R.id.tv_point_change_points);
            tvDesc = (TextView) itemView.findViewById(R.id.tv_point_change_desc);

        }

    }
    //设置数据源，刷新界面
    public void setItems(ArrayList<IntegralInfoResp> items){
        this.items = items;
        this.notifyDataSetChanged();
    }
}
