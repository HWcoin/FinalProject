package spencer.cn.finalproject.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import spencer.cn.finalproject.R;
import spencer.cn.finalproject.adapter.GroupAdapter;
import spencer.cn.finalproject.dojo.GsonNews;
import spencer.cn.finalproject.dojo.resp.HotNewsResp;
import spencer.cn.finalproject.iexport.NewsCallBack;
import spencer.cn.finalproject.manager.NetWorkManager;
import spencer.cn.finalproject.util.PublicVar;

/**
 * Created by Administrator on 2017/5/4.
 */

public class HotNewsFragment extends Fragment {
    private TabLayout hotTabs;
    private ViewPager hotPages;
    SwipeRefreshLayout refreshHotNews;
    private ArrayList<BaseFragment> fragments;
    private GroupAdapter adapter;
    private String [] hotTitles = {
            "本周热门", "历史热门"
    };
    Gson parser = new GsonBuilder().serializeNulls().create();
    private Handler newsHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0xf781){
                refreshHotNews.setRefreshing(false);
                String gsonStrings = (String) msg.obj;
                int pos = msg.arg1;
                HotNewsResp requestNews = parser.fromJson(gsonStrings, HotNewsResp.class);
                if (requestNews != null){
                    fragments.get(pos).refreshDatas(requestNews);
                }else{
                    Toast.makeText(getActivity(), "链接超时", Toast.LENGTH_LONG).show();
                }
            }
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.page_hot_news, container, false);
        initViews(v);
        return v;
    }

    private void initViews(View v){
        hotTabs = (TabLayout) v.findViewById(R.id.tl_hot_news);
        hotPages = (ViewPager) v.findViewById(R.id.vp_hot_news);
        refreshHotNews = (SwipeRefreshLayout) v.findViewById(R.id.srl_refresh_hot_news);
        hotTabs.setTabMode(TabLayout.MODE_SCROLLABLE);
        hotPages.setOffscreenPageLimit(3);

        refreshHotNews.setColorSchemeResources(android.R.color.holo_blue_light,android.R.color.holo_green_light);
        refreshHotNews.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //访问网络数据

//                viewpager.getCurrentItem()
                final int position = hotPages.getCurrentItem();
                String url = PublicVar.hotUrls[position];
                NetWorkManager.doGet(url, new NewsCallBack() {
                    @Override
                    public void onNewsReturn(String gstring) {
                        Message msg = new Message();
                        msg.what = 0xf781;
                        msg.obj = gstring;
                        msg.arg1 = position;
                        newsHandler.sendMessage(msg);
                    }
                });
            }
        });

        fragments = new ArrayList<>();
        for (int i = 0; i < hotTitles.length; i++) {
            HotFragment fragment = HotFragment.newInstance(i);
            fragments.add(fragment);
        }

        adapter = new GroupAdapter(getActivity().getSupportFragmentManager(), fragments, hotTitles);
        hotPages.setAdapter(adapter);//给ViewPager设置适配器
        hotTabs.setupWithViewPager(hotPages);//将TabLayout和ViewPager关联起来。
        hotTabs.setTabsFromPagerAdapter(adapter);//给Tabs设置适配器
        hotTabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                hotPages.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        /**
         * 解决SwipeRefreshLayout与ViewPager冲突
         */
        hotPages.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        refreshHotNews.setEnabled(false);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        refreshHotNews.setEnabled(true);
                        break;
                }
                return false;
            }
        });
    }
}
