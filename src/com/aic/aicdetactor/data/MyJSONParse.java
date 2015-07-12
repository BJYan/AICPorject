package com.aic.aicdetactor.data;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.aic.aicdetactor.comm.CommonDef;
import com.aic.aicdetactor.database.DBHelper;
import com.aic.aicdetactor.database.RouteDao;

public class MyJSONParse {
	String TAG = "luotest";
	public static String STATIONINFO = "StationInfo";
	public static String WORKERINFO = "WorkerInfor";
	public static String TRUNINFO = "TurnInfo";
	public static String STATIONITEM = "StationItem";
	public static String DEVICEITEM = "DeviceItem";
	public static String DEVICENAME = "DeviceName";
	public static String PARTITEM = "PartItem";
	public static String PARTITEMDATA = "PartItemData";
	public static String STATIONNAME = "StationName";
	public static String IDINFO = "IDInfo";
	public static String IDNUMBER = "IDNumber";
	public static String PLANNAME = "PlanName";
	public static String ITEMDEF = "ItemDef";
	public static int ITEMDEF_INDEX = 5;
	private RouteDao mRouteDao = null;
	private Context mContext = null;
	private SharedPreferences mSharedPreferences = null;
	private int mCurrentRouteIndex =0;//当前或最近的路线索引
	private int mCurrentStationIndex =0;//当前或最近的站点索引
	private int mCurrentDeviceIndex=0;//当前或最近的设备索引
	private int mCurrentPartItemIndex =0;//当前或最近的巡检项索引
	private int mIsReverseChecking = 0;//是否反向巡检
	private String mCurrentFileName = null;//当前巡检的文件名即guid
	private String mSavePath = null;//检查路线数据存储的路径
	private List<RouteInfo> mRouteInfoList = null;
	// partitemData split key word
	public static String PARTITEMDATA_SPLIT_KEYWORD = "\\*";

	private Cursor mCursor = null;
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
		editor.putLong(CommonDef.ROUTE_INDEX, cv.getAsLong(CommonDef.ROUTE_INDEX));
		editor.putLong(CommonDef.STATION_INDEX, cv.getAsLong(CommonDef.STATION_INDEX));
		editor.putLong(CommonDef.DEVICE_INDEX, cv.getAsLong(CommonDef.DEVICE_INDEX));
		editor.putLong(CommonDef.PARTITEM_INDEX, cv.getAsLong(CommonDef.PARTITEM_INDEX));
		editor.putLong(CommonDef.ISREVERSECHECKING, cv.getAsLong(CommonDef.ISREVERSECHECKING));
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
	public int InitData(Context context){
		mContext = context;
		if(mRouteDao== null){
		mRouteDao = new RouteDao(mContext);}
		
		mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
		// 实例化SharedPreferences.Editor对象（第二步）
		
		//获取当前巡检的索引信息
		mCurrentRouteIndex = (int) mSharedPreferences.getLong(CommonDef.ROUTE_INDEX,0);
		mCurrentStationIndex = (int) mSharedPreferences.getLong(CommonDef.STATION_INDEX,0);
		mCurrentDeviceIndex = (int) mSharedPreferences.getLong(CommonDef.DEVICE_INDEX,0);
		mCurrentPartItemIndex = (int) mSharedPreferences.getLong(CommonDef.PARTITEM_INDEX,0);
		mIsReverseChecking = (int) mSharedPreferences.getLong(CommonDef.ISREVERSECHECKING,0);
		mCurrentFileName = mSharedPreferences.getString(CommonDef.GUID,null);
		mSavePath = mSharedPreferences.getString(CommonDef.PATH_DIRECTOR,null);		
		
		//再查数据库中是否有完成的巡检路线，加载到mRouteInfoList中
		mRouteInfoList = mRouteDao.getNoCheckFinishRouteInfo();
		
		for(int index =0;index <mRouteInfoList.size();index++){
			String path = mRouteInfoList.get(index).getFileName();
			//从此开始关联各个RouteInfo相关项
			initData(index,path);
		}
		
		return getRouteCount();
	}
	
	/*
	 * 插入新的巡检计划，返回数值是未完成的巡检计划数量,并刷新mRouteInfoList
	 */
	public int insertNewRouteInfo(String fileName,String path,Context context){
		if(fileName == null || path == null){
			return 0;
		}
		if(mContext == null){
			mContext = context;
		}
		if(mRouteDao== null){
			mRouteDao = new RouteDao(mContext);}
		mRouteDao.insertNewRouteInfo(fileName, path);
		return 1;
	}
	/**
	 * 返回未完成的巡检路线的个数，便于UI界面显示
	 * @return
	 */
	public int getRouteCount(){
		int count =0;
		if(mRouteInfoList!=null){
			count = mRouteInfoList.size();
		}
		return count;
	}
	// first init
	private void initData(int routeIndex,String path) {
		String data = openFile(path);
		if (data != null) {
			Log.d("luotest", "data is not null");
			try {
				JSONArray jsonArray = new JSONArray(data);
				for (int i = 0; i < jsonArray.length(); i++) {
					Log.d("luotest", "data is not null" + "i = " + i);
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					String index = jsonObject.getString("Index");
					Log.d("luotest", "name = " + index);
					if (index.equals("7")) {
						mRouteInfoList.get(routeIndex).mPlanName_Root_Object = jsonObject;
					} else if (index.equals("1")) {
						mRouteInfoList.get(routeIndex).mTurnInfo_Root_Object = jsonObject;
					} else if (index.equals("2")) {
						mRouteInfoList.get(routeIndex).mWorkerInfo_Root_Object = jsonObject;
					} else if (index.equals("4")) {
						mRouteInfoList.get(routeIndex).mStationInfo_Root_Object = jsonObject;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	public int getDevicePartItemCount(Object deviceItemObject)
			throws JSONException {

		int count = 0;

		JSONObject object = (JSONObject) deviceItemObject;
		List<Object> partlist = this.getPartList(object);
		count = partlist.size();
		for (int k = 0; k < partlist.size(); k++) {
			Log.d(TAG, " getDevicePartItemCount k=" + k + "," + partlist.get(k));
		}

		return count;
	}

	public int getStationPartItemCount(Object staionItemObject)
			throws JSONException {
		int count = 0;

		JSONObject object = (JSONObject) staionItemObject;
		List<Object> devicelist = this.getDeviceList(object);
		for (int n = 0; n < devicelist.size(); n++) {

			List<Object> partlist = this.getPartList(devicelist.get(n));
			count = count + partlist.size();
			// for(int k =0 ;k<partlist.size();k++){
			// Log.d(TAG, " getStationPartItemCount k=" + k + "," +
			// partlist.get(k));
			// }
		}

		return count;
	}

	public int getRoutePartItemCount(int routeIndex) throws JSONException {
		List<Object> list = getStationList(routeIndex);
		int count = 0;
		for (int i = 0; i < list.size(); i++) {
			JSONObject object = (JSONObject) list.get(i);
			List<Object> devicelist = this.getDeviceList(object);

			for (int n = 0; n < devicelist.size(); n++) {
				List<Object> partlist = this.getPartList(devicelist.get(n));
				count = count + partlist.size();
				// for(int k =0 ;k<partlist.size();k++){
				// Log.d(TAG, " getRoutePartItemCount k=" + k + "," +
				// partlist.get(k));
				// }
			}
		}

		return count;
	}

	public String getRoutName(int routeIndex) throws JSONException {
		String name = null;
		if (mRouteInfoList.get(routeIndex).mPlanName_Root_Object == null) {
			Log.e(TAG, "getRoutName mPlanName_Root_Object is null");
			return null;
		}
		JSONObject object = (JSONObject) mRouteInfoList.get(routeIndex).mPlanName_Root_Object;
		name = object.getString(PLANNAME);
		return name;
	}

	public List<Object> getStationList(int routeIndex) throws JSONException {
		if (mRouteInfoList.get(routeIndex).mStationInfo_Root_Object == null)
			return null;

		if (mRouteInfoList.get(routeIndex).mStationList != null)
			return mRouteInfoList.get(routeIndex).mStationList;

		mRouteInfoList.get(routeIndex).mStationList = new ArrayList<Object>();
		try {
			JSONObject object = (JSONObject) mRouteInfoList.get(routeIndex).mStationInfo_Root_Object;

			JSONArray array = object.getJSONArray(STATIONINFO);

			for (int i = 0; i < array.length(); i++) {
				JSONObject subObject = array.getJSONObject(i);
				Log.d(TAG, "I =" + i + subObject.getString(STATIONNAME));
				mRouteInfoList.get(routeIndex).mStationList.add(subObject);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// mStationList = list;
		return mRouteInfoList.get(routeIndex).mStationList;
	}

	public String getStationItemName(Object stationItemobject)
			throws JSONException {
		String name = null;// NAME = NULL;
		try {
			JSONObject newobject = (JSONObject) stationItemobject;

			name = newobject.getString(STATIONNAME);

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

			JSONArray array = object2.getJSONArray(STATIONITEM);
			Log.d(TAG, " object is not null array.size is " + array.length());
			for (int i = 0; i < array.length(); i++) {
				Log.d(TAG, " getDeviceItem object i" + i);
				JSONObject subObject = array.getJSONObject(i);
				if (subObject.getString("Index").equals("6")) {
					// list.add(subObject);
					Log.d(TAG, " getDeviceItem object 6  i" + i);

					JSONArray sub_Array = subObject.getJSONArray(DEVICEITEM);
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

	// input params must be StationItemIndex
	public List<Object> getDeviceItem(int routeIndex,int stationIndex) throws JSONException {

		List<Object> list = new ArrayList<Object>();
		try {
			JSONObject object2 = (JSONObject) getStationItem(routeIndex,stationIndex);
			;

			JSONArray array = object2.getJSONArray(STATIONITEM);
			Log.d(TAG, " object is not null array.size is " + array.length());
			for (int i = 0; i < array.length(); i++) {
				Log.d(TAG, " getDeviceItem object i" + i);
				JSONObject subObject = array.getJSONObject(i);
				if (subObject.getString("Index").equals("6")) {
					// list.add(subObject);
					Log.d(TAG, " getDeviceItem object 6  i" + i);

					JSONArray sub_Array = subObject.getJSONArray(DEVICEITEM);
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
			JSONArray array = object2.getJSONArray(STATIONITEM);
			Log.d(TAG, "getIDInfo object ");
			for (int i = 0; i < array.length(); i++) {
				Log.d(TAG, "getIDInfo object i =" + i);
				JSONObject subObject = array.getJSONObject(i);
				if (subObject.getString("Index").equals("5")) {
					// list.add(subObject);
					Log.d(TAG, "getIDInfo object index is 5");
					JSONArray sub_Array = subObject.getJSONArray(IDINFO);
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
		String str = (String) object.get(ITEMDEF);
		
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
		for (int i = 0; i < mRouteInfoList.get(routeIndex).mStationList.size(); i++) {
			object = (JSONObject) mRouteInfoList.get(routeIndex).mStationList.get(i);
			list = getIDInfo(object);
			Log.d(TAG, "getStationItemIndexByID i = " + i + ",object is "
					+ object.toString());
			if (list != null) {
				for (int j = 0; j < list.size(); j++) {
					object = (JSONObject) list.get(j);
					Log.d(TAG, "getStationItemIndexByID j is " + j
							+ ",object is " + object.toString());
					String idnumber = (String) object.get(IDNUMBER);
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
				name = newObject.getString(PARTITEMDATA);
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
		JSONArray array = ((JSONObject) partItemobject).getJSONArray(PARTITEM);
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
			JSONArray array = newObject.getJSONArray(PARTITEM);
			for (int i = 0; i < array.length(); i++) {
				list.add(array.getJSONObject(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	// input params must be partItem sub
	public String getPartItemName(Object partItemobject) {
		//Log.d(TAG, "getPartItemName 0");
		if (partItemobject == null) {
			Log.d(TAG, "getPartItemName " + " object is null");
			return null;
		}
		String name = null;
		//Log.d(TAG, "getPartItemName 1");
		try {
			JSONObject newObject = (JSONObject) partItemobject;

			//Log.d(TAG, "getPartItemName 3");
			name = newObject.getString(PARTITEMDATA);
			//Log.d(TAG, "getPartItemName 4");

		} catch (Exception e) {
			Log.e(TAG,e.toString());;
		}
		Log.d(TAG, "getPartItemName name is "+name);
		return name;
	}

	public List<Object> getWorkerInfoItem(int routeIndex) throws JSONException {
		if (mRouteInfoList.get(routeIndex).mWorkerInfo_Root_Object == null)
			return null;
		if (mRouteInfoList.get(routeIndex).mWorkerList != null)
			return mRouteInfoList.get(routeIndex).mWorkerList;
		mRouteInfoList.get(routeIndex).mWorkerList = new ArrayList<Object>();
		try {
			JSONObject object = (JSONObject) mRouteInfoList.get(routeIndex).mWorkerInfo_Root_Object;

			JSONArray array = object.getJSONArray("WorkerInfo");

			for (int i = 0; i < array.length(); i++) {

				mRouteInfoList.get(routeIndex).mWorkerList.add(array.getJSONObject(i));

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mRouteInfoList.get(routeIndex).mWorkerList;
	}

	public List<Object> getTurnInfoItem(int routeIndex) throws JSONException {
		if (mRouteInfoList.get(routeIndex).mTurnInfo_Root_Object == null)
			return null;

		if (mRouteInfoList.get(routeIndex).mTurnList != null)
			return mRouteInfoList.get(routeIndex).mTurnList;

		mRouteInfoList.get(routeIndex).mTurnList = new ArrayList<Object>();
		try {
			JSONObject object = (JSONObject) mRouteInfoList.get(routeIndex).mTurnInfo_Root_Object;

			JSONArray array = object.getJSONArray("TurnInfo");

			for (int i = 0; i < array.length(); i++) {

				mRouteInfoList.get(routeIndex).mTurnList.add(array.getJSONObject(i));

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mRouteInfoList.get(routeIndex).mTurnList;
	}

	public Object getStationItem(int routeIndex,int index) {
		Log.d(TAG, "getStationItem");
		return mRouteInfoList.get(routeIndex).mStationList.get(index);

	}	

	public String openFile(String path) {
		if (null == path) {

			return null;
		}

		Log.d("luotest", "path 1= " + path);
		File file = new File(path);
		if (file.exists()) {
			Log.d("luotest", "path 2= " + path);
			try {
				StringBuffer sb = new StringBuffer();
				// HttpEntity entity = response.getEntity();
				InputStream is = new FileInputStream(path);// entity.getContent();
				BufferedReader br = new BufferedReader(new InputStreamReader(
						is, "GB2312"));
				String data = "";

				while ((data = br.readLine()) != null) {
					sb.append(data);
				}
				String result = sb.toString();
				// Log.d("luotest","result is :"+result);
				// return result.getBytes("UTF-8");
				return result;
			} catch (Exception e) {
				Log.d("luotest", "read data exception " + e.toString());
				e.printStackTrace();
			}
		}
		Log.d("luotest", "path 3 = " + path);
		return null;
	}

	// ��ȡվ�������б�
	@SuppressLint("NewApi")
	public String parseLevel_StationItems(String path) {
		String result = null;
		String data = openFile(path);
		if (data != null) {
			Log.d("luotest", "parseLevel_StationItems is not null");
			try {
				// ��Ŀ¼
				JSONArray jsonArray = new JSONArray(data);
				for (int i = 0; i < jsonArray.length(); i++) {
					Log.d("luotest", "parseLevel_StationItems is not null"
							+ "i = " + i);
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					String name = jsonObject.getString("Index");
					Log.d("luotest", "parseLevel_StationItems = " + name);
					if (name.equals("4")) {
						JSONArray jsonArray_station = new JSONArray(jsonObject);
						// stationinfo
						for (int j = 0; j < jsonArray_station.length(); j++) {
							// get stationItem info
							JSONObject jsonObject_stationitem = jsonArray
									.getJSONObject(j);
							String stationName = jsonObject_stationitem
									.getString("StationName");
							Log.d(TAG, "StationinfoName = " + stationName
									+ ",j=" + j);
						}
						// result = jsonObject.getString("PlanName");
						Log.d("luotest", "parseLevel_StationItems 2= " + name);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}
		return result;
	}

	// ��ȡһ��Ŀ¼��Index is 7�� PlanName
	@SuppressLint("NewApi")
	public String parsePlanName(String path) {
		String result = null;
		String data = openFile(path);
		if (data != null) {
			Log.d("luotest", "data is not null");
			try {
				JSONArray jsonArray = new JSONArray(data);
				for (int i = 0; i < jsonArray.length(); i++) {
					Log.d("luotest", "data is not null" + "i = " + i);
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					String name = jsonObject.getString("Index");
					Log.d("luotest", "name = " + name);
					if (name.equals("7")) {
						result = jsonObject.getString("PlanName");
						Log.d("luotest", "name 2= " + name);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}
		return result;
	}	
}