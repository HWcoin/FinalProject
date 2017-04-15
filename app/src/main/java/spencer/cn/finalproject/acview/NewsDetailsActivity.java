package spencer.cn.finalproject.acview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import spencer.cn.finalproject.R;
import spencer.cn.finalproject.dojo.NewsInfo;

public class NewsDetailsActivity extends AppCompatActivity {
    private WebView webView;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);

        webView = (WebView) findViewById(R.id.wv_news_details);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);//允许执行JavaScript脚本
        settings.setAllowFileAccess(true);//支持访问文件
        settings.setBuiltInZoomControls(true);//支持缩放

        intent = getIntent();
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
