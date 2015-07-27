package com.aic.aicdetactor;

import java.util.ArrayList;
import java.util.List;

import com.aic.aicdetactor.comm.CommonDef;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class TestSetting {
	/**
	 * 
	 */
	Context mContext;
	public TestSetting(Context context){
		mContext = context;
	}

	public void setAppTestKey(boolean isTest) {
		
		SharedPreferences mSharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(mContext);

	
		SharedPreferences.Editor editor = mSharedPreferences.edit();
		// 用putString的方法保存数据
		if (isTest) {
		editor.putString("file1", "/sdcard/down1.txt");
		editor.putString("file", "/sdcard/down.txt");
		}
		editor.putBoolean("isTest", isTest);
		
		editor.commit();
	}
	

	public List<String> getTestFile(){
		SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
	
		boolean isTest = mSharedPreferences.getBoolean("isTest",false);
		String file =  mSharedPreferences.getString("file",null);
		String file1 =  mSharedPreferences.getString("file1",null);
		
		if(isTest){
			List<String>list = new ArrayList<String>();
			list.add(file1);
			list.add(file);
			return list;
		}
		return null;
		
	}
}
