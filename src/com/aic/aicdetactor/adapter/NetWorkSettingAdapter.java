package com.aic.aicdetactor.adapter;

import com.aic.aicdetactor.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NetWorkSettingAdapter extends BaseExpandableListAdapter {
	private LayoutInflater mInflater;
	private Context mContext = null;
	String[] groupViewText;
	
	public NetWorkSettingAdapter(Context context) {
		// TODO Auto-generated constructor stub
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
		groupViewText = context.getResources().getStringArray(R.array.network_setting);
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
		View childView = null ;
		switch (arg0) {
		case 0: childView = mInflater.inflate(R.layout.network_setting_item_machineinfo, null,false); break;
		case 1: childView = mInflater.inflate(R.layout.network_setting_item_retpassword, null,false); break;
		case 2: childView = mInflater.inflate(R.layout.network_setting_item_pushsetting, null,false); break;
		case 3: childView = mInflater.inflate(R.layout.network_setting_item_cardreader, null,false); break;
		case 4: childView = mInflater.inflate(R.layout.network_setting_item_storage, null,false); break;
		case 5: childView = mInflater.inflate(R.layout.network_setting_item_timesync, null,false); break;
		default:
			break;
		}
		
		return childView;
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
		return 6;
	}

	@Override
	public long getGroupId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getGroupView(int arg0, boolean isExpanded, View arg2, ViewGroup arg3) {
		// TODO Auto-generated method stub
		View groupView = mInflater.inflate(R.layout.network_setting_group_layout, null);
		TextView groupTextView = (TextView)groupView.findViewById(R.id.network_setting_groupview);
		groupTextView.setText(groupViewText[arg0]);
		ImageView arrowImage = (ImageView)groupView.findViewById(R.id.setting_arrow);
		 if(isExpanded){
			 arrowImage.setImageResource(R.drawable.network_setting_arrow_open);
         }else{
        	 arrowImage.setImageResource(R.drawable.network_setting_arrow_close);
         } 
		return groupView;
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
