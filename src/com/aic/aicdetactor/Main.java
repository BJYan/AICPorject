package com.aic.aicdetactor;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.aic.aicdetactor.app.myApplication;
import com.aic.aicdetactor.fragment.BlueToothFragment;
import com.aic.aicdetactor.fragment.BlueToothFragment.BlueToothListener;
import com.aic.aicdetactor.fragment.LoginFragment;
import com.aic.aicdetactor.fragment.LoginFragment.LoginListener;
import com.aic.aicdetactor.fragment.RouteFragment;


public class Main extends Activity implements OnClickListener,LoginListener,BlueToothListener {

	

	private boolean mIsLogin = false;
	private RadioGroup mGroup = null; 
	TestSetting testControl = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main);		
		testControl = new TestSetting(this.getApplicationContext());
		testControl.setAppTestKey(true);	
		
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
						initFragment();
					}
				}
					break;
				case R.id.btnC://查询
					if(mIsLogin){						
					}else{
						Toast.makeText(getApplicationContext(), "搜索：您还没登录", Toast.LENGTH_LONG).show();
						initFragment();
						Toast.makeText(getApplicationContext(), "搜索：功能还没完成", Toast.LENGTH_LONG).show();
					}
					
					break;
				case R.id.btnD://通知
					if(mIsLogin){						
					}else{
						Toast.makeText(getApplicationContext(), "通知：您还没登录", Toast.LENGTH_LONG).show();
						initFragment();
					}
					break;
				case R.id.btnE://云端
					if(mIsLogin){						
						}else{
							Toast.makeText(getApplicationContext(), "云端：您还没登录", Toast.LENGTH_LONG).show();
							initFragment();
						}
					
					break;
				}
			}
			
		});
		initFragment();
	}
	void initFragment(){		
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		Fragment fragment = new LoginFragment();	
		fragmentTransaction.replace(R.id.fragment_main,fragment);		
		fragmentTransaction.commit();
		
	}
	
	

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getId()){
			case R.id.login:
			
				break;
		}
	}
	 
	String TAG = "luotest";
	
	@Override
	public void Click(boolean logIn,String Name,String pwd) {
		// TODO Auto-generated method stub
		if(logIn){
			mIsLogin = true;
			((myApplication) getApplication()).mWorkerName = Name;
			((myApplication) getApplication()).mWorkerPwd = pwd;
			((myApplication) getApplication()).gBLogIn = true;
			((myApplication) getApplication()).setUserInfo(Name, pwd);
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
			
		}else{
			Toast.makeText(this.getApplicationContext(), "用户名或密码有误，请再次确认重试！", Toast.LENGTH_LONG).show();	
		}
	}
	@Override
	public void Click(boolean logIn) {
		// TODO Auto-generated method stub
		
	}
}

