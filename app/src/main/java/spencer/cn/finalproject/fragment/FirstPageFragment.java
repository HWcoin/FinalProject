package spencer.cn.finalproject.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import spencer.cn.finalproject.R;
import spencer.cn.finalproject.adapter.FunctionsFragmentAdapter;
import spencer.cn.finalproject.application.BaseApplication;
import spencer.cn.finalproject.dojo.BaseNewType;
import spencer.cn.finalproject.dojo.GsonNews;
import spencer.cn.finalproject.dojo.LoginBean;
import spencer.cn.finalproject.dojo.NewType;
import spencer.cn.finalproject.iexport.NewsCallBack;
import spencer.cn.finalproject.manager.LocalDataManager;
import spencer.cn.finalproject.manager.NetWorkManager;

import static spencer.cn.finalproject.manager.LocalDataManager.loadBaseConfig;

/**
 * Created by Administrator on 2017/3/5.
 */

public class FirstPageFragment extends Fragment {
    private AppCompatActivity activity;
    private TabLayout subTab;
    private ViewPager subPages;
    private SwipeRefreshLayout refresh;
    ArrayList<NewsTabFragment> fragmentList;
    FunctionsFragmentAdapter fragmentAdapter;
    public static String[] news_types = {
    };

    private Handler refreshHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0xfd1){
                String gsonStrings = (String) msg.obj;
                Gson parser = new GsonBuilder().serializeNulls().create();
                GsonNews requestNews = parser.fromJson(gsonStrings, GsonNews.class);
                NewsTabFragment cur = fragmentList.get(msg.arg1);
                NewType _type = cur.getType();
                String cachefilename = _type.getType().concat(".txt");
                LocalDataManager.pullToCache(getActivity(), requestNews, cachefilename);
                cur.refreshDatas(requestNews);
                refresh.setRefreshing(false);
            }
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(View view,  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = (AppCompatActivity) getActivity();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("fragment", "onStart");
        refreshDatas();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_first_layout, container,false);

        //初始化布局
        initViews(view);
        Log.e("fragment", "onCreateView");
        return view;

    }

    private void initViews(View v) {
        //初始化ViewPager
        refresh = (SwipeRefreshLayout) v.findViewById(R.id.srl_refresh);
        this.subPages = (ViewPager) v.findViewById(R.id.vp_sub_pages);
        this.subTab = (TabLayout) v.findViewById(R.id.layout_sub_tab);
        this.subTab.setTabMode(TabLayout.MODE_SCROLLABLE);
        this.subPages.setOffscreenPageLimit(20);


        /**
         * 设置下拉刷新
         */
        refresh.setColorSchemeResources(android.R.color.holo_blue_light,android.R.color.holo_green_light);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //访问网络数据

//                viewpager.getCurrentItem()
                final int position = subPages.getCurrentItem();
                NewType _type = fragmentList.get(position).getType();
                String url = getActivity().getResources().getString(R.string.url_get_type_news);
                url = url + _type.getType();
                NetWorkManager.doGet(url, new NewsCallBack() {
                    @Override
                    public void onNewsReturn(String gstring) {
                        Message msg = new Message();
                        msg.what = 0xfd1;
                        msg.arg1 = position;
                        msg.obj = gstring;
                        refreshHandler.sendMessage(msg);
                    }
                });
            }
        });

        initViewPage(v);
    }
    private void initTabsDatas(){
        BaseNewType baseNewType = loadBaseConfig(getActivity());
        LoginBean loginBean = BaseApplication.getLoginBean();
        if (loginBean!=null){
            List<Long> types_uid = loginBean.getData().getUserConfig().getUserNewType();
            news_types = new String[types_uid.size()];
            for (int i=0; i < types_uid.size(); i++){
                NewType _type = getNewTypeByUid(types_uid.get(i));
                news_types[i] = _type.getTypeName();
            }
        }else{
            news_types = new String[baseNewType.getData().size()];
            for (int i=0; i < baseNewType.getData().size(); i++){
                news_types[i] = baseNewType.getData().get(i).getTypeName();
            }
        }
    }
    private void refreshDatas(){
        initTabsDatas();

        fragmentList = new ArrayList<>();
        Log.e("caonima", news_types.length+"");
        for (int i = 0; i < news_types.length; i++) {
            NewType type = getNewTypeByName(news_types[i]);
            NewsTabFragment fragment = NewsTabFragment.newInstance(type);
            fragmentList.add(fragment);
        }

        if (fragmentAdapter != null){
            fragmentAdapter = new FunctionsFragmentAdapter(getActivity().getSupportFragmentManager(), fragmentList, news_types);
            Log.e("xxx", "ragmentAdapter.notifyDataSetChanged();");
//            fragmentAdapter.notifyDataSetChanged();
        }

    }
    private void initViewPage(View v) {


        initTabsDatas();



        for (int i=0; i < news_types.length; i++){
            TabLayout.Tab  tab = this.subTab.newTab();
            tab.setText(news_types[i]);
            this.subTab.addTab(tab);
        }

        fragmentList = new ArrayList<>();
        for (int i = 0; i < news_types.length; i++) {
            NewType type = getNewTypeByName(news_types[i]);
            NewsTabFragment fragment = NewsTabFragment.newInstance(type);
            fragmentList.add(fragment);
        }

        fragmentAdapter = new FunctionsFragmentAdapter(getActivity().getSupportFragmentManager(), fragmentList, news_types);
        this.subPages.setAdapter(fragmentAdapter);//给ViewPager设置适配器
        this.subTab.setupWithViewPager(this.subPages);//将TabLayout和ViewPager关联起来。
        this.subTab.setTabsFromPagerAdapter(fragmentAdapter);//给Tabs设置适配器
        this.subTab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                subPages.setCurrentItem(tab.getPosition());
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
        subPages.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        refresh.setEnabled(false);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        refresh.setEnabled(true);
                        break;
                }
                return false;
            }
        });
    }
    ///////////////根据新闻类型名字获取新闻类型对象
    private NewType getNewTypeByName(String name){
        LoginBean loginBean = BaseApplication.getLoginBean();
        List<NewType> lists = null;
        if (loginBean != null){
            lists = loginBean.getData().getUserConfig().getNewTypes();
        }else {
            lists = LocalDataManager.loadBaseConfig(getActivity()).getData();
        }

        for (int i=0; i < lists.size(); i++){
            if (name.equals(lists.get(i).getTypeName())){
                return lists.get(i);
            }
        }
        return null;
    }
    /////////////////根据uid获取新闻类型对象
    private NewType getNewTypeByUid(Long uid){
        LoginBean loginBean = BaseApplication.getLoginBean();
        List<NewType> lists = loginBean.getData().getUserConfig().getNewTypes();
        for (int i=0; i < lists.size(); i++){
            if (uid == lists.get(i).getUid()){
                return lists.get(i);
            }
        }
        return null;
    }
    @Override
    public void onPause() {
        super.onPause();
    }
}
