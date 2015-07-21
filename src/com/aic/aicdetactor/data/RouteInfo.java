package com.aic.aicdetactor.data;

import java.io.File;
import java.util.List;

public class RouteInfo {
	public String mRoutName;// GUID
	public int mIsChecked;
	public int mIsBeiginChecked;
	public int mStationIndex;
	public int mDeviceIndex;
	public int mPartItemIndex;
	public int mIsReverseCheck;
	public Object mStationInfo_Root_Object = null;
	public Object mWorkerInfo_Root_Object = null;
	public Object mTurnInfo_Root_Object = null;
	public Object mPlanName_Root_Object = null;
	public String mFileName= null;//巡检文件的全路径
	
	public File mFile = null;


	public List<Object> mStationList = null;
	public List<Object> mWorkerList = null;
	public List<Object> mTurnList = null;
	public RouteInfo() {
		mRoutName = null;// GUID
		mIsChecked = 0;
		mIsBeiginChecked = 0;
		mStationIndex = 0;
		mDeviceIndex = 0;
		mPartItemIndex = 0;
		mIsReverseCheck = 0;
		mFileName = null;
	}
	public void setRouteName(String rootName){
		mRoutName = rootName;
	}
	public String getXJName(){
		return mRoutName;
	}
	public void setFileName(String fileName){
		mFileName = fileName;
	}
	public String getFileName(){
		return mFileName;
	}
	
	
	
}
