package com.aic.aicdetactor.adapter;

import com.aic.aicdetactor.R;
import com.aic.aicdetactor.app.myApplication;
import com.aic.aicdetactor.comm.CommonDef;
import com.aic.aicdetactor.data.DownloadNormalRootData;
import com.aic.aicdetactor.util.SystemUtil;
import com.aic.aicdetactor.view.GroupViewHolder;
import com.alibaba.fastjson.JSON;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;

public class SearchResultStationExListAdapter extends BaseExpandableListAdapter {
	private LayoutInflater mInflater;
	Context context;
	Intent intent;
	Activity av;
	myApplication app;
	public SearchResultStationExListAdapter(Intent intent,Activity av) {
		// TODO Auto-generated constructor stub
		this.context = av.getApplicationContext();
		this.intent=intent;
		this.av =av;
		mInflater = LayoutInflater.from(context);
		initData();
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
		exListView.setPadding(50, 0, 0, 0);
		exListView.setLayoutParams(lp);
		return exListView;
	}
	
	@Override
	public View getChildView(int arg0, int arg1, boolean arg2, View arg3, ViewGroup arg4) {
		// TODO Auto-generated method stub
		final ExpandableListView expandableListView = getExpandableListView();
		SerachResultDeviceExListAdapter deviceExListAdapter = new SerachResultDeviceExListAdapter(context);
		expandableListView.setAdapter(deviceExListAdapter);
		
		expandableListView.setOnGroupExpandListener(new OnGroupExpandListener() {

			@Override
			public void onGroupExpand(int arg0) {
				// TODO Auto-generated method stub
				AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
						(1+app.mLocalSearchLineJsonData.StationInfo.get(arg0).DeviceItem.size())*44);
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
		return app.mLocalSearchLineJsonData.StationInfo.size();
	}

	@Override
	public Object getGroup(int arg0) {
		// TODO Auto-generated method stub
		return app.mLocalSearchLineJsonData;
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public long getGroupId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getGroupView(int arg0, boolean arg1, View arg2, ViewGroup arg3) {
		// TODO Auto-generated method stub
		GroupViewHolder holder =null;
		if (arg2 == null) {			
			arg2 = mInflater.inflate(R.layout.search_result_station_groupview_item, null);
			holder = new GroupViewHolder();
			holder.NameText = (TextView) arg2.findViewById(R.id.pathname);
			holder.DeadLineText = (TextView) arg2.findViewById(R.id.search_local_date);
			holder.ProcessText = (TextView) arg2.findViewById(R.id.progress);
			arg2.setTag(holder);
		}else{
			holder=(GroupViewHolder) arg2.getTag();
		}
		holder.NameText.setText(intent.getExtras().get("Name").toString());
		holder.DeadLineText.setText(intent.getExtras().get("Date").toString());
		holder.ProcessText.setText(intent.getExtras().get("Process").toString());
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

	void initData(){
		app = (myApplication)av.getApplication();
		String path =intent.getExtras().get("Path").toString();
		 String jsondata = SystemUtil.openFile(path);
		app.mLocalSearchLineJsonData = JSON.parseObject(jsondata,DownloadNormalRootData.class);  
	}
}
