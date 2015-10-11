package com.aic.aicdetactor.util;

import android.util.Log;

public class MLog {
	final static boolean bOpenLog=false;
public static void Logd(String tag,String msg){
	if(bOpenLog){
	Log.d(tag,msg);
	}
}

public static void Loge(String tag,String msg){
	if(bOpenLog){
	Log.e(tag,msg);
	}
}
public static void Logi(String tag,String msg){
	if(bOpenLog){
	Log.i(tag,msg);
	}
}
public static void Logw(String tag,String msg){
	if(bOpenLog){

	Log.w(tag,msg);
	}
}
}
