package com.example.jpushdemo;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cain.jpush.R;
import com.umeng.analytics.MobclickAgent;

import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.InstrumentedActivity;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;


public class MainActivity extends InstrumentedActivity implements OnClickListener{

	private Button mInit;
	private Button mSetting;
	private Button mStopPush;
	private Button mResumePush;
	private EditText msgText;
	
	public static boolean isForeground = false;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		initView();   
		registerMessageReceiver();  // used for receive msg

		//API 的使用

		//1.如何设置Tag,通过代码来设置；
		//Tag实际上都是应该是服务器来维护的;

		Set<String> tags = new HashSet<>();

		tags.add("小鲜肉");
		tags.add("90后");
		tags.add("IT");
		tags.add("睡觉困难户");
		JPushInterface.setTags(this, tags, new TagAliasCallback() {
			@Override
			public void gotResult(int i, String s, Set<String> set) {
				//如果S!=null代表别名设置成功
				//如果set != null && set 有值，代表tag设置成功
				Toast.makeText(MainActivity.this,"设置成功",Toast.LENGTH_LONG).show();

			}
		});
	}
//
//	private Tencent mTencent;
//
//	public void btnQQLogin(View view){
//		//TODO:QQ的登录
//
//		//参数1：APP ID
//		mTencent = Tencent.createInstance();
//
//		//2.进行QQ登录
//		//2.1检查当前是否已经进行过QQ登录，如果登录过，就不用再登陆了
//		if (!mTencent.isSessionVaild()) {
//			//参数1：Activity
//			//参数2：scope API的范围，代表登录成功之后，客户端能够调用什么功能
//			//参数3：登录是否成功的接口回调
//			mTencent.login(this,"get_user_info",this);
//		}
//	}
	private void initView(){
		TextView mImei = (TextView) findViewById(R.id.tv_imei);
		String udid =  ExampleUtil.getImei(getApplicationContext(), "");
        if (null != udid) mImei.setText("IMEI: " + udid);
        
		TextView mAppKey = (TextView) findViewById(R.id.tv_appkey);
		String appKey = ExampleUtil.getAppKey(getApplicationContext());
		if (null == appKey) appKey = "AppKey异常";
		mAppKey.setText("AppKey: " + appKey);

		String packageName =  getPackageName();
		TextView mPackage = (TextView) findViewById(R.id.tv_package);
		mPackage.setText("PackageName: " + packageName);

		String deviceId = ExampleUtil.getDeviceId(getApplicationContext());
		TextView mDeviceId = (TextView) findViewById(R.id.tv_device_id);
		mDeviceId.setText("deviceId:" + deviceId);
		
		String versionName =  ExampleUtil.GetVersion(getApplicationContext());
		TextView mVersion = (TextView) findViewById(R.id.tv_version);
		mVersion.setText("Version: " + versionName);
		
	    mInit = (Button)findViewById(R.id.init);
		mInit.setOnClickListener(this);
		
		mStopPush = (Button)findViewById(R.id.stopPush);
		mStopPush.setOnClickListener(this);
		
		mResumePush = (Button)findViewById(R.id.resumePush);
		mResumePush.setOnClickListener(this);
		
		mSetting = (Button)findViewById(R.id.setting);
		mSetting.setOnClickListener(this);
		
		msgText = (EditText)findViewById(R.id.msg_rec);
	}

	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.init:
			init();
			break;
		case R.id.setting:
			Intent intent = new Intent(MainActivity.this, PushSetActivity.class);
			startActivity(intent);
			break;
		case R.id.stopPush:
			JPushInterface.stopPush(getApplicationContext());
			break;
		case R.id.resumePush:
			JPushInterface.resumePush(getApplicationContext());
			break;
		}
	}
	
	// 初始化 JPush。如果已经初始化，但没有登录成功，则执行重新登录。
	private void init(){
		 JPushInterface.init(getApplicationContext());
	}


	@Override
	protected void onResume() {
		isForeground = true;
		super.onResume();

		//增加页面统计
		MobclickAgent.onPageStart("Main");

		//增加友盟统计分析的功能 Session统计
		MobclickAgent.onResume(this);
	}


	@Override
	protected void onPause() {
		isForeground = false;
		super.onPause();

		MobclickAgent.onPageEnd("Main");

		//增加友盟的统计分析功能
		MobclickAgent.onPause(this);
	}


	@Override
	protected void onDestroy() {
		unregisterReceiver(mMessageReceiver);
		super.onDestroy();
	}
	

	//for receive customer msg from jpush server
	private MessageReceiver mMessageReceiver;
	public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
	public static final String KEY_TITLE = "title";
	public static final String KEY_MESSAGE = "message";
	public static final String KEY_EXTRAS = "extras";
	
	public void registerMessageReceiver() {
		mMessageReceiver = new MessageReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(MESSAGE_RECEIVED_ACTION);
		registerReceiver(mMessageReceiver, filter);
	}

	public class MessageReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
              String messge = intent.getStringExtra(KEY_MESSAGE);
              String extras = intent.getStringExtra(KEY_EXTRAS);
              StringBuilder showMsg = new StringBuilder();
              showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
              if (!ExampleUtil.isEmpty(extras)) {
            	  showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
              }
              setCostomMsg(showMsg.toString());
			}
		}
	}
	
	private void setCostomMsg(String msg){
		 if (null != msgText) {
			 msgText.setText(msg);
			 msgText.setVisibility(View.VISIBLE);
         }
	}

}