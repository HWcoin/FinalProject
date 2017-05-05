package spencer.cn.finalproject.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import spencer.cn.finalproject.R;
import spencer.cn.finalproject.adapter.HotNewsAdapter;
import spencer.cn.finalproject.dojo.HotNews;
import spencer.cn.finalproject.dojo.resp.HotNewsResp;
import spencer.cn.finalproject.iexport.NewsCallBack;
import spencer.cn.finalproject.manager.NetWorkManager;
import spencer.cn.finalproject.util.PublicVar;

/**
 * Created by Administrator on 2017/5/4.
 */

public class HotFragment extends BaseFragment {
    private RecyclerView newsItems;
    private HotNewsAdapter newsAdapter;
    private String [] urls = {
        "http://120.25.97.250:8008/new/info/topNewsOnWeek?rows=10",
            "http://120.25.97.250:8008/new/info/topNewsOnHistory?rows=10"
    };
    Gson parser = new GsonBuilder().serializeNulls().create();
    private Handler newsHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            if (msg.what == 0xf17){
                String gsonStrings = (String) msg.obj;
                HotNewsResp requestNews = parser.fromJson(gsonStrings, HotNewsResp.class);
                if (requestNews != null){
                    refreshDatas(requestNews);
                }else{
                    Toast.makeText(getActivity(), "链接超时", Toast.LENGTH_LONG).show();
                }
            }
        }
    };
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.page_news_layout, container, false);
        initViews(v);
        requestForDatas();
        return v;
    }

    public void requestForDatas() {
        int _type = this.getArguments().getInt("type", 0);
        String url = PublicVar.hotUrls[_type];
        NetWorkManager.doGet(url, new NewsCallBack() {
            @Override
            public void onNewsReturn(String gstring) {
                Message msg = new Message();
                msg.what = 0xf17;
                msg.obj = gstring;
                newsHandler.sendMessage(msg);
            }
        });
    }

    public static HotFragment newInstance(int i){
        HotFragment instance = new HotFragment();
        Bundle args = new Bundle();
        args.putInt("type", i);
        instance.setArguments(args);
        return instance;
    }

    private void initViews(View v) {
        newsItems = (RecyclerView) v.findViewById(R.id.rv_news_item);
        newsItems.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        newsItems.setItemAnimator(new DefaultItemAnimator());
        newsAdapter = new HotNewsAdapter(getActivity(), new ArrayList<HotNews>());
        newsItems.setAdapter(newsAdapter);
    }

    public void refreshDatas(HotNewsResp requestNews){
        newsAdapter = new HotNewsAdapter(getActivity(), requestNews.getData());
        if (newsItems != null)
            newsItems.setAdapter(newsAdapter);
    }
}
