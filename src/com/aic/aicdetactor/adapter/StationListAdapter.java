package com.aic.aicdetactor.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.aic.aicdetactor.R;
import com.aic.aicdetactor.app.myApplication;
import com.aic.aicdetactor.comm.CommonDef;
import com.aic.aicdetactor.data.CheckStatus;
import com.aic.aicdetactor.data.KEY;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsoluteLayout;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StationListAdapter extends BaseExpandableListAdapter {

	private myApplication app = null;
	private Context mContext = null;
	private int mrouteIndex = 0;
	private Activity mActivity = null;
	private LayoutInflater mInflater;
	private final String TAG = "luotest";
	private List<Object> mDeviceItemList = null;
	
	// groupView data
	private List<Map<String, String>> mDataList = null;
	private ArrayList<ArrayList<Map<String, String>>> mChildrenList = null;
	private List<Object> mStationItemList = null;

	public StationListAdapter(Activity av, Context context, int routeIndex) {
		mContext = context;
		this.mrouteIndex = routeIndex;
		mInflater = LayoutInflater.from(mContext);
		mActivity = av;
		mDataList = new ArrayList<Map<String, String>>();
		app = ((myApplication) mActivity.getApplication());

		InitData();
		initeChild();
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
		AbsListView.LayoutParams lp = new AbsListView.LayoutParams(LayoutParams.WRAP_CONTENT, 50);
		ExListView.setLayoutParams(lp);
		return ExListView;
	}

	@Override
	public View getChildView(int arg0, int arg1, boolean arg2, View arg3,
			ViewGroup arg4) {
		
		/*final TextView indexText;
		final TextView NameText;
		final TextView CheckValueText;
		final TextView DeadLineText;
		
		if (arg3 == null) {			
			arg3 = mInflater.inflate(R.layout.checkunit, null);
		}
		HashMap<String, String> map = (HashMap<String, String>) mChildrenList
				.get(arg0).get(arg1);

		
		indexText = (TextView) arg3.findViewById(R.id.index);
		NameText = (TextView) arg3.findViewById(R.id.pathname);
		CheckValueText = (TextView) arg3.findViewById(R.id.checkvalue);
		DeadLineText = (TextView) arg3.findViewById(R.id.deadtime);
		
		
		NameText.setText(map.get(CommonDef.device_info.NAME));
		//CheckValueText.setText(map.get(CommonDef.device_info.VALUE));	
		DeadLineText.setText(map.get(CommonDef.device_info.DEADLINE));	
		
		//NameText.setTextColor(Color.RED);	
		return arg3;*/
		final ExpandableListView secGroupView = getExpandableListView();
		final SecExpandableListAdapter secExListAdapter = new SecExpandableListAdapter(mContext, mChildrenList);
		secGroupView.setAdapter(secExListAdapter);
		
		secGroupView.setOnGroupExpandListener(new OnGroupExpandListener() {

			@Override
			public void onGroupExpand(int arg0) {
				// TODO Auto-generated method stub
				AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
						500);
				secGroupView.setLayoutParams(lp);
			}
		});
		
		secGroupView.setOnGroupCollapseListener(new OnGroupCollapseListener() {

			@Override
			public void onGroupCollapse(int arg0) {
				// TODO Auto-generated method stub
				AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
						50);
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

		final TextView indexText;
		final TextView NameText;
		final TextView DeadLineText;
		final TextView StausText;
		final TextView ProcessText;
		if (arg2 == null) {			
			arg2 = mInflater.inflate(R.layout.checkitem, null);
		}
		HashMap<String, String> map = (HashMap<String, String>) mDataList
				.get(arg0);

		indexText = (TextView) arg2.findViewById(R.id.index);
		indexText.setText(""+(arg0+1));
		NameText = (TextView) arg2.findViewById(R.id.pathname);
		NameText.setText(map.get(CommonDef.station_info.NAME).toString());
		
		DeadLineText = (TextView) arg2.findViewById(R.id.deadtime);
		DeadLineText.setText(map.get(CommonDef.station_info.DEADLINE));
		
		StausText = (TextView) arg2.findViewById(R.id.status);
		StausText.setText(map.get(CommonDef.station_info.STATUS));
		
		ProcessText = (TextView) arg2.findViewById(R.id.progress);
		ProcessText.setText(map.get(CommonDef.station_info.PROGRESS));
		
		indexText.setTextColor(Color.RED);

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

	void InitData() {
		try {
			mStationItemList = app.getStationList(mrouteIndex);

			CheckStatus status = null;
			// int itemindex = mStationIndex;
			int itemindex = 0;
			mDataList.clear();
			for (int i = 0; i < mStationItemList.size(); i++) {
				Map<String, String> map = new HashMap<String, String>();
				status = app.getNodeCount(mStationItemList.get(i), 2, 0);
				status.setContext(mContext);
				map.put(CommonDef.station_info.INDEX, "" + (itemindex + 1));
				map.put(CommonDef.station_info.NAME,
						app.getDeviceItemName(mStationItemList.get(i)));

				map.put(CommonDef.station_info.DEADLINE, status.mLastTime);
				map.put(CommonDef.station_info.STATUS, status.getStatus());

				map.put(CommonDef.station_info.PROGRESS, status.mCheckedCount
						+ "/" + status.mSum);
				mDataList.add(map);
				itemindex++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		mChildrenList = new ArrayList<ArrayList<Map<String, String>>>();
		for (int i = 0; i < mStationItemList.size(); i++) {
			InitChidrenData(i, 0, false);
		}
	}

	void initeChild() {
		Log.d(TAG,"initeChild() start ");
		
		Log.d(TAG,"initeChild() end ");
	}

	void InitChidrenData(int stationIndex, int itemIndexs, boolean updateAdapter) {
		try {

			Object object = mStationItemList.get(stationIndex);
			Log.d(TAG,"InitChidrenData() object is "+object.toString());
			mDeviceItemList = app.getDeviceList(object);
			Log.d(TAG,"InitChidrenData() mDeviceItemList size is "+mDeviceItemList.size());
			ArrayList<Map<String, String>> childList = new ArrayList<Map<String, String>>();
		
			for (int i = 0; i < mDeviceItemList.size(); i++) {
				Map<String, String> map = new HashMap<String, String>();
				Log.d(TAG,"InitChidrenData() PartItemData is "+mDeviceItemList.get(i).toString());
				map.put(CommonDef.device_info.NAME,	app.getDeviceItemName(mDeviceItemList.get(i)));
//				map.put(CommonDef.check_item_info.DATA_TYPE,
//						app.getPartItemCheckUnitName(mDeviceItemList.get(i),
//								CommonDef.partItemData_Index.PARTITEM_DATA_TYPE));

//				map.put(CommonDef.check_item_info.VALUE,
//						app.getPartItemCheckUnitName(
//								mDeviceItemList.get(i),
//								CommonDef.partItemData_Index.PARTITEM_ADDITIONAL_INFO));

//				map.put(CommonDef.check_item_info.DEADLINE,
//						app.getPartItemCheckUnitName(
//								mDeviceItemList.get(i),
//								CommonDef.partItemData_Index.PARTITEM_ADD_END_DATE_20));

			
				childList.add(map);
			}
			mChildrenList.add(childList);
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		InitData();
		initeChild();
		super.notifyDataSetChanged();
	}
	
}
