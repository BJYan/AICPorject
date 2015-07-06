package com.aic.aicdetactor.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
	//有问题的的
	public static float ByteArrayToFloat(byte[] bytes) {		
		int i = ((((bytes[0] & 0xff) << 8 | (bytes[1] & 0xff)) << 8) | (bytes[2] & 0xff)) << 8 | (bytes[3] & 0xff);
		for(int k =0;k<bytes.length;k++){
			Log.d(TAG, "ByteArrayToFloat  bytes[" + k +"]" + bytes[k] + ","+i);
		}
		//int i = ((((bytes[3] & 0xff) << 8 | (bytes[2] & 0xff)) << 8) | (bytes[1] & 0xff)) << 8 | (bytes[0] & 0xff);
		float f1 = Float.intBitsToFloat(i);
		Log.d(TAG, "ByteArrayToFloat  " + f1);
		return f1;
	}	
}
