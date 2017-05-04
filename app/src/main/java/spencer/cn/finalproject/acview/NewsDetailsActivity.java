package spencer.cn.finalproject.acview;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

import spencer.cn.finalproject.R;
import spencer.cn.finalproject.dojo.GetDetailsResp;
import spencer.cn.finalproject.dojo.NewsInfo;
import spencer.cn.finalproject.dojo.resp.CommentResp;
import spencer.cn.finalproject.dojo.resp.RequestDetailBean;
import spencer.cn.finalproject.iexport.NewsCallBack;
import spencer.cn.finalproject.manager.LocalDataManager;
import spencer.cn.finalproject.manager.NetWorkManager;
import spencer.cn.finalproject.util.PublicVar;

public class NewsDetailsActivity extends BaseActivity {
    private WebView webView;
    private Intent intent;
    private EditText comments;
    private Button send;
    private Button check;
    private Button collect;

    private String url;
    private String picUrl;
    private String title;
    private String uniqueKey;
    private String newDate;

    private GetDetailsResp resp;

    private Gson parser = new GsonBuilder().serializeNulls().create();
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0xf81){
                String newDetail = (String) msg.obj;
                RequestDetailBean result = parser.fromJson(newDetail, RequestDetailBean.class);
                if (result.getCode() != 200){
                    Toast.makeText(NewsDetailsActivity.this, result.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }
                resp = result.getData();
                if (TextUtils.isEmpty(url)){
                    Toast.makeText(NewsDetailsActivity.this, "URL解析错误，请重试", Toast.LENGTH_LONG).show();
                }else {
                    webView.loadUrl(url);
                    webView.setWebViewClient(new WebViewClient());//设置web视图
                }
            }else if (msg.what == 0xf82){
                String newComment = (String) msg.obj;
                CommentResp result = parser.fromJson(newComment, CommentResp.class);
                if (result.getCode() == 200 ){
                    comments.setText("");
                    Toast.makeText(NewsDetailsActivity.this, "发表成功，快去评论区看看吧！", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(NewsDetailsActivity.this, "发表评论超时！", Toast.LENGTH_LONG).show();
                }

            }else if (msg.what == 0xf83){
                String newComment = (String)msg.obj;
                Log.e("xx", newComment);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);


        intent = getIntent();
        initViews();

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);//允许执行JavaScript脚本
        settings.setAllowFileAccess(true);//支持访问文件
        settings.setBuiltInZoomControls(true);//支持缩放


    }

    private void initViews() {
        webView = (WebView) findViewById(R.id.wv_news_details);
        comments = (EditText) findViewById(R.id.edt_edit_commont);
        send = (Button) findViewById(R.id.btn_send_commont);
        check = (Button) findViewById(R.id.btn_check_commonts);
        collect = (Button) findViewById(R.id.btn_check_collect);

        //查看评论
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (resp == null || resp.getUid() <= 0){
                    Toast.makeText(NewsDetailsActivity.this, "等待新闻加载", Toast.LENGTH_LONG).show();
                    return;
                }
                Intent cui = new Intent(NewsDetailsActivity.this, ChangeUserInfoActivity.class);
                cui.putExtra(PublicVar.NEWS_ID, resp.getUid());
                cui.putExtra(PublicVar.VIEW_NAME, PublicVar.VIEW_CHECK_COMMENTS);
                startActivity(cui);
            }
        });
        //发送评论
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentContent = comments.getText().toString();
                if (TextUtils.isEmpty(commentContent)){
                    Toast.makeText(NewsDetailsActivity.this, "请输入评论", Toast.LENGTH_LONG).show();
                    return;
                }
                if (resp == null || resp.getUid()<=0){
                    Toast.makeText(NewsDetailsActivity.this, "等待新闻加载", Toast.LENGTH_LONG).show();
                    return;
                }

                String postCommentUrl = getResources().getString(R.string.url_post_a_comment);
                String accesssToken = LocalDataManager.getAccessToken(NewsDetailsActivity.this);
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("content", commentContent);
                params.put("newId", resp.getUid()+"");
                NetWorkManager.doPost(postCommentUrl + accesssToken, params, new NewsCallBack() {
                    @Override
                    public void onNewsReturn(String gstring) {
                        Message msg = new Message();
                        msg.what = 0xf82;
                        msg.obj = gstring;
                        handler.sendMessage(msg);
                    }
                });

            }
        });
        //收藏评论
        collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (resp == null || resp.getUid() <= 0){
                    Toast.makeText(NewsDetailsActivity.this, "等待新闻加载", Toast.LENGTH_LONG).show();
                    return;
                }
                String url = getResources().getString(R.string.url_post_collect_news);
                String accessToken = LocalDataManager.getAccessToken(NewsDetailsActivity.this);
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("newId", resp.getUid()+"");
                NetWorkManager.doPost(url + accessToken, params, new NewsCallBack() {
                    @Override
                    public void onNewsReturn(String gstring) {
                        Message msg = new Message();
                        msg.what = 0xf83;
                        msg.obj = gstring;
                        handler.sendMessage(msg);
                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        /**
         * 加载网页
         */
        url = intent.getStringExtra(NewsInfo.URL);
        picUrl = intent.getStringExtra(NewsInfo.PICTUREURL);
        uniqueKey = intent.getStringExtra(NewsInfo.UNIQUEKEY);
        title = intent.getStringExtra(NewsInfo.TITLE);
        newDate = intent.getStringExtra(NewsInfo.NEWDATE);


        String newsUrl = getResources().getString(R.string.url_post_news_Details);
        HashMap<String, String> params = new HashMap<>();
        params.put("pictureUrl", picUrl);
        params.put("title", title);
        params.put("uniquekey", uniqueKey);
        params.put("newDate", newDate);
        params.put("url", url);
        NetWorkManager.doPost(newsUrl, params, new NewsCallBack() {
            @Override
            public void onNewsReturn(String gstring) {
                Message msg = new Message();
                Log.e("xxx", gstring);
                msg.what = 0xf81;
                msg.obj = gstring;
                handler.sendMessage(msg);
            }
        });
    }

}
