package spencer.cn.finalproject.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import spencer.cn.finalproject.R;
import spencer.cn.finalproject.adapter.ItemsAdapter;
import spencer.cn.finalproject.dojo.GsonNews;
import spencer.cn.finalproject.dojo.NewType;
import spencer.cn.finalproject.dojo.News;
import spencer.cn.finalproject.iexport.NewsCallBack;
import spencer.cn.finalproject.manager.NetWorkManager;

/**
 * Created by Administrator on 2017/3/6.
 */

public class NewsTabFragment extends Fragment {
    private RecyclerView newsItems;
    private ItemsAdapter newsAdapter;
    private NewType _type;

    private Handler newsHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0xffe){
                String gsonStrings = (String) msg.obj;
                Gson parser = new GsonBuilder().create();
                GsonNews requestNews = parser.fromJson(gsonStrings, GsonNews.class);
                newsAdapter = new ItemsAdapter(getActivity(), requestNews.getData());
                newsItems.setAdapter(newsAdapter);
            }
        }
    };
    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();


    }
    public static NewsTabFragment newInstance(NewType type){
        NewsTabFragment fragment = new NewsTabFragment();
        fragment.setType(type);
        return  fragment;
    }
    public void setType(NewType type){
        this._type = type;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_news_layout, container,false);
        initViews(view);
        requestForDatas();
        return view;
    }

    private void requestForDatas() {
        NetWorkManager.requestNews("http://120.25.97.250:8008/new/info/list",
                "top", new NewsCallBack() {
                    @Override
                    public void onNewsReturn(String gstring) {
                        Message msg = new Message();
                        msg.what = 0xffe;
                        msg.obj = gstring;
                        newsHandler.sendMessage(msg);
                    }
                });
    }

    private void initViews(View v) {
        newsItems = (RecyclerView) v.findViewById(R.id.rv_news_item);
        newsItems.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        newsItems.setItemAnimator(new DefaultItemAnimator());
        newsAdapter = new ItemsAdapter(getActivity(), new ArrayList<News>());
        newsItems.setAdapter(newsAdapter);
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
