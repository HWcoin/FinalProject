package spencer.cn.finalproject.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import spencer.cn.finalproject.R;
import spencer.cn.finalproject.iexport.ISelectedItem;
import spencer.cn.finalproject.util.PublicVar;

/**
 * Created by Administrator on 2017/3/26.
 */

public class ChangeIconAdapter extends RecyclerView.Adapter<ChangeIconAdapter.IconHolder> {
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private ISelectedItem callback;

    public ChangeIconAdapter(Context mContext){
        this.mContext = mContext;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }
    @Override
    public IconHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        IconHolder h = new IconHolder(mLayoutInflater.inflate(R.layout.item_change_icons, parent, false));
        h.setCallback(callback);
        return h;
    }

    @Override
    public void onBindViewHolder(IconHolder holder, final int position) {
        Bitmap conBitmap = BitmapFactory.decodeResource(mContext.getResources(), PublicVar.ICONS[position]);
        holder.content.setImageBitmap(conBitmap);
        holder.itemView.setOnClickListener(holder);
        holder.content.setOnClickListener(holder);
    }

    @Override
    public int getItemCount() {
        return PublicVar.ICONS.length;
    }

    public static class IconHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView background;
        ImageView content;
        ISelectedItem callback;

        public IconHolder(View itemView) {
            super(itemView);
            background = (ImageView) itemView.findViewById(R.id.iv_change_icon_background);
            content = (ImageView) itemView.findViewById(R.id.iv_change_icon_content);
        }

        @Override
        public void onClick(View v) {
            if (this.callback != null){
                this.callback.onSelected(getPosition());
            }
        }

        public void setCallback(ISelectedItem callback){
            this.callback = callback;
        }
    }
    public void setCallback(ISelectedItem callback){
        this.callback = callback;
    }
}
