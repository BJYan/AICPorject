package com.aic.aicdetactor;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class NetTestActivity extends Activity {
private TextView mNetState = null;
private TextView mIsWifi= null;
private TextView m234G = null;
ConnectionChangeReceiver myReceiver = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);  //无title  
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
		              WindowManager.LayoutParams.FLAG_FULLSCREEN); 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nettest);
		
		mNetState = (TextView)findViewById(R.id.netcanlink);
		
		mIsWifi = (TextView)findViewById(R.id.textView2);
		
		m234G = (TextView)findViewById(R.id.textView3);
		
		IntentFilter filter=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        myReceiver=new ConnectionChangeReceiver();
        this.registerReceiver(myReceiver, filter);
        
		initData();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		unregisterReceiver(myReceiver);
		super.onDestroy();
	}
	public class ConnectionChangeReceiver extends BroadcastReceiver {
	    @Override
	    public void onReceive(Context context, Intent intent) {
	        ConnectivityManager connectivityManager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	        NetworkInfo  mobNetInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
	        NetworkInfo  wifiNetInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	        
	        if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
	        	mNetState.setText("网络不可以用");
	            
	        }else {
	            if(wifiNetInfo.isConnected()){
	            	mIsWifi.setText("Wi-Fi 已连接");
	            }
	        }
	    }
	}
	String type = null;

	void initData() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if (info == null) {
			type = "null";
			mNetState.setText("网络异常");
		} else {
			mNetState.setText("网络正常");
			if (info.getType() == ConnectivityManager.TYPE_WIFI) {

				type = "WIFI";
				mIsWifi.setText(type);
			} else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
				int subType = info.getSubtype();
				if (subType == TelephonyManager.NETWORK_TYPE_CDMA
						|| subType == TelephonyManager.NETWORK_TYPE_GPRS
						|| subType == TelephonyManager.NETWORK_TYPE_EDGE) {
					type = "2G";
				} else if (subType == TelephonyManager.NETWORK_TYPE_UMTS
						|| subType == TelephonyManager.NETWORK_TYPE_HSDPA
						|| subType == TelephonyManager.NETWORK_TYPE_EVDO_A
						|| subType == TelephonyManager.NETWORK_TYPE_EVDO_0
						|| subType == TelephonyManager.NETWORK_TYPE_EVDO_B) {
					type = "3G";
				} else if (subType == TelephonyManager.NETWORK_TYPE_LTE) {// LTE是3g到4g的过渡，是3.9G的全球标准
					type = "4G";
				}
				m234G.setText(type);
			}
		}}

}
