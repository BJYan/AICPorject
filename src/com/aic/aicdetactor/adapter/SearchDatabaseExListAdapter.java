package com.aic.aicdetactor.adapter;

import java.util.List;

import com.aic.aicdetactor.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.RadioButton;

public class SearchDatabaseExListAdapter extends BaseExpandableListAdapter {
	Context context;
	private LayoutInflater mInflater;
	List<View> dBsearchItemList;
	
	public SearchDatabaseExListAdapter(Context context, List<View> dBsearchItemList) {
		// TODO Auto-generated constructor stub
		this.context = context;
		mInflater = LayoutInflater.from(context);
		this.dBsearchItemList = dBsearchItemList;
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

		return dBsearchItemList.get(arg0);
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
		return dBsearchItemList.size();
	}

	@Override
	public long getGroupId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getGroupView(int arg0, boolean arg1, View arg2, ViewGroup arg3) {
		// TODO Auto-generated method stub
		View DBSearchGroupItem = mInflater.inflate(R.layout.search_database_group_item, null);
		RadioButton radioButton = (RadioButton) DBSearchGroupItem.findViewById(R.id.search_db_radiobutton);
		radioButton.setChecked(arg1);
		return DBSearchGroupItem;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return true;
	}

}
