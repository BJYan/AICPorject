package com.aic.aicdetactor.check;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aic.aicdetactor.R;
import com.aic.aicdetactor.myApplication;
import com.aic.aicdetactor.R.id;
import com.aic.aicdetactor.R.layout;
import com.aic.aicdetactor.util.CommonDef;
import com.aic.aicdetactor.util.MyJSONParse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class DeviceActivity extends Activity {
	int mStationIndex = 0;
	ListView mListView = null;
	String TAG = "luotest";
	
	String rotename = null;
	String rootName = null;
	
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

		setContentView(R.layout.device_activity);
		Log.d("test", "startDevideActivity");
		Intent intent = getIntent();
		mStationIndex = intent.getExtras().getInt(CommonDef.STATION_INDEX);
		String oneCatalog = intent.getExtras().getString(CommonDef.ONE_CATALOG);
		rotename = intent.getExtras().getString(CommonDef.CHECKNAME);
		String indexStr = intent.getExtras().getString(CommonDef.LISTITEM_INDEX);
		TextView planNameTextView = (TextView) findViewById(R.id.planname);
		planNameTextView.setText(oneCatalog);
		Log.d(TAG, "oneCatalog is " + oneCatalog + "rotename is " + rotename);
		TextView RouteNameTextView = (TextView) findViewById(R.id.station_text_name);
		rootName = intent.getExtras().getString(CommonDef.ROUTENAME);
		RouteNameTextView.setText(indexStr + "        " + rotename);

		Log.d(TAG, "ONcREATE index is " + mStationIndex);
		mListView = (ListView) findViewById(R.id.listView);
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		try {

			List<Object> deviceItemList = ((myApplication) getApplication())
					.getDeviceItemList(mStationIndex);

			for (int i = 0; i < deviceItemList.size(); i++) {
				Map<String, String> map = new HashMap<String, String>();
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
				intent.putExtra(CommonDef.ROUTENAME, rotename);
				intent.putExtra(CommonDef.STATION_INDEX, mStationIndex);
				intent.putExtra(CommonDef.DEVICE_INDEX, arg2);
				intent.putExtra(CommonDef.ONE_CATALOG, "计划巡检");
				intent.putExtra(CommonDef.ROOTNAME, rootName);
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
