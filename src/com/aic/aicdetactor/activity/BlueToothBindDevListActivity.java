package com.aic.aicdetactor.activity;

import com.aic.aicdetactor.CommonActivity;
import com.aic.aicdetactor.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

public class BlueToothBindDevListActivity extends CommonActivity{
	ListView DevList;
	LayoutInflater inflater;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bluetooth_bind_sensor_list_layout);
		
		inflater = getLayoutInflater();
		DevList = (ListView) findViewById(R.id.bluetooth_bind_sensor_list);
		ListAdapter listAdapter = new ListAdapter();
		DevList.setAdapter(listAdapter);
		
		setActionBar("传感器设置",true); 
	}
	
	class ListAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 6;
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
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			if(arg1==null) arg1 = inflater.inflate(R.layout.bluetooth_device_list_item, null);
			arg1.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					intent.setClass(BlueToothBindDevListActivity.this, BlueToothRenameActivity.class);
					BlueToothBindDevListActivity.this.startActivity(intent);
				}
			});
			return arg1;
		}
		
	}
}
