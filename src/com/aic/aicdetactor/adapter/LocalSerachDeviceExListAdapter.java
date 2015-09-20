package com.aic.aicdetactor.adapter;

import com.aic.aicdetactor.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

public class LocalSerachDeviceExListAdapter extends BaseExpandableListAdapter {
	private LayoutInflater mInflater;
	Context context;

	public LocalSerachDeviceExListAdapter(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		mInflater = LayoutInflater.from(context);
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
		return null;
	}

	@Override
	public int getChildrenCount(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getGroup(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return 5;
	}

	@Override
	public long getGroupId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getGroupView(int arg0, boolean arg1, View arg2, ViewGroup arg3) {
		// TODO Auto-generated method stub
		if (arg2 == null) {			
			arg2 = mInflater.inflate(R.layout.local_search_device_groupview_item, null);
		}
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
