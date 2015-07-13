package com.aic.aicdetactor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

     private static String DISCOVERY_STARTED = "android.bluetooth.adapter.action.DISCOVERY_STARTED";
     private static String DISCOVERY_FINISHED = "android.bluetooth.adapter.action.DISCOVERY_FINISHED";
      List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    private int count = 0;
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
		IntentFilter filter = new IntentFilter();
		filter.addAction(DISCOVERY_STARTED);
		filter.addAction(DISCOVERY_FINISHED);
		registerReceiver(bluetoothReceiver, filter);
		
		IntentFilter mFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		this.registerReceiver(mReceiver, mFilter);
		
		mListView = (ListView)findViewById(R.id.listView);

		
		mSwitch = (Switch)findViewById(R.id.link_switch);
		mSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg1){
					//直接打开蓝牙
					mBTAdapter.enable();
					mBTAdapter.startDiscovery();
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
			mBTAdapter.startDiscovery();
			mSwitch.setChecked(true);
			mBTStatusTextView.setText(getString(R.string.link));
		}else{
			mSwitch.setChecked(false);
			mBTStatusTextView.setText(getString(R.string.unlink));
		}
		
		
		
	}
	
	private BroadcastReceiver bluetoothReceiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			SimpleAdapter adapter = new SimpleAdapter(BtActivity.this, list, R.layout.two_text_item, new String[] { "check_name",  "value" }, new int[] { R.id.checkitem_name, R.id.value });
			mListView.setAdapter(adapter);
			mListView.setVisibility(View.VISIBLE);
		}
		
	};
	
	
	private BroadcastReceiver mReceiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			// 查找到设备action
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				// 得到蓝牙设备
				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				// 如果是已配对的则略过，已得到显示，其余的在添加到列表中进行显示
				Map<String, Object> map = new HashMap<String, Object>();
 				map.put("check_name", device.getName());
				map.put("value", device.getAddress());
				list.add(map); 
				// 搜索完成action
				count++;
				Log.d("test", "count = " + count);
			}
		}
		
	};
}
