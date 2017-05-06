package spencer.cn.finalproject.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;

import spencer.cn.finalproject.R;
import spencer.cn.finalproject.adapter.MyNewsAdapter;
import spencer.cn.finalproject.dojo.XiaozhongNewResp;
import spencer.cn.finalproject.dojo.resp.MyListBean;
import spencer.cn.finalproject.iexport.NewsCallBack;
import spencer.cn.finalproject.manager.LocalDataManager;
import spencer.cn.finalproject.manager.NetWorkManager;

/**
 * Created by Administrator on 2017/4/24.
 */

public class OthersNews extends BaseFragment {
    private SwipeRefreshLayout refreshMyNews;
    private RecyclerView mynewsItems;
    private MyNewsAdapter mynewsAdapter;
    private ArrayList<XiaozhongNewResp> datas;
    private int curPage;

    Gson parser = new GsonBuilder().serializeNulls().create();
    private Handler newsHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            if (msg.what == 0xe23){
                String gsonStrings = (String) msg.obj;
                MyListBean result = parser.fromJson(gsonStrings, MyListBean.class);
                if (result.getCode()==200){
                    refreshMyNews.setRefreshing(false);
                    if (datas==null || datas.size()==0){
                        datas = result.getData();
                        mynewsAdapter = new MyNewsAdapter(getActivity(), datas);
                        mynewsItems.setAdapter(mynewsAdapter);
                    }else{
                        if (result.getData().size()<=0){
                            Toast.makeText(getActivity(), "已经是全部内容了", Toast.LENGTH_LONG).show();
                            return;
                        }
                        for (int i=0; i < result.getData().size(); i++){
                            datas.add(0, result.getData().get(i));
                        }
                        mynewsAdapter.setItems(datas);
                    }
                }else {
                    Toast.makeText(getActivity(), "timeout", Toast.LENGTH_LONG).show();
                }
            }
        }
    };

    @Override
    public void onStart() {
        super.onStart();


    }
    public void getMyLists(int curpage){
        String url = getActivity().getResources().getString(R.string.url_get_shequ_list);
        HashMap<String, String> params = new HashMap<>();
        String accessToken = LocalDataManager.getAccessToken(getActivity());
        params.put("accessToken", accessToken);
        params.put("page", curpage+"");
        params.put("rows", 15+"");
        String tail = NetWorkManager.mapToGetParams(params);
        NetWorkManager.doGet(url+tail, new NewsCallBack() {
            @Override
            public void onNewsReturn(String gstring) {
                Message msg = new Message();
                msg.what = 0xe23;
                msg.obj = gstring;
                newsHandler.sendMessage(msg);
            }
        });
    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.page_my_news, container, false);
        initViews(v);
        curPage = 1;
        getMyLists(curPage);
        return v;
    }

    private void initViews(View v) {
        refreshMyNews = (SwipeRefreshLayout) v.findViewById(R.id.srl_my_news);
        mynewsItems = (RecyclerView) v.findViewById(R.id.rv_xiaozhong_artical_list);
        mynewsItems.setItemAnimator(new DefaultItemAnimator());
        mynewsItems.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mynewsAdapter = new MyNewsAdapter(getActivity(), new ArrayList<XiaozhongNewResp>());
        mynewsItems.setAdapter(mynewsAdapter);
        refreshMyNews.setColorSchemeResources(android.R.color.holo_blue_light,android.R.color.holo_green_light);
        refreshMyNews.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //访问网络数据
//                refreshComments.setRefreshing(false);
                curPage += 1;
                getMyLists(curPage);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
