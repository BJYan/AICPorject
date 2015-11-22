package com.aic.aicdetactor.adapter;

import java.util.List;

import com.aic.aicdetactor.R;
import com.aic.aicdetactor.activity.BlueToothRenameActivity;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BlueToothDevListAdapter extends BaseAdapter{
	Context context;
	private LayoutInflater mInflater;
	List<BluetoothDevice> btDevices;

	public BlueToothDevListAdapter(Context context, List<BluetoothDevice> btDevices) {
		// TODO Auto-generated constructor stub
		this.context = context;
		mInflater = LayoutInflater.from(context);
		this.btDevices = btDevices;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return btDevices.size();
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
		if(arg1==null) arg1 = mInflater.inflate(R.layout.bluetooth_device_list_item, null);
		arg1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("BluetoothDev", btDevices.get(arg0));
				intent.setClass(context, BlueToothRenameActivity.class);
				context.startActivity(intent);
			}
		});
		TextView DevName = (TextView) arg1.findViewById(R.id.bluetooth_device_name);
		TextView Devmac = (TextView) arg1.findViewById(R.id.bluetooth_mac);
		DevName.setText(btDevices.get(arg0).getName());
		Devmac.setText(btDevices.get(arg0).getAddress());
		return arg1;
	}

}
