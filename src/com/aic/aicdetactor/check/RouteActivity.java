package com.aic.aicdetactor.check;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.aic.aicdetactor.R;
import com.aic.aicdetactor.app.myApplication;
import com.aic.aicdetactor.comm.CommonDef;
import com.aic.aicdetactor.data.CheckStatus;
import com.aic.aicdetactor.util.SystemUtil;
import com.aic.aicdetactor.view.QuiteToast;
import com.aic.aicdetactor.view.SwitchView;

public class RouteActivity extends Activity {

	// RadioGroup mRadioGroup = null;
	private ViewPager mViewPager = null;
	private List<View> mList_Views = null;
	private int mStationIndex =0;
	private String mStationNameStr = null;
	private boolean isStationClicked = false;
	private boolean isTestInterface = false;
	//
	private ListView mListView;
	private boolean isUseWivewPager =false;
	String TAG = "luotest";
	private boolean bTempCheck = false;
	private SwitchView mSwitch = null;
	private List<String> mFileList = null;	
	private String name = null;
	private String pwd= null;
	private List<Map<String, String>> mItemDatas = null;
	private SimpleAdapter mListViewAdapter = null;
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无title
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		if (isUseWivewPager) {
			setContentView(R.layout.plancheck_main);
		} else {
			setContentView(R.layout.route_activity);
		}

		if (isUseWivewPager) {
			initViewPager();
		} else {
			mListView = (ListView) findViewById(R.id.listView);
			mSwitch = (SwitchView) findViewById(R.id.link_switch);
			// mSwitch.setOnCheckedChangeListener(listener);
			// init();
			Intent intent = getIntent();
			name = intent.getStringExtra("name");
			pwd = intent.getStringExtra("pwd");

			//iniDataThread.start();
			mItemDatas = new ArrayList<Map<String, String>>();
			initListData();
			mListViewAdapter = new SimpleAdapter(this, mItemDatas,
					R.layout.checkitem, new String[] {
							CommonDef.route_info.INDEX,
							CommonDef.route_info.NAME,
							CommonDef.route_info.DEADLINE,
							CommonDef.route_info.STATUS,
							CommonDef.route_info.PROGRESS }, new int[] {
							R.id.index, R.id.pathname, R.id.deadtime,
							R.id.status, R.id.progress });
			Log.d(TAG,
					"in init() setAdapter"
							+ SystemUtil
									.getSystemTime(SystemUtil.TIME_FORMAT_YYMMDDHHMM));
			mListView.setAdapter(mListViewAdapter);
			Log.d(TAG,
					"in init() after setAdapter"
							+ SystemUtil
									.getSystemTime(SystemUtil.TIME_FORMAT_YYMMDDHHMM));
			mListView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					HashMap<String, String> mapItem = (HashMap<String, String>) (mListView
							.getItemAtPosition(arg2));
					Log.d(TAG,
							"MAPITEM is "
									+ mapItem.toString()
									+ " pathname is "
									+ (String) mapItem
											.get(CommonDef.route_info.NAME));
					((myApplication) getApplication()).gRouteName = mapItem.get(CommonDef.route_info.NAME);
					 ((myApplication) getApplication()).mRouteIndex = arg2;
					Intent intent = new Intent();
					intent.putExtra(CommonDef.route_info.LISTVIEW_ITEM_INDEX,
							arg2);
					((myApplication) getApplication())
							.setCurrentRouteIndex(arg2);
					intent.putExtra(CommonDef.ROUTE_CLASS_NAME, "计划巡检");
					intent.putExtra(CommonDef.route_info.NAME,
							(String) mapItem.get(CommonDef.route_info.NAME));
					intent.putExtra(CommonDef.route_info.INDEX,
							(String) mapItem.get(CommonDef.route_info.INDEX));
					intent.setClass(getApplicationContext(),
							StationActivity.class);
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
							.getStationItemIndexByID(0, teststr);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Log.d(TAG, " idtest myid is " + myid);

				// test getpartitemsub,TEST pass
				teststr = "0102030405*B302皮带机电机*电机震动*00000005*mm/s*1*40E33333*40900000*3D23D70A*";
				for (int n = 0; n < 10; n++) {

					Log.d(TAG,
							"teststr is "
									+ ((myApplication) getApplication())
											.getPartItemSubStr(teststr, n));
				}

				// /

			}
		}

	}

	
	public void initViewPager(){
		mViewPager = (ViewPager)findViewById(R.id.viewPager1);
		LayoutInflater layoutinflater = getLayoutInflater();
		mList_Views = new ArrayList<View>();
		
		
		View checkview = layoutinflater.inflate(R.layout.route_activity, null);
		mList_Views.add(checkview);
		View checkitemview = layoutinflater.inflate(R.layout.device_activity, null);
		mList_Views.add(checkitemview);
		
	}


		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.main, menu);
			return true;
		}
		
	void initListData() {
		Log.d(TAG, "initListData() start " + SystemUtil.getSystemTime(SystemUtil.TIME_FORMAT_YYMMDDHHMM) + "name is "+name + " pwd is "+pwd);
		
		((myApplication) getApplication()).setUserInfo(name, pwd);
	
		Log.d(TAG, "in init() 1 start " + SystemUtil.getSystemTime(SystemUtil.TIME_FORMAT_YYMMDDHHMM));
		
		int iRouteCount = ((myApplication) getApplication()).InitData();
		Log.d(TAG, "in init() 2 start " + SystemUtil.getSystemTime(SystemUtil.TIME_FORMAT_YYMMDDHHMM));
		CheckStatus status = null;
		mItemDatas.clear();
		for (int routeIndex = 0; routeIndex < iRouteCount; routeIndex++) {
			try {
				Log.d(TAG, "in init() for start i=" + routeIndex + ","
						+ SystemUtil.getSystemTime(SystemUtil.TIME_FORMAT_YYMMDDHHMM));
				Map<String, String> map = new HashMap<String, String>();
				status = ((myApplication) getApplication()).getNodeCount(null,
						0, routeIndex);
				status.setContext(getApplicationContext());
				map.put(CommonDef.route_info.NAME,
						((myApplication) getApplication())
								.getRoutName(routeIndex));
				map.put(CommonDef.route_info.DEADLINE, status.mLastTime);
				map.put(CommonDef.route_info.STATUS, status.getStatus());

				map.put(CommonDef.route_info.PROGRESS, status.mCheckedCount
						+ "/" + status.mSum);
				// map.put("progress", "2/");
				String index = "" + (routeIndex + 1);
				map.put(CommonDef.route_info.INDEX, index);

				mItemDatas.add(map);
				Log.d(TAG, "in init() for end i=" + routeIndex + ","
						+ SystemUtil.getSystemTime(SystemUtil.TIME_FORMAT_YYMMDDHHMM));

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if(mListViewAdapter !=null){
		mListViewAdapter.notifyDataSetChanged();
		}
	}
	private	Handler InitDataHandler = new Handler()
	    {
			public final  int INIT_DATA = 0;
	        @Override
	        public void handleMessage(Message msg) {
	            // TODO Auto-generated method stub
	        	switch(msg.what){
	        	case INIT_DATA:
	        		//initListData();
	        		break;
	        	}
	           
	        }       
	    };
	    
	    @Override
	protected void onResume() {
		// TODO Auto-generated method stub
	    initListData();
		super.onResume();
	}
		private Thread iniDataThread = new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Message msg = new Message();
				msg.what = 0;
				InitDataHandler.sendMessage(msg);
			}
	    	
	    });
	    private long mExitTime =0;
	    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			mExitTime = System.currentTimeMillis();
			QuiteToast.showTips(getApplicationContext(), mExitTime,RouteActivity.this);

			return true;
		}
		return false;
	}
	   
	   
}
