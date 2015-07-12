package com.aic.aicdetactor.check;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aic.aicdetactor.R;
import com.aic.aicdetactor.myApplication;
import com.aic.aicdetactor.R.id;
import com.aic.aicdetactor.R.layout;


import com.aic.aicdetactor.comm.CommonDef;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class DeviceActivity extends Activity {
	int mStationIndex = 0;
	int mRouteIndex = 0;
	ListView mListView = null;
	String TAG = "luotest";
	
	String mCheckNameStr = null;
	String mRouteNameStr = null;
	
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

		requestWindowFeature(Window.FEATURE_NO_TITLE);  //无title  
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
		              WindowManager.LayoutParams.FLAG_FULLSCREEN);  
		setContentView(R.layout.device_activity);
		Log.d("test", "startDevideActivity");
		Intent intent = getIntent();
		mRouteIndex = intent.getExtras().getInt(CommonDef.ROUTE_INDEX);
		mStationIndex = intent.getExtras().getInt(CommonDef.STATION_INDEX);
		String oneCatalog = intent.getExtras().getString(CommonDef.ONE_CATALOG);
		mCheckNameStr = intent.getExtras().getString(CommonDef.CHECKNAME);
		String indexStr = intent.getExtras().getString(CommonDef.LISTITEM_INDEX);
		TextView planNameTextView = (TextView) findViewById(R.id.planname);
		planNameTextView.setText(oneCatalog);
		Log.d(TAG, "oneCatalog is " + oneCatalog + "mCheckNameStr is " + mCheckNameStr);
		TextView RouteNameTextView = (TextView) findViewById(R.id.station_text_name);
		mRouteNameStr = intent.getExtras().getString(CommonDef.ROUTENAME);
		RouteNameTextView.setText(mRouteNameStr);

		Log.d(TAG, "ONcREATE index is " + mStationIndex);
		mListView = (ListView) findViewById(R.id.listView);
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		try {

			List<Object> deviceItemList = ((myApplication) getApplication())
					.getDeviceItemList(mRouteIndex,mStationIndex);

			for (int i = 0; i < deviceItemList.size(); i++) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("index",""+(mStationIndex+1) );
				map.put("device_name", ((myApplication) getApplication())
						.getDeviceItemName(deviceItemList.get(i)));
				((myApplication) getApplication())
						.getDeviceItemDefList(deviceItemList.get(i));
				map.put("deadline", "2015-06-20 10:00");
				map.put("status", "已检查");
				map.put("progress",
						"2/"
								+ ((myApplication) getApplication())
										.getDevicePartItemCount(deviceItemList
												.get(i)));
				list.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		SimpleAdapter adapter = new SimpleAdapter(this, list,
				R.layout.checkitem, new String[] { "index", "device_name",
						"deadline", "status", "progress" }, new int[] {
						R.id.index, R.id.pathname, R.id.deadtime, R.id.status,
						R.id.progress });
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				HashMap<String, String> map = (HashMap<String, String>) mListView
						.getItemAtPosition(arg2);
				Intent intent = new Intent();
				intent.putExtra(CommonDef.ROUTENAME, mRouteNameStr);
				intent.putExtra(CommonDef.ROUTE_INDEX, mRouteIndex);
				intent.putExtra(CommonDef.STATION_INDEX, mStationIndex);
				intent.putExtra(CommonDef.DEVICE_INDEX, arg2);
				intent.putExtra(CommonDef.ONE_CATALOG, "计划巡检");
				intent.putExtra(CommonDef.ROOTNAME, mRouteNameStr);
				intent.putExtra(CommonDef.CHECKNAME, map.get("device_name"));
				intent.setClass(getApplicationContext(),
						DeviceItemActivity.class);
				startActivity(intent);
			}
		});

		ImageView imageView = (ImageView) findViewById(R.id.imageView1);
		imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Log.d(TAG, "imageView.setOnClickListener");
				// TODO Auto-generated method stub
				finish();
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
