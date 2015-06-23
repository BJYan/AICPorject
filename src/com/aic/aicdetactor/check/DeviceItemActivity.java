package com.aic.aicdetactor.check;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aic.aicdetactor.R;
import com.aic.aicdetactor.myApplication;
import com.aic.aicdetactor.R.id;
import com.aic.aicdetactor.R.layout;
import com.aic.aicdetactor.util.MyJSONParse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class DeviceItemActivity extends Activity {

	ListView mListView =null;
	String TAG = "luotest";
	 public String mPath = "/sdcard/down.txt";
     MyJSONParse json = new MyJSONParse();
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.deviceitem_activity);
		
		Intent intent =getIntent();
		int stationIndex = intent.getExtras().getInt("stationIndex");
		int deviceIndex = intent.getExtras().getInt("deviceIndex");
		
		String  oneCatalog = intent.getExtras().getString("oneCatalog");
		String  rotename = intent.getExtras().getString("checkName");
		String  roteNmaeStr = intent.getExtras().getString("rotename");
		
		TextView planNameTextView  =(TextView)findViewById(R.id.planname);
		planNameTextView.setText(oneCatalog);
		
		TextView RouteNameTextView  =(TextView)findViewById(R.id.station_text_name);
		RouteNameTextView.setText(rotename);
		
		TextView secondcatalognameTextView  =(TextView)findViewById(R.id.secondcatalogname);
		secondcatalognameTextView.setText(roteNmaeStr);
		
		
		Log.d(TAG,"ONcREATE stationIndex is " +stationIndex +"deviceIndex"+deviceIndex);
		mListView = (ListView)findViewById(R.id.listView);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try{
						
			List<Object> partItemDataList = ((myApplication) getApplication()).getPartItemDataList(stationIndex,deviceIndex);
			Log.d(TAG,"partItemDataList IS " +partItemDataList.toString() +",partItemDataList size is "+partItemDataList.size());
			for(int i = 0;i<partItemDataList.size();i++){
				Map<String, Object> map = new HashMap<String, Object>();
				//Log.d(TAG,"partItemDataList.get(i) IS "+partItemDataList.get(i)); 
		        map.put("device_name", ((myApplication) getApplication()).getPartItemName(partItemDataList.get(i)));
		        
		        map.put("deadline", "2015-06-20 10:00");
				map.put("status", "已检查");
				map.put("progress", "2/6");
		        list.add(map);
			}
		}catch(Exception e){e.printStackTrace();}
		
		
		SimpleAdapter adapter = new SimpleAdapter(this, list,
				R.layout.checkitem, new String[] { "index","device_name","deadline","status",
						"progress" }, new int[] { R.id.index,R.id.pathname,R.id.deadtime,R.id.status,R.id.progress
						 });
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub				
//				HashMap<String,String> map=(HashMap<String,String>)mListView.getItemAtPosition(arg2);  
//				Intent intent = new Intent();
//				intent.putExtra("stationIndex", arg2);
//				intent.putExtra("title", "计划巡检");
//				intent.putExtra("checkName", map.get("pathname"));
//				intent.setClass(getApplicationContext(),
//						DeviceItemActivity.class);
//				startActivity(intent);
			}
		});
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

}
