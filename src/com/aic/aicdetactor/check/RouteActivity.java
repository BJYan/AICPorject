package com.aic.aicdetactor.check;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.aic.aicdetactor.BtActivity;
import com.aic.aicdetactor.R;
import com.aic.aicdetactor.myApplication;
import com.aic.aicdetactor.R.id;
import com.aic.aicdetactor.R.layout;
import com.aic.aicdetactor.R.menu;

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
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if (isUseWivewPager) {
			setContentView(R.layout.plancheck_main);
		} else {
			setContentView(R.layout.route_activity);
		}

//		// json.initData(mPath);
//		mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup1);
//		mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//			@Override
//			public void onCheckedChanged(RadioGroup arg0, int arg1) {
//				// TODO Auto-generated method stub
//				Intent intent = new Intent();
//				switch (arg1) {
//				case R.id.radio_bt:
//
//					intent.setClass(getApplicationContext(), BtActivity.class);
//					startActivity(intent);
//					Toast.makeText(getApplicationContext(), "BT",
//							Toast.LENGTH_LONG).show();
//					break;
//				case R.id.radio_check:
//
//					intent.setClass(getApplicationContext(),
//							RouteActivity.class);
//					startActivity(intent);
//					Toast.makeText(getApplicationContext(), "check",
//							Toast.LENGTH_LONG).show();
//					break;
//				case R.id.radio_search:
//					Toast.makeText(getApplicationContext(), "search",
//							Toast.LENGTH_LONG).show();
//					break;
//				case R.id.radio_downup:
//					Toast.makeText(getApplicationContext(), "downup",
//							Toast.LENGTH_LONG).show();
//					break;
//
//				}
//			}
//		});
		if (isUseWivewPager) {
			initViewPager();
		} else {
			mListView = (ListView) findViewById(R.id.listView);
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();
			try {

				List<Object> routeList = ((myApplication) getApplication())
						.getStationList();

//				for (int i = 0; i < routeList.size(); i++) {
//					Map<String, String> map = new HashMap<String, String>();
//					map.put("pathname", ((myApplication) getApplication())
//							.getStationItemName(routeList.get(i)));
//					map.put("deadline", "2015-06-20 10:00");
//					map.put("status", "已检查");
//					map.put("progress", "2/6");
//					String index = "" + i + 1;
//					map.put("index", index);
//
//					list.add(map);
//					
					
					Map<String, String> map = new HashMap<String, String>();
					map.put("routeName", ((myApplication) getApplication())
							.getRoutName());
					map.put("deadline", "2015-06-20 10:00");
					map.put("status", "已检查");
					map.put("progress", "2/"+ ((myApplication) getApplication())
							.getRoutePartItemCount(0));
//					map.put("progress", "2/");
					String index = ""+ 1;
					map.put("index", index);

					list.add(map);
					
					
					
					//List<Object> stationList = ((myApplication) getApplication()).getDeviceItem(stationItemList.get(i));
				//	for(int h=0 ;h<stationList.size();h++){
					//	JSONObject jsonObject= (JSONObject) stationList.get(h);
//					List<Object> partitemDataList =((myApplication) getApplication()).getPartItemDataList(i, 0);
//						((myApplication) getApplication()).getPartItemListByItemDef();
					//}
					
			//	} 
			} catch (Exception e) {
				e.printStackTrace();
			}

			SimpleAdapter adapter = new SimpleAdapter(this, list,
					R.layout.checkitem, new String[] { "index", "routeName",
							"deadline", "status", "progress" }, new int[] {
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
							"MAPITEM is " + mapItem.toString()
									+ " pathname is "
									+ (String) mapItem.get("pathname"));
					Intent intent = new Intent();
					intent.putExtra("routeIndex", arg2);
					intent.putExtra("oneCatalog", "计划巡检");
					intent.putExtra("routeName",
							(String) mapItem.get("routeName"));
					intent.putExtra("itemIndex", (String) mapItem.get("index"));
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
							.getStationItemIndexByID(teststr);
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
	public void initViewPager(){
		mViewPager = (ViewPager)findViewById(R.id.viewPager1);
		LayoutInflater layoutinflater = getLayoutInflater();
		mList_Views = new ArrayList<View>();
		
		
		View checkview = layoutinflater.inflate(R.layout.route_activity, null);
		mList_Views.add(checkview);
		View checkitemview = layoutinflater.inflate(R.layout.device_activity, null);
		mList_Views.add(checkitemview);
		
		mViewPager.setAdapter(new myViewPagerAdapter(mList_Views));
		mViewPager.setOnPageChangeListener(new myOnPageChangeListener());
		mViewPager.setCurrentItem(0);
		
	}

	 public  class myOnPageChangeListener implements OnPageChangeListener{

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			Log.d(TAG, "ONpAGERsCROLLsTATEcHANGED "+arg0);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			
			switch(arg0){
			case 0:
				//if(!mRadioGroup.isEnabled())mRadioGroup.setEnabled(true);
				break;
			case 5:
 {
				if (isStationClicked) {
					ListView mListView = null;
					mListView = (ListView) mList_Views.get(arg0).findViewById(
							R.id.listView);
					List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
					//
					// json.initData(mPath);
//					try {
//						Object stationItemobject = json
//								.getStationItem(mStationIndex);
//						List<Object> stationItemList = json
//								.getDeviceItem(stationItemobject);
//						Log.d(TAG, "instantiateItem I=8989");
//						for (int i = 0; i < stationItemList.size(); i++) {
//							Map<String, Object> map = new HashMap<String, Object>();
//							map.put("index", i + 1);
//
//							Log.d(TAG, "instantiateItem I=" + i);
//							map.put("pathname", json
//									.getDeviceItemName(stationItemList.get(i)));
//							map.put("deadtime", "20150610 12:00");
//							map.put("status", "已检查");
//							map.put("progress", "2/10");
//							list.add(map);
//						}
//						for (int i = 0; i < 10; i++) {
//							Map<String, Object> map = new HashMap<String, Object>();
//							map.put("index", i + 1);
//
//							Log.d(TAG, "instantiateItem I=" + i);
//							map.put("pathname", "testabc");
//							map.put("deadtime", "20150610 12:00");
//							map.put("status", "已检查");
//							map.put("progress", "2/10");
//							list.add(map);
//						}
//
//						SimpleAdapter adapter = new SimpleAdapter(
//								PlanCheckActivity.this, list,
//								R.layout.checkitem, new String[] { "index",
//										"pathname", "deadtime", "status",
//										"progress" }, new int[] { R.id.index,
//										R.id.pathname, R.id.deadtime,
//										R.id.status, R.id.progress });
//						mListView.setAdapter(adapter);
//					} catch (Exception e) {
//						e.printStackTrace();
//						Log.d(TAG, "instantiateItem I=8989 " + e.toString());
//					}
				}
			}
				//mRadioGroup.setVisibility(View.GONE);
				break;
			
			}
		}
		 
	 }
	  public class myViewPagerAdapter extends PagerAdapter{
		  public List<View> listViews;
		  @Override
		public void destroyItem(View container, int position, Object object) {
			// TODO Auto-generated method stub
			//super.destroyItem(container, position, object);
			  ((ViewPager)container).removeView(listViews.get(position));
		}

		@Override
		public Object instantiateItem(View container, int position) {
			// TODO Auto-generated method stub
			// return super.instantiateItem(container, position);
			ListView mListView = null;
			Log.d(TAG,"in instantiateItem " +position);
			switch (position) {
			case 0: {
//				mListView = (ListView) listViews.get(position).findViewById(
//						R.id.listView);
//				final List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
//				try {
//
//					List<Object> stationItemList = json.getStationItem();
//
//					for (int i = 0; i < stationItemList.size(); i++) {
//						Map<String, Object> map = new HashMap<String, Object>();
//						map.put("check_name",
//								json.getStationItemName(stationItemList.get(i)));
//						map.put("value", "test");
//						list.add(map);
//					}
//					
//					////////////////
//					Object stationItemobject = json
//							.getStationItem(0);
//					List<Object> stationItemLists = json
//							.getDeviceItem(stationItemobject);
//					Log.d(TAG,"instantiateItem I=8989");
//					for (int i = 0; i < stationItemLists.size(); i++) {
//						Map<String, Object> map = new HashMap<String, Object>();
//						map.put("value",i+1);
//						
//						Log.d(TAG,"instantiateItem I="+i);
//						map.put("check_name",
//								json.getDeviceItemName(stationItemLists.get(i)));
//						
//						list.add(map);
//					}
//					//////////////
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//
//				
//				
//				
//				
//					
//
//
//					
//				
//			
//			
//			SimpleAdapter adapter = new SimpleAdapter(
//					PlanCheckActivity.this, list, R.layout.two_text_item,
//					new String[] { "check_name", "value" }, new int[] {
//							R.id.checkitem_name, R.id.value });
//			
//				mListView.setAdapter(adapter);
//				mListView
//						.setOnItemClickListener(new ListView.OnItemClickListener() {
//
//							@Override
//							public void onItemClick(AdapterView<?> arg0,
//									View arg1, int arg2, long arg3) {
//								isStationClicked = true;
//								// TODO Auto-generated method stub
//								Map<String, Object> maps = new HashMap<String, Object>();
//								maps = list.get(arg2);
//								mStationNameStr = (String) maps
//										.get("check_nme");
//								mStationIndex = arg2;
//								mViewPager.setCurrentItem(1);
//							}
//						});
//
			}
				break;
			case 1:// checkitem
			{
			
				
//				Log.d(TAG,"IN pager 1");
//				TextView name = (TextView) listViews.get(position)
//						.findViewById(R.id.planname);
//				name.setText("test");
//				mListView = (ListView) listViews.get(position).findViewById(
//						R.id.listView);
//				List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
//				//
//				// json.initData(mPath);
//				try {
//					Object stationItemobject = json
//							.getStationItem(mStationIndex);
//					List<Object> stationItemList = json
//							.getDeviceItem(stationItemobject);
//					Log.d(TAG,"instantiateItem I=8989");
//					for (int i = 0; i < stationItemList.size(); i++) {
//						Map<String, Object> map = new HashMap<String, Object>();
//						map.put("index",i+1);
//						
//						Log.d(TAG,"instantiateItem I="+i);
//						map.put("pathname",
//								json.getDeviceItemName(stationItemList.get(i)));
//						map.put("deadtime", "20150610 12:00");
//						map.put("status", "已检查");
//						map.put("progress", "2/10");
//						list.add(map);
//					}
//
////					for (int i = 0; i < 5; i++) {
////						Map<String, Object> map = new HashMap<String, Object>();
////						map.put("index",i+1);
////						
////						Log.d(TAG,"instantiateItem I="+i);
////						map.put("pathname",
////								"abc_test");
////						map.put("deadtime", "20150610 12:00");
////						map.put("status", "已检查");
////						map.put("progress", "2/10");
////						list.add(map);
////					}
//					
//					SimpleAdapter adapter = new SimpleAdapter(
//							PlanCheckActivity.this, list,
//							R.layout.checkitem, new String[] {
//									"index", "pathname","deadtime","status","progress" }, new int[] {
//									R.id.index, R.id.pathname,R.id.deadtime,R.id.status,R.id.progress });
//					mListView.setAdapter(adapter);
//				} catch (Exception e) {
//					e.printStackTrace();
//					Log.d(TAG,"instantiateItem I=8989 "+e.toString());
//				}
			}
				break;

			}
			((ViewPager) container).addView(listViews.get(position), 0);
			return listViews.get(position);
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listViews.size();
		}

		public myViewPagerAdapter(List<View> listView){
			this.listViews = listView;
		}
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return (arg0== arg1);
		}
		  
	  }
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.main, menu);
			return true;
		}
}
