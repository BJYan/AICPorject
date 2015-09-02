package com.aic.aicdetactor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.aic.aicdetactor.comm.CommonDef;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class TestSetting {
	private final String TAG = "luotest";
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
		editor.putString("file1", "/sdcard/AICNormal.txt");
		editor.putString("file", "/sdcard/AICNormal.txt");
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
			File f1 = new File(file);
		    if(f1!=null &&f1.exists()){
			list.add(file);
			}else{
				Log.e(TAG, "getTestFile() "+ file+" not exist");
			}
		    File f = new File(file1);
		    if(f!=null &&f.exists()){
			list.add(file);
		    }else{
		    	Log.e(TAG, "getTestFile() "+ file1+" not exist");
		    }
			return list;
		}
		return null;
		
	}
}
