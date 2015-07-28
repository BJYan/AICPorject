package com.aic.aicdetactor;

import java.text.SimpleDateFormat;
import java.util.List;

import com.aic.aicdetactor.app.myApplication;
import com.aic.aicdetactor.check.RouteActivity;
import com.aic.aicdetactor.comm.CommonDef;
import com.aic.aicdetactor.database.RouteDao;
import com.aic.aicdetactor.util.SystemUtil;
import com.aic.aicdetactor.view.QuiteToast;

import android.app.ActivityGroup;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

/**
 * 
 * 椤圭洰鍚嶇О锛歝om.ch15    
 * 绫诲悕绉帮細MainActivity    
 * 绫绘弿杩帮細鍚姩椤甸潰
 * 鍒涘缓浜猴細鏂瑰媷   
 * 鍒涘缓鏃堕棿锛�012-11-23 涓嬪崍10:26:03   
 * Copyright (c) 鏂瑰媷-鐗堟潈鎵�湁
 */
public class MainActivity extends ActivityGroup implements OnClickListener {

	/* 搴曢儴鑿滃崟锛宐bar */
	private RadioGroup group;
	/* 涓棿閮ㄥ垎锛宐ody */
	private LinearLayout body;
	private Button mButtonLog = null;
	private EditText mEditTextUserName = null;
	private EditText mEditTextUserPwd = null;
	/* 椤堕儴鑿滃崟,tbar */
	//private TextView view_title;

	TestSetting testControl = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  //无title  
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
		              WindowManager.LayoutParams.FLAG_FULLSCREEN);  
		setContentView(R.layout.activity_main);
		findViews();
		setListeners();
		testControl = new TestSetting(this.getApplicationContext());
		testControl.setAppTestKey(true);		
	}

	private void findViews() {
		group = (RadioGroup) findViewById(R.id.group);
		body = (LinearLayout) findViewById(R.id.body);
		//view_title = (TextView) findViewById(R.id.title);
		mButtonLog = ( Button)findViewById(R.id.login);
		mButtonLog.setOnClickListener(this);
		mEditTextUserName = (EditText)findViewById(R.id.name);
		mEditTextUserPwd = (EditText)findViewById(R.id.password);
	}

	private void setListeners() {
		group.setOnCheckedChangeListener(onCheckedChangeListener);
	}

	private OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			body.removeAllViews();
			body.setGravity(Gravity.TOP);
		
			switch (checkedId) {
			/* Home椤�*/
			case R.id.btnA:
				/* 璁剧疆璺宠浆 */
				Intent homeIntent = new Intent();
				homeIntent.setClass(MainActivity.this, BtActivity.class);
				/*
				 * 1銆�Activity鍜孴ask(鏍�鐨勫叧绯�				 * 鏍堬紙Task锛夊氨鍍忎竴涓鍣紝鑰孉ctivity灏辩浉褰撲笌濉厖杩欎釜瀹瑰櫒鐨勪笢瑗匡紝
				 * 绗竴涓笢瑗匡紙Activity锛夊垯浼氬浜庢渶涓嬮潰锛屾渶鍚庢坊鍔犵殑涓滆タ锛圓ctivity锛夊垯浼氬湪鏈�綆绔�浠嶵ask涓彇鍑轰笢瑗匡紙Activity锛夊垯鏄粠鏈�《绔彇鍑猴紝涔�				 * 灏辨槸璇存渶鍏堝彇鍑虹殑鏄渶鍚庢坊鍔犵殑涓滆タ锛圓ctivity锛夛紝涓�绫绘帹锛屾渶鍚庡彇鍑虹殑鏄涓�娣诲姞鐨凙ctivity锛�				 * 鑰孉ctivity鍦═ask涓殑椤哄簭鏄�鍙互鎺у埗鐨勶紝閭ｅ垯鍦ˋctivity璺宠浆鏃剁敤鍒癐ntent Flag
				 * 
				 * 2銆両ntent.FLAG
				 * 濡傛灉activity鍦╰ask瀛樺湪锛屾嬁鍒版渶椤剁,涓嶄細鍚姩鏂扮殑Activity
				 * intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
				 * 
				 * 濡傛灉activity鍦╰ask瀛樺湪锛屽皢Activity涔嬩笂鐨勬墍鏈堿ctivity缁撴潫鎺�				 * intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				 * 
				 * 榛樿鐨勮烦杞被鍨�灏咥ctivity鏀惧埌涓�釜鏂扮殑Task涓�				 * intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				 * 
				 * 濡傛灉Activity宸茬粡杩愯鍒颁簡Task锛屽啀娆¤烦杞笉浼氬啀杩愯杩欎釜Activity
				 * intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				 */

				homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				/* 鑾峰彇璺宠浆椤甸潰Activity缁戝畾鐨凩ayout瑙嗗浘 */
				View homeView = getLocalActivityManager().startActivity("home", homeIntent).getDecorView();
				/* 娣诲姞Layout鍒癇ody甯冨眬涓�*/
				body.addView(homeView);
				/* 璁剧疆鏍囬鏂囨湰 */
				//view_title.setText("home");
				break;
			/* 鍒嗙被椤�*/
			case R.id.btnB:
				/* 璁剧疆璺宠浆 */
//				Intent categoryIntent = new Intent();
//				categoryIntent.setClass(MainActivity.this, RouteActivity.class);
//
//				categoryIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//				View categoryView = getLocalActivityManager().startActivity("category", categoryIntent).getDecorView();
//				body.addView(categoryView);
				Login();
				//view_title.setText("category");
				break;
			/* 璐墿杞﹂〉 */
			case R.id.btnC:
				/* 璁剧疆璺宠浆 */
				Intent shopcartIntent = new Intent();
				shopcartIntent.setClass(MainActivity.this, SearchActivity.class);

				shopcartIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

				View shopcartView = getLocalActivityManager().startActivity("shopcart", shopcartIntent).getDecorView();
				body.addView(shopcartView);

				//view_title.setText("shopcart");
				break;
			/* 鎴戠殑璐︽埛椤�*/
			case R.id.btnD:
				/* 璁剧疆璺宠浆 */
				Intent accountIntent = new Intent();
				accountIntent.setClass(MainActivity.this, NotificationActivity.class);
				accountIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				View accountView = getLocalActivityManager().startActivity("account", accountIntent).getDecorView();
				body.addView(accountView);
				//view_title.setText("account");
				break;
			/* 鏇村椤�*/
			case R.id.btnE:
				/* 璁剧疆璺宠浆 */
				Intent moreIntent = new Intent();
				moreIntent.setClass(MainActivity.this, DownupActivity.class);
				moreIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				View moreView = getLocalActivityManager().startActivity("more", moreIntent).getDecorView();
				body.addView(moreView);
				//view_title.setText("more");
				break;
			}

		}

	};

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getId()){
			case R.id.login:
				Login();
				break;
		}
	}
	 
	String TAG = "luotest";
	void Login(){
		String name = mEditTextUserName.getText().toString();
		String pwd = mEditTextUserPwd.getText().toString();
		//search table 
		TestSetting test = new TestSetting(this.getApplicationContext());
		List<String> testlist = test.getTestFile();

		if (testlist != null) {
			for (int testi = 0; testi < testlist.size(); testi++) {
				Log.d(TAG,"read file from test=" + testi + ","+ testlist.get(testi));
				((myApplication) getApplication()).insertNewRouteInfo(SystemUtil.createGUID(), testlist.get(testi), this);
			}
		} 
		RouteDao dao = new RouteDao(this.getApplicationContext());
	List<String>fileList = 	dao.queryLogIn(name, pwd);
	
	if(fileList.size()<1){
	Toast.makeText(getApplicationContext(), "用户名或密码有误，请再次确认重试！", Toast.LENGTH_LONG).show();	
	}else{
		Intent categoryIntent = new Intent();
		categoryIntent.setClass(MainActivity.this, RouteActivity.class);
		categoryIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		categoryIntent.putExtra("name",name );
		categoryIntent.putExtra("pwd",pwd );
		View categoryView = getLocalActivityManager().startActivity("category", categoryIntent).getDecorView();
		body.addView(categoryView);
	}
	}
}

