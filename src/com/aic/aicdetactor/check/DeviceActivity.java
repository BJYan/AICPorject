package com.aic.aicdetactor.check;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aic.aicdetactor.R;
import com.aic.aicdetactor.R.id;
import com.aic.aicdetactor.R.layout;


import com.aic.aicdetactor.app.myApplication;
import com.aic.aicdetactor.comm.CommonDef;
import com.aic.aicdetactor.data.CheckStatus;


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
	String mStationNameStr = null;
	
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
		
		Intent intent = getIntent();
		mRouteIndex = intent.getExtras().getInt(CommonDef.route_info.LISTVIEW_ITEM_INDEX);
		mStationIndex = intent.getExtras().getInt(CommonDef.station_info.LISTVIEW_ITEM_INDEX);
		String oneCatalog = intent.getExtras().getString(CommonDef.ONE_CATALOG);
		mCheckNameStr = intent.getExtras().getString(CommonDef.device_info.NAME);
		//String indexStr = intent.getExtras().getString(CommonDef.LISTITEM_INDEX);
		mRouteNameStr = intent.getExtras().getString(CommonDef.route_info.NAME);
		mStationNameStr = intent.getExtras().getString(CommonDef.station_info.NAME);
		TextView planNameTextView = (TextView) findViewById(R.id.planname);
		planNameTextView.setText(oneCatalog);
		Log.d("test", "startDevideActivity:mRouteIndex=" +mRouteIndex +",mStationIndex="+mStationIndex+",mRouteNameStr is "+mRouteNameStr+",mCheckNameStr is "+mCheckNameStr);
		
		TextView RouteNameTextView = (TextView) findViewById(R.id.station_text_name);
		
		RouteNameTextView.setText(mRouteNameStr+">>"+mStationNameStr);

		Log.d(TAG, "oncreate() index is " + mStationIndex);
		mListView = (ListView) findViewById(R.id.listView);
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		try {

			/**
			 * 返回的DeviceItem 下的数组list,每项包含 DeviceName ,ItemDef,QueryNumber及PartItem
			 */
			List<Object> deviceItemList = ((myApplication) getApplication())
					.getDeviceItemList(mStationIndex);

			CheckStatus status = null;
			int itemindex = mStationIndex;
			for (int i = 0; i < deviceItemList.size(); i++) {
				Map<String, String> map = new HashMap<String, String>();
				status = ((myApplication) getApplication())
						.getDevicePartItemCount(deviceItemList
								.get(i));
				status.setContext(getApplicationContext());
				map.put(CommonDef.device_info.INDEX,""+(itemindex+1) );
				map.put(CommonDef.device_info.NAME, ((myApplication) getApplication())
						.getDeviceItemName(deviceItemList.get(i)));
				((myApplication) getApplication())
						.getDeviceItemDefList(deviceItemList.get(i));
				map.put(CommonDef.device_info.DEADLINE, status.mLastTime);
				map.put(CommonDef.device_info.STATUS, status.getStatus());
			
				map.put(CommonDef.device_info.PROGRESS,
						status.mCheckedCount+"/"+status.mSum);
				list.add(map);
				itemindex++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		SimpleAdapter adapter = new SimpleAdapter(this, list,
				R.layout.checkitem, new String[] { CommonDef.device_info.INDEX, CommonDef.device_info.NAME,
				CommonDef.device_info.DEADLINE, CommonDef.device_info.STATUS, CommonDef.device_info.PROGRESS }, new int[] {
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
				intent.putExtra(CommonDef.route_info.NAME, mRouteNameStr);
				intent.putExtra(CommonDef.route_info.LISTVIEW_ITEM_INDEX, mRouteIndex);
				intent.putExtra(CommonDef.station_info.LISTVIEW_ITEM_INDEX, mStationIndex);
				intent.putExtra(CommonDef.device_info.LISTVIEW_ITEM_INDEX, arg2);
				intent.putExtra(CommonDef.ONE_CATALOG, "计划巡检");
				intent.putExtra(CommonDef.station_info.NAME, mStationNameStr);
				intent.putExtra(CommonDef.device_info.NAME, map.get(CommonDef.device_info.NAME));
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
