package com.aic.aicdetactor.check;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.aic.aicdetactor.R;
import com.aic.aicdetactor.app.myApplication;
import com.aic.aicdetactor.comm.CommonDef;
import com.aic.aicdetactor.data.CheckStatus;
import com.aic.aicdetactor.util.SystemUtil;

public class StationActivity extends Activity {

	private RadioGroup mRadioGroup = null;
	private ViewPager mViewPager = null;
	private  List<View> mList_Views = null;
	private  int mStationIndex =0;
	private String mStationNameStr = null;
	private boolean isStationClicked = false;
	private boolean isTestInterface = true;
	//
	private int mRouteIndex =0;
	private ListView mListView;
	private boolean isUseWivewPager =false;
	String TAG = "luotest";
	private String  routeName = null;
	private SimpleAdapter mListViewAdapter = null;
	private List<Map<String, String>> mListDatas = null;
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  //无title  
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
		              WindowManager.LayoutParams.FLAG_FULLSCREEN);  
			setContentView(R.layout.station_activity);
			
			
			
			Intent intent =getIntent();
			mRouteIndex = intent.getExtras().getInt(CommonDef.route_info.LISTVIEW_ITEM_INDEX);
			
			String  oneCatalog = intent.getExtras().getString(CommonDef.ROUTE_CLASS_NAME);		
			routeName = intent.getExtras().getString(CommonDef.route_info.NAME);
			TextView planNameTextView  =(TextView)findViewById(R.id.planname);
			planNameTextView.setText(oneCatalog);			
			TextView RouteNameTextView  =(TextView)findViewById(R.id.station_text_name);
			RouteNameTextView.setText(""+(mRouteIndex +1)+ "		"+routeName);
			ImageView imageView = (ImageView)findViewById(R.id.imageView1);
			imageView.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					Log.d(TAG,"imageView.setOnClickListener");
					// TODO Auto-generated method stub
					finish();
				}
				
			});

		
			mListView = (ListView) findViewById(R.id.listView);
			mListDatas = new ArrayList<Map<String, String>>();
			
			initListViewData();
			mListViewAdapter = new SimpleAdapter(this, mListDatas,
					R.layout.checkitem, new String[] { CommonDef.station_info.INDEX, CommonDef.station_info.NAME,
					CommonDef.station_info.DEADLINE, CommonDef.station_info.STATUS, CommonDef.station_info.PROGRESS }, new int[] {
							R.id.index, R.id.pathname, R.id.deadtime,
							R.id.status, R.id.progress });
			mListView.setAdapter(mListViewAdapter);
			mListView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					HashMap<String, String> mapItem = (HashMap<String, String>) (mListView
							.getItemAtPosition(arg2));
					Log.d(TAG,
							"stationActivit StationName is "
									+ (String) mapItem.get(CommonDef.station_info.NAME));
					
					
					 
					 ((myApplication) getApplication()).gStationName = routeName ;
					 ((myApplication) getApplication()).mStationIndex = arg2;
					 
					 
					 
					Intent intent = new Intent();
					intent.putExtra(CommonDef.route_info.LISTVIEW_ITEM_INDEX , mRouteIndex);
					intent.putExtra(CommonDef.station_info.LISTVIEW_ITEM_INDEX, arg2);
					intent.putExtra(CommonDef.ROUTE_CLASS_NAME, "计划巡检");
					intent.putExtra(CommonDef.station_info.NAME,
							(String) mapItem.get(CommonDef.station_info.NAME));
					intent.putExtra(CommonDef.route_info.NAME, routeName);					
					intent.setClass(getApplicationContext(),
							DeviceActivity.class);
					startActivity(intent);
				}
			});

			if (isTestInterface) {
				// //test idinfo ,test pass
				int myid = 100;
				String teststr = "AIC8E791D89B";
				teststr = "AIC8D7D1E09C";
				try {
					myid = ((myApplication) getApplication())
							.getStationItemIndexByID(0,teststr);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Log.d(TAG, " idtest myid is " + myid);
				
				// test getpartitemsub,TEST pass
				teststr = "0102030405*B302皮带机电机*电机震动*00000005*mm/s*1*40E33333*40900000*3D23D70A*";
				//"0102*F#测点*转速*00000007*r/min*129*461C4000*460CA000*459C4000*"

				for (int n = 0; n < 10; n++) {

					String str = ((myApplication) getApplication())
							.getPartItemSubStr(teststr, n);
					Log.d(TAG,"teststr is "	+ str);
				}
				float f1 = SystemUtil.getTemperature("42700000");
				Log.d(TAG,"temperature is "	+ SystemUtil.getTemperature("42700000"));
				Log.d(TAG,"temperature is "	+ SystemUtil.getTemperature("42b40000"));
				//Log.d(TAG,"temperature is "	+ SystemUtil.getTemperature("42700000"));
				///
				
			}
		}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		initListViewData();
		super.onResume();
	}



	private void initListViewData(){
		Log.d(TAG,"initListViewData()");
		try {

			List<Object> stationItemList = ((myApplication) getApplication())
					.getStationList(mRouteIndex);

			CheckStatus status = null;
			mListDatas.clear();
			for (int i = 0; i < stationItemList.size(); i++) {
				Map<String, String> map = new HashMap<String, String>();
				status = ((myApplication) getApplication())
						.getNodeCount(stationItemList.get(i),1,0);
				status.setContext(getApplicationContext());
				map.put(CommonDef.station_info.NAME, ((myApplication) getApplication())
						.getStationItemName(stationItemList.get(i)));
				map.put(CommonDef.station_info.DEADLINE, status.mLastTime);
				map.put(CommonDef.station_info.STATUS, status.getStatus());
				
				map.put(CommonDef.station_info.PROGRESS, status.mCheckedCount+"/"+status.mSum);
				//String index = "" + (i + 1);
				//map.put("index", index);

				mListDatas.add(map);
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(mListViewAdapter != null){
			mListViewAdapter.notifyDataSetChanged();
		}
	}
}
