package com.aic.aicdetactor;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.aic.aicdetactor.CustomDialog.ClickListenerInterface;
import com.aic.aicdetactor.app.myApplication;
import com.aic.aicdetactor.database.RouteDao;
import com.aic.aicdetactor.fragment.BlueToothFragment;
import com.aic.aicdetactor.fragment.BlueToothFragment.BlueToothListener;
import com.aic.aicdetactor.fragment.DownLoadFragment;
import com.aic.aicdetactor.fragment.LoginFragment;
import com.aic.aicdetactor.fragment.LoginFragment.LoginListener;
import com.aic.aicdetactor.fragment.Message_Fragment;
import com.aic.aicdetactor.fragment.RouteFragment;
import com.aic.aicdetactor.fragment.SearchFragment;
import com.aic.aicdetactor.fragment.Search_fragment;


public class Main extends Activity implements BlueToothListener,ClickListenerInterface {

	

	private boolean mIsLogin = false;
	private RadioGroup mGroup = null; 
	TestSetting testControl = null;
	public myApplication app = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);  //无title  
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
//		              WindowManager.LayoutParams.FLAG_FULLSCREEN);  
		setContentView(R.layout.main);	
		app = (myApplication) getApplication();
		testControl = new TestSetting(this.getApplicationContext());
		testControl.setAppTestKey(true);	
		Intent intent = getIntent();
		mIsLogin=intent.getExtras().getBoolean("isLog");
		String Name=intent.getExtras().getString("name");
		String pwd=intent.getExtras().getString("pwd");
		
		ActionBar actionBar = getActionBar();		
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME  
		        | ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_CUSTOM);  
		mGroup = (RadioGroup)findViewById(R.id.group);
		mGroup.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				switch(arg0.getCheckedRadioButtonId()){
				case R.id.btnA://BlueTooth
				{
					FragmentManager fragmentManager = getFragmentManager();
					FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
					Fragment fragment = new BlueToothFragment();	
					fragmentTransaction.replace(R.id.fragment_main,fragment);		
					fragmentTransaction.commit();
				}
					break;
				case R.id.btnB://巡检
				{
					if(mIsLogin){
					//跳转到巡检项页面
					FragmentManager fragmentManager = getFragmentManager();
					FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
					Fragment fragment = new RouteFragment();
					fragmentTransaction.replace(R.id.fragment_main,fragment);			
					fragmentTransaction.commit();
					}else{
						//Toast.makeText(getApplicationContext(), "巡检：您还没登录", Toast.LENGTH_LONG).show();
						//initFragment();
					}
				}
					break;
				case R.id.btnC://查询
				{
						FragmentManager fragmentManager = getFragmentManager();
						FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
						Fragment fragment = new SearchFragment();
						fragmentTransaction.replace(R.id.fragment_main,fragment);			
						fragmentTransaction.commit();				
				
				}
					break;
				case R.id.btnD://通知
					{	if(mIsLogin){	
						FragmentManager fragmentManager = getFragmentManager();
						FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
						Fragment fragment = new Message_Fragment();
						fragmentTransaction.replace(R.id.fragment_main,fragment);			
						fragmentTransaction.commit();
					}else{
						Toast.makeText(getApplicationContext(), "通知：您还没登录", Toast.LENGTH_LONG).show();
						//initFragment();
					}
					}
					break;
				case R.id.btnE://云端
				{
					FragmentManager fragmentManager = getFragmentManager();
					FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
					Fragment fragment = new DownLoadFragment();
					fragmentTransaction.replace(R.id.fragment_main,fragment);			
					fragmentTransaction.commit();
				}
					
					break;
				}
			}
			
		});
		initFragment(Name,pwd);
	}
	void initFragment(String Name,String pwd){		
//		FragmentManager fragmentManager = getFragmentManager();
//		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//		Fragment fragment = new LoginFragment();	
//		fragmentTransaction.replace(R.id.fragment_main,fragment);		
//		fragmentTransaction.commit();
		mIsLogin = true;
		app.mWorkerName = Name;
		app.mWorkerPwd = pwd;
		app.gBLogIn = true;
		app.setUserInfo(Name, pwd);
		//跳转到巡检项页面
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		Fragment fragment = new RouteFragment();
		Bundle args = new Bundle();
		args.putString("name", Name);
		args.putString("pwd", pwd);
		fragment.setArguments(args);
		fragmentTransaction.replace(R.id.fragment_main,fragment);			
		fragmentTransaction.commit();
	}
	
	

//	@Override
//	public void onClick(View arg0) {
//		// TODO Auto-generated method stub
//		switch(arg0.getId()){
//			case R.id.login:
//			
//				break;
//		}
//	}
	 
	String TAG = "luotest";
	
//	@Override
//	public void Click(boolean logIn,String Name,String pwd,String error) {
//		// TODO Auto-generated method stub
//		if(logIn){
//			mIsLogin = true;
//			app.mWorkerName = Name;
//			app.mWorkerPwd = pwd;
//			app.gBLogIn = true;
//			app.setUserInfo(Name, pwd);
//			//跳转到巡检项页面
//			FragmentManager fragmentManager = getFragmentManager();
//			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//			Fragment fragment = new RouteFragment();
//			Bundle args = new Bundle();
//			args.putString("name", Name);
//			args.putString("pwd", pwd);
//			fragment.setArguments(args);
//			fragmentTransaction.replace(R.id.fragment_main,fragment);			
//			fragmentTransaction.commit();
//			
//		}else{
//			Toast.makeText(this.getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();	
////			Toast.makeText(this.getApplicationContext(), "用户名或密码有误，请再次确认重试！", Toast.LENGTH_LONG).show();	
//		}
//	}
	@Override
	public void Click(boolean logIn) {
		// TODO Auto-generated method stub
		
	}
	 private long mExitTime;
	 @Override  
	 public boolean onKeyDown(int keyCode, KeyEvent event) {
         if (keyCode == KeyEvent.KEYCODE_BACK) {
                 if ((System.currentTimeMillis() - mExitTime) > 2000) {
                         Object mHelperUtils;
                         Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                         mExitTime = System.currentTimeMillis();

                 } else {
                         finish();
                 }
                 return true;
         }
         return super.onKeyDown(keyCode, event);
	 }
	 
	 @Override  
	  public boolean onCreateOptionsMenu(Menu menu) {  
	    getMenuInflater().inflate(R.menu.main, menu);  
	    return true;  
	  }  
	 @Override  
	  public boolean onOptionsItemSelected(MenuItem item) {  
	    switch (item.getItemId()) {  
	    case R.id.action_modify_pwd: {
	    	CustomDialog dialog=new CustomDialog(this, R.style.customDialog, R.layout.modify_password,app.mWorkerName);
	        dialog.show();
	        }
	      break;  
	    case R.id.action_changeWorker:
	    //	initFragment();
	    	break;
	    case R.id.action_more:{
			}
	    	break;
	    case R.id.action_about:{
	    	final Dialog dialog = new AboutDialog(this);
			dialog.show();
			}
	    	break;
	    	
	    default:  
	      break;  
	    }  
	    return true;  
	  }
	@Override
	public void doConfirm(String oldPwd, String newPwd, String surePwd) {
		// TODO Auto-generated method stub
		
	}  
	 
	
}

