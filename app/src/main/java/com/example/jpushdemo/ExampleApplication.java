package com.example.jpushdemo;

import android.app.Application;
import android.util.Log;

import com.cain.jpush.BuildConfig;

import cn.jpush.android.api.JPushInterface;

/**
 * For developer startup JPush SDK
 * <p/>
 * 一般建议在自定义 Application 类里初始化。也可以在主 Activity 里。
 */
public class ExampleApplication extends Application {
    private static final String TAG = "JPush";

    @Override
    public void onCreate() {
        Log.d(TAG, "[ExampleApplication] onCreate");
        super.onCreate();
        if (BuildConfig.DEBUG) {
            //软件的发布版本一定要把这个内容去掉
            JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
        }

        //真正的极光推送，启动入口，加载清单文件的AppKey配置，并进行客户端的注册
        JPushInterface.init(this);            // 初始化 JPush
    }
}
