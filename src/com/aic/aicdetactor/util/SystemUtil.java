package com.aic.aicdetactor.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import android.util.Log;



public class  SystemUtil {

	static String TAG ="luotest";
	public  static String getSystemTime(){
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd   HH:mm:ss");
		String str = df.format(new Date());
		Log.d(TAG, "getSystemTime time is " + str);
		return str;
	}
	public static String createGUID(){
		String str = null;
		UUID uuid = UUID.randomUUID();		
		str = uuid.toString();
		Log.d(TAG, "createGUID create a new GUID is  " + str);
		return str;
	}
}
