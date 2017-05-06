package spencer.cn.finalproject.acview;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import spencer.cn.finalproject.R;
import spencer.cn.finalproject.adapter.MyNewsComAdapter;
import spencer.cn.finalproject.application.BaseApplication;
import spencer.cn.finalproject.dojo.CommentInfoResp;
import spencer.cn.finalproject.dojo.resp.CommentResp;
import spencer.cn.finalproject.dojo.resp.MyNewsBean;
import spencer.cn.finalproject.iexport.NewsCallBack;
import spencer.cn.finalproject.manager.LocalDataManager;
import spencer.cn.finalproject.manager.NetWorkManager;
import spencer.cn.finalproject.util.PublicVar;

public class CommentActivity extends BaseActionBarActivity {
    ImageView articalImg;
    TextView articalTitle;
    TextView articalContent;
    SwipeRefreshLayout refresharticalComment;
    MyNewsComAdapter adapter;
    RecyclerView commentsContent;
    EditText edtContent;
    Button send;
    int curPage;
    ArrayList<CommentInfoResp> datas;

    Intent myNewsContent;
    Long curMyNewsId;
    Gson parser = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    private Handler newsHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            if (msg.what == 0xee1) {
                String gsonStrings = (String) msg.obj;
                MyNewsBean result = parser.fromJson(gsonStrings, MyNewsBean.class);
                if (result!=null && result.getCode()==200){
                    String url = getResources().getString(R.string.url_download_img)+result.getData().getPictureUrl();
                    Picasso.with(CommentActivity.this).load(url).into(articalImg);
                    articalTitle.setText(result.getData().getTitle());
                    articalContent.setText(result.getData().getContent());
                }else {
                    Toast.makeText(CommentActivity.this, "请求超时", Toast.LENGTH_SHORT).show();
                }
            }else if (msg.what == 0xee2){
                String gsonStrings = (String) msg.obj;
                Log.e("xxx", gsonStrings);
                CommentResp result = parser.fromJson(gsonStrings, CommentResp.class);
                if (result.getCode() == 200){
                    curPage = 1;
                    Toast.makeText(CommentActivity.this, "发表成功", Toast.LENGTH_SHORT).show();
                        datas = result.getData();
                        Long uid = BaseApplication.getLoginBean().getData().getUser().getUid();
                        adapter = new MyNewsComAdapter(CommentActivity.this, datas, uid);
                        commentsContent.setAdapter(adapter);

                }else {
                    curPage = 1;
                    Toast.makeText(CommentActivity.this, "请求超时", Toast.LENGTH_SHORT).show();
                }
            }else if (msg.what == 0xee3){
                String gsonStrings = (String) msg.obj;
                Log.e("xxx", gsonStrings);
                CommentResp result = parser.fromJson(gsonStrings, CommentResp.class);
                if (result.getCode() == 200){
                    curPage = 1;
                    if (datas==null){
                        datas = result.getData();
                        Long uid = BaseApplication.getLoginBean().getData().getUser().getUid();
                        adapter = new MyNewsComAdapter(CommentActivity.this, datas, uid);
                        commentsContent.setAdapter(adapter);
                    }else{
                        if ( result.getData().size() <= 0){
                            Toast.makeText(CommentActivity.this, "这已经是全部喽", Toast.LENGTH_LONG).show();
                            return;
                        }
                        for(int i=0; i < result.getData().size(); i++){
                            datas.add(0, result.getData().get(i));
                        }
                        adapter.setItems(datas);
                    }
                }else {
                    curPage = 1;
                    Toast.makeText(CommentActivity.this, "请求超时", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        initViews();
    }

    @Override
    protected void onStart() {
        super.onStart();
        myNewsContent = getIntent();
        curMyNewsId = myNewsContent.getLongExtra(PublicVar.MY_NEWS_ID, 0);
        if (curMyNewsId == 0L){
            Toast.makeText(this, "无效文章", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        requestForNewsDetail(curMyNewsId);
        curPage = 1;
        requestForCommentDetails(curMyNewsId);
    }

    private void requestForCommentDetails(Long curMyNewsId) {
        String url = getResources().getString(R.string.url_get_get_my_news_comments);
        String accessToken = LocalDataManager.getAccessToken(this);
        HashMap<String, String> params = new HashMap<>();
        params.put("accessToken", accessToken);
        params.put("newId", curMyNewsId+"");
        params.put("page", curPage+"");
        params.put("rows", 15+"");
        String tail = NetWorkManager.mapToGetParams(params);
        NetWorkManager.doGet(url + tail, new NewsCallBack() {
            @Override
            public void onNewsReturn(String gstring) {
                Message msg = new Message();
                msg.what = 0xee3;
                msg.obj = gstring;
                newsHandler.sendMessage(msg);
            }
        });
    }

    private void requestForNewsDetail(Long curMyNewsId) {
        String url = getResources().getString(R.string.url_get_my_news_details);
        String accessToken = LocalDataManager.getAccessToken(this);
        HashMap<String, String> params = new HashMap<>();
        params.put("accessToken", accessToken);
        params.put("newId", curMyNewsId+"");
        String tail = NetWorkManager.mapToGetParams(params);
        NetWorkManager.doGet(url + tail, new NewsCallBack() {
            @Override
            public void onNewsReturn(String gstring) {
                Message msg = new Message();
                msg.what = 0xee1;
                msg.obj = gstring;
                newsHandler.sendMessage(msg);
            }
        });
    }

    private void initViews() {
        articalImg = (ImageView) findViewById(R.id.iv_my_news_img);
        articalTitle = (TextView) findViewById(R.id.tv_my_news_title);
        articalContent = (TextView) findViewById(R.id.tv_my_news_content);
        refresharticalComment = (SwipeRefreshLayout) findViewById(R.id.srl_my_news_comment);
        commentsContent = (RecyclerView) findViewById(R.id.rv_my_news_comment);
        edtContent = (EditText) findViewById(R.id.edt_edit_mynews_commont);
        send = (Button) findViewById(R.id.btn_send_mynews_commont);

        commentsContent.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        commentsContent.setItemAnimator(new DefaultItemAnimator());

//        if (curUid <= 0L){
//            Toast.makeText(this, "等待新闻加载", Toast.LENGTH_LONG).show();
//            finish();
//            return;
//        }
        ///////////////////////加载新闻

        refresharticalComment.setColorSchemeResources(android.R.color.holo_blue_light,android.R.color.holo_green_light);
        refresharticalComment.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //访问网络数据
//                refreshComments.setRefreshing(false);
//                curPage += 1;
//                getComments(curUid, curPage);
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usrContent = edtContent.getText().toString();
                if (TextUtils.isEmpty(usrContent)){
                    Toast.makeText(CommentActivity.this, "内容不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                String url = getResources().getString(R.string.url_post_my_news_comments);
                String accessToken = LocalDataManager.getAccessToken(CommentActivity.this);
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("content", usrContent);
                params.put("newId", curMyNewsId+"");
                NetWorkManager.doPost(url + accessToken, params, new NewsCallBack() {
                    @Override
                    public void onNewsReturn(String gstring) {
                        Message msg = new Message();
                        msg.what = 0xee2;
                        msg.obj = gstring;
                        newsHandler.sendMessage(msg);
                    }
                });
            }
        });
    }
}
