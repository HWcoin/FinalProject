package spencer.cn.finalproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import java.util.ArrayList;

import spencer.cn.finalproject.R;
import spencer.cn.finalproject.dojo.NewType;
import spencer.cn.finalproject.iexport.ISelectMyTitle;
import spencer.cn.finalproject.iexport.ISelectTotalTitle;
import spencer.cn.finalproject.util.PublicVar;

/**
 * Created by Administrator on 2017/4/9.
 */

public class TitlesAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<NewType> dataSource;
    private int _type;
    private ISelectMyTitle myCallBack;
    private ISelectTotalTitle totalCallBack;

    public TitlesAdapter(Context mContext, ArrayList<NewType> dataSource, int _type){
        this.mContext = mContext;
        this.dataSource = dataSource;
        this._type = _type;
    }

    public void setMyTitleCallBack(ISelectMyTitle myCallBack){
        this.myCallBack = myCallBack;
    }

    public void setTotalCallBack(ISelectTotalTitle totalCallBack){
        this.totalCallBack = totalCallBack;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        final NewType selectItem = dataSource.get(position);
        TitleHolder holder;
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_titles, parent, false);
            holder = new TitleHolder();
            holder.title = (Button) convertView.findViewById(R.id.btn_title_content);
            holder.delete = (Button) convertView.findViewById(R.id.btn_title_delete);
            convertView.setTag(holder);
        }else{
            holder = (TitleHolder) convertView.getTag();
        }

        holder.title.setText(selectItem.getTypeName());
        if (selectItem.isShow()){
            holder.delete.setVisibility(View.VISIBLE);
        }else{
            holder.delete.setVisibility(View.GONE);
        }
        if (_type == PublicVar.TABS_MY_TITLES){
            //////////////////////////////////////////////////////////////////我的：长按事件
            holder.title.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    cleanDataSourceTag(true);
                    notifyDataSetChanged();
                    return false;
                }
            });
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (myCallBack != null){
                        myCallBack.onMyTitleSelected(selectItem);
                    }
                }
            });
        }else{
            /////////////////////////////////////////////////////////////////total：点击事件
            holder.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (totalCallBack != null){
                        totalCallBack.onTotleitleSelected(selectItem);
                    }
                }
            });
        }
        return convertView;
    }

    private void cleanDataSourceTag(boolean tagStatus){
        for (int i=0; i < dataSource.size(); i++){
            dataSource.get(i).setShow(tagStatus);
        }
    }

    static class TitleHolder{
        Button title;
        Button delete;

    }
}
