package com.aic.aicdetactor.adapter;

import com.aic.aicdetactor.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;

public class SearchResultConExListAdapter extends BaseExpandableListAdapter {
	Context context;
	LayoutInflater inflater;
	OnClickListener listener;

	public SearchResultConExListAdapter(Context context,OnClickListener listener) {
		// TODO Auto-generated constructor stub
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.listener = listener;
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
		if (arg3 == null) {			
			arg3 = inflater.inflate(R.layout.search_condition_result_child_item, null);
		}
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
			arg2 = inflater.inflate(R.layout.search_condition_result_group_item, null);
		}
		ImageView spectrum = (ImageView) arg2.findViewById(R.id.search_con_result_spectrum);
		spectrum.setOnClickListener(listener);
		ImageView diagram = (ImageView) arg2.findViewById(R.id.search_con_result_diagram);
		diagram.setOnClickListener(listener);
		ImageView more = (ImageView) arg2.findViewById(R.id.search_con_result_more);
		more.setOnClickListener(listener);
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
