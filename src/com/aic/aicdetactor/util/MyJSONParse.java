package com.aic.aicdetactor.util;

import android.annotation.SuppressLint;
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

public class MyJSONParse {
	String TAG = "luotest";
	private Object mStationInfo_Root_Object = null;
	private Object mWorkerInfo_Root_Object = null;
	private Object mTurnInfo_Root_Object = null;
	private Object mPlanName_Root_Object = null;

	private List<Object> mStationList = null;
	private List<Object> mWorkerList = null;
	private List<Object> mTurnList = null;

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
	// partitemData split key word
	public static String PARTITEMDATA_SPLIT_KEYWORD = "\\*";

	public MyJSONParse() {

	}

	// first init
	public void initData(String path) {
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
						mPlanName_Root_Object = jsonObject;
					} else if (index.equals("1")) {
						mTurnInfo_Root_Object = jsonObject;
					} else if (index.equals("2")) {
						mWorkerInfo_Root_Object = jsonObject;
					} else if (index.equals("4")) {
						mStationInfo_Root_Object = jsonObject;
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
		List<Object> list = getStationList();
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

	public String getRoutName() throws JSONException {
		String name = null;
		if (mPlanName_Root_Object == null) {
			Log.e(TAG, "getRoutName mPlanName_Root_Object is null");
			return null;
		}
		JSONObject object = (JSONObject) mPlanName_Root_Object;
		name = object.getString(PLANNAME);
		return name;
	}

	public List<Object> getStationList() throws JSONException {
		if (mStationInfo_Root_Object == null)
			return null;

		if (mStationList != null)
			return mStationList;

		mStationList = new ArrayList<Object>();
		try {
			JSONObject object = (JSONObject) mStationInfo_Root_Object;

			JSONArray array = object.getJSONArray(STATIONINFO);

			for (int i = 0; i < array.length(); i++) {
				JSONObject subObject = array.getJSONObject(i);
				Log.d(TAG, "I =" + i + subObject.getString(STATIONNAME));
				mStationList.add(subObject);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// mStationList = list;
		return mStationList;
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
	public List<Object> getDeviceItem(int stationIndex) throws JSONException {

		List<Object> list = new ArrayList<Object>();
		try {
			JSONObject object2 = (JSONObject) getStationItem(stationIndex);
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

	public int getStationItemIndexByID(String strIdCode) throws JSONException {
		if (strIdCode == null) {
			Log.d(TAG, "getStationItemIndexByID strIdCode is null");
			return -1;
		}
		int index = 0;
		JSONObject object = null;

		List<Object> list = null;
		for (int i = 0; i < mStationList.size(); i++) {
			object = (JSONObject) mStationList.get(i);
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

	/*
	 * input: index is itemDef index from left to right ,start from 0,that is
	 * combox widget index input:object is deviceItem intent is to get itemDef
	 */	
	public List<Object> getPartItemListByItemDef(Object object, int index)
			throws JSONException {
		List<Object> list = new ArrayList<Object>();

		if (object == null) {
			Log.e(TAG, " getPartItemListByItemDef input param object is null");
			return null;
		}
		JSONArray array = ((JSONObject) object).getJSONArray(PARTITEM);

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
		Log.d(TAG, "getPartItemName 0");
		if (partItemobject == null) {
			Log.d(TAG, "getPartItemName " + " object is null");
			return null;
		}
		String name = null;
		Log.d(TAG, "getPartItemName 1");
		try {
			JSONObject newObject = (JSONObject) partItemobject;

			Log.d(TAG, "getPartItemName 3");
			name = newObject.getString(PARTITEMDATA);
			Log.d(TAG, "getPartItemName 4");

		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.d(TAG, "getPartItemName 5");
		return name;
	}

	public List<Object> getWorkerInfoItem() throws JSONException {
		if (mWorkerInfo_Root_Object == null)
			return null;
		if (mWorkerList != null)
			return mWorkerList;
		mWorkerList = new ArrayList<Object>();
		try {
			JSONObject object = (JSONObject) mWorkerInfo_Root_Object;

			JSONArray array = object.getJSONArray("WorkerInfo");

			for (int i = 0; i < array.length(); i++) {

				mWorkerList.add(array.getJSONObject(i));

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mWorkerList;
	}

	public List<Object> getTurnInfoItem() throws JSONException {
		if (mTurnInfo_Root_Object == null)
			return null;

		if (mTurnList != null)
			return mTurnList;

		mTurnList = new ArrayList<Object>();
		try {
			JSONObject object = (JSONObject) mTurnInfo_Root_Object;

			JSONArray array = object.getJSONArray("TurnInfo");

			for (int i = 0; i < array.length(); i++) {

				mTurnList.add(array.getJSONObject(i));

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mTurnList;
	}

	public Object getStationItem(int index) {
		Log.d(TAG, "getStationItem");
		return mStationList.get(index);

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
