package com.aic.aicdetactor.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.aic.aicdetactor.CommonActivity;
import com.aic.aicdetactor.R;
import com.aic.aicdetactor.app.myApplication;
import com.aic.aicdetactor.comm.CommonDef;
import com.aic.aicdetactor.util.MLog;
import com.aic.aicdetactor.util.SystemUtil;
import com.aic.aicdetactor.view.GroupViewHolder;
import com.alibaba.fastjson.JSON;

public class StationListAdapter extends BaseExpandableListAdapter {

	private myApplication app = null;
	private Context mContext = null;
	private int mrouteIndex = 0;
	private CommonActivity mActivity = null;
	private LayoutInflater mInflater;
	private final String TAG = "luotest.StationListAdapter";
	private boolean mIsSpecial =false;
	// groupView data
	private List<Map<String, String>> mStationDisplayDataList = null;
	private ArrayList<ArrayList<Map<String, String>>> mDeviceArrayDisplayDataList = null;
	int secExlistItemHigh,thrExlistItemHigh;

	public StationListAdapter(CommonActivity av, Context context, int routeIndex) {
		mContext = context;
		this.mrouteIndex = routeIndex;
		mInflater = LayoutInflater.from(mContext);
		mActivity = av;
		mStationDisplayDataList = new ArrayList<Map<String, String>>();
		app = ((myApplication) mActivity.getApplication());
		this.mIsSpecial =app.isSpecialLine;
		thrExlistItemHigh = av.getResources().getDimensionPixelSize(R.dimen.exlist_item_high_level3);
		secExlistItemHigh = av.getResources().getDimensionPixelSize(R.dimen.exlist_item_high_level2); 
		InitStationData();
	}

	@Override
	public Object getChild(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return mDeviceArrayDisplayDataList.get(arg0).get(arg1);
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
	public View getChildView(int arg0, final int arg1, boolean arg2, View arg3,
			ViewGroup arg4) {
		MLog.Logd(TAG,"getChildView " + arg0 +" ,"+arg1);

		final ExpandableListView secGroupView = getExpandableListView();
		final DeviceListAdapter secExListAdapter = new DeviceListAdapter(mContext, mActivity,arg0,arg1,mIsSpecial);
		secGroupView.setAdapter(secExListAdapter);
		
		secGroupView.setSelectedGroup(arg1);
		
		secGroupView.setOnGroupExpandListener(new OnGroupExpandListener() {

			@Override
			public void onGroupExpand(int arg0) {
				// TODO Auto-generated method stub
				AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
						thrExlistItemHigh*app.mLineJsonData.StationInfo.get(mrouteIndex).DeviceItem.get(arg1).PartItem.size()+secExlistItemHigh);
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
		
		secGroupView.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View arg0) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		return secGroupView;
	}

	@Override
	public int getChildrenCount(int arg0) {
		// TODO Auto-generated method stub
		return mDeviceArrayDisplayDataList.get(arg0).size();
		//return 1;
	}

	@Override
	public Object getGroup(int arg0) {
		// TODO Auto-generated method stub
		MLog.Logd(TAG,"getGroup " + arg0 );
		return mStationDisplayDataList.get(arg0);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return mStationDisplayDataList.size();
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
		HashMap<String, String> map = (HashMap<String, String>) mStationDisplayDataList
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
			app.getLineDataClassifyFromOneFile(mIsSpecial);
			mStationDisplayDataList.clear();
			mDeviceArrayDisplayDataList = new ArrayList<ArrayList<Map<String, String>>>();
			for (int i = 0; i < app.mLineJsonData.StationInfo.size(); i++) {
				
				Map<String, String> map = new HashMap<String, String>();
				map.put(CommonDef.station_info.NAME,app.mLineJsonData.StationInfo.get(i).Name);
				if(!app.gIsDataChecked){
				map.put(CommonDef.station_info.DEADLINE, "2015");
				}
				map.put(CommonDef.station_info.PROGRESS, app.mLineJsonData.getItemCounts(1, i, true,mIsSpecial)+ "/" + app.mLineJsonData.getItemCounts(1, i, false,mIsSpecial));
				mStationDisplayDataList.add(map);
				try {
					ArrayList<Map<String, String>> deviceDisplayDataList = new ArrayList<Map<String, String>>();
					for (int deviceIndex = 0; deviceIndex < app.mLineJsonData.StationInfo.get(i).DeviceItem.size(); deviceIndex++) {
								Map<String, String> mapDevice = new HashMap<String, String>();
								mapDevice.put(CommonDef.device_info.NAME,app.mLineJsonData.StationInfo.get(i).DeviceItem.get(deviceIndex).Name);
								deviceDisplayDataList.add(mapDevice);	
					}
					mDeviceArrayDisplayDataList.add(deviceDisplayDataList);					
					
				} catch (Exception e) {
					e.printStackTrace();
				}	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		MLog.Logd(TAG, " InitData()<< "+String.valueOf(System.currentTimeMillis()-g));

	}

	

	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
	//	InitStationData();
		super.notifyDataSetChanged();
	}
	
	public List<String>getDeviceStatusArray(int station,int device){
		List<String> statusList = new ArrayList<String>();
		String str="";
		boolean bFind = false;
		for (int i = 0; i < app.mLineJsonData.StationInfo.size(); i++) {
			if(bFind) break;
			try {
				for (int deviceIndex = 0; deviceIndex < app.mLineJsonData.StationInfo.get(i).DeviceItem.size(); deviceIndex++) {
					if(i == station && deviceIndex == device){
						str =app.mLineJsonData.StationInfo.get(i).DeviceItem.get(deviceIndex).Status_Array;
						bFind=true;
						break;
						}
				}				
			} catch (Exception e) {
				e.printStackTrace();
			}	
		}
		
		String[]array = str.split("\\/");
		for(int k=0;k<array.length;k++){
			statusList.add(array[k]);
		}
		return statusList;
		
	}
}
