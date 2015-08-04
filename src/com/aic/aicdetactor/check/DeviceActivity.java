package com.aic.aicdetactor.check;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.aic.aicdetactor.R;
import com.aic.aicdetactor.app.myApplication;
import com.aic.aicdetactor.comm.CommonDef;
import com.aic.aicdetactor.data.CheckStatus;
import com.aic.aicdetactor.data.TurnInfo;
import com.aic.aicdetactor.util.SystemUtil;

public class DeviceActivity extends Activity implements OnClickListener{
	private int mStationIndex = 0;
	private int mRouteIndex = 0;
	private ListView mListView = null;
	String TAG = "luotest";
	
	private String mCheckNameStr = null;
	private String mRouteNameStr = null;
	private String mStationNameStr = null;
	private Button mButtonStartCheck = null;
	private List<Map<String, String>> mDataList = null;
	private SimpleAdapter mListViewAdapter = null;
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
		String oneCatalog = intent.getExtras().getString(CommonDef.ROUTE_CLASS_NAME);
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
		mDataList = new ArrayList<Map<String, String>>();
		
		InitData();
		mListViewAdapter = new SimpleAdapter(this, mDataList,
				R.layout.checkitem, new String[] { CommonDef.device_info.INDEX, CommonDef.device_info.NAME,
				CommonDef.device_info.DEADLINE, CommonDef.device_info.STATUS, CommonDef.device_info.PROGRESS }, new int[] {
						R.id.index, R.id.pathname, R.id.deadtime, R.id.status,
						R.id.progress });
		mListView.setAdapter(mListViewAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				HashMap<String, String> map = (HashMap<String, String>) mListView
						.getItemAtPosition(arg2);
				
				 ((myApplication) getApplication()).gDeviceName = map.get(CommonDef.device_info.NAME);
				 ((myApplication) getApplication()).mDeviceIndex = arg2;
				 
				Intent intent = new Intent();
				intent.putExtra(CommonDef.route_info.NAME, mRouteNameStr);
				intent.putExtra(CommonDef.route_info.LISTVIEW_ITEM_INDEX, mRouteIndex);
				intent.putExtra(CommonDef.station_info.LISTVIEW_ITEM_INDEX, mStationIndex);
				intent.putExtra(CommonDef.device_info.LISTVIEW_ITEM_INDEX, arg2);
				intent.putExtra(CommonDef.ROUTE_CLASS_NAME, "计划巡检");
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
		
		mButtonStartCheck = (Button)findViewById(R.id.start_checkdevice);
		mButtonStartCheck.setOnClickListener(this);
	}

	void InitData(){
		try {
			/**
			 * 返回的DeviceItem 下的数组list,每项包含 DeviceName ,ItemDef,QueryNumber及PartItem
			 */
			List<Object> deviceItemList = ((myApplication) getApplication())
					.getDeviceItemList(mStationIndex);

			CheckStatus status = null;
			int itemindex = mStationIndex;
			mDataList.clear();
			for (int i = 0; i < deviceItemList.size(); i++) {
				Map<String, String> map = new HashMap<String, String>();
				status = ((myApplication) getApplication())
						.getNodeCount(deviceItemList
								.get(i),2,0);
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
				mDataList.add(map);
				itemindex++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(mListViewAdapter != null){
			mListViewAdapter.notifyDataSetChanged();
		}
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
		InitData();
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

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.start_checkdevice: {			
			if(!IsInworkerTime()){
				Toast.makeText(this.getApplicationContext(), getString(R.string.cannot_check_time_tips), Toast.LENGTH_LONG).show();
			}else{
				((myApplication) getApplication()).mStartDate = SystemUtil.getSystemTime(0);
				startCheck();
			}
		}
		break;
		}
	}

	void startCheck(){
		int itemindex = -1;
		try{
		List<Object> deviceItemList = ((myApplication) getApplication())
				.getDeviceItemList(mStationIndex);

		CheckStatus status = null;		
		
		for ( int i = 0; i < deviceItemList.size(); i++) {
			Map<String, String> map = new HashMap<String, String>();
			status = ((myApplication) getApplication())
					.getNodeCount(deviceItemList
							.get(i),2,0);
			if(!status.hasChecked){
				itemindex = i ;
				break;
				}
		}
			
		}catch(JSONException e){
			e.printStackTrace();
		}
		if(itemindex != -1){
		HashMap<String, String> map = (HashMap<String, String>) mListView
				.getItemAtPosition(itemindex);
		Intent intent = new Intent();
		intent.putExtra(CommonDef.route_info.NAME, mRouteNameStr);
		intent.putExtra(CommonDef.route_info.LISTVIEW_ITEM_INDEX,
				mRouteIndex);
		intent.putExtra(CommonDef.station_info.LISTVIEW_ITEM_INDEX,
				mStationIndex);
		intent.putExtra(CommonDef.device_info.LISTVIEW_ITEM_INDEX,itemindex);
		intent.putExtra(CommonDef.ROUTE_CLASS_NAME, "计划巡检");
		intent.putExtra(CommonDef.station_info.NAME, mStationNameStr);
		intent.putExtra(CommonDef.device_info.NAME,
				map.get(CommonDef.device_info.NAME));
		intent.setClass(getApplicationContext(), DeviceItemActivity.class);
		startActivity(intent);
		}else{
			Toast.makeText(getApplicationContext(),getString(R.string.no_need_check_again), Toast.LENGTH_LONG).show();
		}
	}
		
	
	
	boolean IsInworkerTime() {
		String systemTime = SystemUtil
				.getSystemTime(SystemUtil.TIME_FORMAT_HHMM);
		String tisp = getString(R.string.cannot_check_time_tips);
		List<TurnInfo> turnInfo = null;
		;
		try {
			turnInfo = ((myApplication) getApplication()).getRouteTurnInfoList();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		boolean bok = false;
		for (int i = 0; i < turnInfo.size(); i++) {
			String strstartTime = turnInfo.get(i).StartTime;
			if (systemTime.compareTo(strstartTime) > 0 && systemTime.compareTo(turnInfo.get(i).EndTime)<0) {
				((myApplication) getApplication()).mTurnNumber=turnInfo.get(i).Number;
				((myApplication) getApplication()).mTurnStartTime=turnInfo.get(i).StartTime;
				((myApplication) getApplication()).mTurnEndTime=turnInfo.get(i).EndTime;
				Log.d(TAG,"IsInworkerTime() NUMBER is " + turnInfo.get(i).Number +
						",startTime is " +turnInfo.get(i).Number
						+",endTime is " +turnInfo.get(i).EndTime);
				bok = true;
			}
		}
		return bok;
	}
	boolean canBeiginCheck(){
		boolean ok = false ;
		//先穷举ID号，再者就是比较实际了。
		
		SimpleDateFormat    sDateFormat    =   new    SimpleDateFormat("MMdd    hh:mm:ss");       
		String    date    =    sDateFormat.format(new    java.util.Date());   
		
		return ok;
	}
}
