package spencer.cn.finalproject.acview;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import spencer.cn.finalproject.R;
import spencer.cn.finalproject.dojo.NewsInfo;
import spencer.cn.finalproject.util.PublicVar;

public class NewsDetailsActivity extends BaseActivity {
    private WebView webView;
    private Intent intent;
    private EditText comments;
    private Button send;
    private Button check;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);

        initViews();


        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);//允许执行JavaScript脚本
        settings.setAllowFileAccess(true);//支持访问文件
        settings.setBuiltInZoomControls(true);//支持缩放

        intent = getIntent();
    }

    private void initViews() {
        webView = (WebView) findViewById(R.id.wv_news_details);
        comments = (EditText) findViewById(R.id.edt_edit_commont);
        send = (Button) findViewById(R.id.btn_send_commont);
        check = (Button) findViewById(R.id.btn_check_commonts);

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cui = new Intent(NewsDetailsActivity.this, ChangeUserInfoActivity.class);
                cui.putExtra(PublicVar.VIEW_NAME, PublicVar.VIEW_CHECK_COMMENTS);
                startActivity(cui);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        /**
         * 加载网页
         */
        String url = intent.getStringExtra(NewsInfo.URL);
        if (TextUtils.isEmpty(url)){
            Toast.makeText(this, "URL解析错误，请重试", Toast.LENGTH_LONG).show();
        }else {
            webView.loadUrl(url);
            webView.setWebViewClient(new WebViewClient());//设置web视图
        }

    }


}
