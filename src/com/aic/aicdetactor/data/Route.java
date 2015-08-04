package com.aic.aicdetactor.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.aic.aicdetactor.app.myApplication;
import com.aic.aicdetactor.util.SystemUtil;

public class Route {
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
	public Object mOrganization_Root_Object = null;	
	public Object mAuxiliary_Root_Object = null;
	public String mFileName= null;//巡检文件的全路径
	
	public class info{
		public String PlanGuid = null;
		public String PlanName = null;
		public info(){}
	}
	
	
	public File mFile = null;
	public List<WorkerInfo> WorkerInfoList = null;
	public List<TurnInfo> TurnInfoList = null;
	public info minfo = null;
	public List<Object> mStationList = null;
	public List<Object> mWorkerList = null;
	public List<Object> mTurnList = null;
	public Route() {
		mRoutName = null;// GUID
		mIsChecked = 0;
		mIsBeiginChecked = 0;
		mStationIndex = 0;
		mDeviceIndex = 0;
		mPartItemIndex = 0;
		mIsReverseCheck = 0;
		mFileName = null;
	}


	public void parseBaseInfo(){	
		Log.e("luotest", "parseBaseInfo() 1");
		if(mWorkerInfo_Root_Object != null){
			try {
				
				WorkerInfoList = MyJSONParse.parseWorkerNode(mWorkerInfo_Root_Object);
				Log.e("luotest", "parseBaseInfo()"+WorkerInfoList.toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(mPlanName_Root_Object != null){
			try {
				minfo = MyJSONParse.parseRouteNameNode(mPlanName_Root_Object);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(mTurnInfo_Root_Object != null){
			try {
				TurnInfoList = MyJSONParse.parseTurnNode(mTurnInfo_Root_Object);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
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
	public void SaveData(String fileName) throws JSONException{
		
		//Auxiliary information node
		JSONArray rootArray = new JSONArray();
		rootArray.put(0, mTurnInfo_Root_Object);
		rootArray.put(1, mWorkerInfo_Root_Object);
		rootArray.put(2, mOrganization_Root_Object);
		rootArray.put(3, mStationInfo_Root_Object);
		rootArray.put(4, mPlanName_Root_Object);
		
		if(mAuxiliary_Root_Object != null){
			rootArray.put(5, mAuxiliary_Root_Object);
		}
		try {
			SystemUtil.writeFileToSD(fileName, rootArray.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
