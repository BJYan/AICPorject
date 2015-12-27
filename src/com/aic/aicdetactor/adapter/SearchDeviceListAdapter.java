package com.aic.aicdetactor.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aic.aicdetactor.CommonActivity;
import com.aic.aicdetactor.R;
import com.aic.aicdetactor.acharEngine.ChartBuilder;
import com.aic.aicdetactor.activity.PartItemListSelectActivity;
import com.aic.aicdetactor.bluetooth.analysis.DataAnalysis;
import com.aic.aicdetactor.comm.LineType;
import com.aic.aicdetactor.data.PartItemJsonUp;
import com.aic.aicdetactor.dialog.CommonDialog;
import com.aic.aicdetactor.paramsdata.DeviceListInfoParams;
import com.aic.aicdetactor.util.MLog;
import com.aic.aicdetactor.view.GroupViewHolder;

public class SearchDeviceListAdapter extends DeviceListAdapter {

	public SearchDeviceListAdapter(Context context, CommonActivity av,
			int stationIndex, int deviceIndex, LineType lineType) {
		super(context, av, stationIndex, deviceIndex, lineType);
		// TODO Auto-generated constructor stub
		TAG="SearchDeviceListAdapter";
	}
	
	void InitData() {
		long g=System.currentTimeMillis();
		MLog.Logd(TAG, "InitData>> ");
		try {
			mDeviceList.clear();
			if(mChildrenList==null){
			mChildrenList = new ArrayList<ArrayList<PartItemJsonUp>>();
			}else{
				mChildrenList.clear();
			}
			for (int i = 0; i < app.mLineJsonData.StationInfo.get(mStationIndex).DeviceItem.size(); i++) {
				DeviceListInfoParams deviceInfo = new DeviceListInfoParams();							
				deviceInfo.setName(app.mLineJsonData.StationInfo.get(mStationIndex).DeviceItem.get(i).Name);
				MLog.Logd(TAG,"name is"+app.mLineJsonData.StationInfo.get(mStationIndex).DeviceItem.get(i).Name);
				deviceInfo.setDeadLine( app.mLineJsonData.StationInfo.get(mStationIndex).DeviceItem.get(i).End_Check_Datetime);
			
				deviceInfo.setProcess(app.mLineJsonData.getItemCounts(2, i, true,true)+ "/" + app.mLineJsonData.getItemCounts(2, i, false,true));
				mDeviceList.add(deviceInfo);
				InitChidrenData(mStationIndex, i);
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
		MLog.Logd(TAG, "InitData<< "+String.valueOf(System.currentTimeMillis()-g));
	}


	

	void InitChidrenData(int stationIndex, int itemIndexs) {
		long gg=System.currentTimeMillis();
		MLog.Logd(TAG, "InitChidrenData>> stationIndex="+stationIndex);
		try {
			ArrayList<PartItemJsonUp> childList = new ArrayList<PartItemJsonUp>();
			PartItemJsonUp item =null;
			for (int i = 0; i < app.mLineJsonData.StationInfo.get(mStationIndex).DeviceItem.get(itemIndexs).PartItem.size(); i++) {
				
				item =  app.mLineJsonData.StationInfo.get(mStationIndex).DeviceItem.get(itemIndexs).PartItem.get(i);
				childList.add(item);
			}
			mChildrenList.add(childList);
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
		MLog.Logd(TAG, "InitChidrenData<< stationIndex ="+stationIndex+","+String.valueOf(System.currentTimeMillis()-gg));
	}
	
	@Override
	public View getChildView(final int arg0, final int arg1, boolean arg2, View arg3, ViewGroup arg4) {
		// TODO Auto-generated method stub
				GroupViewHolder holder = null;

			if (arg3 == null) {
				arg3 = mInflater.inflate(R.layout.searchpartitem, null);
				holder = new GroupViewHolder();
				holder.NameText = (TextView) arg3
						.findViewById(R.id.pathname);
				holder.DeadLineText = (TextView) arg3
						.findViewById(R.id.checkdate);
				holder.StausText = (TextView) arg3
						.findViewById(R.id.checkvalue);
				arg3.setTag(holder);
			} else {
				holder = (GroupViewHolder) arg3.getTag();
				
			}
			holder.NameText.setText(mChildrenList.get(mDeviceIndex).get(arg1).Check_Content);
			holder.DeadLineText.setText(mChildrenList.get(mDeviceIndex).get(arg1).End_Check_Datetime);
			holder.StausText.setText(mChildrenList.get(mDeviceIndex).get(arg1).Extra_Information+" "+mChildrenList.get(mDeviceIndex).get(arg1).Unit);
			return arg3;
	}
}
