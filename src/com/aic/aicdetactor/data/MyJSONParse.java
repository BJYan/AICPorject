package com.aic.aicdetactor.data;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.aic.aicdetactor.comm.CommonDef;
import com.aic.aicdetactor.database.RouteDao;
import com.aic.aicdetactor.util.SystemUtil;

public class MyJSONParse {
	String TAG = "luotest";
	
	public static int ITEMDEF_INDEX = 5;
	private RouteDao mRouteDao = null;
	private Context mContext = null;
	private SharedPreferences mSharedPreferences = null;
	private int mRouteIndex =0;//当前或最近的路线索引
	private int mStationIndex =0;//当前或最近的站点索引
	private int mDeviceIndex=0;//当前或最近的设备索引
	private int mPartItemIndex =0;//当前或最近的巡检项索引
	private int mIsReverseChecking = 0;//是否反向巡检
	private String mCurrentFileName = null;//当前巡检的文件名即guid
	private String mSavePath = null;//检查路线数据存储的路径
	private List<Route> mRouteList = null;

	// partitemData split key word
	public static String PARTITEMDATA_SPLIT_KEYWORD = "\\*";



	public MyJSONParse() {

	}

	/**
	 * 主要存储一些索引信息，其他数据需要进入数据库里进行查询及修改
	 * @param cv
	 */
	public void SaveCheckStatus(ContentValues cv ){			
		
		// 实例化SharedPreferences对象（第一步）
		if(mSharedPreferences ==null){
			mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
		}
		// 实例化SharedPreferences.Editor对象（第二步）
		SharedPreferences.Editor editor = mSharedPreferences.edit();
		// 用putString的方法保存数据
		editor.putLong(CommonDef.route_info.LISTVIEW_ITEM_INDEX, cv.getAsLong(CommonDef.route_info.LISTVIEW_ITEM_INDEX));
		editor.putLong(CommonDef.station_info.LISTVIEW_ITEM_INDEX, cv.getAsLong(CommonDef.station_info.LISTVIEW_ITEM_INDEX));
		editor.putLong(CommonDef.device_info.LISTVIEW_ITEM_INDEX, cv.getAsLong(CommonDef.device_info.LISTVIEW_ITEM_INDEX));
		editor.putLong(CommonDef.check_item_info.LISTVIEW_ITEM_INDEX, cv.getAsLong(CommonDef.check_item_info.LISTVIEW_ITEM_INDEX));
		editor.putLong(CommonDef.check_item_info.IS_REVERSE_CHECKING, cv.getAsLong(CommonDef.check_item_info.IS_REVERSE_CHECKING));
		editor.putString(CommonDef.GUID, cv.getAsString(CommonDef.GUID));
		editor.putString(CommonDef.PATH_DIRECTOR, cv.getAsString(CommonDef.PATH_DIRECTOR));
		// 提交当前数据
		editor.commit();
	}
	
	/**本想法是通过SharedPreferences来存储正在巡检的路线的相关索引等信息，例如刚在巡检到哪一具体项了。
	 * 
	 * 
	 * @param context
	 */
	public int InitData(Context context,List<String>list){
		mContext = context;
		if(mRouteDao== null){
		mRouteDao = new RouteDao(mContext);}
		
		mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
		// 实例化SharedPreferences.Editor对象（第二步）
		
		//获取当前巡检的索引信息
		mRouteIndex = (int) mSharedPreferences.getLong(CommonDef.route_info.LISTVIEW_ITEM_INDEX,0);
		mStationIndex = (int) mSharedPreferences.getLong(CommonDef.station_info.LISTVIEW_ITEM_INDEX,0);
		mDeviceIndex = (int) mSharedPreferences.getLong(CommonDef.device_info.LISTVIEW_ITEM_INDEX,0);
		mPartItemIndex = (int) mSharedPreferences.getLong(CommonDef.check_item_info.LISTVIEW_ITEM_INDEX,0);
		mIsReverseChecking = (int) mSharedPreferences.getLong(CommonDef.check_item_info.IS_REVERSE_CHECKING,0);
		mCurrentFileName = mSharedPreferences.getString(CommonDef.GUID,null);
		mSavePath = mSharedPreferences.getString(CommonDef.PATH_DIRECTOR,null);		
		
		//再查数据库中是否有完成的巡检路线，加载到mRouteList中
		mRouteList = mRouteDao.getRouteInfoByFilePath(list);
		
		for(int index =0;index <mRouteList.size();index++){
			String path = mRouteList.get(index).getFileName();
			
			//从此开始关联各个RouteInfo相关项
			parseData(index,path);
		}
		
		return getRouteCount();
	}
	
	/*
	 * 插入新的巡检计划，返回数值是未完成的巡检计划数量,并刷新mRouteList
	 */
	public int insertNewRouteInfo(String fileName,String path,Context context){
		if(fileName == null || path == null){
			return 0;
		}
		if(mContext == null){
			mContext = context;
		}
		if(mRouteDao== null){
			mRouteDao = new RouteDao(mContext);
			}
		//if(mRouteDao.getCount()<12){		
		mRouteDao.insertNewRouteInfo(fileName, path);
		//}
		return 1;
	}
	public static Route getPlanInfo(String Routepath){
		Route route = new Route();
		String data = SystemUtil.openFile(Routepath);	
		
		if (data != null) {
			
			try {
				JSONTokener jsonTokener = new JSONTokener(data);
				JSONArray jsonArray = (JSONArray) jsonTokener.nextValue();
				for (int i = 0; i < jsonArray.length(); i++) {
				//	Log.d(TAG, "data is not null" + "i = " + i);
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					String index = jsonObject.getString("Index");
					Log.e("luotest", "name = " + index + ","+jsonObject.toString());
					if (index.equals(CommonDef.route_info.JSON_INDEX)) {
						route.mPlanName_Root_Object = jsonObject;
					} else if (index.equals(CommonDef.turn_info.JSON_INDEX)) {
						route.mTurnInfo_Root_Object = jsonObject;
					} else if (index.equals(CommonDef.worker_info.JSON_INDEX)) {
						route.mWorkerInfo_Root_Object = jsonObject;
					} else if (index.equals(CommonDef.station_info.JSON_INDEX)) {
						route.mStationInfo_Root_Object = jsonObject;
					} else if(index.equals(CommonDef.organization_info.JSON_INDEX)){						
						route.mOrganization_Root_Object = jsonObject;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}		
		return route;
	}
	/**
	 * 返回未完成的巡检路线的个数，便于UI界面显示
	 * @return
	 */
	public int getRouteCount(){
		int count =0;
		if(mRouteList!=null){
			count = mRouteList.size();
		}
		return count;
	}
	/**
	 * 一旦巡检完一个设备节点就需要保存数据。
	 * @param RouteIndex
	 */
	public void SaveData(int RouteIndex){
		
		Route info =mRouteList.get(RouteIndex);
		
		try {
			info.SaveData();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * 从文件中解析数据，开始解析。
	 * @param routeIndex 在数据库中的索引
	 * @param path 对应的文件名
	 */
	private void parseData(int routeIndex,String path) {
		String data = SystemUtil.openFile(path);	
	
		if (data != null) {
			Log.d(TAG, "data is not null");
			try {
				JSONTokener jsonTokener = new JSONTokener(data);
				JSONArray jsonArray = (JSONArray) jsonTokener.nextValue();
				for (int i = 0; i < jsonArray.length(); i++) {
					Log.d(TAG, "data is not null" + "i = " + i);
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					String index = jsonObject.getString("Index");
					Log.d(TAG, "name = " + index);
					if (index.equals(CommonDef.route_info.JSON_INDEX)) {
						mRouteList.get(routeIndex).mPlanName_Root_Object = jsonObject;
					} else if (index.equals(CommonDef.turn_info.JSON_INDEX)) {
						mRouteList.get(routeIndex).mTurnInfo_Root_Object = jsonObject;
					} else if (index.equals(CommonDef.worker_info.JSON_INDEX)) {
						mRouteList.get(routeIndex).mWorkerInfo_Root_Object = jsonObject;						
					} else if (index.equals(CommonDef.station_info.JSON_INDEX)) {
						mRouteList.get(routeIndex).mStationInfo_Root_Object = jsonObject;
					} else if(index.equals(CommonDef.organization_info.JSON_INDEX)){						
						mRouteList.get(routeIndex).mOrganization_Root_Object = jsonObject;
					}
					//mRouteList.get(routeIndex).parseBaseInfo();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}
	
	public void setAuxiliaryNode(int RouteIndex,Object object){

		mRouteList.get(RouteIndex).mAuxiliary_Root_Object = object;
	}


	public String getDeviceQueryNumber(Object object){
		if(object == null )return null;
		String strNumber = null;
		JSONObject json = (JSONObject)object;
		try {
			strNumber = json.getString(KEY.KEY_QUERY_NUMBER);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return strNumber;
	}
	
	/**
	 * 巡检时，记录巡检选择的ITEMDEF，以备向服务器回传数据
	 * value 要求前后不带*
	 */
	//从服务上获取的初始PartItemData数值的*分割的项个数，默认为十个，如果少的话，需要用*给补充上。
	int partItemDefaultLenth = 10;
	int partItemDefault_Max_Lenth = 12;
	public JSONObject setPartItem_ItemDef(JSONObject partItemDataJson,int deviceIndex,String Value){
		if(partItemDataJson == null) return null;
		JSONObject json =partItemDataJson;
		try {		
			String Oldvalue = this.getPartItemName(json);
			Oldvalue =json.getString(KEY.KEY_PARTITEMDATA);
			String[] array1 = Oldvalue.split(PARTITEMDATA_SPLIT_KEYWORD);
			
			String[] array2 = Value.split(PARTITEMDATA_SPLIT_KEYWORD);
			Log.d(TAG, "setPartItem_ItemDef() old length ="+array1.length + ",new Length ="+array2.length);
			if((array1.length+array2.length) <partItemDefault_Max_Lenth){
				if(array1.length<partItemDefaultLenth){
					for(int i = array1.length;i<partItemDefaultLenth;i++){
						Oldvalue=Oldvalue+"*";
					}
				}
			}
			Oldvalue= Oldvalue + Value;
			json.put(KEY.KEY_PARTITEMDATA, Oldvalue);
		
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}
	
	
	/**
	 * 获取巡检路线、站点、及设备节点的巡检情况，包括总数、已巡检、及巡检时间等信息
	 * @param Object
	 * @param nodeType：0 路线、1站点、2设备
	 * @param RouteIndex
	 * @return
	 * @throws JSONException
	 * 0路线：
	 * 统计各巡检路线的巡检子项
	 * @param routeIndex，是各个巡检路线的序号，在路线界面调用
	 */
	public CheckStatus getNodeCount(Object Object,int nodeType,int RouteIndex) throws JSONException{
		CheckStatus status = new CheckStatus();
		int count = 0;
		String checkTimeStr=null;
		
		//root count
		if(nodeType == 0){
			
			List<Object> list = getStationList(RouteIndex);			
			
			List<Object> itemList  = null;
			for (int i = 0; i < list.size(); i++) {				
				itemList = this.getDeviceList((JSONObject) list.get(i));
				
				for (int n = 0; n < itemList.size(); n++) {
					List<Object> partlist = this.getPartList(itemList.get(n));
					count = count + partlist.size();
					for(int k=0;k<partlist.size();k++){
						JSONObject itemObject = (JSONObject) partlist.get(k);				
						checkTimeStr = this.getPartItemCheckUnitName(itemObject, CommonDef.partItemData_Index.PARTITEM_CHECKED_TIME);
						if(checkTimeStr != null){
							status.mCheckedCount++;
							status.mLastTime = checkTimeStr;
						}
						
					}
				}
				status.mSum = count;
			}

		}
		
		//sation count
		if(nodeType == 1){
			List<Object> devicelist = this.getDeviceList((JSONObject) Object);
			List<Object> partlist =null;
			for (int n = 0; n < devicelist.size(); n++) {
				partlist = this.getPartList(devicelist.get(n));
				count = count + partlist.size();
				for(int k=0;k<partlist.size();k++){
					JSONObject itemObject = (JSONObject) partlist.get(k);				
					checkTimeStr = this.getPartItemCheckUnitName(itemObject, CommonDef.partItemData_Index.PARTITEM_CHECKED_TIME);
					if(checkTimeStr != null){
						status.mCheckedCount++;
						status.mLastTime = checkTimeStr;
					}			
				}
			}
			status.mSum = count;
		}
		
		//device count
		if(nodeType ==2){		
			
			List<Object> partlist = this.getPartList((JSONObject) Object);
			count = partlist.size();
			JSONObject itemObject = null;	
			
			for (int k = 0; k < partlist.size(); k++) {
				Log.d(TAG, " getDevicePartItemCount k=" + k + "," + partlist.get(k));
				itemObject = (JSONObject) partlist.get(k);			
				checkTimeStr = this.getPartItemCheckUnitName(itemObject, CommonDef.partItemData_Index.PARTITEM_CHECKED_TIME);
				if(checkTimeStr != null){
					status.mCheckedCount++;
					status.mLastTime = checkTimeStr;
				}
			}
			status.mSum = count;
		}
		if(status.mSum == status.mCheckedCount){
			status.hasChecked=true;
		}else{
			status.hasChecked=false;
		}
		return status;
	}
	
	
//	public CheckStatus getDevicePartItemCount(Object deviceItemObject)
//			throws JSONException {
//
//		int count = 0;
//		CheckStatus status = new CheckStatus();
//		JSONObject object = (JSONObject) deviceItemObject;
//		List<Object> partlist = this.getPartList(object);
//		count = partlist.size();
//		JSONObject itemObject = null;
//		String checkTimeStr = null;
//		
//		for (int k = 0; k < partlist.size(); k++) {
//			Log.d(TAG, " getDevicePartItemCount k=" + k + "," + partlist.get(k));
//			itemObject = (JSONObject) partlist.get(k);			
//			checkTimeStr = this.getPartItemCheckUnitName(itemObject, CommonDef.partItemData_Index.PARTITEM_CHECKED_TIME);
//			if(checkTimeStr != null){
//				status.mCheckedCount++;
//				status.mLastTime = checkTimeStr;
//			}
//		}
//		status.mSum = count;
//		return status;
//	}
//
//	public CheckStatus getStationPartItemCount(Object staionItemObject)
//			throws JSONException {
//		int count = 0;
//		CheckStatus status = new CheckStatus();
//		String checkTimeStr=null;
//		JSONObject object = (JSONObject) staionItemObject;
//		List<Object> devicelist = this.getDeviceList(object);
//		List<Object> partlist =null;
//		for (int n = 0; n < devicelist.size(); n++) {
//			partlist = this.getPartList(devicelist.get(n));
//			count = count + partlist.size();
//			for(int k=0;k<partlist.size();k++){
//				JSONObject itemObject = (JSONObject) partlist.get(k);				
//				checkTimeStr = this.getPartItemCheckUnitName(itemObject, CommonDef.partItemData_Index.PARTITEM_CHECKED_TIME);
//				if(checkTimeStr != null){
//					status.mCheckedCount++;
//					status.mLastTime = checkTimeStr;
//				}			
//			}
//		}
//		status.mSum = count;
//		return status;
//	}
//
//	/**
//	 * 统计各巡检路线的巡检子项
//	 * @param routeIndex，是各个巡检路线的序号，在路线界面调用。
//	 * @return
//	 * @throws JSONException
//	 */
//	public CheckStatus getRoutePartItemCount(int routeIndex) throws JSONException {
//		
//		CheckStatus status = new CheckStatus();
//		List<Object> list = getStationList(routeIndex);
//		int count = 0;		
//		String checkTimeStr=null;
//		List<Object> devicelist  = null;
//		for (int i = 0; i < list.size(); i++) {
//			JSONObject object = (JSONObject) list.get(i);
//			devicelist = this.getDeviceList(object);
//			
//			for (int n = 0; n < devicelist.size(); n++) {
//				List<Object> partlist = this.getPartList(devicelist.get(n));
//				count = count + partlist.size();
//				for(int k=0;k<partlist.size();k++){
//					JSONObject itemObject = (JSONObject) partlist.get(k);				
//					checkTimeStr = this.getPartItemCheckUnitName(itemObject, CommonDef.partItemData_Index.PARTITEM_CHECKED_TIME);
//					if(checkTimeStr != null){
//						status.mCheckedCount++;
//						status.mLastTime = checkTimeStr;
//					}
//					
//				}
//			}
//			status.mSum = count;
//		}
//
//		return status;
//	}

	/**
	 * 获取各个巡检路线的路线名称，在路线界面调用。
	 * @param routeIndex
	 * @return
	 * @throws JSONException
	 */
	public String getRoutName(int routeIndex) throws JSONException {
		String name = null;
		if (mRouteList.get(routeIndex).mPlanName_Root_Object == null) {
			Log.e(TAG, "getRoutName mPlanName_Root_Object is null");
			return null;
		}
		JSONObject object = (JSONObject) mRouteList.get(routeIndex).mPlanName_Root_Object;
		name = object.getString(KEY.KEY_PLANNAME);
		return name;
	}

	/**
	 * 从路线序号来查询站点信息，
	 * @param routeIndex，是路线序号
	 * @return
	 * @throws JSONException
	 */
	public List<Object> getStationList(int routeIndex) throws JSONException {
		mRouteIndex = routeIndex;
		
		if (mRouteList.get(mRouteIndex).mStationInfo_Root_Object == null)
			return null;
		
		if (mRouteList.get(mRouteIndex).mStationList != null)
			return mRouteList.get(mRouteIndex).mStationList;

		mRouteList.get(mRouteIndex).mStationList = new ArrayList<Object>();
		try {
			JSONObject object = (JSONObject) mRouteList.get(mRouteIndex).mStationInfo_Root_Object;

			JSONArray array = object.getJSONArray(KEY.KEY_STATIONINFO);

			for (int i = 0; i < array.length(); i++) {
				JSONObject subObject = array.getJSONObject(i);
				Log.d(TAG, "I =" + i + subObject.getString(KEY.KEY_STATIONNAME));
				mRouteList.get(mRouteIndex).mStationList.add(subObject);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// mStationList = list;
		return mRouteList.get(mRouteIndex).mStationList;
	}

	public String getStationItemName(Object stationItemobject)
			throws JSONException {
		String name = null;// NAME = NULL;
		try {
			JSONObject newobject = (JSONObject) stationItemobject;

			name = newobject.getString(KEY.KEY_STATIONNAME);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return name;
	}

	// input params must be StationItem
	public List<Object> getDeviceList(Object StationItemobject)
			throws JSONException {
		if (StationItemobject == null) {
			Log.d(TAG, " object is null");
			return null;
		}
		List<Object> list = new ArrayList<Object>();
		try {
			JSONObject object2 = (JSONObject) StationItemobject;

			JSONArray array = object2.getJSONArray(KEY.KEY_STATIONITEM);
			Log.d(TAG, " object is not null array.size is " + array.length());
			for (int i = 0; i < array.length(); i++) {
				Log.d(TAG, " getDeviceItem object i" + i);
				JSONObject subObject = array.getJSONObject(i);
				if (subObject.getString("Index").equals("6")) {
					// list.add(subObject);
					Log.d(TAG, " getDeviceItem object 6  i" + i);

					JSONArray sub_Array = subObject.getJSONArray(KEY.KEY_DEVICEITEM);
					for (int k = 0; k < sub_Array.length(); k++) {
						list.add(sub_Array.getJSONObject(k));
						Log.d(TAG, " getDeviceItem object 6  k " + k);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;

	}

	/**
	 *  input params must be StationItemIndex
	 * @param stationIndex
	 * @return
	 * @throws JSONException
	 * 返回每个partItem的list
	 */
	public List<Object> getDeviceItem(int stationIndex) throws JSONException {

		mStationIndex = stationIndex;
		List<Object> list = new ArrayList<Object>();
		try {
			//每个stationItem
			JSONObject stationItem = (JSONObject) getStationItem(mRouteIndex,mStationIndex);			
			Log.d(TAG, " getDeviceItem  stationItem is " + stationItem.toString());
			JSONArray array = stationItem.getJSONArray(KEY.KEY_STATIONITEM);
			Log.d(TAG, " getDeviceItem  stationItem array " + array.toString());
			for (int i = 0; i < array.length(); i++) {				
				JSONObject subObject = array.getJSONObject(i);
				if (subObject.getString("Index").equals("6")) {
					// list.add(subObject);
					JSONArray sub_Array = subObject.getJSONArray(KEY.KEY_DEVICEITEM);
					Log.d(TAG, " getDeviceItem sub_Array " + ","+sub_Array.toString());
					for (int k = 0; k < sub_Array.length(); k++) {
						list.add(sub_Array.getJSONObject(k));
						Log.d(TAG, " getDeviceItem sub_Array " + k + ","+sub_Array.getJSONObject(k).toString());
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;

	}

	// input params must be StationItem
	public List<Object> getIDInfo(Object StationItemobject)
			throws JSONException {
		if (StationItemobject == null) {
			Log.d(TAG, "getIDInfo object is null");
			return null;
		}
		List<Object> list = new ArrayList<Object>();
		try {
			JSONObject object2 = (JSONObject) StationItemobject;
			JSONArray array = object2.getJSONArray(KEY.KEY_STATIONITEM);
			Log.d(TAG, "getIDInfo object ");
			for (int i = 0; i < array.length(); i++) {
				Log.d(TAG, "getIDInfo object i =" + i);
				JSONObject subObject = array.getJSONObject(i);
				if (subObject.getString("Index").equals("5")) {
					// list.add(subObject);
					Log.d(TAG, "getIDInfo object index is 5");
					JSONArray sub_Array = subObject.getJSONArray(KEY.KEY_IDINFO);
					for (int k = 0; k < sub_Array.length(); k++) {
						list.add(sub_Array.getJSONObject(k));
						Log.d(TAG, "getIDInfo object k =" + k);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;

	}

	
	public List<String>getDeviceItemDefList(Object deviceItemObject) throws JSONException{
		if(deviceItemObject == null){
			Log.e(TAG, "getDeviceItemDefList deviceItemObject is null");
			return null;
		}
		List<String> list = new ArrayList<String>();
		
		//
		JSONObject object = (JSONObject) deviceItemObject;
		String str = (String) object.get(KEY.KEY_ITEMDEF);
		
		if(str == null){
			Log.e(TAG, "getDeviceItemDefList str is null");
			return null;
		}
		String[] splitStr = str.split("\\/");
		Log.d(TAG, " getDeviceItemDefList splitStr size is  =" +splitStr.length+","+str);
		for(int i =0 ;i<splitStr.length;i++){			
			list.add(splitStr[i].toString());
			Log.d(TAG, " getDeviceItemDefList test i =" + i +","+splitStr[i]);
		}
		
		return list;
	}
	// input params must be DeviceItem sub
	public String getDeviceItemName(Object DeviceItemobject) {
		if (DeviceItemobject == null)
			return null;
		String name = null;
		try {
			JSONObject newObject = (JSONObject) DeviceItemobject;
			name = newObject.getString("DeviceName");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return name;
	}

	public int getStationItemIndexByID(int routeIndex,String strIdCode) throws JSONException {
		if (strIdCode == null) {
			Log.d(TAG, "getStationItemIndexByID strIdCode is null");
			return -1;
		}
		int index = 0;
		JSONObject object = null;

		List<Object> list = null;
		for (int i = 0; i < mRouteList.get(mRouteIndex).mStationList.size(); i++) {
			object = (JSONObject) mRouteList.get(mRouteIndex).mStationList.get(i);
			list = getIDInfo(object);
			Log.d(TAG, "getStationItemIndexByID i = " + i + ",object is "
					+ object.toString());
			if (list != null) {
				for (int j = 0; j < list.size(); j++) {
					object = (JSONObject) list.get(j);
					Log.d(TAG, "getStationItemIndexByID j is " + j
							+ ",object is " + object.toString());
					String idnumber = (String) object.get(KEY.KEY_IDNUMBER);
					if (strIdCode.equals(idnumber)) {
						index = i;
						break;
					}
				}
			} else {
				Log.d(TAG, "getStationItemIndexByID list is null");
			}
		}
		return index;
	}

	/*
	 * input:partItemDataStr is partItemData intent to get Specific item by
	 * split *
	 */
	public String getPartItemSubStr(String partItemDataStr, int index) {
		if (partItemDataStr == null) {
			Log.d(TAG, "getPartItemSubStr partItemDataStr is null");
			return null;
		}

		String[] array = partItemDataStr.split(PARTITEMDATA_SPLIT_KEYWORD);
		if (index < 0 || index >= array.length) {
			Log.d(TAG, "getPartItemSubStr index out of array size");
			return null;
		}

		return array[index];

	}

	// input params must be partItem sub
		public String getPartItemCheckUnitName(Object partItemobject,int index) {
			//Log.d(TAG, "getPartItemName 0");
			if (partItemobject == null) {
				Log.d(TAG, "getPartItemCheckUnitName " + " object is null");
				return null;
			}
			String name = null;
			//Log.d(TAG, "getPartItemName 1");
			try {
				JSONObject newObject = (JSONObject) partItemobject;

				//Log.d(TAG, "getPartItemName 3");
				name = newObject.getString(KEY.KEY_PARTITEMDATA);
				name = getPartItemSubStr(name,index);
				//Log.d(TAG, "getPartItemName 4");

			} catch (Exception e) {
				Log.e(TAG,e.toString());;
			}
			Log.d(TAG, "getPartItemCheckUnitName name is "+name);
			return name;
		}	
	
	/*
	 * input: index is itemDef index from left to right ,start from 0,that is
	 * combox widget index input:object is deviceItem intent is to get itemDef
	 */	
	public List<Object> getPartItemListByItemDef(Object partItemobject, int index)
			throws JSONException {
		List<Object> list = new ArrayList<Object>();

		if (partItemobject == null) {
			Log.e(TAG, " getPartItemListByItemDef input param object is null");
			return null;
		}
		Log.e(TAG, " getPartItemListByItemDef ,"+partItemobject.toString());
		JSONArray array = ((JSONObject) partItemobject).getJSONArray(KEY.KEY_PARTITEM);
		Log.e(TAG, " getPartItemListByItemDef array size is "+array.length()); 
		for (int i = 0; i < array.length(); i++) {
			JSONObject subObject = (JSONObject) array.get(i);
			String substr = getPartItemName(subObject);
			String str2 = getPartItemSubStr(substr, ITEMDEF_INDEX);
			
			int value = Integer.parseInt(str2);
			int btrue = ((value >> index) & 1);
			Log.d(TAG, " getPartItemListByItemDef str2 is "+str2 + "value =" +value + ",btrue ="+btrue);
			if (btrue != 0) {
				Log.d(TAG, " getPartItemListByItemDef array.get(i) is "+array.get(i));				
				list.add(array.get(i));
			}
		}
		return list;
	}
	
	// input params must be DeviceItem sub
	//入参：JSON 节点的DeviceItem 下一目录节点
	public List<Object> getPartList(Object DeviceItemobject) {
		if (DeviceItemobject == null)
			return null;
		List<Object> list = new ArrayList<Object>();
		try {
			JSONObject newObject = (JSONObject) DeviceItemobject;
			JSONArray array = newObject.getJSONArray(KEY.KEY_PARTITEM);
			for (int i = 0; i < array.length(); i++) {
				list.add(array.getJSONObject(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public Temperature getPartItemTemperatrue(Object object){
		if (object == null) {
			Log.d(TAG, "getPartItemTemperatrue " + " object is null");
			return null;
		}
		Temperature info = new Temperature();
		String name = getPartItemName(object);		
		if(name != null){
			info.max = SystemUtil.getTemperature(this.getPartItemSubStr(name, CommonDef.partItemData_Index.PARTITEM_MAX_VALUE_NAME));
			info.mid = SystemUtil.getTemperature(this.getPartItemSubStr(name, CommonDef.partItemData_Index.PARTITEM_MIDDLE_VALUE_NAME));
			info.min = SystemUtil.getTemperature(this.getPartItemSubStr(name, CommonDef.partItemData_Index.PARTITEM_MIN_VALUE_NAME));
		}		
		return info;
	}
	// input params must be partItem sub
	public String getPartItemName(Object partItemobject) {
		//Log.d(TAG, "getPartItemName 0");
		if (partItemobject == null) {
			Log.d(TAG, "getPartItemName " + " object is null");
			return null;
		}
		String name = null;	
		try {
			JSONObject newObject = (JSONObject) partItemobject;			
			name = newObject.getString(KEY.KEY_PARTITEMDATA);	
		} catch (Exception e) {
			Log.e(TAG,e.toString());;
		}
		Log.d(TAG, "getPartItemName name is "+name);
		return name;
	}

	public List<Object> getWorkerInfoItem(int routeIndex) throws JSONException {
		if (mRouteList.get(mRouteIndex).mWorkerInfo_Root_Object == null)
			return null;
		if (mRouteList.get(mRouteIndex).mWorkerList != null)
			return mRouteList.get(mRouteIndex).mWorkerList;
		mRouteList.get(mRouteIndex).mWorkerList = new ArrayList<Object>();
		try {
			JSONObject object = (JSONObject) mRouteList.get(mRouteIndex).mWorkerInfo_Root_Object;

			JSONArray array = object.getJSONArray("WorkerInfo");

			for (int i = 0; i < array.length(); i++) {

				mRouteList.get(mRouteIndex).mWorkerList.add(array.getJSONObject(i));

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mRouteList.get(mRouteIndex).mWorkerList;
	}

public static List<WorkerInfo> parseWorkerNode(Object WorkerObject) throws JSONException {
		
		if(WorkerObject == null) return null;
		
		List<WorkerInfo> workerList = new ArrayList<WorkerInfo>();
		
		JSONArray jsonArray = ((JSONObject)WorkerObject).getJSONArray("WorkerInfo");
		
		for(int i =0 ;i<jsonArray.length();i++){
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);			
			WorkerInfo info = new WorkerInfo();
			info.GroupName = jsonObject.getString("GroupName");
			info.WorkerName = jsonObject.getString("WorkerName");
			info.WorkerNumber = jsonObject.getString("WorkerNumber");
			info.WorkerPwd = jsonObject.getString("WorkerPwd");
			info.isAdministrator = Integer.valueOf(jsonObject.getString("Adminstrator")) == 1;
			workerList.add(info);
			Log.d("luotest","parseWorkerNode()info "+info.GroupName);
			
			
		}	
		Log.d("luotest","parseWorkerNode()"+workerList.toString());
		return workerList;
	}
	
public static Route.info parseRouteNameNode(Object RouteNameObject) throws JSONException {
		
		if(RouteNameObject == null) return null;
		

		Route rout = new Route();
		Route.info infor = rout.new info();		
		infor.PlanName = ((JSONObject)RouteNameObject).getString("PlanName");
		infor.PlanGuid = ((JSONObject)RouteNameObject).getString("PlanGuid");		
		return infor;
	}

public static List<TurnInfo> parseTurnNode(Object TurnObject) throws JSONException {
	
	if(TurnObject == null) return null;

	List<TurnInfo> list = new ArrayList<TurnInfo>();
	try {
		JSONObject object = (JSONObject)TurnObject;

		JSONArray array = object.getJSONArray("TurnInfo");

		for (int i = 0; i < array.length(); i++) {
			TurnInfo info = new TurnInfo();
			//mRouteList.get(mRouteIndex).mTurnList.add(array.getJSONObject(i));
			info.Number=((JSONObject)array.get(i)).getString("Number");
			info.StartTime=((JSONObject)array.get(i)).getString("StartTime");
			info.EndTime=((JSONObject)array.get(i)).getString("EndTime");
			info.DutyNumber=((JSONObject)array.get(i)).getString("DutyNumber");					
			list.add(info);

		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	
	return list;
	
}
	
	public List<TurnInfo> getTurnInfoItem(int routeIndex) throws JSONException {
		if (mRouteList.get(mRouteIndex).mTurnInfo_Root_Object == null)
			return null;

		if (mRouteList.get(mRouteIndex).TurnInfoList != null)
			return mRouteList.get(mRouteIndex).TurnInfoList;

		mRouteList.get(mRouteIndex).TurnInfoList = new ArrayList<TurnInfo>();
		try {
			JSONObject object = (JSONObject) mRouteList.get(mRouteIndex).mTurnInfo_Root_Object;

			JSONArray array = object.getJSONArray("TurnInfo");

			for (int i = 0; i < array.length(); i++) {
				TurnInfo info = new TurnInfo();
				info.Number=((JSONObject)array.get(i)).getString("Number");
				info.StartTime=((JSONObject)array.get(i)).getString("StartTime");
				info.EndTime=((JSONObject)array.get(i)).getString("EndTime");
				info.DutyNumber=((JSONObject)array.get(i)).getString("DutyNumber");					
				mRouteList.get(mRouteIndex).TurnInfoList.add(info);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mRouteList.get(mRouteIndex).TurnInfoList;
	}

	public Object getStationItem(int routeIndex,int index) {
		Log.d(TAG, "getStationItem");
		return mRouteList.get(mRouteIndex).mStationList.get(mStationIndex);

	}	
	
}
