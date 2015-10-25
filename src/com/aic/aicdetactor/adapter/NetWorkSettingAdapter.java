package com.aic.aicdetactor.adapter;

import java.util.List;

import com.aic.aicdetactor.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

public class NetWorkSettingAdapter extends BaseExpandableListAdapter{
	private LayoutInflater mInflater;
	private Context mContext = null;
	String[] groupViewText;
	List<View> settingViewList;
	
	public NetWorkSettingAdapter(Context context, List<View> settingViewList) {
		// TODO Auto-generated constructor stub
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
		this.settingViewList = settingViewList;
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
		return settingViewList.get(arg0);
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
		return settingViewList.size();
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
