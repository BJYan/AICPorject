package com.aic.aicdetactor.Config;

import com.aic.aicdetactor.app.myApplication;
import com.aic.aicdetactor.comm.CommonDef;
import com.aic.aicdetactor.fragment.DownLoadFragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Config {
//public static String server_IpC="222.128.3.208";

public  String server_IpC="192.168.1.145";
public  int server_port=10000;
public static String  getServiceIP(){	
	Context context=myApplication.getApplication().getApplicationContext();
	SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
	return settings.getString(CommonDef.APP_Settings.ServiceIP,"192.168.1.145");
}

public static int  getServicePort(){	
	return 10000;
}
}
