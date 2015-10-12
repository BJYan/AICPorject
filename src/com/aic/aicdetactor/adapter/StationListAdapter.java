package com.aic.aicdetactor.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.aic.aicdetactor.R;
import com.aic.aicdetactor.app.myApplication;
import com.aic.aicdetactor.comm.CommonDef;
import com.aic.aicdetactor.data.DownloadNormalData;
import com.aic.aicdetactor.util.MLog;
import com.aic.aicdetactor.util.SystemUtil;
import com.aic.aicdetactor.view.GroupViewHolder;
import com.alibaba.fastjson.JSON;

public class StationListAdapter extends BaseExpandableListAdapter {

	private myApplication app = null;
	private Context mContext = null;
	private int mrouteIndex = 0;
	private Activity mActivity = null;
	private LayoutInflater mInflater;
	private final String TAG = "luotest.StationListAdapter";
	
	// groupView data
	private List<Map<String, String>> mDataList = null;
	private ArrayList<ArrayList<Map<String, String>>> mChildrenList = null;

	public StationListAdapter(Activity av, Context context, int routeIndex) {
		mContext = context;
		this.mrouteIndex = routeIndex;
		mInflater = LayoutInflater.from(mContext);
		mActivity = av;
		mDataList = new ArrayList<Map<String, String>>();
		app = ((myApplication) mActivity.getApplication());

		InitStationData();
	}

	@Override
	public Object getChild(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return mChildrenList.get(arg0).get(arg1);
	}

	@Override
	public long getChildId(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public ExpandableListView getExpandableListView(){
		ExpandableListView ExListView = new ExpandableListView(mContext);
		AbsListView.LayoutParams lp = new AbsListView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		ExListView.setPadding(30, 0, 10, 0);
		ExListView.setLayoutParams(lp);
		ExListView.setGroupIndicator(null);
		return ExListView;
	}

	@Override
	public View getChildView(int arg0, int arg1, boolean arg2, View arg3,
			ViewGroup arg4) {
		MLog.Logd(TAG,"getChildView " + arg0 +" ,"+arg1);

		final ExpandableListView secGroupView = getExpandableListView();
		final DeviceListAdapter secExListAdapter = new DeviceListAdapter(mContext, mActivity,arg0,arg1);
		secGroupView.setAdapter(secExListAdapter);
		
		secGroupView.setSelectedGroup(arg1);
		
		secGroupView.setOnGroupExpandListener(new OnGroupExpandListener() {

			@Override
			public void onGroupExpand(int arg0) {
				// TODO Auto-generated method stub
				AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
						(3+CommonDef.LISTITEM_HEIGHT)*mDataList.size());
				secGroupView.setLayoutParams(lp);
			}
		});
		
		secGroupView.setOnGroupCollapseListener(new OnGroupCollapseListener() {

			@Override
			public void onGroupCollapse(int arg0) {
				// TODO Auto-generated method stub
				AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
						LayoutParams.WRAP_CONTENT);
				secGroupView.setLayoutParams(lp);
			}
			
		});
		return secGroupView;
	}

	@Override
	public int getChildrenCount(int arg0) {
		// TODO Auto-generated method stub
		return mChildrenList.get(arg0).size();
	}

	@Override
	public Object getGroup(int arg0) {
		// TODO Auto-generated method stub
		MLog.Logd(TAG,"getGroup " + arg0 );
		return mDataList.get(arg0);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return mDataList.size();
	}

	@Override
	public long getGroupId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getGroupView(int arg0, boolean arg1, View arg2, ViewGroup arg3) {
		// TODO Auto-generated method stub
		
		MLog.Logd(TAG,"getGroupView " + arg0 );
		GroupViewHolder holder =null;
		HashMap<String, String> map = (HashMap<String, String>) mDataList
				.get(arg0);
		if (arg2 == null) {			
			arg2 = mInflater.inflate(R.layout.station_list_item, null);
			holder = new GroupViewHolder();
			holder.image = (ImageView) arg2.findViewById(R.id.arrow);
			holder.NameText = (TextView) arg2.findViewById(R.id.pathname);
			holder.DeadLineText = (TextView) arg2.findViewById(R.id.deadtime);
			holder.ProcessText = (TextView) arg2.findViewById(R.id.progress);
			arg2.setTag(holder);
		}else{
			holder=(GroupViewHolder) arg2.getTag();
			
		}
		holder.NameText.setText(map.get(CommonDef.station_info.NAME).toString());
		holder.DeadLineText.setText(map.get(CommonDef.station_info.DEADLINE));
		holder.ProcessText.setText(map.get(CommonDef.station_info.PROGRESS));
		if(arg1){
			holder.image.setBackgroundResource(R.drawable.arrow_ex);
		}else{
			holder.image.setBackgroundResource(R.drawable.arrow);
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
		return true;
	}

	void InitStationData() {

		long g=System.currentTimeMillis();
		MLog.Logd(TAG, " InitData()>> "+g);
		try {
           String path= app.mFileList.get(mrouteIndex).get("LinePath");
           String  planjson = SystemUtil.openFile(path);
			 app.mNormalLineJsonData=JSON.parseObject(planjson,DownloadNormalData.class);
			mDataList.clear();
			mChildrenList = new ArrayList<ArrayList<Map<String, String>>>();
			for (int i = 0; i < app.mNormalLineJsonData.StationInfo.size(); i++) {
				Map<String, String> map = new HashMap<String, String>();
				map.put(CommonDef.station_info.NAME,app.mNormalLineJsonData.StationInfo.get(i).Name);
				map.put(CommonDef.station_info.DEADLINE, "2015");
				map.put(CommonDef.station_info.PROGRESS, app.mNormalLineJsonData.getItemCounts(1, i, true)+ "/" + app.mNormalLineJsonData.getItemCounts(1, i, false));
				mDataList.add(map);
				InitDeviceData(i, 0, false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		MLog.Logd(TAG, " InitData()<< "+String.valueOf(System.currentTimeMillis()-g));

	}

	
	void InitDeviceData(int stationIndex, int itemIndexs, boolean updateAdapter) {

		long g=System.currentTimeMillis();
		MLog.Logd(TAG, " InitChidrenData()>> stationIndex="+stationIndex +","+g);
		try {
			ArrayList<Map<String, String>> childList = new ArrayList<Map<String, String>>();
			for (int i = 0; i < app.mNormalLineJsonData.StationInfo.get(stationIndex).DeviceItem.size(); i++) {
				Map<String, String> map = new HashMap<String, String>();
				map.put(CommonDef.device_info.NAME,	app.mNormalLineJsonData.StationInfo.get(stationIndex).DeviceItem.get(i).Name);
				childList.add(map);
			}
			mChildrenList.add(childList);
			
		} catch (Exception e) {
			e.printStackTrace();
		}	

		MLog.Logd(TAG, " InitChidrenData()<< stationIndex="+stationIndex +","+String.valueOf(System.currentTimeMillis()-g));
	}

	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		InitStationData();
		super.notifyDataSetChanged();
	}
	
}
