package com.aic.aicdetactor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aic.aicdetactor.util.MyJSONParse;



import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class BtActivity extends Activity {

	ListView mListView =null;
	String TAG = "luotest";
	Switch mSwitch = null;
	BluetoothAdapter mBTAdapter = null;
	TextView mBTStatusTextView = null;
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
		
		
		
		
		mSwitch = (Switch)findViewById(R.id.link_switch);
		mSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg1){
					//直接打开蓝牙
					mBTAdapter.enable();
					mBTStatusTextView.setText(getString(R.string.link));
				}else{
					//关闭蓝牙
					mBTAdapter.disable();
					mBTStatusTextView.setText(getString(R.string.unlink));
				}
				
			}});
		mBTStatusTextView = (TextView)findViewById(R.id.link_statusView1);
		mBTAdapter = BluetoothAdapter.getDefaultAdapter();
		if(mBTAdapter==null){
			Toast.makeText(getApplicationContext(), getString(R.string.no_bt_device), Toast.LENGTH_LONG).show();
			finish();
		}
		if(mBTAdapter.enable()){
			mSwitch.setChecked(true);
			mBTStatusTextView.setText(getString(R.string.link));
		}else{
			mSwitch.setChecked(false);
			mBTStatusTextView.setText(getString(R.string.unlink));
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

}
