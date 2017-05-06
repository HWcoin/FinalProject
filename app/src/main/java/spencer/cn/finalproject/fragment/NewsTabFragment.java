package spencer.cn.finalproject.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import spencer.cn.finalproject.manager.LocalDataManager;
import spencer.cn.finalproject.manager.NetWorkManager;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Administrator on 2017/3/6.
 */

public class NewsTabFragment extends Fragment {
    private RecyclerView newsItems;
    private ItemsAdapter newsAdapter;
    private NewType _type;
    Gson parser = new GsonBuilder().serializeNulls().create();
    private Handler newsHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            if (msg.what == 0xf33){
                String gsonStrings = (String) msg.obj;
                GsonNews requestNews = parser.fromJson(gsonStrings, GsonNews.class);
                String cachefilename = _type.getType().concat(".txt");
                LocalDataManager.pullToCache(getActivity(), requestNews, cachefilename);
                refreshDatas(requestNews);
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
        Log.e("xe", "onstart");
//        requestForDatas();
    }
    public static NewsTabFragment newInstance(NewType type){
        NewsTabFragment fragment = new NewsTabFragment();
        fragment.setType(type);
        return  fragment;
    }
    public NewType getType(){
        return this._type;
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


    public void refreshDatas(GsonNews requestNews){
        newsAdapter = new ItemsAdapter(getActivity(), requestNews.getData());
        if (newsItems != null){
            newsItems.setAdapter(newsAdapter);
//            newsAdapter.setItems(requestNews.getData());
            newsAdapter.notifyDataSetChanged();
        }
    }


    //////////
    // 读取缓存、
    // 缓存不存在，-> 请求网络数据
    //缓存存在->判断上一次的存取时间，超过5分钟请求网络数据，否则只显示缓存
    public void requestForDatas() {
        /**
         * type.txt:文件保存新闻缓存
         * type.xml:保存文件最近一次缓存的时间
         * typexml包含一个 _type:long数据
         */
        String cachefilename = _type.getType().concat(".txt");
//

        SharedPreferences settings = getActivity().getSharedPreferences(_type.getType(), MODE_PRIVATE);
        Long curMill = System.currentTimeMillis();
        Long oldMill = settings.getLong("_"+_type.getType(), 0);
        GsonNews gCaches = LocalDataManager.popFromCache(getActivity(), cachefilename);
        if (gCaches == null){
            String url = getActivity().getResources().getString(R.string.url_get_type_news);
            url = url + _type.getType();
            NetWorkManager.doGet(url, new NewsCallBack() {
                @Override
                public void onNewsReturn(String gstring) {
                    Message msg = new Message();
                    msg.what = 0xf33;
                    msg.obj = gstring;
                    newsHandler.sendMessage(msg);
                }
            });
        }else {
            refreshDatas(gCaches);
        }
//        SharedPreferences.Editor editor = settings.edit();
//        if (curMill-oldMill < 1000 * 60 * 5){//大于5分钟刷新
//            http://120.25.97.250:8008/new/info/list?type

//        }

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
