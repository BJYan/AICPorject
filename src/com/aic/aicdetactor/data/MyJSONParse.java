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
import com.google.gson.Gson;

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
	private List<T_Route> mRouteList = null;
	private final boolean isBstar=false;
	private Gson mgson = null;
	// partitemData split key word
	public static String PARTITEMDATA_SPLIT_KEYWORD = "\\*";


	private static MyJSONParse mMyJSONParse=null;
		private MyJSONParse() {
			mgson = new Gson();
		}
	public static MyJSONParse getInstance(){
		if(mMyJSONParse==null){
			mMyJSONParse = new MyJSONParse();
		}
		return mMyJSONParse;
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
	
	/**
	 * 给指定的DeviceItem 对象下赋值新的GUID
	 */
	void genGUIDUnderDeviceItem(JSONObject object){
		if(object == null) return;
		JSONObject json = object;
		
		try {
			json.put(KEY.KEY_Data_Exist_Guid, SystemUtil.createGUID());
			Log.d(TAG, "genGUIDUnderDeviceItem() object is "+json.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	/**本想法是通过SharedPreferences来存储正在巡检的路线的相关索引等信息，例如刚在巡检到哪一具体项了。
	 * 
	 * 
	 * @param context
	 */
	public int InitData(Context context,List<String>list){
		mContext = context;
		if(mRouteDao== null){
		mRouteDao = RouteDao.getInstance(mContext);}
		
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
			String path = mRouteList.get(index).Path;
			
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
			mRouteDao = RouteDao.getInstance(mContext);
			}
		//if(mRouteDao.getCount()<12){		
		mRouteDao.insertNewRouteInfo(path);
		//}
		return 1;
	}
	public int insertUpLoadInfo(Context context){
		if(mContext == null){
			mContext = context;
		}
		upLoadInfo up = new upLoadInfo();
		up.Base_Point="point";
		up.Class_Group = "work";//worker 数据表中
		up.Date= SystemUtil.getSystemTime(0);
		up.File_Guid = "guid";//worker 数据表中,文件名
		up.Is_Updateed = ""+0;;
		up.Is_Uploaded=""+0;
		up.Span="span";
		up.Start_Point="point";
		up.T_Line_Guid=SystemUtil.createGUID();//worker数据表总
		up.T_Line_Name = "guid+worker+turn+system";
		up.T_Period_Unit_Code="";
		up.Task_Mode="0";
		up.Turn_Finish_Mode="0";
		up.Turn_Name="";
		up.Turn_Number="";
		up.Worker_Name="";
		up.Turn_Number="";
		if(mRouteDao== null){
			mRouteDao = RouteDao.getInstance(mContext);
			}
		//if(mRouteDao.getCount()<12){		
		mRouteDao.insertUploadFile(up);
		return 1;
	}
	public static T_Route getPlanInfo(String Routepath){
		T_Route route = new T_Route();
		String data = SystemUtil.openFile(Routepath);	
		route.Path = Routepath;
		if (data != null) {
			
			try {
				JSONTokener jsonTokener = new JSONTokener(data);		
				JSONObject object = (JSONObject) jsonTokener.nextValue();
				
				route.mGloableObject = object.getJSONObject(GlobalInfo.NodeName);
				route.mLineObject = object.getJSONObject(T_Line.RootNodeName);
				route.mStationArrary= (JSONArray) object.getJSONArray(KEY.KEY_STATIONINFO);
				route.mTurnArrary= (JSONArray) object.getJSONArray(T_Turn.RootNodeName);
				route.mWorkerArrary= (JSONArray) object.getJSONArray(T_Worker.RootNodeName);
				JSONObject sub_object = object.getJSONObject(T_Period.RootNodeName);
				route.mT_PeriodArray = sub_object.getJSONArray(T_Period.ArrayName);
				route.mOrganizationObject = object.getJSONObject(T_Organization.NodeName);
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
	public void SaveData(int RouteIndex,String fileName){
		
		T_Route info =mRouteList.get(RouteIndex);
		
		try {
			info.SaveData(fileName);
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
				JSONObject object = (JSONObject) jsonTokener.nextValue();
				
				mRouteList.get(routeIndex).mGloableObject = object.getJSONObject(GlobalInfo.NodeName);
				mRouteList.get(routeIndex).mLineObject =object.getJSONObject(T_Line.RootNodeName);
				mRouteList.get(routeIndex).mStationArrary= (JSONArray) object.getJSONArray(KEY.KEY_STATIONINFO);
				mRouteList.get(routeIndex).Item_Abnormal_GradeArrary= (JSONArray) object.getJSONArray(KEY.KEY_T_Item_Abnormal_Grade);
				mRouteList.get(routeIndex).Measure_TypeArrary= (JSONArray) object.getJSONArray(KEY.KEY_T_Measure_Type);
				mRouteList.get(routeIndex).mTurnArrary= (JSONArray) object.getJSONArray(T_Turn.RootNodeName);
				mRouteList.get(routeIndex).mWorkerArrary= (JSONArray) object.getJSONArray(T_Worker.RootNodeName);
				mRouteList.get(routeIndex).mOrganizationObject = object.getJSONObject(T_Organization.NodeName);
				JSONObject sub_object = object.getJSONObject(T_Period.RootNodeName);
				mRouteList.get(routeIndex).mT_PeriodArray = sub_object.getJSONArray(T_Period.ArrayName);
				
				if (mRouteList.get(routeIndex).mWorkerArrary != null) {
					try {

						mRouteList.get(routeIndex).mWorkerList = parseWorkerNode(mRouteList.get(routeIndex).mWorkerArrary);
						Log.e("luotest", "parseBaseInfo() mWorkerList is" + mRouteList.get(routeIndex).mWorkerList.toString());
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				 if(mRouteList.get(routeIndex).mLineObject != null){
				 try {
					 mRouteList.get(routeIndex).mLineInfo = parseRouteNameNode(mRouteList.get(routeIndex).mLineObject);
					
				 } catch (JSONException e) {
				 // TODO Auto-generated catch block
				 e.printStackTrace();
				 }
				 }
				if (mRouteList.get(routeIndex).mTurnArrary != null) {
					try {
						mRouteList.get(routeIndex).mTurnList = parseTurnNode(mRouteList.get(routeIndex).mTurnArrary);
						Log.e("luotest", "parseBaseInfo() mTurnList is" + mRouteList.get(routeIndex).mTurnList.toString());
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

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
			String[] arrayOld = Oldvalue.split(PARTITEMDATA_SPLIT_KEYWORD);
			
			String[] array2 = Value.split(PARTITEMDATA_SPLIT_KEYWORD);
			Log.d(TAG, "setPartItem_ItemDef() old length ="+arrayOld.length + ",new Length ="+array2.length);
			Oldvalue = null;
			if(arrayOld.length<partItemDefaultLenth){
				for(int i = 0;i<arrayOld.length;i++){
				Oldvalue = Oldvalue + arrayOld[i]+"*";
				}
				Oldvalue = Oldvalue + Value;
			}else{
				arrayOld[9] = Value;
				for(int i = 0;i<partItemDefaultLenth;i++){
					Oldvalue = Oldvalue + arrayOld[i]+"*";
					}
					Oldvalue = Oldvalue + Value;
			}
			Log.d(TAG, "setPartItem_ItemDef() Oldvalue is "+Oldvalue);
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
					Object deviceItemObject = itemList.get(n);
					List<Object> partlist = this.getPartList(deviceItemObject);
					//Is_Device_Checked : 0
//					if(((JSONObject)deviceItemObject).optBoolean("Is_Device_Checked")){
//						status.mCheckedCount++;
//					}
					count = count + partlist.size();
					for(int k=0;k<partlist.size();k++){
						JSONObject itemObject = (JSONObject) partlist.get(k);				
						checkTimeStr = this.getPartItemCheckUnitName(itemObject, CommonDef.partItemData_Index.PARTITEM_ADD_END_DATE_20);
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
				Object deviceItemObject = devicelist.get(n);
				partlist = this.getPartList(deviceItemObject);
//				if(((JSONObject)deviceItemObject).optBoolean("Is_Device_Checked")){
//					status.mCheckedCount++;
//				}
				count = count + partlist.size();
				for(int k=0;k<partlist.size();k++){
					JSONObject itemObject = (JSONObject) partlist.get(k);				
					checkTimeStr = this.getPartItemCheckUnitName(itemObject, CommonDef.partItemData_Index.PARTITEM_ADD_END_DATE_20);
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
//			if(((JSONObject)Object).optBoolean("Is_Device_Checked")){
//				status.mCheckedCount++;
//			}
			for (int k = 0; k < partlist.size(); k++) {
				Log.d(TAG, " getDevicePartItemCount k=" + k + "," + partlist.get(k));
				itemObject = (JSONObject) partlist.get(k);	
				
				checkTimeStr = this.getPartItemCheckUnitName(itemObject, CommonDef.partItemData_Index.PARTITEM_ADD_END_DATE_20);
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
	
	/**
	 * 根据stationIndex,查询没有巡检的DeviceIndex,以便上层丛此处开始巡检
	 * @param mStationIndex
	 * @return
	 */
	public ContentValues getNeedCheckDeviceItemIndex(int mStationIndex){
		ContentValues va = new ContentValues();
		int index =-1;
		String name ="";
		Log.d(TAG, "getNeedCheckDeviceItemIndex() ");
		try {
			List<Object> deviceItemList = getDeviceItem(mStationIndex);
			for ( int i = 0; i < deviceItemList.size(); i++) {
				JSONObject object = (JSONObject) deviceItemList.get(i);
				Log.d(TAG, "getNeedCheckDeviceItemIndex() "+i + ",object is "+object.toString());
				int b =object.optInt(T_Device_Item.Device_Array_Item_Const.Key_Is_Device_Checked);
				Log.d(TAG, "getNeedCheckDeviceItemIndex() b ="+b);
				if(b==0){
					index = i;
					name = object.optString(T_Device_Item.Device_Array_Item_Const.Key_Name);
					break;
				}
				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		va.put(KEY.KEY_Device_Index, index);
		va.put(KEY.KEY_Device_Name, name);
		return va;
	}


	/**
	 * 获取各个巡检路线的路线名称，在路线界面调用。
	 * @param routeIndex
	 * @return
	 * @throws JSONException
	 */
	public String getRoutName(int routeIndex) throws JSONException {
		String name = null;
		if (mRouteList.get(routeIndex).mLineObject == null) {
			Log.e(TAG, "getRoutName mPlanName_Root_Object is null");
			return null;
		}
		name = mRouteList.get(routeIndex).Name;		
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
		
		if (mRouteList.get(mRouteIndex).mStationArrary == null)
			return null;
		
		if (mRouteList.get(mRouteIndex).mStationList != null)
			return mRouteList.get(mRouteIndex).mStationList;

		mRouteList.get(mRouteIndex).mStationList = new ArrayList<Object>();
		try {
			

			JSONArray array = mRouteList.get(mRouteIndex).mStationArrary;

			for (int i = 0; i < array.length(); i++) {
				JSONObject subObject = array.getJSONObject(i);
				
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

			name = newobject.getString(KEY.KEY_NAME);

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

			JSONArray array = object2.getJSONArray(KEY.KEY_DEVICEITEM);
			Log.d(TAG, " object is not null array.size is " + array.length());
			for (int i = 0; i < array.length(); i++) {
				Log.d(TAG, " getDeviceItem object i" + i);
				JSONObject subObject = array.getJSONObject(i);
				
				//genGUIDUnderDeviceItem(subObject);
				list.add(subObject);			
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;

	}

	/**
	 * 根据Item_def_type 丛PartItem数组中获取指定数据。如果Item_def_type 为-1的，即全部数据，不用筛选
	 * 否则都需要筛选。
	 * @param object
	 * @param Item_def_type
	 * @return
	 */
	public List<Object>getPartItem(Object object,int Item_def_type){
		List<Object>list = new ArrayList<Object>();
		
		try {
			JSONArray array = ((JSONObject)object).getJSONArray(KEY.KEY_PARTITEM);
			for(int i=0;i<array.length();i++){
				if(Item_def_type ==-1){
				list.add(array.get(i));
				}else{
					JSONObject part=(JSONObject) array.get(i);
					 String str =  part.optString(KEY.KEY_PARTITEMDATA);
					 String def=getPartItemSubStr(str,CommonDef.partItemData_Index.PARTITEM_START_STOP_STATUS_FLAG);
					 int iv=Integer.valueOf(def);
					 if((iv<<Item_def_type)>0){
						 list.add(array.get(i));
					 }
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
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
			//Log.d(TAG, " getDeviceItem  stationItem is " + stationItem.toString());
			JSONArray array = stationItem.getJSONArray(KEY.KEY_DEVICEITEM);
			//Log.d(TAG, " getDeviceItem  stationItem array " + array.toString());
			for (int i = 0; i < array.length(); i++) {				
				JSONObject subObject = array.getJSONObject(i);
				list.add(subObject);
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
			JSONArray array = object2.getJSONArray(KEY.KEY_DEVICEITEM);
			Log.d(TAG, "getIDInfo object ");
			for (int i = 0; i < array.length(); i++) {
				Log.d(TAG, "getIDInfo object i =" + i);
				JSONObject subObject = array.getJSONObject(i);				
					JSONArray sub_Array = subObject.getJSONArray(KEY.KEY_IDINFO);
					for (int k = 0; k < sub_Array.length(); k++) {
						list.add(sub_Array.getJSONObject(k));
						Log.d(TAG, "getIDInfo object k =" + k);
					}				
			}

		} catch (Exception e) {
			e.printStackTrace();
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
			name = newObject.getString("Name");
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
			//list = getIDInfo(object);
			String codes = object.optString("Code");
			if(codes.contains(strIdCode)){
				index = i;
				break;
			}
			Log.d(TAG, "getStationItemIndexByID i = " + i + ",object is "
					+ object.toString());
			
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
//		for(int i =0;i<array.length;i++){
//		Log.d(TAG, "getPartItemSubStr array is "+array[i]);
//		}

		return array[index];

	}

	// input params must be partItem sub
		public String getPartItemCheckUnitName(Object partItemobject,int index) {
			if (partItemobject == null) {
				Log.d(TAG, "getPartItemCheckUnitName " + " object is null");
				return null;
			}
			
			String name = null;	
			if(isBstar){
			try {
				JSONObject newObject = (JSONObject) partItemobject;

				name = newObject.getString(KEY.KEY_PARTITEMDATA);
				Log.d(TAG, "getPartItemName 3 name is " +name);
				name = getPartItemSubStr(name,index);
				Log.d(TAG, "getPartItemName 4 name is " +name);

			} catch (Exception e) {
				Log.e(TAG,e.toString());;
			}
			Log.d(TAG, "getPartItemCheckUnitName name is "+name);
			}else{
				PartItemItem item=mgson.fromJson(partItemobject.toString(), PartItemItem.class);
				name = item.getCheckContent();
			}
			return name;
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
			info.max = SystemUtil.getTemperature(this.getPartItemSubStr(name, CommonDef.partItemData_Index.PARTITEM_MAX_VALUE));
			info.mid = SystemUtil.getTemperature(this.getPartItemSubStr(name, CommonDef.partItemData_Index.PARTITEM_MIDDLE_VALUE));
			info.min = SystemUtil.getTemperature(this.getPartItemSubStr(name, CommonDef.partItemData_Index.PARTITEM_MIN_VALUE));
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



	/**
	 * 解析工人信息
	 * @param WorkerObject
	 * @return
	 * @throws JSONException
	 */
public static List<WorkerInfo> parseWorkerNode(JSONArray WorkerObject) throws JSONException {
		
		if(WorkerObject == null) return null;
		
		List<WorkerInfo> workerList = new ArrayList<WorkerInfo>();
		
		JSONArray jsonArray = WorkerObject;
		
		for(int i =0 ;i<jsonArray.length();i++){
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);			
			WorkerInfo info = new WorkerInfo();
			info.Alias_Name = jsonObject.getString(T_Worker.Worker_Const.Key_Alias_Name);
			info.Class_Group = jsonObject.optString(T_Worker.Worker_Const.Key_Class_Group);
			info.Number = jsonObject.getString(T_Worker.Worker_Const.Key_Number);
			info.Name = jsonObject.optString(T_Worker.Worker_Const.Key_Name);			
			info.T_Line_Content_Guid = jsonObject.getString(T_Worker.Worker_Const.Key_T_Line_Content_Guid);
			info.T_Line_Guid = jsonObject.getString(T_Worker.Worker_Const.Key_T_Line_Guid);
			
			info.T_Organization_Guid = jsonObject.getString(T_Worker.Worker_Const.Key_T_Organization_Guid);
			workerList.add(info);
			//Log.d("luotest","parseWorkerNode()info "+info.GroupName);
			
			
		}	
		Log.d("luotest","parseWorkerNode()"+workerList.toString());
		return workerList;
	}
	
public static LineInfo parseRouteNameNode(Object RouteNameObject) throws JSONException {
		
		if(RouteNameObject == null) return null;
		

		LineInfo rout = new LineInfo();
		
		rout.Name = ((JSONObject)RouteNameObject).getString(T_Line.Line_Const.Key_Name);
		rout.T_Content_Guid = ((JSONObject)RouteNameObject).getString(T_Line.Line_Const.Key_T_Content_Guid);
		rout.T_Line_Guid = ((JSONObject)RouteNameObject).getString(T_Line.Line_Const.Key_T_Line_Guid);
			
		return rout;
	}

public static List<TurnInfo> parseTurnNode(JSONArray TurnObject) throws JSONException {
	
	if(TurnObject == null) return null;

	List<TurnInfo> list = new ArrayList<TurnInfo>();
	try {
		JSONArray array = TurnObject;

		for (int i = 0; i < array.length(); i++) {
			TurnInfo info = new TurnInfo();
			
			info.End_Time=((JSONObject)array.get(i)).getString(T_Turn.Turn_Const.Key_End_Time);
			info.Name=((JSONObject)array.get(i)).optString(T_Turn.Turn_Const.Key_Name);
			info.Number=((JSONObject)array.get(i)).getString(T_Turn.Turn_Const.Key_Number);
			info.Start_Time=((JSONObject)array.get(i)).getString(T_Turn.Turn_Const.Key_Start_Time);
			info.T_Line_Content_Guid=((JSONObject)array.get(i)).getString(T_Turn.Turn_Const.Key_T_Line_Content_Guid);
			info.T_Line_Guid=((JSONObject)array.get(i)).getString(T_Turn.Turn_Const.Key_T_Line_Guid);
			
						
			list.add(info);

		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	
	return list;
	
}
	
	public List<TurnInfo> getTurnInfoItem(int routeIndex) throws JSONException {
		if (mRouteList.get(mRouteIndex).mTurnList == null){
			return null;
		}else{
			return mRouteList.get(mRouteIndex).mTurnList ;
		}

		
	}

	public Object getStationItem(int routeIndex,int index) {
		Log.d(TAG, "getStationItem routeIndex ="+routeIndex +",index="+index +",mRouteIndex ="+mRouteIndex +",mStationIndex = "+mStationIndex);
		return mRouteList.get(mRouteIndex).mStationList.get(mStationIndex);

	}
	
	public Object genObject(Object object,String value){
		Object newobject = null;
		return newobject;
	}	
	
	/**
	 * 根据ItemDef 选择的 运行/停止/备用/其他等 来筛选有效的 PartItemData巡检项数据
	 * @param PartItem
	 * @param typeIndex
	 * @return
	 */
	public List<Object> fliterPartItemData(JSONArray PartItemArray,int typeIndex){
		List<Object> list = null;
		if(PartItemArray == null){return null;}
		for(int i =0;i<PartItemArray.length();i++){
		try {
			JSONObject object = (JSONObject) PartItemArray.get(i);
			
			getPartItemSubStr(object.optString(KEY.KEY_PARTITEMDATA),typeIndex);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		return list;
	}
}
