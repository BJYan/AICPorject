package com.aic.aicdetactor.check;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.aic.aicdetactor.BtActivity;
import com.aic.aicdetactor.R;
import com.aic.aicdetactor.app.myApplication;
import com.aic.aicdetactor.R.id;
import com.aic.aicdetactor.R.layout;
import com.aic.aicdetactor.R.menu;
import com.aic.aicdetactor.comm.CommonDef;
import com.aic.aicdetactor.data.CheckStatus;
import com.aic.aicdetactor.util.SystemUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
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
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class StationActivity extends Activity {

	 RadioGroup mRadioGroup = null;
     ViewPager mViewPager = null;
     List<View> mList_Views = null;
     int mStationIndex =0;
     String mStationNameStr = null;
     boolean isStationClicked = false;
     boolean isTestInterface = true;
	//
     int mRouteIndex =0;
     ListView mListView;
     boolean isUseWivewPager =false;
	String TAG = "luotest";
	String  routeName = null;
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
			
			String  oneCatalog = intent.getExtras().getString(CommonDef.ONE_CATALOG);		
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
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();
			try {

				List<Object> stationItemList = ((myApplication) getApplication())
						.getStationList(mRouteIndex);

				CheckStatus status = null;
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

					list.add(map);
				} 
			} catch (Exception e) {
				e.printStackTrace();
			}

			SimpleAdapter adapter = new SimpleAdapter(this, list,
					R.layout.checkitem, new String[] { CommonDef.station_info.INDEX, CommonDef.station_info.NAME,
					CommonDef.station_info.DEADLINE, CommonDef.station_info.STATUS, CommonDef.station_info.PROGRESS }, new int[] {
							R.id.index, R.id.pathname, R.id.deadtime,
							R.id.status, R.id.progress });
			mListView.setAdapter(adapter);
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
					Intent intent = new Intent();
					intent.putExtra(CommonDef.route_info.LISTVIEW_ITEM_INDEX , mRouteIndex);
					intent.putExtra(CommonDef.station_info.LISTVIEW_ITEM_INDEX, arg2);
					intent.putExtra(CommonDef.ONE_CATALOG, "计划巡检");
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
}
