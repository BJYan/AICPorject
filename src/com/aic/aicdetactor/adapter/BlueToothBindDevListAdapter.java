package com.aic.aicdetactor.adapter;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.aic.aicdetactor.R;
import com.aic.aicdetactor.activity.BlueToothRenameActivity;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BlueToothBindDevListAdapter extends BaseExpandableListAdapter {
	Context context;
	private LayoutInflater mInflater;
	List<BluetoothDevice> bondedDevices;

	public BlueToothBindDevListAdapter(Context context, List<BluetoothDevice> bondedDevices) {
		// TODO Auto-generated constructor stub
		this.context = context;
		mInflater = LayoutInflater.from(context);
		this.bondedDevices = bondedDevices;
	}
	
	@Override
	public Object getChild(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getChildId(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getChildView(int arg0, int arg1, boolean arg2, View arg3, ViewGroup arg4) {
		// TODO Auto-generated method stub
		if(arg3==null) arg3 = mInflater.inflate(R.layout.bluetooth_binded_devlist_child_item, null);
		
		return arg3;
	}

	@Override
	public int getChildrenCount(int arg0) {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public Object getGroup(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return bondedDevices.size();
	}

	@Override
	public long getGroupId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getGroupView(final int arg0, boolean arg1, View arg2, ViewGroup arg3) {
		// TODO Auto-generated method stub
		if(arg2==null) arg2 = mInflater.inflate(R.layout.bluetooth_binded_devlist_group_item, null);
		ImageView GroupItemArror = (ImageView) arg2.findViewById(R.id.bluetooth_bind_dev_arrow);
		GroupItemArror.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("BluetoothDev", bondedDevices.get(arg0));
				intent.setClass(context, BlueToothRenameActivity.class);
				context.startActivity(intent);
			}
		});
		TextView DevName = (TextView) arg2.findViewById(R.id.bluetooth_device_name);
		DevName.setText(bondedDevices.get(arg0).getName());
		return arg2;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return false;
	}

}
