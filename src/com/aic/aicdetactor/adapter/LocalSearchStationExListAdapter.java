package com.aic.aicdetactor.adapter;

import com.aic.aicdetactor.R;
import com.aic.aicdetactor.comm.CommonDef;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;

public class LocalSearchStationExListAdapter extends BaseExpandableListAdapter {
	private LayoutInflater mInflater;
	Context context;

	public LocalSearchStationExListAdapter(Context context) {
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

	public ExpandableListView getExpandableListView(){
		ExpandableListView exListView = new ExpandableListView(context);
		AbsListView.LayoutParams lp = new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		exListView.setLayoutParams(lp);
		return exListView;
	}
	
	@Override
	public View getChildView(int arg0, int arg1, boolean arg2, View arg3, ViewGroup arg4) {
		// TODO Auto-generated method stub
		final ExpandableListView expandableListView = getExpandableListView();
		LocalSerachDeviceExListAdapter deviceExListAdapter = new LocalSerachDeviceExListAdapter(context);
		expandableListView.setAdapter(deviceExListAdapter);
		
		expandableListView.setOnGroupExpandListener(new OnGroupExpandListener() {

			@Override
			public void onGroupExpand(int arg0) {
				// TODO Auto-generated method stub
				AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
						500);
				expandableListView.setLayoutParams(lp);
			}
		});
		
		expandableListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {

			@Override
			public void onGroupCollapse(int arg0) {
				// TODO Auto-generated method stub
				AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
						LayoutParams.WRAP_CONTENT);
				expandableListView.setLayoutParams(lp);
			}
			
		});
		
		return expandableListView;
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
		return 10;
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
			arg2 = mInflater.inflate(R.layout.local_search_station_groupview_item, null);
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
