package com.aic.aicdetactor;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.aic.aicdetactor.CustomDialog.ClickListenerInterface;
import com.aic.aicdetactor.app.myApplication;
import com.aic.aicdetactor.fragment.BlueTooth_Fragment;
import com.aic.aicdetactor.fragment.DownLoadFragment;
import com.aic.aicdetactor.fragment.Message_Fragment;
import com.aic.aicdetactor.fragment.RouteFragment;
import com.aic.aicdetactor.fragment.SearchFragment;


public class MainActivity extends CommonActivity implements ClickListenerInterface,
	OnClickListener{
	String TAG = "MainActivity";
	private RadioGroup mGroup = null; 
	ImageView settingBtn;
	TextView titleBarName;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		  
		setContentView(R.layout.main_activity);	
		app = (myApplication) getApplication();
	 	
		Intent intent = getIntent();

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
					Fragment fragment = new BlueTooth_Fragment();	
					fragmentTransaction.replace(R.id.fragment_main,fragment);		
					fragmentTransaction.commit();
					setActionBar("AIC传感器设置",false);
				}
					break;
				case R.id.btnB://巡检
				{
					if(app.isLogin()){
					//跳转到巡检项页面
					FragmentManager fragmentManager = getFragmentManager();
					FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
					Fragment fragment = new RouteFragment();
					fragmentTransaction.replace(R.id.fragment_main,fragment);			
					fragmentTransaction.commit(); 
					setActionBar("AIC巡检操作",false);
					}else{
						Toast.makeText(getApplicationContext(), "您还没登录,请登录", Toast.LENGTH_LONG).show();
						mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_ReLogin), 1000);
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
						setActionBar("AIC数据查询",false);
				}
					break;
				case R.id.btnD://通知
					{	if(app.isLogin()){	
						FragmentManager fragmentManager = getFragmentManager();
						FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
						Fragment fragment = new Message_Fragment();
						fragmentTransaction.replace(R.id.fragment_main,fragment);			
						fragmentTransaction.commit(); 
						setActionBar("AIC任务消息",false);
					}else{
						Toast.makeText(getApplicationContext(), "您还没登录,请登录", Toast.LENGTH_LONG).show();						
						mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_ReLogin), 1000);
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
					setActionBar("AIC通讯管理",false);
				}
					
					break;
				}
			}
			
		});
		initFragment();
	}
	void initFragment(){		
		//跳转到巡检项页面
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		Fragment fragment = new RouteFragment();
		fragmentTransaction.replace(R.id.fragment_main,fragment);			
		fragmentTransaction.commit();
	}
	
	
	public static final int MSG_ReLogin =0;
	Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch(msg.what){
			case MSG_ReLogin:
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, LoginActivity.class);
				startActivity(intent);
				finish();
				break;
				
			}
			super.handleMessage(msg);
		}
		
	};
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
	public void doConfirm(String oldPwd, String newPwd, String surePwd) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
	}  
	
	class SettingMenuListener implements PopupMenu.OnMenuItemClickListener{
		Context context;

		public SettingMenuListener(Context context) {
			// TODO Auto-generated constructor stub
			this.context = context;
		}
		
		@Override
		public boolean onMenuItemClick(MenuItem arg0) {
			// TODO Auto-generated method stub
			switch (arg0.getItemId()) {  
//    	    case R.id.action_modify_pwd: {
//    	    	CustomDialog dialog=new CustomDialog(MainActivity.this, R.style.customDialog, R.layout.modify_password,app.getLoginWorkerName());
//    	        dialog.show();
//    	        }
//    	      break;  
//    	    case R.id.action_changeWorker:
//    	    //	initFragment();
//    	    	break;
//    	    case R.id.action_more:{
//    			}
//    	    	break;
    	    case R.id.action_about:{
    	    	final Dialog dialog = new AboutDialog(MainActivity.this);
    			dialog.show();
    			}
    	    	break;
    	    	
    	    default:  
    	      break;  
    	    }  
			return true;
		}
		
	}
	
}

