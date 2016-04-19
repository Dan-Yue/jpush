package com.cain.jpush;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.cain.jpush.web.BrowerInterface;
import com.cain.jpush.web.MyChromeClient;
import com.cain.jpush.web.MyWebViewClient;
import com.umeng.analytics.MobclickAgent;

/**
 * 用于显示网页，网址通过Intent传递的；
 */
public class NewsDetailActivity extends AppCompatActivity implements BrowerInterface{

    private WebView mWebView;

    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        mWebView = (WebView) findViewById(R.id.new_web_view);

        mProgressBar = (ProgressBar) findViewById(R.id.load_progress_bar);

        //1.设置WebView的WebViewClient对象，如果不设置，WebView打开自带的浏览器；
        //能够进行网址的拦截，可以实现自定义网址
        mWebView.setWebViewClient(new MyWebViewClient());

        //1.2设置WebView界面相关的控制器；WebChromeClient
        mWebView.setWebChromeClient(new MyChromeClient());

        //2.设置开启JS
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);

        //3.加载网址
        Intent intent = getIntent();
        if (intent.hasExtra("url")) {
            String url = intent.getStringExtra("url");
            //加载网址
            mWebView.loadUrl(url);
        }
    }

    public void onResume() {
        super.onResume();

        MobclickAgent.onPageStart("news-detail");

        //友盟的统计分析
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();

        MobclickAgent.onPageEnd("new-detail");

        MobclickAgent.onPause(this);
    }

    @Override
    public void onReceivedTitle(String title) {
        setTitle(title);
    }

    @Override
    public void onProgressChanged(int progress) {
        mProgressBar.setProgress(progress);
    }
}
