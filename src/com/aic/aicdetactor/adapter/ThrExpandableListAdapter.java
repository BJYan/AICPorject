package com.aic.aicdetactor.adapter;

import java.util.ArrayList;
import java.util.Map;

import com.aic.aicdetactor.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class ThrExpandableListAdapter extends BaseExpandableListAdapter {
	Context context;
	private LayoutInflater mInflater;
	ArrayList<ArrayList<Map<String, String>>> mChildrenList;
	
	public ThrExpandableListAdapter(Context context, ArrayList<ArrayList<Map<String, String>>> mChildrenList) {
		// TODO Auto-generated constructor stub
		this.context = context;
		mInflater = LayoutInflater.from(context);
		this.mChildrenList = mChildrenList;
	}

	@Override
	public Object getChild(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public long getChildId(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getChildView(int arg0, int arg1, boolean arg2, View arg3, ViewGroup arg4) {
		// TODO Auto-generated method stub
		View argtest = mInflater.inflate(R.layout.checkunit, null);
		TextView NameText = (TextView) argtest.findViewById(R.id.pathname);
		NameText.setText("我是4级目录");
		return argtest;
	}

	@Override
	public int getChildrenCount(int arg0) {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public Object getGroup(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public long getGroupId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getGroupView(int arg0, boolean arg1, View arg2, ViewGroup arg3) {
		// TODO Auto-generated method stub
		arg2 = mInflater.inflate(R.layout.checkunit, null);
		final TextView NameText;
		NameText = (TextView) arg2.findViewById(R.id.pathname);
		NameText.setText("我是三级目录");
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
