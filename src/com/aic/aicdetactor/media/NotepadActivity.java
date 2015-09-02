package com.aic.aicdetactor.media;

import com.aic.aicdetactor.R;
import com.aic.aicdetactor.comm.CommonDef;
import com.aic.aicdetactor.comm.PartItem_Contact;
import com.aic.aicdetactor.util.SystemUtil;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class NotepadActivity extends Activity implements OnClickListener {
	private Button mFinish= null;
	private TextView mTimeStr= null;
	private TextView mPartItemName = null;
	private EditText mContent = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  //无title  
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
		              WindowManager.LayoutParams.FLAG_FULLSCREEN);  
		
		setContentView(R.layout.notepad);
		mFinish = (Button)findViewById(R.id.button1);
		mFinish.setOnClickListener(this);
		Intent intent = getIntent();
		String name = (String) intent.getExtras().get(CommonDef.check_unit_info.NAME);
		mTimeStr = (TextView)findViewById(R.id.time1);
		mPartItemName = (TextView)findViewById(R.id.partItemName);
		mContent = (EditText)findViewById(R.id.editText1);
		
		mPartItemName.setText(name);
		mTimeStr.setText(SystemUtil.getSystemTime(0));
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getId()){
		case R.id.button1:
			saveData();
			setResult(PartItem_Contact.PARTITEM_NOTEPAD_RESULT,null);
			finish();
			break;
		}
	}
	
	void saveData(){
		SharedPreferences mSharedPreferences= PreferenceManager.getDefaultSharedPreferences(NotepadActivity.this);		
		// 实例化SharedPreferences.Editor对象（第二步）
		SharedPreferences.Editor editor = mSharedPreferences.edit();
		// 用putString的方法保存数据
		editor.putString(CommonDef.PartItemData_Shered_info.Time, mTimeStr.getText().toString());
		editor.putString(CommonDef.PartItemData_Shered_info.Content, mContent.getText().toString());
			// 提交当前数据
		editor.commit();
	}
	

}
