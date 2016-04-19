package com.cain.jpush.web;

import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * Created by Administrator on 16-4-19.
 * User:cain[FR]
 * Date:16-4-19
 * Email:1543880711@qq.com
 * ProjectName:jpush
 */
public class MyChromeClient extends WebChromeClient{

   private  BrowerInterface mBrowerInterface;

    /**
     * 当网页的标题获得了之后，会调用这个方法，显示标题；
     * @param view
     * @param title
     */
    @Override
    public void onReceivedTitle(WebView view, String title) {
        if (mBrowerInterface!=null) {
            mBrowerInterface.onReceivedTitle(title);
        }
    }

    /**
     * 网页加载的百分比
     * @param view
     * @param newProgress
     */
    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        if (mBrowerInterface!=null) {
            mBrowerInterface.onProgressChanged(newProgress);
        }
    }
}
