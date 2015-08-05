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
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class BtActivity extends Activity implements OnClickListener{

	private ListView mListView =null;
	private final String TAG = "luotest";
	private Switch mSwitch = null;
	private BluetoothAdapter mBTAdapter = null;
	private TextView mBTStatusTextView = null;
	private ImageView mImageViewSetting = null;
	
	private TextView mBTName = null;	
	private TextView mBTStatus = null;

	private Button bNetButton = null;

	private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	private SimpleAdapter adapter;
 	private final String mName = "Name";
 	private final String mValue = "Value";
    private int count = 0;
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		Log.d(TAG, " onActivityResult() requestCode = "+requestCode +",resultCode = "+resultCode);
		if(requestCode == RESULT_OK){
			Log.d(TAG, " onActivityResult()");
			Bundle b=data.getExtras();
			mBTName.setText(b.get("Name").toString());
			mBTStatus.setText(b.get("Status").toString());
		}
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
		requestWindowFeature(Window.FEATURE_NO_TITLE);  //无title  
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
		              WindowManager.LayoutParams.FLAG_FULLSCREEN); 
		
		setContentView(R.layout.bt_activity);

 		
		IntentFilter mFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		this.registerReceiver(mReceiver, mFilter);
		
		mBTName = (TextView)findViewById(R.id.sensor_name2);
		mBTStatus = (TextView)findViewById(R.id.sensor_status2);
		
		
		
		bNetButton = (Button)findViewById(R.id.net);
		bNetButton.setOnClickListener(this);
		mListView = (ListView)findViewById(R.id.listView);
		
		adapter = new SimpleAdapter(this, list,
				R.layout.bt_adapter_item, new String[] {
				mName,mValue}, new int[] {R.id.bt_name,R.id.bt_adress});

		mListView.setAdapter(adapter);
		mImageViewSetting = (ImageView)findViewById(R.id.sensor_image);
		mImageViewSetting.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(BtActivity.this, btwifi_setting_activity.class);
				startActivityForResult(intent, RESULT_OK);
			}
			
		});
		
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
		initListData();
		
		
	}
	
 
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		CaptrueDataFromRemote();
		super.onResume();
	}


	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

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
 				map.put(mName, device.getName());
				map.put(mValue, device.getAddress());
//				list.add(map); 				
//				adapter.notifyDataSetChanged();
				mListView.setVisibility(View.VISIBLE);
				// 搜索完成action
				count++;
				Log.d("test", "count = " + count);
			}
		}
		
	};
	

 void initListData(){
	 mListView.setVisibility(View.VISIBLE);
	 Map<String, Object> map = new HashMap<String, Object>();
		map.put(mName, getString(R.string.charge));
		//map.put(mValue, device.getAddress());
		list.add(map);
		
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put(mName, getString(R.string.shock));
		//map.put(mValue, device.getAddress());
		list.add(map1);
		
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put(mName, getString(R.string.temperature));
		//map.put(mValue, device.getAddress());
		list.add(map2);
		
		Map<String, Object> map3 = new HashMap<String, Object>();
		map3.put(mName, getString(R.string.revolution_speed));
		//map.put(mValue, device.getAddress());
		list.add(map3);
		
 }
 
 void CaptrueDataFromRemote(){
	 String charging = "38";
	 String shock = "x:12 y:37 z:76";
	 String temperature = "12";
	 String speed = "67";
	 
	 Map<String, Object> map2  = list.get(0);
	 map2.put(mValue, charging);
	 
	 map2  = list.get(1);
	 map2.put(mValue, shock);	
	 
	 map2  = list.get(2);
	 map2.put(mValue, temperature);	
	 
	 map2  = list.get(3);
	 map2.put(mValue, speed);
	 
	 adapter.notifyDataSetChanged();
 }

@Override
public void onClick(View arg0) {
	// TODO Auto-generated method stub
	switch(arg0.getId()){
	case R.id.net:
		Intent intent = new Intent();
		intent.setClass(getApplicationContext(), NetTestActivity.class);
		startActivityForResult(intent, RESULT_OK);
		break;
	}
}

}
