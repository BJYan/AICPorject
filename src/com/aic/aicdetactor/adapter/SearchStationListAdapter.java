package com.aic.aicdetactor.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnLongClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;

import com.aic.aicdetactor.CommonActivity;
import com.aic.aicdetactor.R;
import com.aic.aicdetactor.adapter.StationListAdapter.InitJsonDataThread;
import com.aic.aicdetactor.comm.CommonDef;
import com.aic.aicdetactor.comm.LineType;
import com.aic.aicdetactor.paramsdata.StationListInfoParams;
import com.aic.aicdetactor.util.MLog;

public class SearchStationListAdapter extends StationListAdapter {

	public SearchStationListAdapter(CommonActivity av, Context context,
			Handler handler) {
		super(av, context, handler);
		TAG = "SearchStationListAdapter";
		JsonDataThread InitThread = new JsonDataThread();
		InitThread.start();
		// TODO Auto-generated constructor stub
	}
	@Override
	public View getChildView(int arg0, final int arg1, boolean arg2, View arg3,
			ViewGroup arg4) {
		MLog.Logd(TAG,"getChildView " + arg0 +" ,"+arg1);

		final ExpandableListView secGroupView = getExpandableListView();
		SearchDeviceListAdapter secExListAdapter = new SearchDeviceListAdapter(mContext, mActivity,arg0,arg1,LineType.AllRoute);
		secGroupView.setAdapter(secExListAdapter);
		
		secGroupView.setSelectedGroup(arg1);
		
		secGroupView.setOnGroupExpandListener(new OnGroupExpandListener() {

			@Override
			public void onGroupExpand(int arg0) {
				// TODO Auto-generated method stub
				AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
						thrExlistItemHigh*app.mLineJsonData.StationInfo.get(0).DeviceItem.get(arg1).PartItem.size()+secExlistItemHigh+180);
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
	
class JsonDataThread extends Thread{
		
		
		public JsonDataThread() {
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			long g=System.currentTimeMillis();
			MLog.Logd(TAG, " InitData()>> "+g);
			try {
				
				if(app.getLineDataClassifyFromOneFile(LineType.AllRoute)==null){
					mHandler.sendEmptyMessage(INIT_JSON_DATA_FINISHED);
					return;
				};
				mStationList.clear();
				mDeviceArrayDisplayDataList = new ArrayList<ArrayList<Map<String, String>>>();
				for (int i = 0; i < app.mLineJsonData.StationInfo.size(); i++) {
					
					StationListInfoParams  stationInfo = new StationListInfoParams();
					stationInfo.setName(app.mLineJsonData.StationInfo.get(i).Name);
					if(!app.gIsDataChecked){
						stationInfo.setDeadLine("2015");
					}
					stationInfo.setProcess(app.mLineJsonData.getItemCounts(1, i, true,true)+ "/" + app.mLineJsonData.getItemCounts(1, i, false,true));
					mStationList.add(stationInfo);
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
			
			mHandler.sendEmptyMessage(INIT_JSON_DATA_FINISHED);
		}
	}

}
