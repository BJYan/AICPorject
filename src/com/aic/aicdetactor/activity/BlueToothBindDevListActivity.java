package com.aic.aicdetactor.activity;

import java.util.ArrayList;
import java.util.List;

import com.aic.aicdetactor.CommonActivity;
import com.aic.aicdetactor.R;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class BlueToothBindDevListActivity extends CommonActivity{
	ListView DevList;
	LayoutInflater inflater;
	BluetoothAdapter adapter;
	List<BluetoothDevice> BtDevices;
	ListAdapter listAdapter;
	ProgressBar pbar;
	TextView pbar_text;
	
	BroadcastReceiver mReceiver = new BroadcastReceiver() {

	    public void onReceive(Context context, Intent intent) {
	        String action = intent.getAction();
	        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
	            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
	            BtDevices.add(device);
	            listAdapter.notifyDataSetChanged();
	        }
	        if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
	        	dismissProgressBar();
	        	Toast.makeText(context, "搜索完成！", Toast.LENGTH_SHORT).show();
	        }
	    }
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bluetooth_bind_sensor_list_layout);
		setActionBar("传感器设置",true); 
		
		adapter = BluetoothAdapter.getDefaultAdapter(); 
		BtDevices = new ArrayList<BluetoothDevice>();
		
		inflater = getLayoutInflater();
		DevList = (ListView) findViewById(R.id.bluetooth_bind_sensor_list);
		listAdapter = new ListAdapter();
		DevList.setAdapter(listAdapter);
		
		pbar = (ProgressBar) findViewById(R.id.bluetooth_bind_pbar);
		pbar_text = (TextView)findViewById(R.id.bluetooth_bind_pbar_text);
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// Register the BroadcastReceiver
		BtDevices.clear();
		listAdapter.notifyDataSetChanged();
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		registerReceiver(mReceiver, filter);
		adapter.startDiscovery();
		showProgressBar();
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		unregisterReceiver(mReceiver);
	}
	
	class ListAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return BtDevices.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(final int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			if(arg1==null) arg1 = inflater.inflate(R.layout.bluetooth_device_list_item, null);
			arg1.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(); 
					intent.putExtra("BluetoothDev", BtDevices.get(arg0));
					intent.setClass(BlueToothBindDevListActivity.this, BlueToothRenameActivity.class);
					BlueToothBindDevListActivity.this.startActivity(intent);
				}
			});
			TextView DevName = (TextView) arg1.findViewById(R.id.bluetooth_device_name);
			DevName.setText(BtDevices.get(arg0).getName());
			return arg1;
		}	
	}
	
	private void showProgressBar(){
		pbar_text.setVisibility(View.VISIBLE);
		pbar.setVisibility(View.VISIBLE);
	}
	
	private void dismissProgressBar(){
		pbar_text.setVisibility(View.INVISIBLE);
		pbar.setVisibility(View.INVISIBLE);		
	}
}
