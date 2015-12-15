package com.aic.aicdetactor.Config;

import com.aic.aicdetactor.app.myApplication;
import com.aic.aicdetactor.comm.CommonDef;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;

public class Config {
//public static String server_IpC="222.128.3.208";

//private  String server_IpC="192.168.1.145";
public  int server_port=10000;
/**
 * 获取服务器的IP地址
 * @return
 */
public static String  getServiceIP(){	
	Context context=myApplication.getApplication().getApplicationContext();
	SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
	return settings.getString(CommonDef.APP_Settings.ServiceIP,"192.168.1.130");
}

/**
 * 后去服务器端口
 * @return
 */
public static int  getServicePort(){	
	return 10000;
}

/**
 * 获取终端MAC地址
 * @return
 */
public static String getMACAddress(){
	WifiManager wifi = (WifiManager) myApplication.getApplication().getSystemService(Context.WIFI_SERVICE);
	WifiInfo info = wifi.getConnectionInfo();
	//return info.getMacAddress();
	return "0C:D6:BD:2B:7C:68";
}

/**
 * 获取终端IP地址
 * @return
 */
public static String getLocalIPAddress(){
	WifiManager wifi = (WifiManager) myApplication.getApplication().getSystemService(Context.WIFI_SERVICE);
	WifiInfo info = wifi.getConnectionInfo();
	if(info!=null){
		int ipAddress = info.getIpAddress();  
		if (ipAddress != 0) {  
			return ((ipAddress & 0xff) + "." + (ipAddress >> 8 & 0xff) + "."   
		        + (ipAddress >> 16 & 0xff) + "." + (ipAddress >> 24 & 0xff));  
		}  
	}
	return null;
}
}
