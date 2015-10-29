package com.aic.aicdetactor.fragment;

import java.io.IOException;

import com.aic.aicdetactor.app.myApplication;
import com.aic.aicdetactor.data.DeviceItemJson;
import com.aic.aicdetactor.data.KEY;
import com.aic.aicdetactor.data.PartItemJson;
import com.aic.aicdetactor.data.PartItemJsonUp;
import com.aic.aicdetactor.util.SystemUtil;
import com.google.gson.Gson;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class PartItemMeasureBaseFragment extends Fragment {

	protected int mPartItemIndex=0;
	protected PartItemJsonUp mPartItemData=null;
	private myApplication app = null;
	private String TAG="AIC.MeasureBaseFragment";
	protected DeviceItemJson mDeviceItemData=null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i(TAG,"onCreate()");
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mPartItemIndex =getArguments().getInt("partItemIndex");
		app =(myApplication) PartItemMeasureBaseFragment.this.getActivity().getApplication();
		mPartItemData = app.mLineJsonData.StationInfo.get(app.mStationIndex).DeviceItem.get(app.mDeviceIndex).PartItem.get(mPartItemIndex);
		mDeviceItemData = DeviceItemJson.clone(app.mLineJsonData.StationInfo.get(app.mStationIndex).DeviceItem.get(app.mDeviceIndex));
		mPartItemData = mDeviceItemData.PartItem.get(mPartItemIndex);
		int m=0;
		m++;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	//测量项名称
	protected String getPartItemName(){
		//return app.mLineJsonData.StationInfo.get(app.mStationIndex).DeviceItem.get(app.mDeviceIndex).PartItem.get(mPartItemIndex).Check_Content;
		return mPartItemData.Check_Content;
	}
	
	
	//数据有效范围，上限
	protected float getPartItemUp_Limit(){
		return mPartItemData.Up_Limit;
		//return app.mLineJsonData.StationInfo.get(app.mStationIndex).DeviceItem.get(app.mDeviceIndex).PartItem.get(mPartItemIndex).Up_Limit;
	}
	
	//数据有效范围，中限
	protected float getPartItemMiddle_Limit(){
		return mPartItemData.Middle_Limit;
		//return app.mLineJsonData.StationInfo.get(app.mStationIndex).DeviceItem.get(app.mDeviceIndex).PartItem.get(mPartItemIndex).Middle_Limit;
	}
	//数据的单位
	protected String getPartItemUnit(){
		return mPartItemData.Unit;
		//return app.mLineJsonData.StationInfo.get(app.mStationIndex).DeviceItem.get(app.mDeviceIndex).PartItem.get(mPartItemIndex).Unit;
	}
	//数据有效范围，下限
	protected float getPartItemDown_Limit(){
		return mPartItemData.Down_Limit;
	//	return app.mLineJsonData.StationInfo.get(app.mStationIndex).DeviceItem.get(app.mDeviceIndex).PartItem.get(mPartItemIndex).Down_Limit;
	}
	
	//设置测量的数据结果
	protected void setPartItemData(String value){
		Log.d("atest"," PartItemMeasureBaseFragment: value="+value);
		app.mLineJsonData.StationInfo.get(app.mStationIndex).DeviceItem.get(app.mDeviceIndex).PartItem.get(mPartItemIndex).PartItemData = value;
	String ab=	app.mLineJsonData.StationInfo.get(app.mStationIndex).DeviceItem.get(app.mDeviceIndex).PartItem.get(mPartItemIndex).PartItemData;
		Log.d("atest"," PartItemMeasureBaseFragment: ab="+ab);
	}
	
	//判断是否从上到下顺序测量完，
	public  boolean isCheckedFinish(){
		return(mPartItemIndex+1)== mDeviceItemData.PartItem.size()?true:false;
		//return (mPartItemIndex+1)==app.mLineJsonData.StationInfo.get(app.mStationIndex).DeviceItem.get(app.mDeviceIndex).PartItem.size()?true:false;
	}
	
	
	//保存当前数据到文件中
	public void saveDataToFile(){
	//	app.mLineJsonData
		Gson gson = new Gson();
		String sonStr = gson.toJson(app.mLineJsonData);
		try {
			//文件要保存到数据库中的
			SystemUtil.writeFile("/sdcard/abc0.txt", sonStr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public abstract   void saveCheckValue();

}
