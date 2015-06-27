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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.aic.aicdetactor.R;
import com.aic.aicdetactor.myApplication;

public class DeviceItemActivity extends Activity {

	ListView mListView = null;
	String TAG = "luotest";
	Spinner mSpinner = null;
	private List<String> spinnerList = new ArrayList<String>();
	private ArrayAdapter<String> spinnerAdapter;
	int mLastSpinnerIndex = 0;
	Object partItemObject;
	List<Map<String, Object>> mMapList;
	public final int SPINNER_SELECTCHANGED =0;
	// public String mPath = "/sdcard/down.txt";
	// MyJSONParse json = new MyJSONParse();
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

		setContentView(R.layout.test);

		Intent intent = getIntent();
		int stationIndex = intent.getExtras().getInt("stationIndex");
		int deviceIndex = intent.getExtras().getInt("deviceIndex");

		String oneCatalog = intent.getExtras().getString("oneCatalog");
		String rotename = intent.getExtras().getString("checkName");
		String roteNmaeStr = intent.getExtras().getString("rotename");
		String  rootName = intent.getExtras().getString("rootName");

		TextView planNameTextView = (TextView) findViewById(R.id.planname);
		planNameTextView.setText(oneCatalog);

		TextView RouteNameTextView = (TextView) findViewById(R.id.station_text_name);
		RouteNameTextView.setText(rootName+":"+rotename);

		TextView secondcatalognameTextView = (TextView) findViewById(R.id.secondcatalogname);
		secondcatalognameTextView.setText(roteNmaeStr);

		Log.d(TAG, "ONcREATE stationIndex is " + stationIndex + "deviceIndex"
				+ deviceIndex);
		mListView = (ListView) findViewById(R.id.listView);
		mMapList = new ArrayList<Map<String, Object>>();
		try {

			partItemObject = ((myApplication) getApplication())
					.getPartItemObject(stationIndex, deviceIndex);
			Log.d(TAG, "partItemDataList IS " + partItemObject.toString()
					);
			List<Object> partItemSelectedList = ((myApplication) getApplication()).getPartItemListByItemDef(partItemObject,0);
			for (int i = 0; i < partItemSelectedList.size(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();				
				map.put("device_name", ((myApplication) getApplication())
						.getPartItemName(partItemSelectedList.get(i)));

				map.put("deadline", "2015-06-20 10:00");
				map.put("status", "已检查");
				map.put("progress", ""+(i+1)+"/"+""+(partItemSelectedList.size()));
				mMapList.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		SimpleAdapter adapter = new SimpleAdapter(this, mMapList,
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
				// HashMap<String,String>
				// map=(HashMap<String,String>)mListView.getItemAtPosition(arg2);
				// Intent intent = new Intent();
				// intent.putExtra("stationIndex", arg2);
				// intent.putExtra("title", "计划巡检");
				// intent.putExtra("checkName", map.get("pathname"));
				// intent.setClass(getApplicationContext(),
				// DeviceItemActivity.class);
				// startActivity(intent);
			}
		});
		try {
			spinnerList = ((myApplication) getApplication()).getDeviceItemDefList(partItemObject);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mSpinner = (Spinner) findViewById(R.id.spinner1);
		spinnerAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,spinnerList);    
        //第三步：为适配器设置下拉列表下拉时的菜单样式。    
		spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);    
        //第四步：将适配器添加到下拉列表上    
        mSpinner.setAdapter(spinnerAdapter);    
        //第五步：为下拉列表设置各种事件的响应，这个事响应菜单被选中    
        mSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){    
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {    
                // TODO Auto-generated method stub   
            	if(mLastSpinnerIndex == arg2) return ;
            	
            	Message msg = new Message();
            	msg.arg1 = arg2;
            	msg.what = SPINNER_SELECTCHANGED;
            	myHandler.sendMessage(msg);
            	mLastSpinnerIndex = arg2;
                
            }    
            public void onNothingSelected(AdapterView<?> arg0) {    
                // TODO Auto-generated method stub    
             
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

	Handler myHandler = new Handler() {  
		
        public void handleMessage(Message msg) {   
             switch (msg.what) {   
                  case SPINNER_SELECTCHANGED:   
				try {
					resetListViewData(msg.arg1);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
                       break;   
             }   
             super.handleMessage(msg);   
        }   
   };  
   public void resetListViewData(int index ) throws JSONException{
	   List<Object> partItemSelectedList = ((myApplication) getApplication()).getPartItemListByItemDef(partItemObject,index);
	   Log.d(TAG, "resetListViewData partItemSelectedList SIZE is "+partItemSelectedList.size()+"," +partItemSelectedList.toString());
	   mMapList.clear();
	  // mListView.removeAllViews();
	   for (int i = 0; i < partItemSelectedList.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();				
			map.put("device_name", ((myApplication) getApplication())
					.getPartItemName(partItemSelectedList.get(i)));

			map.put("deadline", "2015-06-20 10:00");
			map.put("status", "已检查");
			map.put("progress", ""+(i+1)+"/"+""+(partItemSelectedList.size()));
			mMapList.add(map);
		}
   }
}
