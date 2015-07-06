package com.aic.aicdetactor.check;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.aic.aicdetactor.R;
import com.aic.aicdetactor.myApplication;
import com.aic.aicdetactor.util.CommonDef;

public class DeviceItemActivity extends Activity {

	private ListView mListView = null;
	String TAG = "luotest";
	private Spinner mSpinner = null;
	private List<String> spinnerList = new ArrayList<String>();
	private ArrayAdapter<String> spinnerAdapter;
	private int mLastSpinnerIndex = 0;
	private Object partItemObject;
	private List<Map<String, Object>> mMapList;
	public final int SPINNER_SELECTCHANGED =0;
	private SimpleAdapter mListViewAdapter = null;
	private List<Map<String, Object>> mPartItemRevertMapList = null;
	private List<Object> mPartItemSelectedList=null;
	private CheckBox mCheckbox = null;
	private int mCurrentStationIndex =0;
	private int mCurrentDeviceIndex = 0;
	//是否需要反向排序来巡检
	private boolean isReverseDetection = false;
	//点击listItem后 ListView 视图消失，显示具体测试点界面
	private boolean bListViewVisible = true;
	private boolean bSpinnerVisible = true;
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

		setContentView(R.layout.deviceitem_activity_ex);

		Intent intent = getIntent();
		mCurrentStationIndex = intent.getExtras().getInt(CommonDef.STATION_INDEX);
		mCurrentDeviceIndex = intent.getExtras().getInt(CommonDef.DEVICE_INDEX);

		String oneCatalog = intent.getExtras().getString(CommonDef.ONE_CATALOG);
		String rotename = intent.getExtras().getString(CommonDef.CHECKNAME);
		String roteNmaeStr = intent.getExtras().getString(CommonDef.ROUTENAME);
		//String  rootName = intent.getExtras().getString(CommonDef.ROOTNAME);

		TextView planNameTextView = (TextView) findViewById(R.id.planname);
		planNameTextView.setText(oneCatalog);

		TextView RouteNameTextView = (TextView) findViewById(R.id.station_text_name);
		RouteNameTextView.setText(rotename);

		TextView secondcatalognameTextView = (TextView) findViewById(R.id.secondcatalogname);
		secondcatalognameTextView.setText(roteNmaeStr);

		ImageView imageView = (ImageView)findViewById(R.id.imageView1);
		imageView.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Log.d(TAG,"imageView.setOnClickListener");
				// TODO Auto-generated method stub
				finish();
			}
			
		});
		Log.d(TAG, "ONcREATE stationIndex is " + mCurrentStationIndex + "deviceIndex"
				+ mCurrentDeviceIndex);
		mListView = (ListView) findViewById(R.id.listView);
		mMapList = new ArrayList<Map<String, Object>>();
		
		InitDataNeeded(0,false);
		mListViewAdapter = new SimpleAdapter(this, mMapList,
				R.layout.checkunit, new String[] { "index", "unit_name","value",
						"deadline"  }, new int[] {
						R.id.index, R.id.pathname,R.id.checkvalue, R.id.deadtime});
		mListView.setAdapter(mListViewAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				bListViewVisible = false;
				needVisible();
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
		mSpinner = (Spinner) findViewById(R.id.spinner1);
		try {
			spinnerList = ((myApplication) getApplication()).getDeviceItemDefList(partItemObject);
			if(spinnerList.size()<=1){
				bSpinnerVisible= false;
				mSpinner.setVisibility(Spinner.GONE);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(bSpinnerVisible){
		
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
        mCheckbox = (CheckBox)findViewById(R.id.checkBox1);
        mCheckbox.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub				
				isReverseDetection = arg1;
				
				//如果为true ，需要对listView里的Item数据进行反向排列并显示
			}});

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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if(!bListViewVisible){
				bListViewVisible = !bListViewVisible;
				needVisible();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
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
					//resetListViewData(msg.arg1);
					InitDataNeeded(msg.arg1,true);			
                       break;   
             }   
             super.handleMessage(msg);   
        }   
   };  
   
   /**
    * 
    * @param itemIndex :spinner widget index
    * @param updateAdapter:是否要重新裝載adapter data
    */
   void InitDataNeeded(int itemIndex,boolean updateAdapter){
	   try {
		   if(partItemObject == null){
			partItemObject = ((myApplication) getApplication())
					.getPartItemObject(mCurrentStationIndex,mCurrentDeviceIndex);
			Log.d(TAG, "partItemDataList IS " + partItemObject.toString());
			}
			mPartItemSelectedList = ((myApplication) getApplication()).getPartItemListByItemDef(partItemObject,itemIndex);
			
			if(updateAdapter){
				 mMapList.clear();
			}
			for (int i = 0; i < mPartItemSelectedList.size(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();				
				map.put("unit_name", ((myApplication) getApplication())
						.getPartItemCheckUnitName(mPartItemSelectedList.get(i),((myApplication) getApplication()).PARTITEM_UNIT_NAME));


				//已检查项的检查数值怎么保存？并显示出来
				map.put("deadline", "2015-06-20 10:00");				
				mMapList.add(map);
			}
			if(updateAdapter){
				 if(mPartItemSelectedList.size()>0){
					   mListViewAdapter.notifyDataSetChanged();
				   }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
   }   
   
   private void needVisible(){
	   if(bListViewVisible){
		   mListView.setVisibility(ListView.VISIBLE);
		   if(bSpinnerVisible){
		   mSpinner.setVisibility(Spinner.VISIBLE);
		   }
		   mCheckbox.setVisibility(CheckBox.VISIBLE);
		   
	   }else{
		   mListView.setVisibility(ListView.GONE);
		   mSpinner.setVisibility(Spinner.GONE);
		   mCheckbox.setVisibility(CheckBox.GONE);
	   }
   } 
}
