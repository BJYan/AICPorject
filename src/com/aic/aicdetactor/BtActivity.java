package com.aic.aicdetactor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aic.aicdetactor.util.MyJSONParse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class BtActivity extends Activity {

	ListView mListView =null;
	String TAG = "luotest";
	 public String mPath = "/sdcard/down.txt";
     MyJSONParse json = new MyJSONParse();
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
		setContentView(R.layout.bt_activity);
		mListView = (ListView)findViewById(R.id.listView);
		try{
			json.initData(mPath);
			List<Object> stationItemList = json.getStationList();
			
			for(int i = 0;i<stationItemList.size();i++){
				
			}
		}catch(Exception e){e.printStackTrace();}
		
		
		
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("check_name", getString(R.string.sensor_name));
        map.put("value", "test");
        list.add(map);
        
        map = new HashMap<String, Object>();
        map.put("check_name", getString(R.string.shock));
        map.put("value", "test");
        list.add(map);
        
        map = new HashMap<String, Object>();
        map.put("check_name", getString(R.string.temperature));
        map.put("value", "test");
        list.add(map);
        
        map = new HashMap<String, Object>();
        map.put("check_name", getString(R.string.revolution_speed));
        map.put("value", "test");
        list.add(map);
        
		SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.two_text_item, new String[] { "check_name",  "value" }, new int[] { R.id.checkitem_name, R.id.value });
		mListView.setAdapter(adapter);
        
		
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
