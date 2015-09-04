package com.aic.aicdetactor.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
import android.widget.TextView;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;

public class SecExpandableListAdapter  extends BaseExpandableListAdapter {
	private Context context;
	private LayoutInflater mInflater;
	ArrayList<ArrayList<Map<String, String>>> mChildrenList;

	public SecExpandableListAdapter(Context context, ArrayList<ArrayList<Map<String, String>>> mChildrenList) {
		// TODO Auto-generated constructor stub
		this.context = context;
		mInflater = LayoutInflater.from(context);
		this.mChildrenList = mChildrenList;
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
		ExpandableListView ExListView = new ExpandableListView(context);
		AbsListView.LayoutParams lp = new AbsListView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		ExListView.setPadding(40, 5, 10, 0);
		ExListView.setLayoutParams(lp);
		return ExListView;
	}
	
	@Override
	public View getChildView(int arg0, int arg1, boolean arg2, View arg3, ViewGroup arg4) {
		// TODO Auto-generated method stub
		final ExpandableListView thrGroupView = getExpandableListView();
		final ThrExpandableListAdapter thrExListAdapter = new ThrExpandableListAdapter(context, mChildrenList);
		
		
		thrGroupView.setOnGroupExpandListener(new OnGroupExpandListener() {

			@Override
			public void onGroupExpand(int arg0) {
				// TODO Auto-generated method stub
				thrGroupView.removeAllViewsInLayout();
				AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
						500);
				thrGroupView.setLayoutParams(lp);
			}
		});
		
		thrGroupView.setOnGroupCollapseListener(new OnGroupCollapseListener() {

			@Override
			public void onGroupCollapse(int arg0) {
				// TODO Auto-generated method stub
				AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
						LayoutParams.WRAP_CONTENT);
				thrGroupView.setLayoutParams(lp);
			}
			
		});
		thrGroupView.setAdapter(thrExListAdapter);
		return thrGroupView;
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
		final TextView indexText;
		final TextView NameText;
		final TextView CheckValueText;
		final TextView DeadLineText;
		
		if (arg2 == null) {			
			arg2 = mInflater.inflate(R.layout.checkitem_sec_item, null);
		}
		/*HashMap<String, String> map = (HashMap<String, String>) mChildrenList
				.get(arg0).get(arg1);

		
		indexText = (TextView) arg3.findViewById(R.id.index);
		NameText = (TextView) arg3.findViewById(R.id.pathname);
		CheckValueText = (TextView) arg3.findViewById(R.id.checkvalue);
		DeadLineText = (TextView) arg3.findViewById(R.id.deadtime);
		
		
		NameText.setText(map.get(CommonDef.check_item_info.NAME));
		CheckValueText.setText(map.get(CommonDef.check_item_info.VALUE));	
		DeadLineText.setText(map.get(CommonDef.check_item_info.DEADLINE));	*/
		
		//NameText.setTextColor(Color.RED);	
		//NameText = (TextView) arg2.findViewById(R.id.pathname);
		//NameText.setText("我是二级目录");
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
