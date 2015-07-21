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
import com.aic.aicdetactor.service.DataService;
import com.aic.aicdetactor.util.SystemUtil;
import com.aic.aicdetactor.view.QuiteToast;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
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
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class RouteActivity extends Activity {

	// RadioGroup mRadioGroup = null;
     ViewPager mViewPager = null;
     List<View> mList_Views = null;
     int mStationIndex =0;
     String mStationNameStr = null;
     boolean isStationClicked = false;
     boolean isTestInterface = false;
	//
     ListView mListView;
     boolean isUseWivewPager =false;
	String TAG = "luotest";
	 //public String mPath = "/sdcard/down.txt";
   //  MyJSONParse json = new MyJSONParse();
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
		if (isUseWivewPager) {
			setContentView(R.layout.plancheck_main);
		} else {
			setContentView(R.layout.route_activity);
		}


		if (isUseWivewPager) {
			initViewPager();
		} else {
			mListView = (ListView) findViewById(R.id.listView);
			//
			//init();
			iniDataThread.start();
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
				for (int n = 0; n < 10; n++) {

					Log.d(TAG,
							"teststr is "
									+ ((myApplication) getApplication())
											.getPartItemSubStr(teststr, n));
				}
				
				
				///
				
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
		
		void init(){
			Log.d(TAG,"in init() start "+SystemUtil.getSystemTime());
			String name = SystemUtil.createGUID();
			name = "down.txt";
			((myApplication) getApplication()).insertNewRouteInfo(name,"/sdcard/down.txt",this);
			name = "down1.txt";
			((myApplication) getApplication()).insertNewRouteInfo(name,"/sdcard/down1.txt",this);
//			startService(new Intent(RouteActivity.this,  
//					DataService.class));  
			Log.d(TAG,"in init() 1 start "+SystemUtil.getSystemTime());
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();
			int iRouteCount = ((myApplication) getApplication()).InitData();
			Log.d(TAG,"in init() 2 start "+SystemUtil.getSystemTime());
			for(int i =0;i<iRouteCount;i++){
			try {	
				Log.d(TAG,"in init() for start i="+i+","+SystemUtil.getSystemTime());
					Map<String, String> map = new HashMap<String, String>();
					map.put(CommonDef.route_info.NAME, ((myApplication) getApplication())
							.getRoutName(i));
					map.put(CommonDef.route_info.DEADLINE, "2015-06-20 10:00");
					map.put(CommonDef.route_info.STATUS, "已检查");
					map.put(CommonDef.route_info.PROGRESS, "0/"+ ((myApplication) getApplication())
							.getRoutePartItemCount(i));
//					map.put("progress", "2/");
					String index = ""+(i+ 1);
					map.put(CommonDef.route_info.INDEX, index);

					list.add(map);
					Log.d(TAG,"in init() for end i="+i+","+SystemUtil.getSystemTime());
					
			} catch (Exception e) {
				e.printStackTrace();
			}
			}

			SimpleAdapter adapter = new SimpleAdapter(this, list,
					R.layout.checkitem, new String[] { CommonDef.route_info.INDEX, CommonDef.route_info.NAME,
					CommonDef.route_info.DEADLINE, CommonDef.route_info.STATUS, CommonDef.route_info.PROGRESS }, new int[] {
							R.id.index, R.id.pathname, R.id.deadtime,
							R.id.status, R.id.progress });
			Log.d(TAG,"in init() setAdapter"+SystemUtil.getSystemTime());
			mListView.setAdapter(adapter);
			Log.d(TAG,"in init() after setAdapter"+SystemUtil.getSystemTime());
			mListView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					HashMap<String, String> mapItem = (HashMap<String, String>) (mListView
							.getItemAtPosition(arg2));
					Log.d(TAG,
							"MAPITEM is " + mapItem.toString()
									+ " pathname is "
									+ (String) mapItem.get(CommonDef.route_info.NAME));
					Intent intent = new Intent();
					intent.putExtra(CommonDef.route_info.LISTVIEW_ITEM_INDEX, arg2);
					intent.putExtra(CommonDef.ONE_CATALOG, "计划巡检");
					intent.putExtra(CommonDef.route_info.NAME,
							(String) mapItem.get(CommonDef.route_info.NAME));
					intent.putExtra(CommonDef.route_info.INDEX, (String) mapItem.get(CommonDef.route_info.INDEX));
					intent.setClass(getApplicationContext(),
							StationActivity.class);
					startActivity(intent);
				}
			});
			
		}
	private	Handler InitDataHandler = new Handler()
	    {
			public final  int INIT_DATA = 0;
	        @Override
	        public void handleMessage(Message msg) {
	            // TODO Auto-generated method stub
	          //  super.handleMessage(msg);
	            //InitDataHandler.post(update_thread);
	        	switch(msg.what){
	        	case INIT_DATA:
	        		init();
	        		break;
	        	}
	           
	        }       
	    };
	    
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
