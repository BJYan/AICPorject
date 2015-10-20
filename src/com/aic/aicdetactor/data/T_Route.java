package com.aic.aicdetactor.data;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.aic.aicdetactor.database.RouteDao;
import com.aic.aicdetactor.util.MLog;
import com.aic.aicdetactor.util.SystemUtil;

import android.util.Log;
public class T_Route extends Route {
	//巡检名称，在ListView 中显示的巡检路径名称
	 public  String Name="";
	 //巡检GUID,同时是JSON的文件名称
	 public  String Guid="";
	 //文件的存储路径
	 public String Path ="";
	 
	 public List<Object> mStationList = null;
	 public JSONArray mStationArrary = null;
	 public JSONArray mWorkerArrary = null;
	 public JSONArray mTurnArrary = null;
	 public JSONArray Item_Abnormal_GradeArrary = null;
	 public JSONArray Measure_TypeArrary = null;
	 private final String TAG = "luotest";
	 public JSONObject mGloableObject = null;
	 public JSONObject mLineObject = null;
	 public JSONObject mOrganizationObject = null;
	 public OrganizationInfo mOrganizationInfo = null;
	 public LineInfo mLineInfo = null;
	 public GlobalInfo mGlobalInfo = null;
	 public List<TurnInfo> mTurnList = null;
	 public List<WorkerInfo> mWorkerList = null;
     public JSONArray mT_PeriodArray = null;
     @Override
     public void setPath(String path){
    	 Path=path;
     }
     @Override
	public boolean isTempRoute() {
		// TODO Auto-generated method stub
		return false;
	}
     @Override
     public void ParseData(Object obj){
    	 JSONObject object=(JSONObject) obj;
    	 try {
    		 mGloableObject = object.getJSONObject(GlobalInfo.NodeName);
			mLineObject = object.getJSONObject(T_Line.RootNodeName);
			mStationArrary= (JSONArray) object.getJSONArray(KEY.KEY_STATIONINFO);
			mTurnArrary= (JSONArray) object.getJSONArray(T_Turn.RootNodeName);
			mWorkerArrary= (JSONArray) object.getJSONArray(T_Worker.RootNodeName);
			JSONObject sub_object = object.getJSONObject(T_Period.RootNodeName);
			mT_PeriodArray = sub_object.getJSONArray(T_Period.ArrayName);
			mOrganizationObject = object.getJSONObject(T_Organization.NodeName);
    	 } catch (Exception e) {
				e.printStackTrace();
			}
     }
//	@Override
//     public void parseBaseInfo() {
//		if (mWorkerArrary != null) {
//			try {
//
//				mWorkerList = MyJSONParse.parseWorkerNode(mWorkerArrary);
//				//Log.e("luotest", "parseBaseInfo() mWorkerList is" + mWorkerList.toString());
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		 if(mLineObject != null){
//		 try {
//			 mLineInfo = MyJSONParse.parseRouteNameNode(mLineObject);
//			 Name = mLineInfo.Name;
//			// mLineInfo.T_Content_Guid;
//			 Guid=  mLineInfo.T_Line_Guid;
//		 } catch (JSONException e) {
//		 // TODO Auto-generated catch block
//		 e.printStackTrace();
//		 }
//		 }
//		if (mTurnArrary != null) {
//			try {
//				mTurnList = MyJSONParse.parseTurnNode(mTurnArrary);
//				//Log.e("luotest", "parseBaseInfo() mTurnList is" + mTurnList.toString());
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		
//		if(mOrganizationObject != null){
//			mOrganizationInfo = new OrganizationInfo();
//			mOrganizationInfo.CorporationName = mOrganizationObject.optString(T_Organization.CorporationName);
//			mOrganizationInfo.GroupName= mOrganizationObject.optString(T_Organization.GroupName);
//			mOrganizationInfo.WorkShopName = mOrganizationObject.optString(T_Organization.WorkShopName);
//			
//			MLog.Logd(TAG,"mOrganizationInfo.CorporationName is "+mOrganizationInfo.CorporationName
//					+ " mOrganizationInfo.GroupName is "+mOrganizationInfo.GroupName
//					+" mOrganizationInfo.WorkShopName is "+mOrganizationInfo.WorkShopName);
//		}
//	}
	
	private final String JSON_GlobalInfo="GlobalInfo";
	private final String JSON_StationInfo="StationInfo";
	private final String JSON_T_Item_Abnormal_Grade="T_Item_Abnormal_Grade";
	private final String JSON_T_Line="T_Line";
	private final String JSON_T_Measure_Type="T_Measure_Type";
	private final String JSON_T_Organization="T_Organization";
	private final String JSON_T_Period="T_Period";
	private final String JSON_T_Turn="T_Turn";
	private final String JSON_T_Worker="T_Worker";
	
	@Override
	public void SaveData(String fileName) throws JSONException{
		
		//Auxiliary information node
		//JSONArray rootArray = new JSONArray();
		JSONObject rootArray = new JSONObject();
		rootArray.put(JSON_GlobalInfo, mGloableObject);
		rootArray.put(JSON_StationInfo, mStationArrary);
		rootArray.put(JSON_T_Item_Abnormal_Grade, Item_Abnormal_GradeArrary );
		rootArray.put(JSON_T_Line, mLineObject);
		rootArray.put(JSON_T_Measure_Type, Measure_TypeArrary);
		
		rootArray.put(JSON_T_Organization, mOrganizationObject );
		rootArray.put(JSON_T_Period, mT_PeriodArray);
		rootArray.put(JSON_T_Turn, mTurnArrary);
		rootArray.put(JSON_T_Worker, mWorkerArrary);
		
		try {
			SystemUtil.writeFileToSD(fileName, rootArray.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
