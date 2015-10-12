package com.aic.aicdetactor.adapter;

import com.aic.aicdetactor.R;
import com.aic.aicdetactor.activity.BlueToothRenameActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class BlueToothDevListAdapter extends BaseAdapter{
	Context context;
	private LayoutInflater mInflater;

	public BlueToothDevListAdapter(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		mInflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 3;
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
		if(arg1==null) arg1 = mInflater.inflate(R.layout.bluetooth_device_list_item, null);
		arg1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(context, BlueToothRenameActivity.class);
				context.startActivity(intent);
			}
		});
		return arg1;
	}

}
