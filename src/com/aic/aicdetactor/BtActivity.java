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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
	ImageView mImageViewSetting = null;

     List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
     BaseSimpleAdapter adapter;
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

 		
		IntentFilter mFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		this.registerReceiver(mReceiver, mFilter);
		
		mListView = (ListView)findViewById(R.id.listView);
		
		adapter = new BaseSimpleAdapter(BtActivity.this);
		adapter.setList(list);
		mImageViewSetting = (ImageView)findViewById(R.id.sensor_image);
		mImageViewSetting.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), btwifi_setting_activity.class);
				startActivity(intent);
			}
			
		});
		mListView.setAdapter(adapter);
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
 				map.put("check_name", device.getName());
				map.put("value", device.getAddress());
				list.add(map); 
				adapter.setList(list);
				adapter.notifyDataSetChanged();
				mListView.setVisibility(View.VISIBLE);
				// 搜索完成action
				count++;
				Log.d("test", "count = " + count);
			}
		}
		
	};
	
}

class BaseSimpleAdapter extends BaseAdapter{

	List<Map<String,Object>> list;
	Context mContext;
	BaseSimpleAdapter(Context context){
		mContext = context;
	}
	
	
	public void setList(List<Map<String,Object>> dataList){
		list = dataList;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = (View)LayoutInflater.from(mContext).inflate(R.layout.two_text_item, null);
		((TextView)convertView.findViewById(R.id.checkitem_name)).setText(list.get(position).get("check_name").toString());;
		((TextView)convertView.findViewById(R.id.value)).setText(list.get(position).get("value").toString());;
			return convertView;
	}
	
}
