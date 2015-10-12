package com.aic.aicdetactor;

import java.util.List;

import com.aic.aicdetactor.app.myApplication;
import com.aic.aicdetactor.comm.CommonDef;
import com.aic.aicdetactor.database.RouteDao;
import com.aic.aicdetactor.util.SystemUtil;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends CommonActivity implements OnClickListener {

	private Button mLogInButton = null;
	private CheckBox mSaveUInfoCheckBox =null;
	private CheckBox mNoLogModeCheckBox =null;
	private String mLogName=null;
	private String mLogPwd = null;
	private String error= null;
	private EditText mEditTextUserName = null;
	private EditText mEditTextUserPwd = null;
	String TAG = "luotest.mainActivity";
	private SharedPreferences mSharedPreferences = null;
	private boolean bSaveUInfo = false;
	public myApplication app = null;
	//TestSetting testControl = null;
	MainHandler mainHandler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);  //无title  
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
		              WindowManager.LayoutParams.FLAG_FULLSCREEN); 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainactivity);
		
		mainHandler = new MainHandler(this);
		mEditTextUserName = (EditText)findViewById(R.id.editText1);
		mEditTextUserPwd = (EditText)findViewById(R.id.editText2);
		mLogInButton = (Button) findViewById(R.id.button1);
		mLogInButton.setOnClickListener(this);
		
		app = (myApplication) getApplication();
		mSaveUInfoCheckBox = (CheckBox) findViewById(R.id.checkBox1);
		mNoLogModeCheckBox = (CheckBox) findViewById(R.id.checkBox2);
		mNoLogModeCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg1){
					mEditTextUserName.setEnabled(false);
					mEditTextUserPwd.setEnabled(false);
				}else{
					mEditTextUserName.setEnabled(true);
					mEditTextUserPwd.setEnabled(true);
				}
			}});
		
		getUInfo();
		
		//testControl = new TestSetting(this.getApplicationContext());
		//testControl.setAppTestKey(true);	
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub 
		switch(arg0.getId()){ 
		case R.id.button1:
			if(mNoLogModeCheckBox.isChecked()){
				Intent intent = new Intent();
				intent.putExtra("isLog", false);
				intent.putExtra("name", "");
				intent.putExtra("pwd", "");
				intent.setClass(getApplicationContext(),
						MainActivity.class);
				startActivity(intent);
				finish();
			}else {
				showLoadingDialog("正在登录，请稍后……");
				new LoginThread().start();
			}
			break;
		}
	}
	boolean Login(){
		mLogName = mEditTextUserName.getText().toString();
		mLogPwd = mEditTextUserPwd.getText().toString();
		//search table 
	//	TestSetting test = new TestSetting(this.getApplicationContext());
		//List<String> testlist = test.getTestFile();

//		if (testlist != null) {
//			boolean bTempRoute=false;
//			for (int testi = 0; testi < testlist.size(); testi++) {
//				Log.d(TAG,"read file from test=" + testi + ","+ testlist.get(testi));
//				if(testlist.get(testi).contains("Temp")){
//					bTempRoute=true;
//				}
//				((myApplication) this.getApplication()).insertNewRouteInfo(SystemUtil.createGUID(), testlist.get(testi), this.getApplicationContext(),bTempRoute);
//			}
//		} 
		RouteDao dao = RouteDao.getInstance(this.getApplicationContext());
		ContentValues cv = new ContentValues();
		List<String>fileList = 	dao.queryLogIn(mLogName, mLogPwd,cv);
		error=cv.getAsString("error");
	Log.d(TAG," Login() error = "+ cv.get("error"));
	
	if(fileList.size()>0){	
		ContentValues cverr = new ContentValues();
//		dao.ModifyWorkerPwd(mLogName, mLogPwd, "11111111",cverr);
//		Log.d(TAG," Login() modify error = "+ cverr.get("error"));
		return true;
	}
	return false;
	}
	
	void saveUInfo(){
		if(mSharedPreferences ==null){
			mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
		}
		// 实例化SharedPreferences.Editor对象（第二步）
		SharedPreferences.Editor editor = mSharedPreferences.edit();
		// 用putString的方法保存数据
		editor.putString("name", mEditTextUserName.getText().toString());
		editor.putString("name2", mEditTextUserPwd.getText().toString());
		editor.putBoolean("SaveU", mSaveUInfoCheckBox.isChecked());
		editor.putBoolean("noLogM", mNoLogModeCheckBox.isChecked());
		// 提交当前数据
		editor.commit();
	}
	void getUInfo(){
		mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
		mLogName =  mSharedPreferences.getString("name",null);
		mLogPwd =  mSharedPreferences.getString("name2",null);
		bSaveUInfo =  mSharedPreferences.getBoolean("SaveU",false);
		//mLogName =  mSharedPreferences.getBoolean("noLogM",null);
		
		if(bSaveUInfo){
			mEditTextUserName.setText(mLogName);
			mEditTextUserPwd.setText(mLogPwd);
			mSaveUInfoCheckBox.setChecked(true);
		}
		
	}
	
	class MainHandler extends Handler{
		Context context;
		
		public MainHandler(Context context) {
			// TODO Auto-generated constructor stub
			this.context = context;
		}

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			boolean LoginResult = (Boolean) msg.obj;
			dismissLoadingDialog();
			if(LoginResult){
				if(mSaveUInfoCheckBox.isChecked()){
				saveUInfo();
				}
				app.mWorkerName = mLogName;
				app.mWorkerPwd = mLogPwd;
				app.gBLogIn = true;
				app.setUserInfo(mLogName, mLogPwd);
				
				Intent intent = new Intent();
				intent.putExtra("isLog", true);
				intent.putExtra("name", mLogName);
				intent.putExtra("pwd", mLogPwd);
				intent.setClass(getApplicationContext(),
						MainActivity.class);
				startActivity(intent);
				finish();
			}else{
				Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();	
			}
		}
	}
	
	class LoginThread extends Thread {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			boolean LoginResult = Login();
			Message msg = Message.obtain();
			msg.obj = LoginResult;
			//mainHandler.sendMessage(msg);
			mainHandler.sendMessageDelayed(msg, 2000);
		}
	}
	
}
