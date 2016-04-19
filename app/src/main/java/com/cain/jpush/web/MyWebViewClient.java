package com.cain.jpush.web;

import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Administrator on 16-4-19.
 * User:cain[FR]
 * Date:16-4-19
 * Email:1543880711@qq.com
 * ProjectName:jpush
 */
public class MyWebViewClient extends WebViewClient {
    /**
     * 如果这个方法返回true，那么WebView不会自己加载网址；
     * 相当于网址的拦截，
     * @param view
     * @param event
     * @return
     */
    @Override
    public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
        return super.shouldOverrideKeyEvent(view, event);
    }
}
